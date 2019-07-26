package engine;

import DB.*;
import DBcontroller.*;
import com.google.common.base.Throwables;
import configuration.settings;
import controller.popup.PopUpRunController;
import controller.util.CommonFunctions;
import javafx.concurrent.Task;
import model.TestCasesExecution;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class for the engine of the software. The tests are launched by this class.
 *
 * @author Thomas M.
 * @version 1.0
 */
public class Engine {

    private final MacroDB macroHandler = new MacroDB();
    /**
     * ArrayList of steps to execute.
     */
    public ArrayList<CaseExecutions> toExecute;
    /**
     *
     */
    public PopUpRunController popUpRunController;
    String stepResult = "OS", resultOKWC = "OKWC", resultOK = "OK", resultNOK = "NOK", resultNotTestable = "Not testable", resultOutOfScope = "OS", resultIncomplete = "Incomplete";
    int nbCaseOK = 0, nbCaseOKWC = 0, nbCaseNOK = 0, nbCaseNtestable = 0, nbCaseIncomplete = 0, nbCaseOS = 0, nbCaseNT = 0;
    int nbStepOK = 0, nbStepOKWC = 0, nbStepNOK = 0, nbStepNotTestable = 0, nbStepOutOfScope = 0, nbStepIncomplete = 0;
    // HashMap of buffer
    HashMap<String, Object> hashMap = new HashMap<>();
    String caseResult;
    String macroResult;
    String iterationResult;
    String baselineName;
    int iteration;
    TestExecution testExecutionHandler = new TestExecution();
    Task<Void> task;

    /**
     * Constructor of an engine.
     *
     * @param toExecute          ArrayList of cases to execute
     * @param popUpRunController
     * @param baselineName
     * @param iteration
     */
    public Engine(ArrayList<CaseExecutions> toExecute, PopUpRunController popUpRunController, String baselineName, int iteration, Task<Void> task) {
        this.iteration = iteration;
        this.baselineName = baselineName;
        this.toExecute = toExecute;
        this.popUpRunController = popUpRunController;
        this.task = task;
    }

