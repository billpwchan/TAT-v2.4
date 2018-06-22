/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.CaseExecutionsResult;
import DB.Iterations;
import DB.Script;
import DB.ScriptHasBeenConfigured;
import DB.ScriptHasParameters;
import DB.TestCampaign;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.*;

/**
 *
 * @author tmorin
 */
public class TestCaseDB {
    //private Configuration cfg=new Configuration();

    /**
     *
     */
    
    public TestCaseDB() {
        //this.cfg.configure("hibernate.cfg.xml");
    }

    /**
     *
     * @return
     */
    public ArrayList<TestCase> getAllTestCases() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        Query qry = session.createQuery("from TestCase");
        List l = qry.list();
        ArrayList<TestCase> testCases = (ArrayList) l;
        session.getTransaction().commit();
        //EntityManager.persist
        session.close();
        return testCases;
    }
    
    public ArrayList<TestCase> getTestCasesFromMacros(Script Macro) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ArrayList<TestCase> testCases= new ArrayList<>();
//        Query qry = session.createQuery("select tc from TestCase as tc left join tc.testSteps as tctc where tctc.testStepHasScripts.script.idScript=:idScript");
        Query qry = session.createQuery("select tc from TestCase as tc left join tc.testSteps as tctc left join tctc.testStepHasScripts as tctctc where tctctc.script.idScript=:idScript");
        qry.setParameter("idScript", Macro.getIdScript());
        testCases = (ArrayList) qry.list();
        session.close();
        return testCases;
    }

    /**
     *
     * @param testCase
     * @return
     */
    public boolean getTestCaseUsed(TestCase testCase) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        Query qry = session.createQuery("from TestCampaignTestCase tc where tc.testCase.idtestCase=:testCaseID ");
        qry.setParameter("testCaseID", testCase.getIdtestCase());
        List l = qry.list();
        session.close();
        return !l.isEmpty();
    }

    /**
     *
     * @param caseExecution
     */
    public void getCaseAndStepExecutionsFromCaseExecution(CaseExecutions caseExecution) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(caseExecution);
        Hibernate.initialize(caseExecution.getTestCase());
        Hibernate.initialize(caseExecution.getStepExecutionses());
        session.close();
    }

    /**
     *
     * @param id
     * @return
     */
    public TestCase getTestCaseFromID(int id) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        TestCase testCase = new TestCase();
        testCase = (TestCase) session.get(TestCase.class, id);
        session.getTransaction().commit();
        session.close();
        return testCase;
    }

    /**
     *
     * @param testCase
     */
    public void deleteTestCase(TestCase testCase) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.delete(testCase);
        session.beginTransaction().commit();
        session.close();
    }

    /**
     *
     * @param testcase
     */
    public void getAllFromCase(TestCase testcase) {
        //System.out.println("1");
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(testcase);
        Iterator<TestStep> itSteps = testcase.getTestSteps().iterator();
        while (itSteps.hasNext()) {
            TestStep testStep = itSteps.next();
            Hibernate.initialize(testStep.getRequirements());
            Iterator<TestStepHasScript> itTestStepHasScript = testStep.getTestStepHasScripts().iterator();
            while (itTestStepHasScript.hasNext()) {
                TestStepHasScript testStepHasScriptObject = itTestStepHasScript.next();
                //testStepHasScriptObject.setTestStep(testStep);
                Hibernate.initialize(testStepHasScriptObject.getScriptHasBeenConfigureds());
                Iterator<ScriptHasBeenConfigured> itScriptHasBeenConfigured = testStepHasScriptObject.getScriptHasBeenConfigureds().iterator();
                while (itScriptHasBeenConfigured.hasNext()) {
                    Hibernate.initialize(itScriptHasBeenConfigured.next().getRefScriptHasBeenConfigured()); //Potential Bug. No row with the given identifier exists.
                }
                System.out.println("SCRIPT HAS BEEN CONFIGURE = "+ testStepHasScriptObject.getScriptHasBeenConfigureds());
                Iterator<ScriptHasParameters> itScHasParameters = testStepHasScriptObject.getScript().getScriptHasParameterses().iterator();
                while (itScHasParameters.hasNext()) {
                    ScriptHasParameters scHasParameters = itScHasParameters.next();
                    Hibernate.initialize(scHasParameters.getParameters());
                }
            }
        }
        session.close();
    }

    /**
     *
     * @param baselineID
     * @param iterationNumber
     * @return
     */
    public ArrayList<CaseExecutions> getTestCasesAndResults(String baselineID, int iterationNumber) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ArrayList<CaseExecutions> caseExecutions;
        Query qry2 = session.createQuery("Select IT from Iterations IT where IT.baselineId=:baselineID and IT.iterationNumber=0");
        qry2.setString("baselineID", baselineID);
        Iterations iteration = (Iterations) qry2.list().get(0);
        caseExecutions = new ArrayList<>(iteration.getCaseExecutionses());
        Query qry = session.createQuery("from CaseExecutionsResult CER where CER.id.caseExecutionsIdcaseExecutions=:idCase and CER.id.iterationNumber=:iterationNumber");
        for (CaseExecutions caseExecution : caseExecutions) {
            Hibernate.initialize(caseExecution.getTestCase());
            if (iterationNumber == 0) {
                caseExecution.setCaseExecutionResult("");
                caseExecution.setCaseExecutionComment("");
            } else {
                qry.setInteger("idCase", caseExecution.getIdcaseExecutions());
                qry.setInteger("iterationNumber", iterationNumber);
                List l = qry.list();
                if (!l.isEmpty()) {
                    CaseExecutionsResult caseResult = (CaseExecutionsResult) l.get(0);
                    caseExecution.setCaseExecutionResult(caseResult.getResult());
                    caseExecution.setCaseExecutionComment(caseResult.getComment());
                    caseExecution.setOriginalResult(caseResult.getResult());
                    caseExecution.setCaseExecutionResultObj(caseResult);
                } else {
                    caseExecution.setCaseExecutionResult("NExec");
                    caseExecution.setCaseExecutionComment("");
                }
            }
        }
        session.close();
        return caseExecutions;
    }

    /**
     *
     * @param campaignID
     * @return
     */
    public long numberoOfCasesInCampaign(int campaignID) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query query = session.createQuery("select count(*) from TestCampaignTestCase as TCTC where TCTC.id.testCampaignIdtestCampaign=:campaignID");
        query.setInteger("campaignID", campaignID);
        Long count = (Long) query.uniqueResult();
        session.close();
        return count;
    }

    /**
     *
     * @param testCaseID
     */
    public void deleteCase(int testCaseID) {
        ArrayList<TestStep> testSteps;
        TestStepDB testStepHandler = new TestStepDB();
        testSteps = testStepHandler.getStepsFromCases(testCaseID);
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query query = session.createQuery("Select tctc.id.testCampaignIdtestCampaign from TestCampaignTestCase tctc where tctc.id.testCaseIdtestCase = :testCaseID group by tctc.id.testCampaignIdtestCampaign");
        query.setInteger("testCaseID", testCaseID);
        ArrayList<Integer> campaignID = (ArrayList) query.list();
        for (Integer campaignID1 : campaignID) {
            query = session.createQuery("delete from ScriptParameters SHP where SHP.id.scriptExecutionStepExecutionCampaignIdtestCampaign=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
            query = session.createQuery("delete from ScriptExecution SE where SE.id.stepExecutionCampaignIdtestCampaign=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
            query = session.createQuery("delete from StepExecution SE where SE.id.campaignIdtestCampaign=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
            query = session.createQuery("delete from StepExecution SE where SE.id.campaignIdtestCampaign=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
            query = session.createQuery("delete from TestCampaignTestCase TCTC where TCTC.id.testCampaignIdtestCampaign=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
            query = session.createQuery("delete from TestCampaign TC where TC.id=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
            query = session.createQuery("delete from TestCampaign TC where TC.id=:campaignID");
            query.setInteger("campaignID", campaignID1);
            query.executeUpdate();
        }

        for (TestStep testStep : testSteps) {
            query = session.createQuery("delete from ScriptHasParametersConfigured SHPC where SHPC.id.testStepHasScriptTestStepIdtestStep=:stepID");
            query.setInteger("stepID", testStep.getIdtestStep());
            query.executeUpdate();
            query = session.createQuery("delete from TestStepHasScript TSHS where TSHS.id.testStepIdtestStep=:stepID");
            query.setInteger("stepID", testStep.getIdtestStep());
            query.executeUpdate();
            query = session.createQuery("delete from TestStep TS where TS.id=:stepID");
            query.setInteger("stepID", testStep.getIdtestStep());
            query.executeUpdate();
        }

        query = session.createQuery("delete from TestCase TC where TC.id=:testCaseID");
        query.setInteger("testCaseID", testCaseID);
        query.executeUpdate();
        session.beginTransaction().commit();
        session.close();
    }

//
//    @SuppressWarnings("empty-statement")

    /**
     *
     * @param testCampaign
     * @return
     */
        public ArrayList<TestCase> getTestCasesFromTestCampaign(TestCampaign testCampaign) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ArrayList<TestCase> testCases = new ArrayList<>();
        Query qry = session.createQuery("select tc from TestCase as tc left join tc.testCampaignTestCases as tctc where tctc.testCampaign.idtestCampaign=:idCampaign order by tctc.id.caseOrder");
        qry.setParameter("idCampaign", testCampaign.getIdtestCampaign());
        testCases = (ArrayList) qry.list();
        session.close();
        return testCases;
    }
}
