/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.Macro;
import DB.ParamScriptMacro;
import DB.Requirement;
import DB.Script;
import DB.ScriptHasBeenConfigured;
import java.util.Set;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author T0155041
 */
public class ObjectCopy {

    /**
     *
     * @param testCase
     * @return
     */
    public TestCase copyCompleteTestCase(TestCase testCase) {
        TestCase newTestCase = new TestCase(testCase);
        newTestCase.setTestSteps(copyHashStep(newTestCase.getTestSteps()));
        
        return newTestCase;
    }

    /**
     *
     * @param script
     * @return
     */
    public Script copyCompleteScript(Script script) {
        Script newScript = new Script(script);
        newScript.setMacrosForScriptIdScript(copyHashMacro(newScript.getMacrosForScriptIdScript()));
        return newScript;
    }

    /**
     *
     * @param setMacros
     * @return
     */
    public Set copyHashMacro(Set<Macro> setMacros) {
        Set<Macro> hashMacro = new TreeSet<>(Comparator.comparing(Macro::getScriptOrder));
        Iterator<Macro> itMacro = setMacros.iterator();
        while (itMacro.hasNext()) {
            Macro currMacro = itMacro.next();
            Macro newMacro = new Macro(currMacro);
            Set PSM = copyHashPSM(newMacro.getParamScriptMacros());
            newMacro.setParamScriptMacros(PSM);
            hashMacro.add(newMacro);
        }
        return hashMacro;
    }

    /**
     *
     * @param setPSM
     * @return
     */
    public Set copyHashPSM(Set<ParamScriptMacro> setPSM) {
        Set<ParamScriptMacro> hashPSM = new TreeSet<>(Comparator.comparing(ParamScriptMacro::getParamOrder));
        Iterator<ParamScriptMacro> itPSM = setPSM.iterator();
        while (itPSM.hasNext()) {
            ParamScriptMacro currPSM = itPSM.next();
            ParamScriptMacro newPSM = new ParamScriptMacro(currPSM);
            hashPSM.add(newPSM);
        }
        return hashPSM;
    }

    /**
     *
     * @param setSteps
     * @return
     */
    public Set copyHashStep(Set<TestStep> setSteps) {
        Set<TestStep> hashStep = new TreeSet<>(Comparator.comparing(TestStep::getStepOrder));
        Iterator<TestStep> itSteps = setSteps.iterator();
        while (itSteps.hasNext()) {
            TestStep currStep = itSteps.next();
            TestStep newStep = new TestStep(currStep); //constructor has-> this.stepOrder = currStep.getStepOrder
            Set setRequirements = copyHashRequirement(newStep.getRequirements());
            newStep.setRequirements(setRequirements);
            System.out.println("stepOrder: " + newStep.getStepOrder());
            newStep.setStepOrder(currStep.getStepOrder()); //i just added this just in case, still does not work
            System.out.println(newStep.getStepOrder());
            newStep.setTestStepHasScripts(copyHashStepHasScripts(newStep));
            hashStep.add(newStep);
            
        }
        return hashStep;
    }

    /**
     *
     * @param requirements
     * @return
     */
    public Set copyHashRequirement(Set<Requirement> requirements) {
        Set<Requirement> hashReq = new TreeSet<>();
        Iterator<Requirement> itRequirements = requirements.iterator();
        while (itRequirements.hasNext()) {
            Requirement req = new Requirement(itRequirements.next());
            hashReq.add(req);
        }
        return hashReq;
    }

    /**
     *
     * @param newStep
     * @return
     */
    public Set copyHashStepHasScripts(TestStep newStep) {
        Set<TestStepHasScript> hashStepHasScripts = new TreeSet<>(Comparator.comparing(TestStepHasScript::getExecutionOrder));
        Iterator<TestStepHasScript> itStepHasScripts = newStep.getTestStepHasScripts().iterator();
        while (itStepHasScripts.hasNext()) {
            TestStepHasScript tshs = new TestStepHasScript(itStepHasScripts.next());
            tshs.setScriptHasBeenConfigureds(copyHashScriptHasBeenConfigured(tshs.getScriptHasBeenConfigureds(), tshs));
            
            tshs.setTestStep(newStep);
            hashStepHasScripts.add(tshs);
//            /HashSet<ScriptHasBeenConfigured> shbc=(HashSet) tshs.getScriptHasBeenConfigureds();

        }
        return hashStepHasScripts;
    }

    /**
     *
     * @param shbc
     * @param tshs
     * @return
     */
    public Set copyHashScriptHasBeenConfigured(Set<ScriptHasBeenConfigured> shbc, TestStepHasScript tshs) {
        Set<ScriptHasBeenConfigured> hashScriptHasBeenConfigured = new TreeSet<>(Comparator.comparing(ScriptHasBeenConfigured::getParamOrder));
        Iterator<ScriptHasBeenConfigured> itShbc = shbc.iterator();
        while (itShbc.hasNext()) {
            ScriptHasBeenConfigured scriptHasBeenConfigured = new ScriptHasBeenConfigured(itShbc.next(), tshs);
            hashScriptHasBeenConfigured.add(scriptHasBeenConfigured);
        }
        Comparator.comparing(ScriptHasBeenConfigured::getParamOrder);
        return hashScriptHasBeenConfigured;
    }

}