    /**
     * Method which launch the sequence of tests. Should store the result into
     *
     * @throws Exception
     */
    public void run() throws Exception {
        TestStepDB testStepHandler = new TestStepDB();
        ScriptExecutionDB scriptExecutionHandler = new ScriptExecutionDB();
        float averageTimeCase = 0;
        TestCasesExecution testExecution = new TestCasesExecution();
        Result testResult;
        String scriptName;
        CaseExecutions currentTestCase;
        TestCaseDB caseHandler = new TestCaseDB();
        HashMap<String, Integer> hashMapNumberResultSteps = new HashMap<>();
        HashMap<String, Integer> hashMapNumberResultScript = new HashMap<>();
        HashMap<String, Integer> hashMapNumberResultMacro = new HashMap<>();
        int stepsNumber, scriptNumber, checkInMacro;
        nbCaseNT = this.toExecute.size();
        Set<String> set = new HashSet<>();
        long tempsDebut1;
        long tempsFin1;
        float seconds1;
        int paramToTake;
        Double iterationResultPercentage;
        for (int i = 0; i < this.toExecute.size(); i++) {                        //Loop through each test case
            if(task.isCancelled())
                break;
            hashMap.clear();
            tempsDebut1 = System.currentTimeMillis();
            //this.toExecute.get(0).getStepsExecutionsAndScripts().get(0).getStepExecution().setIterationResult("Incomplete");
            //stepExecutionHandler.setIterationResultInDB(this.toExecute.get(0).getStepsExecutionsAndScripts().get(0).getStepExecution());
            startCaseInit(hashMapNumberResultSteps);
            currentTestCase = testExecution.getStepsScriptsParametersFromCaseExecution(this.toExecute.get(i));
            //Automatic mode to display case in execution
            //CommonFunctions.debugLog.debug("QUERY TIME= " + Float.toString(seconds2));
            //currentTestCaseAndSteps.setStepsExecutionsAndScripts(testStepsExecutionAndScripts);
            //popUpRunController.callDisplaySteps(currentTestCase);
            currentTestCase.setCaseExecutionResult("In execution");
            //Thread.sleep(1000);
            stepsNumber = currentTestCase.getStepExecutionses().size();
            Iterator<StepExecutions> itSteps = currentTestCase.getStepExecutionses().iterator();
            while (itSteps.hasNext()) {                                          //Loop through each STEP EXECUTION
                StepExecutions currentStep = itSteps.next();
                startCaseInit(hashMapNumberResultScript);
                startCaseInit(hashMapNumberResultMacro);
                ArrayList<ParametersExecution> currentParameters = new ArrayList<>();
                if (currentStep.getScriptExecutionses().isEmpty()) {            //If Test step has no script execution
                    testStepHandler.getStepFromStepExecution(currentStep);
                    ParametersExecution scriptParameters = new ParametersExecution();
                    scriptParameters.setValue(currentStep.getTestStep().getHumanStimuli());
                    currentParameters.add(new ParametersExecution());
                    currentParameters.add(scriptParameters);
                    runStimuliScript("popUpStimuli", currentParameters, hashMap);
                    currentParameters.get(1).setValue(currentStep.getTestStep().getHumanCheck());
                    testResult = runCheckScript("popUpCheck", currentParameters, hashMap);
                    stepResult = testResult.getResult();
                    setStepCommentAndResult(stepResult, testResult.getComment(), currentStep);
                } else {                                                        //if Test step has script executions
                    checkInMacro = 0;
                    scriptNumber = 0;
                    Iterator<ScriptExecutions> itScripts = currentStep.getScriptExecutionses().iterator();
                    while (itScripts.hasNext()) {                               //Loop through each SCRIPT EXECUTION
                        ScriptExecutions currentScript = itScripts.next();
                        currentParameters = new ArrayList<>(currentScript.getParametersExecutions());
                            scriptExecutionHandler.getScriptFromScriptExecution(currentScript);
                        if (currentScript.getScript().getIsMacro() == 1) {      //Script is a Macro
                            macroHandler.getAllFromMacro(currentScript.getScript());
                            Iterator<Macro> itMacro = currentScript.getScript().getMacrosForScriptIdScript().iterator();
                            paramToTake = 0;
                            while (itMacro.hasNext()) {                         //loop through each MACRO EXECUTION
                                boolean count = false;
                                Macro mac = itMacro.next();
                                scriptName = mac.getScriptByScriptIdScript1().getCallName();
                                set.add(scriptName);
                                ArrayList<ParametersExecution> paramScriptMacro = new ArrayList<>();
                                Iterator<ParamScriptMacro> itParamScMacro = mac.getParamScriptMacros().iterator(); //get Set of ParamScriptMacros
                                while (itParamScMacro.hasNext()) {              //loop through each ParamScriptMacros from MACRO
                                    ParametersExecution param = new ParametersExecution();      //declare new ParameterExecution
                                    ParamScriptMacro paramScMacro = itParamScMacro.next();
                                    //get current ParamScriptMacro
                                    if (paramScMacro.getToDisplay() == 0) {
                                        if (paramScMacro.getParamScriptMacro() == null) {       //if current ParamScript does not include more ParamScripts
                                            param.setValue(paramScMacro.getValue());
                                            param.setParamOrder(paramScMacro.getScriptHasParameters().getParamOrder());
                                            paramScriptMacro.add(param);
                                        } else {
                                            //if current ParamScript does not include more paramscript.
                                            try {
                                                param.setValue(paramScMacro.getParamScriptMacro().getValue());
                                                param.setParamOrder(paramScMacro.getScriptHasParameters().getParamOrder());
                                                paramScriptMacro.add(param);
                                            } catch (Exception ex) {
                                                CommonFunctions.debugLog.error(
                                                        "Cannot set param value, please check paramScriptMacro Value: " + paramScMacro.getMacro().getIdmacro(), ex);
                                            }
                                        }
                                    } else {
                                        param.setValue(currentParameters.get(paramToTake).getValue());
                                        paramScMacro.setValue(currentParameters.get(paramToTake).getValue());
                                        param.setParamOrder(paramScMacro.getScriptHasParameters().getParamOrder());
                                        paramScriptMacro.add(param);
                                        paramToTake++;
                                    }
                                }      //Catch exception occured in Script. Only focus on InvocationTargetException now. 
                                try {
                                    if (mac.getScriptByScriptIdScript1().getIsStimuli() == 1) {
                                        runStimuliScript(scriptName, paramScriptMacro, hashMap);
                                        setMacroComment("Exec", "", currentScript, mac.getScriptByScriptIdScript1());
                                        //setScriptCommentAndResult("Exec", "", currentScript);
                                    } else if (mac.getScriptByScriptIdScript1().getIsStimuli() == 0) {
                                        checkInMacro++;
                                        if (!count) {
                                            scriptNumber++;
                                            count = true;
                                        }
                                        CommonFunctions.debugLog.debug("ScriptName : " + scriptName);
                                        CommonFunctions.debugLog.debug("paramScriptMacro : " + paramScriptMacro.toString());
                                        //This line might cause exception. Catch the error here.

                                        testResult = runCheckScript(scriptName, paramScriptMacro, hashMap);
                                        CommonFunctions.debugLog.debug("SCRIPT = " + scriptName);
                                        //CommonFunctions.debugLog.debug("Script = " + scriptName + " effectue en : " + Float.toString(seconds3));
                                        CommonFunctions.debugLog.debug("RESULT SCRIPT MACRO = " + testResult.getResult());

                                        hashMapNumberResultMacro.put(testResult.getResult(), hashMapNumberResultMacro.get(testResult.getResult()) + 1);
                                        setMacroComment(testResult.getResult(), testResult.getComment(), currentScript, mac.getScriptByScriptIdScript1());
                                    }
                                } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    // Problem with this catch: The result of Not Testable is not saved to the database. 
                                    String stackTrace = Throwables.getStackTraceAsString(ex);
                                    currentTestCase.setCaseExecutionResult("Not testable");
                                    testResult = new Result();
                                    testResult.setResult("Not testable");  //Only will use this value when proper testResult cannot be obtained from checkScript.
                                    //Finish remaining execusion steps for properly updating controller.
                                    hashMapNumberResultMacro.put(testResult.getResult(), hashMapNumberResultMacro.get(testResult.getResult()) + 1);
                                    if (mac.getScriptByScriptIdScript1().getIsStimuli() == 0) {        //Without Stimuli, need to use extra two steps.
                                        setMacroComment(testResult.getResult(), testResult.getComment(), currentScript, mac.getScriptByScriptIdScript1());
                                    } else if (mac.getScriptByScriptIdScript1().getIsStimuli() == 1) {        //With Stimuli, three extra steps.
                                        setMacroComment("Exec", "", currentScript, mac.getScriptByScriptIdScript1());
                                    }

                                    throw ex;    //Go to PopUpRunController.java
                                }
                            }
                            this.stateMachineMacro(hashMapNumberResultMacro, checkInMacro);
                            setMacroResult(macroResult, currentScript);
                            hashMapNumberResultScript.put(macroResult, hashMapNumberResultScript.get(macroResult) + 1);
                        } else {        //Script is not a macro.
                            scriptName = currentScript.getScript().getCallName();
                            set.add(scriptName);
                            if (currentScript.getIsStimuli() == 1) {
                                runStimuliScript(scriptName, currentParameters, hashMap);
                                setScriptCommentAndResult("Exec", "", currentScript);
                            } else if (currentScript.getIsStimuli() == 0) {
                                scriptNumber++;
                                testResult = runCheckScript(scriptName, currentParameters, hashMap);
                                hashMapNumberResultScript.put(testResult.getResult(), hashMapNumberResultScript.get(testResult.getResult()) + 1);
                                setScriptCommentAndResult(testResult.getResult(), testResult.getComment(), currentScript);
                            }
                        }
                        stateMachineStepResult(hashMapNumberResultScript, scriptNumber);
                        currentStep.setStepExecutionResult(stepResult);
                    }
                    CommonFunctions.debugLog.debug("RESULT STEP = " + stepResult);
                    hashMapNumberResultSteps.put(stepResult, hashMapNumberResultSteps.get(stepResult) + 1);

                }

                tempsFin1 = System.currentTimeMillis();
                seconds1 = (tempsFin1 - tempsDebut1) / 1000;
                //CommonFunctions.debugLog.debug("Case execution in = " + seconds1);
                //CommonFunctions.debugLog.debug("1ere part= " + (1 - 1 / (i + 1)) * averageTimeCase + " 2eme part= " + (1 / (i + 1)) * seconds1);
                averageTimeCase = ((1 - 1 / ((float) i + 1)) * averageTimeCase + (1 / ((float) i + 1)) * seconds1);
                //CommonFunctions.debugLog.debug("Average Time Case = " + averageTimeCase);

            }
            stateMachineCaseResult(hashMapNumberResultSteps, stepsNumber);
            //Thread.sleep(1000);
            endCaseSetResultChartAndDB(currentTestCase, caseHandler, averageTimeCase);

            iterationResultPercentage = ((double) nbCaseOK / (double) this.toExecute.size()) * 100;
//                CommonFunctions.debugLog.debug("ITERATION RESULT = " + iterationResultPercentage);
            this.popUpRunController.setIterationPercentage(iterationResultPercentage);
        }

