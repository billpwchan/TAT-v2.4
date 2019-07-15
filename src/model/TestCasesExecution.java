
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.CaseExecutions;
import DB.Iterations;
import DB.ScriptExecutions;
import DB.StepExecutions;
import DBcontroller.ScriptDB;
import DBcontroller.TestCaseDB;
import DBcontroller.TestStepDB;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author tmorin
 */
public class TestCasesExecution {

    /**
     *
     */
    public TestCasesExecution() {

    }

    /**
     * @param caseExecution
     * @return
     */
    public CaseExecutions getStepsScriptsParametersFromCaseExecution(CaseExecutions caseExecution) {
        CaseExecutions caseExecutions = new CaseExecutions();
        TestStepDB testStepHandler = new TestStepDB();
        caseExecutions = testStepHandler.getStepsScriptsParametersFromCaseExecution(caseExecution);
        return caseExecutions;
    }

    /**
     * @param caseToDisplay
     */
    public void prepareCaseDisplay(CaseExecutions caseToDisplay) {
        TestCaseDB testCaseHandler = new TestCaseDB();
        TestStepDB testStepHandler = new TestStepDB();
        ScriptDB scriptHandler = new ScriptDB();
        testCaseHandler.getCaseAndStepExecutionsFromCaseExecution(caseToDisplay);
        Iterator<StepExecutions> itSteps = caseToDisplay.getStepExecutionses().iterator();
        while (itSteps.hasNext()) {
            StepExecutions stepExecution = itSteps.next();
            testStepHandler.getStepAndScriptsFromStepExecution(stepExecution);
            Iterator<ScriptExecutions> itScripts = stepExecution.getScriptExecutionses().iterator();
            while (itScripts.hasNext()) {
                ScriptExecutions scriptExecution = itScripts.next();
                scriptHandler.getScriptAndParametersExecutionFromScriptExecution(scriptExecution);
            }
        }

    }

