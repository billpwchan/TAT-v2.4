/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import DB.User;
import java.util.ArrayList;
import java.util.TreeSet;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author T0155040
 */
public class UserDB {

    public UserDB() {

    }

    public User getUser(String login, String password) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        Query qry = session.createQuery("from User u where u.login=:userName and u.password=:password");
        qry.setString("userName", login);
        qry.setString("password", password);
        User user = (User) qry.uniqueResult();
        session.close();
        return user;
    }

}
