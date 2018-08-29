/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
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
public class DacSimulator {

    String indexNameReturn;
    String epqState;
    String ip;
    String user;
    String password;

    /**
     * Constructor of ExecuteSSHCommand.
     */
    public void ExecuteSSHCommand() {
    }

//    @Override
//    public void InitParametersType() {
//    }
//    @Override
//    public ArrayList<ParametersScript> getParametersType() {
//        return null;
//    }

    /**
     *
     * @param parameters
     * @param hashMap
     * @return
     * @throws JSchException
     * @throws IOException
     */
        public String run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws JSchException, IOException {
        Session session;
//        this.station = "WCH";
//        this.equipmentCode = "ACG1";
//        this.equipmentNumber = "WCH_ACG_201";
//        this.equipmentDescription = "Air Curtain Group";
//        this.stateLowLevel = "Stop";
//        this.stateHighLevel = "Running";
        ArrayList<String> toReturn = new ArrayList<>();
        this.ip = parameters.get(1).getValue().trim();
        this.user = parameters.get(2).getValue().trim();
        this.password = parameters.get(3).getValue().trim();
        this.epqState = parameters.get(4).getValue().trim();
        if (this.epqState.contains("@&Buffer_")) {
            this.epqState = (String) hashMap.get(this.epqState);
        }
        this.indexNameReturn = parameters.get(5).getValue().trim();

        session = ServerConnection.getInstance(this.ip, this.user, this.password);
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.setCommand("echo extvar set "+this.epqState+ "| dacsimulator");
        channel.connect();
        String msg;
        //msg="-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Running||||ACG1||WCH_ACG_201|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"";

//        toReturn.add("1234");
//        toReturn.add("-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Running||||ACG1||WCH_ACG_201|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"");
//        toReturn.add("-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Running||||ACG1||WCH_ACG_202|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"");
//        toReturn.add("-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1.5\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Stopped||||ACG1||WCH_ACG_201|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"");
//        toReturn.add("-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1.5\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Running||||ACG1||WCH_ACG_202|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"");
        while ((msg = in.readLine()) != null) {
            //System.out.println("READLINE : "+msg);
            toReturn.add(msg);
        }
        channel.disconnect();

        hashMap.put(this.indexNameReturn, toReturn);
        //session.disconnect();
        //System.out.println("RESULT = "+this.result);
        return null;
    }

    /**
     *
     * @throws JSchException
     */
    public void close() throws JSchException {
        Session session;
        session = ServerConnection.getInstance("", "", "");
    }

}
