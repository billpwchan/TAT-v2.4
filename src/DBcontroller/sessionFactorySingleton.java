/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author tmorin
 */
public class sessionFactorySingleton {

    private static Configuration cfg;

    private static SessionFactory factory;

    private sessionFactorySingleton() {
        cfg = new Configuration().configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        factory = cfg.buildSessionFactory(builder.build());

        ScriptDB scriptHandler = new ScriptDB();
        if (scriptHandler.getScriptListWithParameters().isEmpty()) {
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
        sessionFactorySingleton.cfg.configure("hibernate.cfg.xml");
        sessionFactorySingleton.cfg.setProperty("hibernate.connection.url", "jdbc:sqlite:" + DatabasePath);
//        this.cfg.setProperty("hibernate.connection.username", "");
//        this.cfg.setProperty("hibernate.connection.password", "");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        factory = cfg.buildSessionFactory(builder.build());
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
