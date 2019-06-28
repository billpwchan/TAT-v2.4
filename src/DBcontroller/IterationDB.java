/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import DB.TestCampaign;
import controller.util.CommonFunctions;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author T0155040
 */
public class IterationDB {

    /**
     *
     */
    public IterationDB() {

    }
    public static SessionFactory factory = sessionFactorySingleton.getInstance();
    public static Session session;

    /**
     *
     * @param ite
     */
    public void initializeIteration(Iterations ite) {
        ArrayList<CaseExecutions> caseExecutions = new TestCaseDB().getTestCasesAndResults(ite.getBaselineId(), ite.getIterationNumber());
        TestStepDB testStepDB = new TestStepDB();
        caseExecutions.stream().forEach((test1) -> {
            testStepDB.getStepExecutionAndScriptsResults(test1, ite);
        });
        session = factory.openSession();
        session.update(ite);
        Hibernate.initialize(ite.getTestCampaign());
        session.close();
        ite.setCaseExecutionses(new TreeSet<>(caseExecutions));
    }

    /**
     *
     * @param ite
     * @param iterationResult
     */
    public void setIterationResult(Iterations ite, Double iterationResult) {
        session = factory.openSession();
        session.beginTransaction();
        ite.setIterationResult(iterationResult);
        session.update(ite);
        session.getTransaction().commit();
        session.close();
    }

    /**
     *
     * @param ite
     */
    public void deleteExecution(Iterations ite) {
          session = factory.openSession();
//        Query query = session.createQuery("delete from CaseExecutionsResult CER where CER.id.iterationNumber=:iterationNumber and CER.baselineId=:baselineId");
//        query.setInteger("iterationNumber", ite.getIterationNumber());
//        query.setString("baselineId", ite.getBaselineId());
//        query.executeUpdate();
//        query = session.createQuery("delete from StepExecutionsResult SER where SER.id.iterationNumber=:iterationNumber and SER.baselineId=:baselineId");
//        query.setInteger("iterationNumber", ite.getIterationNumber());
//        query.setString("baselineId", ite.getBaselineId());
//        query.executeUpdate();
//        query = session.createQuery("delete from ScriptExecutionResult SER where SER.id.iterationNumber=:iterationNumber and SER.baselineId=:baselineId");
//        query.setInteger("iterationNumber", ite.getIterationNumber());
//        query.setString("baselineId", ite.getBaselineId());
//        query.executeUpdate();
        if (ite != null) {
            session.delete(ite);
            session.beginTransaction().commit();
        }
        System.out.println(session.createSQLQuery("delete from case_executions where iterations_iditerations not in (select iditerations from iterations)").executeUpdate());
        session.beginTransaction().commit();
        session.createSQLQuery("delete from step_executions where case_executions_idcase_executions not in (select idcase_executions from case_executions)").executeUpdate();
        session.beginTransaction().commit();
        session.createSQLQuery("delete from script_executions where step_executions_idstep_executions not in (select idstep_executions from step_executions)").executeUpdate();
        session.beginTransaction().commit();
        session.createSQLQuery("delete from parameters_execution where script_executions_idscript_executions not in (select idscript_executions from script_executions)").executeUpdate();
        session.beginTransaction().commit();
        session.close();
    }

    /**
     *
     * @param tCampaign
     * @return
     */
    public ArrayList<Iterations> getIterationsFromCampaign(TestCampaign tCampaign) {
        session = factory.openSession();
        ArrayList<Iterations> Iterations = new ArrayList<>();
        Query qry = session.createQuery("select it from Iterations as it where it.testCampaign.idtestCampaign=:idCampaign and iterationNumber=0");
        qry.setParameter("idCampaign", tCampaign.getIdtestCampaign());
        Iterations = (ArrayList) qry.list();
        session.close();
        return Iterations;
    }

    /**
     *
     * @param id
     * @return
     */
    public Iterations getIterationFromBaselineID(String id) {
        session = factory.openSession();
        Query qry = session.createQuery("Select IT From Iterations IT where IT.baselineId=:id");
        qry.setString("baselineId", id);
        List q = qry.list();
        session.close();
        return (Iterations) q.get(0);
    }

    /**
     *
     * @param id
     * @return
     */
    public Iterations getIterationFromID(int id) {
        session = factory.openSession();
        Query qry = session.createQuery("Select IT From Iterations IT where IT.iditerations=:iditerations");
        qry.setInteger("iditerations", id);
        List q = qry.list();
        session.close();
        return (Iterations) q.get(0);
    }

    /**
     *
     * @param ite
     */
    public void deleteIterationFromIterationNum(Iterations ite) {
        session = factory.openSession();
        Query qry = session.createQuery("Delete from Iterations IT where IT.baselineId=:baseline_id and IT.iterationNumber=:iteration_num");
        qry.setParameter("baseline_id", ite.getBaselineId());
        qry.setInteger("iteration_num", ite.getIterationNumber());
        int result = qry.executeUpdate();
        session.beginTransaction().commit();
        session.close();
        CommonFunctions.reportLog.info("In total " + result + " rows are affected by this delete.");
    }
}
