/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.*;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author tmorin
 */
public class TestExecution {

    /**
     *
     */
    public TestExecution() {
    }

    /**
     *
     * @param campaign
     * @return
     * @throws ParseException
     */
    public ArrayList<Iterations> getBaselinesFromCampaign(TestCampaign campaign) throws ParseException {
        ArrayList<Iterations> baselines = new ArrayList<>();
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("select IT from Iterations IT where IT.testCampaign.idtestCampaign=:campaignId group by IT.baselineId");
        qry.setInteger("campaignId", campaign.getIdtestCampaign());
        baselines = (ArrayList) qry.list();
        for (Iterations baseline : baselines) {
            Criteria query = session.createCriteria(Iterations.class);
            query.add(Restrictions.eq("baselineId", baseline.getBaselineId()));
            query.setProjection(Projections.max("date"));
            String maxDate = (String) query.uniqueResult();
            baseline.setDate(maxDate);
            baseline.setType("baseline");
        }
        session.close();
        return baselines;
    }

    /**
     *
     * @param baseline
     * @return
     */
    public ArrayList<Iterations> getExecutionsFromBaseline(Iterations baseline) {
        ArrayList<Iterations> executions;
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("Select IT From Iterations IT where IT.testCampaign.idtestCampaign=:campaignId and IT.baselineId=:baselineId order by IT.iterationNumber");
        qry.setInteger("campaignId", baseline.getTestCampaign().getIdtestCampaign());
        qry.setString("baselineId", baseline.getBaselineId());
        executions = (ArrayList) qry.list();
        executions.stream().forEach((execution) -> {
            execution.setType("execution");
        });
        session.close();
        return executions;
    }


    /**
     *
     * @param baselineName
     * @return
     */
        public Iterations prepareIteration(String baselineName) {
        Iterations iterationCreated = new Iterations();
        Iterations iteration0 = this.getIterationFromBaselineName(baselineName);
        int Iteration = this.getIterationNumber(iteration0) + 1;
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(iteration0);
        iterationCreated.setBaselineId(iteration0.getBaselineId());
        iterationCreated.setIterationNumber(Iteration);
        iterationCreated.setTestCampaign(iteration0.getTestCampaign());
        iterationCreated.setDate(main.Main.df.format(new Date()));
        session.save(iterationCreated);
        session.beginTransaction().commit();
        session.close();
        return iterationCreated;
    }

