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

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class ScadaCheck {

    String station;
    String stationFound;

    String equipmentCode;
    String equipmentCodeFound;

    String equipmentNumber;
    String equipmentNumberFound;

    String equipmentDescription;
    String equipmentDescriptionFound;

    String stateLowLevel;
    String stateLowLevelFound;

    String stateHighLevel;
    String stateFound;

    char severity;
    char severityFound;

    String ip;
    String user;
    String password;
    /**
     * Result of the test.
     */
    String result = "";

    /**
     * Constructor of ScadaCheck.
     */
    public void ScadaCheck() {
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
     * @return
     * @throws JSchException
     * @throws IOException
     */
        public String run(ArrayList<ParametersExecution> parameters) throws JSchException, IOException {
        Session session;
        boolean highLevelFound = false;
        boolean lowLevelFound = false;
//        this.station = "WCH";
//        this.equipmentCode = "ACG1";
//        this.equipmentNumber = "WCH_ACG_201";
//        this.equipmentDescription = "Air Curtain Group";
//        this.stateLowLevel = "Stop";
//        this.stateHighLevel = "Running";

        this.station = parameters.get(0).getValue();
        this.equipmentCode = parameters.get(1).getValue();
        this.equipmentNumber = parameters.get(2).getValue();
        this.equipmentDescription = parameters.get(3).getValue();
        this.stateLowLevel = parameters.get(4).getValue();
        this.stateHighLevel = parameters.get(5).getValue();
        this.ip = parameters.get(6).getValue();
        this.user = parameters.get(7).getValue();
        this.password = parameters.get(8).getValue();

        session = ServerConnection.getInstance(this.ip, this.user, this.password);
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.setCommand("scsolsshow -lEventList -r | tail -f -n 10");
        channel.connect();
        String msg;
        //msg="-18622 1 13094 2142351 0 \":WCH:ECS:ACG10001\" \"1\" 0 0 0 0 0 \";0||||||Air Curtain Group|Running Status|Running||||ACG1||WCH_ACG_201|$$;:WCH:ECS:ACG10001:dciECS-RUNSTA:dal.valueAlarmVector\" 0 0 0 1430215853 835800 1430215853 835800 1 13 0 0 \"SILENV\" \"\" \"\" \"\"";
        while ((msg = in.readLine()) != null && (lowLevelFound == false || highLevelFound == false)) {
            lineDecoder(msg);
            //System.out.println("TEST = "+this.equipmentNumber.equals(this.equipmentNumberFound));
            //System.out.println("TEST = "+this.stateHighLevel.contains(stateFound));
            if (this.equipmentNumber.equals(this.equipmentNumberFound) && this.stateFound.equals(stateHighLevel) && highLevelFound == false) {
                highLevelFound = true;
                comparison("High Level");
            } else if (this.equipmentNumber.equals(this.equipmentNumberFound) && this.stateFound.equals(stateLowLevel) && lowLevelFound == false) {
                lowLevelFound = true;
                comparison("Low Level");
            }
        }
        if (lowLevelFound == false) {
            this.result = this.result + " Low Level not found";
        }
        if (highLevelFound == false) {
            this.result = this.result + " High Level not found";
        }
        if (result.equals("")) {
            this.result = "OK";
        }
        channel.disconnect();
        //session.disconnect();
        //System.out.println("RESULT = "+this.result);
        return this.result;
    }

    /**
     *
     */
    public void result() {
        this.result = "success";
    }

    /**
     *
     * @param msg
     */
    public void lineDecoder(String msg) {
        int index1;
        int index2;
        index1 = msg.indexOf("||||||") + 6;
        index2 = msg.indexOf("|", index1);
        equipmentDescriptionFound = msg.substring(index1, index2);
        //System.out.println("INDEX 1= " + index1);
        this.severityFound = msg.charAt(index1 - 15);
        
        index1 = msg.indexOf("|", index2) + 1;
        index2 = msg.indexOf("|", index1);
        index1 = msg.indexOf("|", index2) + 1;
        index2 = msg.indexOf("|", index1);
        this.stateFound = msg.substring(index1, index2);
        index1 = msg.indexOf("|", index2) + 4;
        index2 = msg.indexOf("|", index1);
        this.equipmentCodeFound = msg.substring(index1, index2);
        index1 = msg.indexOf("|", index2) + 2;
        index2 = msg.indexOf("|", index1);
        this.equipmentNumberFound = msg.substring(index1, index2);
        index1 = msg.indexOf(":", index2) + 1;
        index2 = msg.indexOf(":", index1);
        this.stationFound = msg.substring(index1, index2);
        //System.out.println("DESCRIPTION "+ equipmentDescriptionFound+" state= "+stateFound+" equipment code= "+equipmentCodeFound+" equipmentNumber= "+equipmentNumberFound+" Station= "+stationFound);
    }

    /**
     *
     * @param state
     */
    public void comparison(String state) {
        if (!this.equipmentCodeFound.equals(this.equipmentCode)) {
            this.result = this.result + "Problem with the equipment code of the " + state + "\n";
        }

        if (!this.equipmentDescription.equals(this.equipmentDescriptionFound)) {
            this.result = this.result + "Problem with the equipment description of the " + state + "\n";
        }

        if (!this.stationFound.equals(this.station)) {
            this.result = this.result + "Problem with the station of the " + state + "\n";
        }
        
        if (!(this.severityFound == this.severity)) {
            this.result = this.result + "Problem with the severity of the " + state + "\n";
        }
        
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
