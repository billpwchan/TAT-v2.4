package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CheckandCompare implements InterfaceScript{

    //Important instances of the classes
    TCPMasterConnection connection = null;
    ModbusTCPTransaction transaction = null;
    ReadCoilsRequest coilsRequest = null;
    ReadCoilsResponse coilsResponse = null;
    ReadMultipleRegistersRequest multipleRegistersRequest = null;
    ReadMultipleRegistersResponse multipleRegistersResponse = null;
    WriteCoilRequest writeCoilRequest = null;

    //Variables for storing the parameters
    InetAddress address = null;
    int port = Modbus.DEFAULT_PORT;
    int reference = 0;
    int count = 1;
    String functionCode;
    String value;
    private String receivedValue;
    int scalingFactor = 1;

    /**
     * Params of the test.
     */
    String params;

    /**
     * Result of the test.
     */
    String result = "Failed";

    public void result() {
        this.result = "success";
    }

    @Override
    public void close() {

    }

    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws UnknownHostException, InterruptedException {
        Result result = new Result();
        result.setResult("NOK");
        this.address = InetAddress.getByName(parameters.get(1).getValue().trim().replace(',','.'));
        this.port = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));
        this.value = parameters.get(3).getValue().trim();
        this.scalingFactor =  (int) Double.parseDouble(parameters.get(4).getValue().trim());
        this.reference = ((int) Double.parseDouble(parameters.get(5).getValue().trim()));
        this.functionCode = parameters.get(6).getValue().trim();
        if(scalingFactor == '0')
            scalingFactor = 1;
        TimeUnit.MILLISECONDS.sleep(500);
        launchServer(address, port, reference, count, functionCode, result);

        return result;
    }
    private void launchServer(InetAddress address, int port, int reference, int count, String functionCode, Result result) {

        try{
        //2. Open the connection
        connection = new TCPMasterConnection(address);
        connection.setPort(port);
        connection.connect();

        switch (functionCode)
        {
            case "15": case "15.0": case "0x0F": case "0x0f":
                //3. Prepare the request
                coilsRequest = new ReadCoilsRequest(reference, count);

                //4. Prepare tht transaction
                transaction = new ModbusTCPTransaction(connection);
                transaction.setRequest(coilsRequest);

                //5. Execute the transaction

                transaction.execute();
                coilsResponse = (ReadCoilsResponse) transaction.getResponse();
                receivedValue =coilsResponse.getCoils().toString();
                CommonFunctions.debugLog.info("Received value: "+receivedValue.charAt(7));
                CommonFunctions.debugLog.info("Requested value: "+value.charAt(0));
                if (receivedValue.charAt(7)==value.charAt(0))
                {
                    result.setResult("OK");
                    result.setComment("Sent : " +value + "\n" + "Found : " + receivedValue.charAt(7)+"\nReading Register : "+reference);
                }
                else
                {
                    //3. Prepare the request
                    writeCoilRequest = new WriteCoilRequest(reference,false);

                    //4. Prepare tht transaction
                    transaction = new ModbusTCPTransaction(connection);
                    transaction.setRequest(writeCoilRequest);

                    //5. Execute the transaction

                    transaction.execute();
                    receivedValue =coilsResponse.getCoils().toString();
                    result.setComment("Forced to set 0\nMissmatch \n" + "Sent : " +value + "\n" + "Found : " + receivedValue.charAt(7)+"\nReading Register : "+reference);
                }
                break;

            case "16": case "16.0": case "0x10":
                //3. Prepare the request
                multipleRegistersRequest = new ReadMultipleRegistersRequest(reference, count);

                //4. Prepare tht transaction
                transaction = new ModbusTCPTransaction(connection);
                transaction.setRequest(multipleRegistersRequest);

                //5. Execute the transaction

                transaction.execute();
                multipleRegistersResponse = (ReadMultipleRegistersResponse) transaction.getResponse();
                receivedValue = Integer.toString(multipleRegistersResponse.getRegisterValue(0));

                if (Integer.parseInt(receivedValue) == ((int) Double.parseDouble(value))*scalingFactor)
                {
                    result.setResult("OK");
                    result.setComment("Sent : " +value + "\n" + "Found : " + receivedValue+"\nReading Register : "+reference);
                }
                else
                {
                    result.setComment("Missmatch \n" + "Sent : " +value + "\n" + "Found : " + receivedValue+"\nReading Register : "+reference);
                }
                break;
        }
        connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<Parameters> parameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Script scriptInfos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
