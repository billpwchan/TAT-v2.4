/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.Macro;
import DB.Requirement;
import DB.Script;
import DB.ScriptHasBeenConfigured;
import java.util.HashSet;
import java.util.Set;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author T0155041
 */
public class ObjectCopy {

    public TestCase copyCompleteTestCase(TestCase testCase) {
        TestCase newTestCase = new TestCase(testCase);
        newTestCase.setTestSteps(copyHashStep(newTestCase.getTestSteps()));
        return newTestCase;
    }
    
    public Macro copyCompleteMacro(Macro macro) throws ParseException {
        Macro newMacro = new Macro();
//        Script scriptByScriptIdScript = new Script();
//        scriptByScriptIdScript.setCallName(macro.getScriptByScriptIdScript().getCallName());
//        scriptByScriptIdScript.setCreationDate(macro.getScriptByScriptIdScript().getCreationDate());
//        scriptByScriptIdScript.setDesciption(macro.getScriptByScriptIdScript().getDesciption());
//        scriptByScriptIdScript.setEditionDate(macro.getScriptByScriptIdScript().getEditionDate());
//        scriptByScriptIdScript.setName(macro.getScriptByScriptIdScript().getName());
        newMacro.setScriptByScriptIdScript(macro.getScriptByScriptIdScript());
        newMacro.setScriptByScriptIdScript1(macro.getScriptByScriptIdScript1());
        newMacro.setParamScriptMacros(macro.getParamScriptMacros());
        newMacro.setScriptOrder(macro.getScriptOrder());
        
        return newMacro;
    }
    
    public Script copyCompleteScript(Script script) {
        Script newScript = new Script(script);
        return newScript;
    }
    

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

    public Set copyHashRequirement(Set<Requirement> requirements) {
        Set<Requirement> hashReq = new TreeSet<>();
        Iterator<Requirement> itRequirements = requirements.iterator();
        while (itRequirements.hasNext()) {
            Requirement req = new Requirement(itRequirements.next());
            hashReq.add(req);
        }
        return hashReq;
    }

    public Set copyHashStepHasScripts(TestStep newStep) {
        Set<TestStepHasScript> stepHasScripts= newStep.getTestStepHasScripts();
        Set<TestStepHasScript> hashStepHasScripts = new TreeSet<>(Comparator.comparing(TestStepHasScript::getExecutionOrder));
        Iterator<TestStepHasScript> itStepHasScripts = stepHasScripts.iterator();
        while (itStepHasScripts.hasNext()) {
            TestStepHasScript tshs = new TestStepHasScript(itStepHasScripts.next());
            tshs.setScriptHasBeenConfigureds(copyHashScriptHasBeenConfigured(tshs.getScriptHasBeenConfigureds(), tshs));
            hashStepHasScripts.add(tshs);
            tshs.setTestStep(newStep);
//            /HashSet<ScriptHasBeenConfigured> shbc=(HashSet) tshs.getScriptHasBeenConfigureds();

        }
        return hashStepHasScripts;
    }

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