    /**
     *
     * @param iteration
     */
    public void deleteIteration(Iterations iteration) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(iteration);
        session.delete(iteration);
        session.beginTransaction().commit();
        session.close();
    }

    /**
     * For iterations with iteration number 0 (Baseline)
     *
     * @param iteration
     */
    public void deleteBaseline(Iterations iteration) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("delete from Iterations IT where IT.testCampaign.idtestCampaign=:campaignId and IT.baselineId=:baselineID");
        qry.setString("baselineID", iteration.getBaselineId());
        qry.setInteger("campaignId", iteration.getTestCampaign().getIdtestCampaign());
        session.beginTransaction().commit();
        qry = session.createQuery("delete from CaseExecutionsResult CE where CE.baselineId=:baselineID");
        qry.setString("baselineID", iteration.getBaselineId());
        session.beginTransaction().commit();
        qry = session.createQuery("delete from StepExecutionsResult SE where SE.baselineId=:baselineID");
        qry.setString("baselineID", iteration.getBaselineId());
        session.beginTransaction().commit();
        qry = session.createQuery("delete from ScriptExecutionResult SE where SE.baselineId=:baselineID");
        qry.setString("baselineID", iteration.getBaselineId());
        session.beginTransaction().commit();
        session.close();
    }

    /**
     *
     * @param caseExecution
     * @param iterationNumber
     */
    public void resultInDB(CaseExecutions caseExecution, int iterationNumber, String baselineId) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        CaseExecutionsResultId caseResultId = new CaseExecutionsResultId(caseExecution.getIdcaseExecutions(), (byte) iterationNumber);
        CaseExecutionsResult caseResult = new CaseExecutionsResult(caseResultId, caseExecution, caseExecution.getCaseExecutionResult(), caseExecution.getCaseExecutionComment(), baselineId);
        session.save(caseResult);
        Iterator<StepExecutions> itStepExecutions = caseExecution.getStepExecutionses().iterator();
        while (itStepExecutions.hasNext()) {
            StepExecutions stepExecution = itStepExecutions.next();
            StepExecutionsResultId stepResultId = new StepExecutionsResultId(stepExecution.getIdstepExecutions(), (byte) iterationNumber);
            StepExecutionsResult stepResult = new StepExecutionsResult(stepResultId, stepExecution, stepExecution.getStepExecutionResult(), stepExecution.getStepExecutionComment(), baselineId);
            session.save(stepResult);
            Iterator<ScriptExecutions> itScriptExecutions = stepExecution.getScriptExecutionses().iterator();
            while (itScriptExecutions.hasNext()) {
                ScriptExecutions scriptExecution = itScriptExecutions.next();
                ScriptExecutionResultId scriptResultId = new ScriptExecutionResultId(scriptExecution.getIdscriptExecutions(), (byte) iterationNumber);
                ScriptExecutionResult scriptResult = new ScriptExecutionResult(scriptResultId, scriptExecution, scriptExecution.getScriptExecutionResult(), scriptExecution.getScriptExecutionComment(), baselineId);
                session.save(scriptResult);
            }
        }
        session.beginTransaction().commit();
        session.close();
    }
    
    /**
     *
     * @param caseExecution
     * @param iterationNumber
     */
    public void resultInDBRemove(CaseExecutions caseExecution, int iterationNumber) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        CaseExecutionsResultId caseResultId = new CaseExecutionsResultId(caseExecution.getIdcaseExecutions(), (byte) iterationNumber);
        CaseExecutionsResult caseResult = new CaseExecutionsResult(caseResultId, caseExecution, caseExecution.getCaseExecutionResult(), caseExecution.getCaseExecutionComment());
        session.delete(caseResult);
        Iterator<StepExecutions> itStepExecutions = caseExecution.getStepExecutionses().iterator();
        while (itStepExecutions.hasNext()) {
            StepExecutions stepExecution = itStepExecutions.next();
            StepExecutionsResultId stepResultId = new StepExecutionsResultId(stepExecution.getIdstepExecutions(), (byte) iterationNumber);
            StepExecutionsResult stepResult = new StepExecutionsResult(stepResultId, stepExecution, stepExecution.getStepExecutionResult(), stepExecution.getStepExecutionComment());
            session.delete(stepResult);
            Iterator<ScriptExecutions> itScriptExecutions = stepExecution.getScriptExecutionses().iterator();
            while (itScriptExecutions.hasNext()) {
                ScriptExecutions scriptExecution = itScriptExecutions.next();
                ScriptExecutionResultId scriptResultId = new ScriptExecutionResultId(scriptExecution.getIdscriptExecutions(), (byte) iterationNumber);
                ScriptExecutionResult scriptResult = new ScriptExecutionResult(scriptResultId, scriptExecution, scriptExecution.getScriptExecutionResult(), scriptExecution.getScriptExecutionComment());
                session.delete(scriptResult);
            }
        }
        session.beginTransaction().commit();
        session.close();        
    }

    /**
     *
     * @param iteration0
     * @return
     */
    public int getIterationNumber(Iterations iteration0) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("Select MAX(IT.iterationNumber) from Iterations IT where IT.baselineId=:baselineID");
        qry.setString("baselineID", iteration0.getBaselineId());
        List q = qry.list();
        session.close();
        return (int) q.get(0);
    }

    /**
     *
     * @param baselineName
     * @return
     */
    public Iterations getIterationFromBaselineName(String baselineName) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("Select IT From Iterations IT where IT.baselineId=:baselineID and IT.iterationNumber=0");
        qry.setString("baselineID", baselineName);
        List q = qry.list();
        session.close();
        return (Iterations) q.get(0);
    }

    /**
     *
     * @param currCaseEx
     */
    public void initializeStepEx(CaseExecutions currCaseEx) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(currCaseEx);
        Hibernate.initialize(currCaseEx.getStepExecutionses());
        session.close();
    }
//
//    public int getIteration(int campaignID, String baselineID) {
//        SessionFactory factory = sessionFactorySingleton.getInstance();
//        Session session = factory.openSession();
//        Query qry = session.createQuery("SELECT MAX(ST.id.iterationNumber) from StepExecution ST where ST.id.baselineId=:baselineName and ST.id.campaignIdtestCampaign=:campaignID");
//        qry.setInteger("campaignID", campaignID);
//        qry.setString("baselineName", baselineID);
//        List q = qry.list();
//        session.flush();
//        session.clear();
//        session.close();
//        return (int) q.get(0);
//    }
//
//    public ArrayList<StepExecution> getResultsFromIteration(int CampaignID, String baselineName, int iteration) {
//        ArrayList<StepExecution> stepExecutions;
//        SessionFactory factory = sessionFactorySingleton.getInstance();
//        Session session = factory.openSession();
//        session.beginTransaction();
//
//        Query qry = session.createQuery("Select SE From StepExecution SE where SE.id.baselineId=:baselineName and SE.id.campaignIdtestCampaign=:CampaignID and SE.id.iterationNumber=:iterationNumber ORDER BY SE.id.caseExecutionOrder ASC");
//        qry.setInteger("CampaignID", CampaignID);
//        qry.setString("baselineName", baselineName);
//        qry.setInteger("iterationNumber", iteration);
//        List l = qry.list();
//        stepExecutions = (ArrayList) l;
//        //session.getTransaction().commit();
//        session.close();
//        return stepExecutions;
//    }
}