    /**
     * preparation of the campaign to display
     *
     * @param campaignID the ID of the campaign
     * @param baselineID the name of the baseline
     * @param iteration the number of the iteration
     * @return the Arraylist of TestCaseAndSteps
     * @throws InterruptedException
     */
//    public ArrayList<TestCaseAndSteps> PrepareTestDiplay(int campaignID, String baselineID, int iteration) throws InterruptedException {
//        TestCaseDB testCaseHandler = new TestCaseDB();
//        TestStepDB testStepHandler = new TestStepDB();
//        ScriptDB testScriptHandler = new ScriptDB();
//        ParametersDB parametersHandler = new ParametersDB();
//        ArrayList<StepExecution> stepExecutions;
//        ArrayList<TestCaseAndSteps> testCasesAndSteps = new ArrayList<>();
//        //System.out.println("INTERATION= " + iteration);
//        stepExecutions = testCaseHandler.getTestCasesToExecute(campaignID, baselineID, iteration);
//        //System.out.println("CASE EXECUTION SIZE= " + stepExecutions.size());
//        Task<Void> task = new Task<Void>() {
//            @Override
//            public Void call() throws InterruptedException {
//                for (int i = 0; i < stepExecutions.size(); i++) {
//                    updateProgress(i, stepExecutions.size());
//                    TestCaseAndSteps testCaseAndSteps = new TestCaseAndSteps();
//                    TestCase tc = new TestCase(stepExecutions.get(i).getTestCase());
//                    testCaseAndSteps.setTestCase(tc);
//                    testCaseAndSteps.getTestCase().setResult(stepExecutions.get(i).getCaseResult());
//                    //System.out.println("RESULT -= " + testCaseAndSteps.getTestCase().getResult());
//                    testCaseAndSteps.getTestCase().setOrdre(stepExecutions.get(i).getId().getCaseExecutionOrder());
//                    testCasesAndSteps.add(testCaseAndSteps);
//                    ArrayList<StepExecution> stepStepExecution = testStepHandler.getStepsFromStepExecution(campaignID, baselineID, stepExecutions.get(i).getTestCase().getIdtestCase(), iteration, stepExecutions.get(i).getId().getCaseExecutionOrder());
//                    //System.out.println("STEP EXECUTION SIZE= " + stepStepExecution.size());
//                    for (int j = 0; j < stepStepExecution.size(); j++) {
//                        TestStepExecutionAndScripts stepExecutionAndScripts = new TestStepExecutionAndScripts();
//                        TestStep testStep = new TestStep(stepStepExecution.get(j).getTestStep());
//                        StepExecution stepExec = new StepExecution(stepStepExecution.get(j));
//                        testStep.setResult(stepExec.getResult());
//                        stepExecutionAndScripts.setTestStep(testStep);
//                        stepExecutionAndScripts.setStepExecution(stepExec);
//                        //stepExecutionAndScripts.getTestStep().setOrdre(stepExec.getId().getStepExecutionOrder());
//                        testCasesAndSteps.get(i).getStepsExecutionsAndScripts().add(stepExecutionAndScripts);
//                        //System.out.println("NOMBRE DE STEPS : "+testCasesAndSteps.get(i).getStepsExecutionsAndScripts().size());
//                        ArrayList<ScriptExecution> scriptExecutions = testScriptHandler.getScriptsFromStepExecution(stepStepExecution.get(j));
//                        //System.out.println("SCRIPT EXECUTION SIZE= " + scriptExecutions.size());
//                        for (int k = 0; k < scriptExecutions.size(); k++) {
//                            ArrayList<ScriptParameters> parameters = parametersHandler.getParametersFromScriptExecution(scriptExecutions.get(k));
//                            //System.out.println("CASE NUMERO : "+i+" Step NUMERO : "+j+" Script Numero : "+k);
//                            TestScriptsAndParameters scriptAndParameters = new TestScriptsAndParameters();
//                            Script script = new Script(scriptExecutions.get(k).getScript());
//                            ScriptExecution scriptExecution = new ScriptExecution(scriptExecutions.get(k));
//                            scriptAndParameters.setScript(script);
//                            scriptAndParameters.getScript().setResult(scriptExecutions.get(k).getResult());
//                            scriptAndParameters.setScriptExecution(scriptExecution);
//                            //System.out.println("STIMULI: "+scriptAndParameters.getScriptExecution().getStimuli());
//                            //scriptAndParameters.getScript().setStimuli(scriptAndParameters.getScriptExecution().getStimuli());
//                            scriptAndParameters.setParameters(parameters);
//                            testCasesAndSteps.get(i).getStepsExecutionsAndScripts().get(j).getScriptAndParameters().add(scriptAndParameters);
//
//                        }
//                    }
//                }
//                return null;
//            }
//        };
//        Platform.runLater(() -> {
//            ProgressForm pForm = new ProgressForm();
//            task.setOnSucceeded(event -> {
//                pForm.getDialogStage().close();
//            });
//            pForm.activateProgressBar(task);
//            pForm.getDialogStage().show();
//        });
//
//        Thread thread = new Thread(task);
//        thread.start();
//        thread.join();
//        return testCasesAndSteps;
//    }
//    /**
//     * Get the ArrayList of test cases to display
//     *
//     * @param testCasesAndSteps an arraylist with all the cases, steps, script
//     * and parameters
//     * @return an arrayList with only test cases
//     */
//    public ArrayList<TestCase> casesToDisplay(ArrayList<TestCaseAndSteps> testCasesAndSteps) {
//        ArrayList<TestCase> testCasesToDsiplay = new ArrayList<>();
//        for (int i = 0; i < testCasesAndSteps.size(); i++) {
//            testCasesToDsiplay.add(testCasesAndSteps.get(i).getTestCase());
//        }
//        return testCasesToDsiplay;
//    }
//    /**
//     * Get all the steps of a particular case to display
//     *
//     * @param testCasesAndSteps an arrayList of testCaseAndStep to search in
//     * @param testCaseID the ID of the case from which get the steps
//     * @param testCaseOrder the order of the case
//     * @return an arraylist of test steps with scripts and parameters for the
//     * particular case
//     */
//    public ArrayList<TestStepExecutionAndScripts> testStepsToDisplay(ArrayList<TestCaseAndSteps> testCasesAndSteps, int testCaseID, int testCaseOrder) {
//        boolean found = false;
//        int i = 0;
//        while (i < testCasesAndSteps.size() && found == false) {
//            if (testCasesAndSteps.get(i).getTestCase().getIdtestCase() == testCaseID && testCasesAndSteps.get(i).getTestCase().getOrder() == testCaseOrder) {
//                found = true;
//            }
//            i++;
//        }
//        return testCasesAndSteps.get(i - 1).getStepsExecutionsAndScripts();
//    }

    /**
     * Get all the scripts of a particular step to display
     *
     * @param baselineID
     * @param testCasesAndSteps an arrayList of testCaseAndStep to search in
     * @param testStepID        the ID of the case from which get the scritp
     * @param testStepOrder     the order of the step
     * @return an ArrayList of scripts with all the parameters
     */
//    public ArrayList<TestScriptsAndParameters> testScriptsToDisplay(ArrayList<TestStepExecutionAndScripts> stepExecutionsAndScripts, int testStepID, int testStepOrder) {
//        boolean found = false;
//        int i = 0;
//        while (i < stepExecutionsAndScripts.size() && found == false) {
//            if (stepExecutionsAndScripts.get(i).getTestStep().getIdtestStep() == testStepID && stepExecutionsAndScripts.get(i).getTestStep().getOrder() == testStepOrder) {
//                found = true;
//            }
//            i++;
//        }
//        return stepExecutionsAndScripts.get(i - 1).getScriptAndParameters();
//    }
    public ArrayList<CaseExecutions> PrepareCaseDisplayResults(String baselineID, int iteration) {
        TestCaseDB testCaseHandler = new TestCaseDB();
        ArrayList<CaseExecutions> caseExecutions;
        caseExecutions = testCaseHandler.getTestCasesAndResults(baselineID, iteration);
        return caseExecutions;
    }

