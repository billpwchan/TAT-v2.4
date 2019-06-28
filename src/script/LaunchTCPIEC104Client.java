package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;
import org.openmuc.j60870.*;
import org.openmuc.j60870.internal.cli.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class LaunchTCPIEC104Client implements InterfaceScript {
    private static final String INTERROGATION_ACTION_KEY = "i";
    private static final String CLOCK_SYNC_ACTION_KEY = "c";

    private static StringCliParameter hostParam = null;
    private static IntCliParameter portParam = null;
    private static IntCliParameter commonAddrParam = null;

    private static volatile Connection connection;
    private static final ActionProcessor actionProcessor = new ActionProcessor(new LaunchTCPIEC104Client.ActionExecutor());

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        String ip = parameters.get(1).getValue().trim().replace(',','.');
        int port = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));
        int asduAddress = ((int) Double.parseDouble(parameters.get(3).getValue().trim()));

        //Initiate Connection to the IEC-104 server.
        hostParam = new CliParameterBuilder("-h")
                .setDescription("The IP/domain address of the server you want to access.")
                .buildStringParameter("host", ip);
        portParam = new CliParameterBuilder("-p")
                .setDescription("The port to connect to.")
                .buildIntParameter("port", port);
        commonAddrParam = new CliParameterBuilder("-ca")
                .setDescription("The address of the target station or the broad cast address.")
                .buildIntParameter("common_address", asduAddress);
        List<CliParameter> cliParameters = new ArrayList<>();
        cliParameters.add(hostParam);
        cliParameters.add(portParam);
        cliParameters.add(commonAddrParam);

        CliParser cliParser = new CliParser("j60870-console-client",
                "A client/master application to access IEC 60870-5-104 servers/slaves.");
        cliParser.addParameters(cliParameters);

        InetAddress address;
        try {
            address = InetAddress.getByName(hostParam.getValue());
        } catch (UnknownHostException e) {
            CommonFunctions.debugLog.error("Unknown host: " + hostParam.getValue());
            return null;
        }

        ClientConnectionBuilder clientConnectionBuilder = new ClientConnectionBuilder(address)
                .setPort(portParam.getValue());

        try {
            connection = clientConnectionBuilder.connect();
        } catch (IOException e) {
            CommonFunctions.debugLog.error("Unable to connect to remote host: " + hostParam.getValue() + ".");
            return null;
        }

        try {
            connection.startDataTransfer(new LaunchTCPIEC104Client.ClientEventListener(), 5000);
        } catch (TimeoutException e2) {
            CommonFunctions.debugLog.error("Starting data transfer timed out. Closing connection.");
            connection.close();
            return null;
        } catch (IOException e) {
            CommonFunctions.debugLog.error("Connection closed for the following reason: " + e.getMessage());
            return null;
        }
        CommonFunctions.reportLog.info("IEC-104 Client successfully connected");

        actionProcessor.addAction(new Action(INTERROGATION_ACTION_KEY, "interrogation C_IC_NA_1"));
        actionProcessor.addAction(new Action(CLOCK_SYNC_ACTION_KEY, "synchronize clocks C_CS_NA_1"));

        actionProcessor.start();
        return null;
    }


    @Override
    public void close() {
        connection.close();
    }

    @Override
    public ArrayList<Parameters> parameters() {
        return null;
    }

    @Override
    public Script scriptInfos() {
        return null;
    }

    private static class ClientEventListener implements ConnectionEventListener {

        @Override
        public void newASdu(ASdu aSdu) {
            CommonFunctions.debugLog.error("\nReceived ASDU:\n" + aSdu);
        }

        @Override
        public void connectionClosed(IOException e) {
            System.out.print("Received connection closed signal. Reason: ");
            if (!e.getMessage().isEmpty()) {
                CommonFunctions.debugLog.error(e.getMessage());
            } else {
                CommonFunctions.debugLog.error("unknown");
            }
            actionProcessor.close();
        }

    }

    private static class ActionExecutor implements ActionListener {

        @Override
        public void actionCalled(String actionKey) throws ActionException {
            try {
                switch (actionKey) {
                    case INTERROGATION_ACTION_KEY:
                        CommonFunctions.reportLog.info("** Sending general interrogation command.");
                        connection.interrogation(commonAddrParam.getValue(), CauseOfTransmission.ACTIVATION,
                                new IeQualifierOfInterrogation(20));
                        Thread.sleep(2000);
                        break;
                    case CLOCK_SYNC_ACTION_KEY:
                        CommonFunctions.reportLog.info("** Sending synchronize clocks command.");
                        connection.synchronizeClocks(commonAddrParam.getValue(), new IeTime56(System.currentTimeMillis()));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new ActionException(e);
            }
        }

        @Override
        public void quit() {
            CommonFunctions.debugLog.error("** Closing connection.");
            connection.close();
            return;
        }
    }
}
