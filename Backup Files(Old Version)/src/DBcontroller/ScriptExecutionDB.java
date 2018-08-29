
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;
import DB.ScriptExecutions;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author MorinT
 */
public class ScriptExecutionDB {

    /**
     * Default constructor of this class.
     */
    public ScriptExecutionDB() {
        //init();
    }

    /**
     *
     * @param scriptExecutions
     */
    public void getScriptFromScriptExecution(ScriptExecutions scriptExecutions) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.update(scriptExecutions);
        Hibernate.initialize(scriptExecutions.getScript());
        session.close();
    }

}
