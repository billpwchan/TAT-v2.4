/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 *
 * @author tmorin
 */
public class ServerConnection {

    private static String oldIP = "0.0.0.0";
    String user = "";
    String pass = "";
    int port = 22;
    private static Session session;

    private ServerConnection(String ip, String user, String password) throws JSchException {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, ip, this.port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (Exception e) {
            //System.out.println(e);
        }

    }

    public static Session getInstance(String ip, String user, String password) throws JSchException {
        if (session == null) {
            new ServerConnection(ip, user, password);
        } else if (ip.equals("") && user.equals("") && password.equals("")) {
            closeInstance();
        } else if (session != null && !oldIP.equals(ip)) {
            session.disconnect();
            new ServerConnection(ip, user, password);
            oldIP = ip;
        }
        return session;
    }

    public static void closeInstance() {
        if (session != null) {
            session.disconnect();
        }
        session=null;
    }

}
