package script;

import DB.ParametersExecution;
import controller.util.CommonFunctions;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Kelvin Cheung
 * @version 1.0
 */

public class ModbusDOInitialization {

    int reference = 0;
    private TCPMasterConnection connection = null;
    private ModbusTCPTransaction transaction = null;
    private WriteCoilRequest writeCoilRequest = null;

    public void run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws UnknownHostException {

        this.reference = (int) Double.parseDouble(parameters.get(1).getValue().trim());

        launchServer(InetAddress.getByName(LaunchTCPServerModbus.ip), LaunchTCPServerModbus.port, reference);

    }

    private void launchServer(InetAddress address, int port, int reference) {

        try {
            //2. Open the connection
            connection = new TCPMasterConnection(address);
            connection.setPort(port);
            connection.connect();

            writeCoilRequest = new WriteCoilRequest(reference, true);

            //4. Prepare tht transaction
            transaction = new ModbusTCPTransaction(connection);
            transaction.setRequest(writeCoilRequest);

            //5. Execute the transaction
            transaction.execute();

            connection.close();
        } catch (Exception e) {
            CommonFunctions.debugLog.info("Check Modbus server connection");
            e.printStackTrace();
        }
    }

    public void close() {

    }
}
