/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import java.util.ArrayList;
import java.util.TreeSet;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author T0155040
 */
public class ResultsDB {

    /**
     *
     */
    public ResultsDB() {

    }

    /**
     *
     * @param ite
     */
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

    /**
     *
     * @param casesChanges
     */
    public void validCaseResultsChange(ObservableList<CaseExecutions> casesChanges) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        for (int i = 0; i < casesChanges.size(); i++) {
            casesChanges.get(i).getCaseExecutionResultObj().setResult(casesChanges.get(i).getCaseExecutionResult());
            if (casesChanges.get(i).getCaseExecutionResultObj().getNewComment()!=null) {
                System.out.println("NO NEW RESULT");
                casesChanges.get(i).getCaseExecutionResultObj().setComment(casesChanges.get(i).getCaseExecutionResultObj().getComment().concat("\n" + casesChanges.get(i).getCaseExecutionResultObj().getNewComment()));
            }
            session.update(casesChanges.get(i).getCaseExecutionResultObj());
        }
        session.getTransaction().commit();
        session.close();
        Notifications notificationBuilder = Notifications.create()
                .title("Results Change")
                .text("New results validation completed")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notificationBuilder.showInformation();
    }

}
