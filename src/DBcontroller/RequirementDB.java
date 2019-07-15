/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.Requirement;
import DB.TestStep;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author T0155040
 */
public class RequirementDB {

    /**
     *
     */
    public RequirementDB() {

    }

    /**
     * @return
     */
    public ArrayList<Requirement> getAllRequirement() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        Query qry = session.createQuery("from Requirement req");        // Cause exception (Invalid syntax for Hibernate)
        List l = qry.list();
        ArrayList<Requirement> requirement = (ArrayList) l;
        session.getTransaction().commit();
        session.close();
        return requirement;
    }

    /**
     * @param HashRequirement
     */
    public void updateRequirement(HashSet<Requirement> HashRequirement) {

        SessionFactory factory = sessionFactorySingleton.getInstance();

        Iterator<Requirement> iteRequirement = HashRequirement.iterator();
        while (iteRequirement.hasNext()) {
            Requirement currentRequirement = iteRequirement.next();
            Iterator<TestStep> iteTestStepReq = currentRequirement.getTestSteps().iterator();

            while (iteTestStepReq.hasNext()) {
                TestStep currentStep = iteTestStepReq.next();
                Hibernate.initialize(currentStep.getStepExecutionses());
            }
        }
    }

}
