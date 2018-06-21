/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.Parameters;
import DB.ScriptHasParameters;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author tmorin
 */
public class ParametersDB {

    /**
     *
     */
    public ParametersDB() {

    }

    /**
     *
     * @return
     */
    public ArrayList<Parameters> getAllParams() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from Parameters");
        ArrayList<Parameters> params = new ArrayList<>();
        params = (ArrayList) qry.list();
        session.close();
        return params;
    }

}