    /**
     * @param caseExecution
     * @param currentIteration
     */
    public void PrepareStepsScriptsParametersDisplayResults(CaseExecutions caseExecution, Iterations currentIteration) {
        TestStepDB testStepHandler = new TestStepDB();
        testStepHandler.getStepExecutionAndScriptsResults(caseExecution, currentIteration);
    }

//        public ArrayList<TestCaseAndSteps> PrepareCase(String baselineID, int iteration) throws InterruptedException {
//        TestCaseDB testCaseHandler = new TestCaseDB();
//        ArrayList<StepExecution> stepExecutions;
//        ArrayList<TestCaseAndSteps> testCasesAndSteps = new ArrayList<>();
//        //System.out.println("INTERATION= " + iteration);
//        stepExecutions = testCaseHandler.getTestCasesToExecute(campaignID, baselineID, iteration);
//        //System.out.println("CASE EXECUTION SIZE= " + stepExecutions.size());
//        for (int i = 0; i < stepExecutions.size(); i++) {
//            TestCaseAndSteps testCaseAndSteps = new TestCaseAndSteps();
//            TestCase tc = new TestCase(stepExecutions.get(i).getTestCase());
//            testCaseAndSteps.setTestCase(tc);
//            testCaseAndSteps.getTestCase().setResult(stepExecutions.get(i).getCaseResult());
//            //System.out.println("RESULT -= " + testCaseAndSteps.getTestCase().getResult());
//            testCaseAndSteps.getTestCase().setOrdre(stepExecutions.get(i).getId().getCaseExecutionOrder());
//            testCasesAndSteps.add(testCaseAndSteps);
//        }
//        return testCasesAndSteps;
//    }
//    public ArrayList<StepExecutions> PrepareStepsDisplay(CaseExecutions caseExecution){
//        ArrayList<StepExecutions> stepExecutions = new ArrayList<>();
//        TestStepDB testStepHandler = new TestStepDB();
//        stepExecutions=testStepHandler.
////        System.out.println("CAMPAIG ID = "+campaignID);
////        System.out.println("BASELINE ID= "+baselineID);
////        System.out.println("CASE ID = "+ testCase.getIdtestCase());
////        System.out.println("ITERATION = "+ iteration);
////        System.out.println("ORDER = "+testCase.getOrder());
//        ArrayList<StepExecution> stepStepExecution = testStepHandler.getStepsFromStepExecution(campaignID, baselineID, testCase.getIdtestCase(), iteration, testCase.getOrder());
//        //System.out.println("STEP EXECUTION SIZE= " + stepStepExecution.size());
//        for (int j = 0; j < stepStepExecution.size(); j++) {
//            TestStepExecutionAndScripts stepExecutionAndScripts = new TestStepExecutionAndScripts();
//            TestStep testStep = new TestStep(stepStepExecution.get(j).getTestStep());
//            StepExecution stepExec = new StepExecution(stepStepExecution.get(j));
//            testStep.setResult(stepExec.getResult());
//            stepExecutionAndScripts.setTestStep(testStep);
//            stepExecutionAndScripts.setStepExecution(stepExec);
//
//            //stepExecutionAndScripts.getTestStep().setOrdre(stepExec.getId().getStepExecutionOrder());
//            //testCasesAndSteps.get(i).getStepsExecutionsAndScripts().add(stepExecutionAndScripts);
//            //System.out.println("NOMBRE DE STEPS : "+testCasesAndSteps.get(i).getStepsExecutionsAndScripts().size());
//            ArrayList<ScriptExecution> scriptExecutions = testScriptHandler.getScriptsFromStepExecution(stepStepExecution.get(j));
//            //System.out.println("SCRIPT EXECUTION SIZE= " + scriptExecutions.size());
//            for (int k = 0; k < scriptExecutions.size(); k++) {
//                ArrayList<ScriptParameters> parameters = parametersHandler.getParametersFromScriptExecution(scriptExecutions.get(k));
//                //System.out.println("CASE NUMERO : "+i+" Step NUMERO : "+j+" Script Numero : "+k);
//                TestScriptsAndParameters scriptAndParameters = new TestScriptsAndParameters();
//                Script script = new Script(scriptExecutions.get(k).getScript());
//                ScriptExecution scriptExecution = new ScriptExecution(scriptExecutions.get(k));
//                scriptAndParameters.setScript(script);
//                scriptAndParameters.getScript().setResult(scriptExecutions.get(k).getResult());
//                scriptAndParameters.setScriptExecution(scriptExecution);
//                //System.out.println("STIMULI: "+scriptAndParameters.getScriptExecution().getStimuli());
//                //scriptAndParameters.getScript().setStimuli(scriptAndParameters.getScriptExecution().getStimuli());
//                scriptAndParameters.setParameters(parameters);
//                //testCasesAndSteps.get(i).getStepsExecutionsAndScripts().get(j).getScriptAndParameters().add(scriptAndParameters);
//                stepExecutionAndScripts.getScriptAndParameters().add(scriptAndParameters);
//
//            }
//            stepExecutionsAndScripts.add(stepExecutionAndScripts);
//        }
//        return stepExecutionsAndScripts;
//    }
}
