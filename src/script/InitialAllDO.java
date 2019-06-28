package script;

import DB.ParametersExecution;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class InitialAllDO {

    TCPMasterConnection connection = null;
    ModbusTCPTransaction transaction =null;
    WriteCoilRequest request = null;
    InetAddress address;
    int port = Modbus.DEFAULT_PORT;

    public String run(ArrayList<ParametersExecution> parameters, HashMap<String, Object> test) throws Exception {
        this.address = InetAddress.getByName(parameters.get(1).getValue().trim().replace(',','.'));
        this.port = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));

        connection = new TCPMasterConnection(address);
        connection.setPort(port);
        connection.connect();

        transaction = new ModbusTCPTransaction(connection);

        for (int reference = 0; reference < 65535; reference++){
        request = new WriteCoilRequest(reference,false);
        transaction.setRequest(request);
        transaction.execute();
        }
        connection.close();
        return null;
    }

    public void close(){
    }
}
