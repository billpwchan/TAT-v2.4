package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;
import org.openmuc.j60870.*;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class LaunchTCPIEC104SampleServer implements InterfaceScript {
    private String ip;
    private int port;
    private static String asduAddress;


    public static void main(String[] args) {
        LaunchTCPIEC104SampleServer temp = new LaunchTCPIEC104SampleServer();
        temp.run(null, null);
    }
    /**
     * Run the script.
     *
     * @param parameters available in input and to use for the script execution
     * @param hashMap    in which it is possible to return objects
     * @return the result of the test. Object result is composed by a comment and a result (Result can be : OK,MOK,OKWC,NT,IC,OS). In case of a stimuli, the object result returned
     * can be "null". In this case put "return null" at the end of the run function.
     */
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) {
//        this.ip = parameters.get(1).getValue().trim();
//        this.port = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));
//        asduAddress = parameters.get(3).getValue().trim();
        this.port = 2404;
        asduAddress = "7";
        start();        //To start the server (IEC-104 protocol). Thales server should be able to scan & show connected.
        return null;
    }

    private void start() {
        Server server = new Server.Builder().build();
        try {
            server.start(new ServerListener());
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("Unable to start listening: \"" + ex.getMessage() + "\". Will quit.");
        }
    }

    private int connectionIdCounter = 1;

    private class ServerListener implements ServerEventListener {

        private class ConnectionListener implements ConnectionEventListener {

            private final Connection connection;
            private final int connectionId;

            ConnectionListener(Connection connection, int connectionId) {
                this.connection = connection;
                this.connectionId = connectionId;
            }

            @Override
            public void newASdu(ASdu aSdu) {
                try {
                    switch (aSdu.getTypeIdentification()) {
                        // interrogation command. It will be used to initiate connection
                        case C_IC_NA_1:
                            connection.sendConfirmation(aSdu);
                            CommonFunctions.debugLog.error("Got interrogation command. Should send monitored information.\n");
                            break;
                            // Case of DI1 ==> Single Point Information without time tag.
                        case M_SP_NA_1:
                            // IeSinglePointWithQuality first parameter refers to the state of the simulated device.
                            connection.send(new ASdu(TypeId.M_SP_NA_1, false, CauseOfTransmission.BACKGROUND_SCAN, false, false,
                                    0x2711, 7,
                                    new InformationObject[]{
                                            new InformationObject(1, new InformationElement[][]{{
                                                new IeSinglePointWithQuality(true, false, true, true, false), new IeTime56(1234567)
                                            }})
                                    }
                            ));
                            break;
                        default:
                            CommonFunctions.debugLog.error("Got unknown request: " + aSdu + ". Will not confirm it.\n");
                    }
                } catch (EOFException e) {
                    CommonFunctions.debugLog.error("Will quit listening for commands on connection (" + connectionId
                            + ") because socket was closed.");
                } catch (IOException e) {
                    CommonFunctions.debugLog.error("Will quit listening for commands on connection (" + connectionId
                            + ") because of error: \"" + e.getMessage() + "\".");
                }

            }

            @Override
            public void connectionClosed(IOException e) {
                CommonFunctions.debugLog.error("Connection (" + connectionId + ") was closed. " + e.getMessage());
            }

        }

        @Override
        public void connectionIndication(Connection connection) {
            int myConnectionId = connectionIdCounter++;
            CommonFunctions.debugLog.error("A client has connected using TCP/IP. Will listen for a StartDT request. Connection ID: "
                    + myConnectionId);
            try {
                connection.waitForStartDT(new ServerListener.ConnectionListener(connection, myConnectionId), 5000);
            } catch (IOException e) {
                CommonFunctions.debugLog.error("Connection (" + myConnectionId + ") interrupted while waiting for StartDT: "
                        + e.getMessage() + ". Will quit.");
                return;
            } catch (TimeoutException e) {
            }
            CommonFunctions.debugLog.error(
                    "Started data transfer on connection (" + myConnectionId + ") Will listen for incoming commands.");

        }

        @Override
        public void serverStoppedListeningIndication(IOException e) {
            CommonFunctions.debugLog.error(
                    "Server has stopped listening for new connections : \"" + e.getMessage() + "\". Will quit.");
        }

        @Override
        public void connectionAttemptFailed(IOException e) {
            CommonFunctions.debugLog.error("Connection attempt failed: " + e.getMessage());

        }

    }

    /**
     * close the script, all the connections opened for example. This function is called at the end of each test case.
     */
    public void close() {

    }

    /**
     * Return all the parameters needed for the script with their name, type and
     * description. This Script is useful to add new scripts in the TAT, all the information of the parameters are automatically filled for the user.
     *
     * @return the ArrayList with all the parameters for the script
     */
    public ArrayList<Parameters> parameters() {
        return null;
    }

    /**
     * Return the script with all its information
     * This Function is useful to add new scripts in the TAT, All the information of the script are automatically filled for the user.
     *
     * @return the script with its information
     */
    public Script scriptInfos() {
        return null;
    }
}
