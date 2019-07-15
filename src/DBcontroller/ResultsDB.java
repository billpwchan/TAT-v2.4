/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author T0155040
 */
public class ResultsDB {

    /**
     *
     */
    public ResultsDB() {

    }

    /**
     * @param casesChanges
     */
    public void validCaseResultsChange(ObservableList<CaseExecutions> casesChanges) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        for (CaseExecutions casesChange : casesChanges) {
            casesChange.getCaseExecutionResultObj().setResult(casesChange.getCaseExecutionResult());
            if (casesChange.getCaseExecutionResultObj().getNewComment() != null) {
                casesChange.getCaseExecutionResultObj().setComment(casesChange.getCaseExecutionResultObj().getComment().concat("\n" + casesChange.getCaseExecutionResultObj().getNewComment()));
            }
            session.update(casesChange.getCaseExecutionResultObj());
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
