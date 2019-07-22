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

public class CreateTCLfile {
    private String indexNameReturn;
    private String SSHCommand;
    private String ip;
    private String user;
    private String password;
    private String RTServerEnvName;
    private String databaseCommandPath0;
    private String databaseCommandPath1;
    private String CommandType;
    private double CommandValue;

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
            this.CommandValue = Double.parseDouble(parameters.get(8).getValue().trim());

            switch (CommandType) {
                case "15":
                case "15.0":
                case "0x0F":
                case "0x0f":
                case "45":
                case "45.0":
                case "0x2D":
                case "0x2d":
                case "46":
                case "46.0":
                case "0x2E":
                case "0x2e":
                case "DI":
                case "DO":
                    this.CommandType = "$SCSCTL_DIOV";
                    this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                            + CommandType + " " + (int) CommandValue + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
                    break;
                case "AI":
                case "AO":
                case "16":
                case "16.0":
                case "0x10":
                    this.CommandType = "$SCSCTL_AIOV";
                    this.SSHCommand = "echo $\'ScsCtl::init\nproc Callback args { }\nScsCtl::sendCommand " + RTServerEnvName + " " + databaseCommandPath0 + ":" + databaseCommandPath1 + " "
                            + CommandType + " " + CommandValue + " $SCSCTL_BYPASS $SCSCTL_BYPASS $SCSCTL_SEND_ANYWAY [Callback]\' >~/Desktop/TAT_Script.tcl";
                    break;
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
