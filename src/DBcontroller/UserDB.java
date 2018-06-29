/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author T0155040
 */
public class UserDB {

    /**
     *
     */
    public UserDB() {

    }

    /**
     *
     * @param login
     * @param password
     * @return
     */
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
