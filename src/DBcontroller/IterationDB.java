/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import DB.TestCampaign;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author T0155040
 */
public class IterationDB {

    public IterationDB() {

    }

    public void initializeIteration(Iterations ite) {
        System.out.println("Start time to construct object : " + System.currentTimeMillis());
        ArrayList<CaseExecutions> caseExecutions = new TestCaseDB().getTestCasesAndResults(ite.getBaselineId(), ite.getIterationNumber());
        TestStepDB testStepDB = new TestStepDB();
        caseExecutions.stream().forEach((test1) -> {
            testStepDB.getStepExecutionAndScriptsResults(test1, ite);
        });
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();

        session.update(ite);
        Hibernate.initialize(ite.getTestCampaign());
        session.close();
        ite.setCaseExecutionses(new TreeSet<>(caseExecutions));
        System.out.println("End time to construct object : " + System.currentTimeMillis());
    }

    public void setIterationResult(Iterations ite, Double iterationResult) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        ite.setIterationResult(iterationResult);
        session.update(ite);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteExecution(Iterations ite) {
        System.out.println("ENTER IN DELETE");
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query query = session.createQuery("delete from CaseExecutionsResult CER where CER.id.iterationNumber=:iterationNumber");
        query.setInteger("iterationNumber", ite.getIterationNumber());
        query.executeUpdate();
        query = session.createQuery("delete from StepExecutionsResult SER where SER.id.iterationNumber=:iterationNumber");
        query.setInteger("iterationNumber", ite.getIterationNumber());
        query.executeUpdate();
        query = session.createQuery("delete from ScriptExecutionResult SER where SER.id.iterationNumber=:iterationNumber");
        query.setInteger("iterationNumber", ite.getIterationNumber());
        query.executeUpdate();
        session.delete(ite);
        session.beginTransaction().commit();
        session.close();
        System.out.println("DELETE FINISHED");
    }
    
    public ArrayList<Iterations> getIterationsFromCampaign(TestCampaign tCampaign){
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ArrayList<Iterations> Iterations = new ArrayList<>();
        Query qry = session.createQuery("select it from Iterations as it where it.testCampaign.idtestCampaign=:idCampaign and iterationNumber=0");
        qry.setParameter("idCampaign", tCampaign.getIdtestCampaign());
        Iterations = (ArrayList) qry.list();
        session.close();
        return Iterations;
    }
    
    public Iterations getIterationFromBaselineID(String id) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("Select IT From Iterations IT where IT.baselineId=:id");
        qry.setString("baselineId", id);
        List q = qry.list();
        session.close();
        return (Iterations) q.get(0);
    }
    
    public Iterations getIterationFromID(int id) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("Select IT From Iterations IT where IT.iditerations=:iditerations");
        qry.setInteger("iditerations", id);
        List q = qry.list();
        session.close();
        return (Iterations) q.get(0);
    }
    
    public void deleteIterationFromIterationNum(Iterations ite){
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("Delete from Iterations IT where IT.baselineId=:baseline_id and IT.iterationNumber=:iteration_num");
        qry.setParameter("baseline_id", ite.getBaselineId());
        qry.setInteger("iteration_num", ite.getIterationNumber());
        int result = qry.executeUpdate();
        System.out.println("Rows affected: "+ result);
//        session.delete(ite);
        session.beginTransaction().commit();
        session.close();
        System.out.println("DELETE FINISHED");        
    }
    
//    public void getIterations(String baselineID){
//        SessionFactory factory=sessionFactorySingleton.getInstance();
//        Session session = factory.openSession();
//        Query query= session.create
//    }

}
