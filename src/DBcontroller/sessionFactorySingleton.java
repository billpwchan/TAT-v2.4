/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import controller.requirements.TabRequirementMainViewController;
import java.sql.Connection;
import javax.persistence.EntityManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author tmorin
 */
public class sessionFactorySingleton {

    private static Configuration cfg;

    private static SessionFactory factory;

    private sessionFactorySingleton() {
        cfg = new Configuration();
        this.cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        ScriptDB scriptHandler = new ScriptDB();
        if (!scriptHandler.getScriptListWithParameters().isEmpty()) {

        } else {
            scriptHandler.fillDB();
        }
    }

    /**
     *
     * @return
     */
    public static SessionFactory getInstance() {
        if (cfg == null) {
            new sessionFactorySingleton();
        } else {
        }
        return factory;
    }

    private sessionFactorySingleton(String DatabasePath) {
        factory.close();
        cfg = new Configuration();
        this.cfg.configure("hibernate.cfg.xml");
        this.cfg.setProperty("hibernate.connection.url", "jdbc:sqlite:" + DatabasePath);
//        this.cfg.setProperty("hibernate.connection.username", "");
//        this.cfg.setProperty("hibernate.connection.password", "");
        factory = cfg.buildSessionFactory();
    }

    /**
     *
     * @param DatabasePath
     * @return
     */
    public static SessionFactory newInstance(String DatabasePath) {
        new sessionFactorySingleton(DatabasePath);
        return factory;
    }

}
