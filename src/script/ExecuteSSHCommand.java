/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.google.common.base.Throwables;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import controller.util.CommonFunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class ExecuteSSHCommand {

    String indexNameReturn;
    String SSHCommand;
    String ip;
    String user;
    String password;

    /**
     * Constructor of ExecuteSSHCommand.
     */
    public void ExecuteSSHCommand() {
    }

    /**
     *
     * @param parameters
     * @param hashMap
     * @return
     * @throws JSchException
     * @throws IOException
     * @throws Exception
     */
    public String run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws JSchException, IOException, Exception {
        Session session;
        ArrayList<String> toReturn = new ArrayList<>();
        try {
            this.ip = parameters.get(1).getValue().trim().replace(',','.');
            this.user = parameters.get(2).getValue().trim();
            this.password = parameters.get(3).getValue().trim();
            this.SSHCommand = parameters.get(4).getValue().trim();
            if (this.SSHCommand.contains("@&Buffer_")) {
                this.SSHCommand = (String) hashMap.get(this.SSHCommand);
            }
            this.indexNameReturn = parameters.get(5).getValue().trim();
            CommonFunctions.debugLog.info(SSHCommand);
            session = ServerConnection.getInstance(this.ip, this.user, this.password);
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.setCommand(this.SSHCommand);
            channel.connect();
            String msg;
            //msg="-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Running||||ACG1||WCH_ACG_201|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"";
            while ((msg = in.readLine()) != null) {
                toReturn.add(msg);
            }
            CommonFunctions.reportLog.info(msg);
            toReturn.add(msg);       // Possibly msg = null / msg = ""
            channel.disconnect();

            hashMap.put(this.indexNameReturn, toReturn);
            return null;
        } catch (JSchException | IOException ex) {
            String stackTrace = Throwables.getStackTraceAsString(ex);
            throw new Exception("Exception in executing SSH command. (IP: " + this.ip + "; User: " + this.user + "; SSHCommand: "+ this.SSHCommand);
        }

    }

    /**
     *
     * @throws JSchException
     */
    public void close() throws JSchException {
        ServerConnection.closeInstance();
    }

}
