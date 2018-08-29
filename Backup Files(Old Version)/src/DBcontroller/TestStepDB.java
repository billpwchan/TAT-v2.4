/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import DB.ScriptExecutions;
import DB.StepExecutions;
import DB.StepExecutionsResult;
import DB.TestStep;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.*;

/**
 *
 * @author tmorin
 */
public class TestStepDB {

    /**
     *
     */
    public TestStepDB() {
    }

    /**
     *
     * @param stepExecution
     */
    public void getStepAndScriptsFromStepExecution(StepExecutions stepExecution) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(stepExecution);
        Hibernate.initialize(stepExecution.getTestStep());
        Hibernate.initialize(stepExecution.getScriptExecutionses());
        session.close();
    }

    /**
     *
     * @param CaseID
     * @return
     */
    public ArrayList<TestStep> getStepsFromCases(int CaseID) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ArrayList<TestStep> testSteps = new ArrayList<>();
        Query qry = session.createQuery("select ts from TestStep as ts left join ts.testCases tstc where tstc.idtestCase=:idCase order by ts."
                + " ASC");
        qry.setParameter("idCase", CaseID);
        List l = qry.list();
        testSteps = (ArrayList) l;
        session.close();
        return testSteps;
    }

    /**
     *
     * @param caseExecution
     * @return
     */
    public CaseExecutions getStepsScriptsParametersFromCaseExecution(CaseExecutions caseExecution) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(caseExecution);
        Iterator<StepExecutions> itSteps = caseExecution.getStepExecutionses().iterator();
        while (itSteps.hasNext()) {
            StepExecutions step = new StepExecutions();
            step = itSteps.next();
            Iterator<ScriptExecutions> itScripts = step.getScriptExecutionses().iterator();
            while (itScripts.hasNext()) {
                ScriptExecutions script = new ScriptExecutions();
                script = itScripts.next();
                Hibernate.initialize(script.getParametersExecutions());
            }
        }
        session.close();
        return caseExecution;
    }

    /**
     *
     * @param stepExecution
     */
    public void getStepFromStepExecution(StepExecutions stepExecution) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(stepExecution);
        Hibernate.initialize(stepExecution.getTestStep());
        session.close();
    }

    /**
     *
     * @param caseExecution
     * @param iteration
     */
    public void getStepExecutionAndScriptsResults(CaseExecutions caseExecution, Iterations iteration) {
        ScriptDB scriptHandler = new ScriptDB();
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(caseExecution);
        Hibernate.initialize(caseExecution.getStepExecutionses());
        Query qryStep = session.createQuery("from StepExecutionsResult SER where SER.id.stepExecutionsIdstepExecutions=:idStep and SER.id.iterationNumber=:iterationNumber");
        Iterator<StepExecutions> itTestStepExecutions = caseExecution.getStepExecutionses().iterator();
        while (itTestStepExecutions.hasNext()) {
            StepExecutions stepExecution = itTestStepExecutions.next();
            Hibernate.initialize(stepExecution.getTestStep().getRequirements());
            if (iteration.getIterationNumber() == 0) {
                stepExecution.setStepExecutionResult("");
                stepExecution.setStepExecutionComment("");
            } else {
                qryStep.setInteger("idStep", stepExecution.getIdstepExecutions());
                qryStep.setInteger("iterationNumber", iteration.getIterationNumber());
                List l = qryStep.list();
                if (!l.isEmpty()) {
                    StepExecutionsResult stepExecutionResult = (StepExecutionsResult) l.get(0);
                    stepExecution.setStepExecutionResult(stepExecutionResult.getResult());
                    stepExecution.setStepExecutionComment(stepExecutionResult.getComment());
                } else {
                    stepExecution.setStepExecutionResult("NExec");
                    stepExecution.setStepExecutionComment("");
                }
            }
            Hibernate.initialize(stepExecution.getTestStep());
            scriptHandler.getScriptParametersAndResults(session, stepExecution, iteration);
        }
        session.close();
    }

    /**
     *
     * @param caseExecution
     * @return
     */
    public ArrayList<StepExecutions> getStepExecutionsAndStepsFromCaseExecution(CaseExecutions caseExecution) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.merge(caseExecution);
        ArrayList<StepExecutions> stepExecutions = (ArrayList) caseExecution.getStepExecutionses();
        session.close();
        return stepExecutions;
    }

}
