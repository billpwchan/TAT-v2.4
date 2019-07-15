
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author MorinT
 */
public class StepExecutionDB {

    /**
     * Default constructor of this class.
     */
    public StepExecutionDB() {
    }

    /**
     * @param campaignID
     * @param baselineID
     * @param iteration
     */
    public void deleteStepExecution(int campaignID, String baselineID, int iteration) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("delete StepExecution SE where SE.id.campaignIdtestCampaign=:campaignID and SE.id.baselineId=:baselineID and SE.id.iterationNumber=:iteration");
        qry.setInteger("campaignID", campaignID);
        qry.setString("baselineID", baselineID);
        qry.setInteger("iteration", iteration);
        qry.executeUpdate();
        session.beginTransaction().commit();
        session.close();
    }

}