        this.stateMachineIterationResult(nbCaseOK, nbCaseNOK, nbCaseOKWC, nbCaseNtestable, nbCaseIncomplete, nbCaseOS, this.toExecute.size());
        //this.toExecute.get(0).getStepsExecutionsAndScripts().get(0).getStepExecution().setIterationResult(iterationResult);
        //stepExecutionHandler.setIterationResultInDB(this.toExecute.get(0).getStepsExecutionsAndScripts().get(0).getStepExecution());
        this.closeAllScripts(set);
        this.popUpRunController.executionFinished();
    }

    //Default method for handling remaining operations when an exception is encountered during the execution of a macro/script.
    private float exceptionCausedExecutionTerminator(HashMap<String, Integer> hashMapNumberResultMacro, Result testResult, ScriptExecutions currentScript, Macro mac, int checkInMacro, HashMap<String, Integer> hashMapNumberResultScript, int scriptNumber, StepExecutions currentStep, HashMap<String, Integer> hashMapNumberResultSteps, long tempsDebut1, float averageTimeCase, int i, Set<String> set, int stepsNumber, CaseExecutions currentTestCase) throws Exception {
        long tempsFin1;
        float seconds1;
        Double iterationResultPercentage;
        this.stateMachineMacro(hashMapNumberResultMacro, checkInMacro);
        setMacroResult(macroResult, currentScript);
        hashMapNumberResultScript.put(macroResult, hashMapNumberResultScript.get(macroResult) + 1);
//        CommonFunctions.debugLog.debug("SCRIPT NUMBER = " + scriptNumber);
        stateMachineStepResult(hashMapNumberResultScript, scriptNumber);
        currentStep.setStepExecutionResult(stepResult);
//        CommonFunctions.debugLog.debug("RESULT STEP = " + stepResult);
        hashMapNumberResultSteps.put(stepResult, hashMapNumberResultSteps.get(stepResult) + 1);
        tempsFin1 = System.currentTimeMillis();
        seconds1 = (tempsFin1 - tempsDebut1) / 1000;
        averageTimeCase = ((1 - 1 / ((float) i + 1)) * averageTimeCase + (1 / ((float) i + 1)) * seconds1);
        iterationResultPercentage = ((double) nbCaseOK / (double) this.toExecute.size()) * 100;
//        CommonFunctions.debugLog.debug("ITERATION RESULT = " + iterationResultPercentage);
        this.popUpRunController.setIterationPercentage(iterationResultPercentage);
        this.stateMachineIterationResult(nbCaseOK, nbCaseNOK, nbCaseOKWC, nbCaseNtestable, nbCaseIncomplete, nbCaseOS, this.toExecute.size());
        this.closeAllScripts(set);
        this.popUpRunController.executionFinished();
        //Possibly caused by setMachineCaseResult.

//        stateMachineCaseResult(hashMapNumberResultSteps, stepsNumber);
////        CommonFunctions.debugLog.debug("Case result = " + caseResult);
//        currentTestCase.setCaseExecutionResult(caseResult);
//        popUpRunController.setNumberNotExecuted(nbCaseOK, nbCaseOKWC, nbCaseNOK, nbCaseNtestable, nbCaseIncomplete, nbCaseOS, nbCaseNT);
//        popUpRunController.updateAverageTimeCase(averageTimeCase, averageTimeCase * (float) nbCaseNT);
        return averageTimeCase;
    }

    /**
     * StateMachine to determine the result of a case
     *
     * @param hashMap     the hashmap of test step results
     * @param numberSteps the number of steps in the case
     */
    public void stateMachineCaseResult(HashMap<String, Integer> hashMap, int numberSteps) {
        if (hashMap.get(resultOK) == 0 && hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) == 0 && hashMap.get(resultOutOfScope) >=0) {
            caseResult = resultOutOfScope;
            nbCaseOS++;
        } else if (hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) == 0 && hashMap.get(resultOutOfScope) != numberSteps) {
            caseResult = resultOK;
            nbCaseOK++;
        } else if (hashMap.get(resultNOK) > 0) {
            caseResult = resultNOK;
            nbCaseNOK++;
        } else if (hashMap.get(resultOKWC) > 0) {
            caseResult = resultOKWC;
            nbCaseOKWC++;
        } else if (hashMap.get(resultOK) == 0 && hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) > 0) {
            caseResult = resultNotTestable;
            nbCaseNtestable++;
        } else if ((hashMap.get(resultOK) > 0 || hashMap.get(resultOKWC) > 0) && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) > 0) {
            caseResult = resultIncomplete;
            nbCaseIncomplete++;
        }
        nbCaseNT--;
    }

    /**
     * StateMachine to determine the result of a case
     *
     * @param hashMap       the hashmap of test step results
     * @param numberScripts
     */
    private void stateMachineMacro(HashMap<String, Integer> hashMap, int numberScripts) {
        CommonFunctions.debugLog.debug("RESULT MACRO");
        CommonFunctions.debugLog.debug("NUMBER OF SCRIPT IN MACRO = " + numberScripts);
        if (numberScripts == 0 || (hashMap.get(resultOK) != numberScripts && hashMap.get(resultNotTestable) == 0 && hashMap.get(resultOutOfScope) > 0)) {
            macroResult = resultOutOfScope;
        } else if (hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) == 0 && hashMap.get(resultOutOfScope) == 0 && numberScripts > 0) {
            macroResult = resultOK;
        } else if (hashMap.get(resultNOK) > 0) {
            macroResult = resultNOK;
        } else if (hashMap.get(resultOKWC) > 0) {
            macroResult = resultOKWC;
        } else if (hashMap.get(resultOK) == 0 && hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) > 0) {
            macroResult = resultNotTestable;
        } else if ((hashMap.get(resultOK) > 0 || hashMap.get(resultOKWC) > 0) && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) > 0) {
            macroResult = resultIncomplete;
        }
    }

    /**
     * StateMachine to determine the result of a case
     *
     * @param hashMap      the hashmap of test step results
     * @param numberScript the number of scripts in the step
     */
    private void stateMachineStepResult(HashMap<String, Integer> hashMap, int numberScript) {
        CommonFunctions.debugLog.debug("IN RESULT");
        if ( numberScript == 0 || (hashMap.get(resultOK) != numberScript && hashMap.get(resultNotTestable) == 0 && hashMap.get(resultOutOfScope) > 0)) {
            CommonFunctions.debugLog.debug("OS");
            stepResult = resultOutOfScope;
            nbStepOutOfScope++;
        } else if (hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) == 0 && hashMap.get(resultOutOfScope) == 0 && numberScript > 0) {
            CommonFunctions.debugLog.debug("OK");
            stepResult = resultOK;
            nbStepOK++;
        } else if (hashMap.get(resultNOK) > 0) {
            CommonFunctions.debugLog.debug("NOK");
            stepResult = resultNOK;
            nbStepNOK++;
        } else if (hashMap.get(resultOKWC) > 0) {
            CommonFunctions.debugLog.debug("OKWC");
            stepResult = resultOKWC;
            nbStepOKWC++;
        } else if (hashMap.get(resultOK) == 0 && hashMap.get(resultOKWC) == 0 && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) > 0) {
            CommonFunctions.debugLog.debug("NT");
            stepResult = resultNotTestable;
            nbStepNotTestable++;
        } else if ((hashMap.get(resultOK) > 0 || hashMap.get(resultOKWC) > 0) && hashMap.get(resultNOK) == 0 && hashMap.get(resultNotTestable) > 0) {
            CommonFunctions.debugLog.debug("IC");
            stepResult = resultIncomplete;
            nbStepIncomplete++;
        }
    }

    /**
     * State machine for the result of the iteration
     *
     * @param nbCaseOK         number of cases OK
     * @param nbCaseNOK        number of cases NOK
     * @param nbCaseOKWC       number of cases OKWC
     * @param nbCaseNtestable  number of cases Not testable
     * @param nbCaseIncomplete number of cases incomplete
     * @param nbCaseOS         number of cases OS
     * @param numberCases      total number of test cases
     */
    private void stateMachineIterationResult(int nbCaseOK, int nbCaseNOK, int nbCaseOKWC, int nbCaseNtestable, int nbCaseIncomplete, int nbCaseOS, int numberCases) {
        if (nbCaseOKWC == 0 && nbCaseNOK == 0 && nbCaseNtestable == 0 && nbCaseOS != numberCases) {
            iterationResult = resultOK;
        } else if (nbCaseNOK > 0) {
            iterationResult = resultNOK;
        } else if (nbCaseOKWC > 0) {
            iterationResult = resultOKWC;
        } else if (nbCaseOK == 0 && nbCaseOKWC == 0 && nbCaseNOK == 0 && nbCaseNtestable > 0) {
            iterationResult = resultNotTestable;
        } else if ((nbCaseOK > 0 || nbCaseOKWC > 0) && nbCaseNOK == 0 && nbCaseNtestable > 0) {
            iterationResult = resultIncomplete;
        } else if (nbCaseOK == 0 && nbCaseOKWC == 0 && nbCaseNOK == 0 && nbCaseNtestable == 0 && nbCaseOS == numberCases) {
            iterationResult = resultOutOfScope;
        }
    }

    /**
     * Init procedure to execute before the execution of each testCases
     *
     * @param hashMapNumber Hashmap for the results of test steps
     */
    private void startCaseInit(HashMap<String, Integer> hashMapNumber) {
        //hashMap.clear();
        hashMapNumber.put(resultOK, 0);
        hashMapNumber.put(resultNOK, 0);
        hashMapNumber.put(resultOKWC, 0);
        hashMapNumber.put(resultOutOfScope, 0);
        hashMapNumber.put(resultNotTestable, 0);
        caseResult = null;
    }

    /**
     * Procedure to execute at the end of each case, update of the result, the
     * pie chart and put Case result in DB
     *
     * @param currentTestCase the current test case in execution
     * @param caseHandler     the TestCaseDB to use
     * @param averageTimeCase
     */
    private void endCaseSetResultChartAndDB(CaseExecutions currentTestCase, TestCaseDB caseHandler, float averageTimeCase) throws InterruptedException {
        CommonFunctions.debugLog.debug("Case result = " + caseResult);
        //this.baselineName will show the name of Iteration (baselineId)
        currentTestCase.setCaseExecutionResult(caseResult);
        popUpRunController.setNumberNotExecuted(nbCaseOK, nbCaseOKWC, nbCaseNOK, nbCaseNtestable, nbCaseIncomplete, nbCaseOS, nbCaseNT);
        popUpRunController.updateAverageTimeCase(averageTimeCase, averageTimeCase * (float) nbCaseNT);
        testExecutionHandler.resultInDB(currentTestCase, iteration, this.baselineName);
        TimeUnit.MILLISECONDS.sleep(50);
        //caseHandler.updateDBwithResults(currentTestCase, (byte) iteration);
    }

    private void closeAllScripts(Set<String> set) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
        Iterator it = set.iterator();
        String scriptName;
        while (it.hasNext()) {
            scriptName = (String) it.next();
            //CommonFunctions.debugLog.debug("SCRIPT NAME = " + scriptName);
            closeScript(scriptName);
        }
    }

    /**
     * set the result and the comment of the current script
     *
     * @param result        the result to set
     * @param comment       the comment to set
     * @param currentScript the script to which set the result
     */
    private void setScriptCommentAndResult(String result, String comment, ScriptExecutions currentScript) {
        currentScript.setScriptExecutionResult(result);
        currentScript.setScriptExecutionComment(comment);
    }

    private void setMacroComment(String result, String comment, ScriptExecutions currentScript, Script scriptByScriptIdScript1) {
        currentScript.setScriptExecutionComment(currentScript.getScriptExecutionComment() + "\n" + scriptByScriptIdScript1.getName() + ": " + result + " " + comment);
        //currentScript.setScriptExecutionResult(result);
    }

    private void setMacroResult(String result, ScriptExecutions currentScript) {
        currentScript.setScriptExecutionResult(result);
    }

    /**
     * set the result and the comment of the current step
     *
     * @param result      the result to set
     * @param comment     the comment to set
     * @param currentStep
     */
    private void setStepCommentAndResult(String result, String comment, StepExecutions currentStep) {
        currentStep.setStepExecutionResult(result);
        currentStep.setStepExecutionComment(comment);
    }

    /**
     * Method which run the stimuli of the step.  Left side (Step description)
     *
     * @param script     script to run
     * @param parameters parameters of the script
     * @param hashMap
     * @throws java.net.MalformedURLException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    private void runStimuliScript(String script, ArrayList<ParametersExecution> parameters, HashMap hashMap)
            throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //get the name of the class of the script
        File root = new File(settings.scriptsPaht + "\\" + script + ".jar");
        Class<?> classe = getClasse(script, root);

        final Constructor<?> constructor = classe.getConstructor();
        final Object o = constructor.newInstance();
        java.lang.reflect.Method method;
        method = o.getClass().getMethod("run", ArrayList.class, HashMap.class);
        if (parameters != null) {
            //if there is parameters for the test, execute the scripts with the parameters
            method.invoke(o, parameters, hashMap);
        } else {
            //if there is no parameters for the test, execute the scripts without parameters
            method.invoke(o, "");
        }
    }

    /**
     * Method which run the check of the step.  Right side (Expected Result)
     *
     * @param script     the name of the script to run
     * @param parameters the parameters for the script
     * @param hashMap
     * @return the result of the step as a string
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws java.net.MalformedURLException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private Result runCheckScript(String script, ArrayList<ParametersExecution> parameters, HashMap hashMap)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, MalformedURLException {
        File root = new File(settings.scriptsPaht + "\\" + script + ".jar");
        Class<?> classe = getClasse(script, root);
        final Constructor<?> constructor = classe.getConstructor();
        final Object o = constructor.newInstance();
        java.lang.reflect.Method method;
        method = o.getClass().getMethod("run", ArrayList.class, HashMap.class);
        //get the name of the class of the script
        if (parameters != null) {
            //if there is parameters for the test, execute the scripts with the parameters
            CommonFunctions.debugLog.debug("Parameters is not null");
            //Should not use catch here, but catch in PopUpRnController.java: 261
            return (Result) method.invoke(o, parameters, hashMap);

        }
        //if there is no parameters for the test, execute the scripts without parameters
        return (Result) method.invoke(o, "");
    }

    /**
     * Method called to close all the scripts used (necessary in case of open
     * SSH connection or create Modbus server)
     *
     * @param script script Name
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws java.net.MalformedURLException
     */
    private void closeScript(String script) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
        File root = new File(settings.scriptsPaht + "\\" + script + ".jar");
        Class<?> classe = getClasse(script, root);
        final Constructor<?> constructor = classe.getConstructor();
        final Object o = constructor.newInstance();
        java.lang.reflect.Method method;
        method = o.getClass().getMethod("close");
        method.invoke(o);
    }

    private Class<?> getClasse(String script, File root) throws MalformedURLException, ClassNotFoundException {
        Class<?> classe;
        if (root.exists()) {
            URLClassLoader classLoader = null;
            classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
            classe = Class.forName(script, false, classLoader);
        } else {
            classe = Class.forName("script." + script);
        }
        return classe;
    }
}
