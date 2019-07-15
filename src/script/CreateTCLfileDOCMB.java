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
import java.util.concurrent.TimeUnit;


/**
 * @author Kelvin Cheung
 * @version 1.0
 */

public class CreateTCLfileDOCMB {
    String indexNameReturn;
    String SSHCommand;
    String ip;
    String user;
    String password;
    String RTServerEnvName;
    String databaseCommandPath0;
    String databaseCommandPath1;
    String CommandType;
    int CommandValue0;
    int CommandValue1;
    int CommandValue2;
    int CommandValue3;
    int CommandValue4;
    int CommandValue5;
    int CommandValue6;


    /**
     * Constructor of ExcuteSSHCommandCreateTCLfile.
     */
    public void ExcuteSSHCommand() {
    }

    /**
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
            this.ip = parameters.get(1).getValue().trim().replace(',', '.');
            this.user = parameters.get(2).getValue().trim();
            this.password = parameters.get(3).getValue().trim();
            this.RTServerEnvName = parameters.get(4).getValue().trim();
            this.databaseCommandPath0 = parameters.get(5).getValue().trim();
            this.databaseCommandPath1 = parameters.get(6).getValue().trim();
            this.CommandType = parameters.get(7).getValue().trim();
            this.CommandValue0 = (int) Double.parseDouble(parameters.get(8).getValue().trim());
            this.CommandValue1 = (int) Double.parseDouble(parameters.get(9).getValue().trim());
            this.CommandValue2 = (int) Double.parseDouble(parameters.get(10).getValue().trim());
            this.CommandValue3 = (int) Double.parseDouble(parameters.get(11).getValue().trim());
            this.CommandValue4 = (int) Double.parseDouble(parameters.get(12).getValue().trim());
            this.CommandValue5 = (int) Double.parseDouble(parameters.get(13).getValue().trim());
            this.CommandValue6 = (int) Double.parseDouble(parameters.get(14).getValue().trim());

            switch (CommandType) {
                case "DI":
                case "DO":
                    this.CommandType = "$SCSCTL_DIOV";
                    break;
                case "AI":
                case "AO":
                    this.CommandType = "$SCSCTL_AIOV";
                    break;
            }
            if (CommandValue6 == 1) {
                CommandValue6 = 7;
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue6 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            } else if (CommandValue5 == 1) {
                CommandValue5 = 6;
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue5 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            } else if (CommandValue4 == 1) {
                CommandValue4 = 5;
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue4 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            } else if (CommandValue3 == 1) {
                CommandValue3 = 4;
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue3 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            } else if (CommandValue2 == 1) {
                CommandValue2 = 3;
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue2 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            } else if (CommandValue1 == 1) {
                CommandValue1 = 2;
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue1 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            } else {
                this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                        + CommandType + " " + CommandValue0 + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
            }
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
            TimeUnit.MILLISECONDS.sleep(50);
            return null;
        } catch (JSchException | IOException ex) {
            String stackTrace = Throwables.getStackTraceAsString(ex);
            throw new Exception("Exception in executing SSH command. (IP: " + this.ip + "; User: " + this.user + "; SSHCommand: " + this.SSHCommand);
        }
    }

    /**
     * @throws JSchException
     */
    public void close() throws JSchException {
        ServerConnection.closeInstance();
    }
}
