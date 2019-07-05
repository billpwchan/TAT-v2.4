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
import org.hibernate.tool.hbm2x.StringUtils;

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
    String functionCode;
    String value;
    private String receivedValue ="";
    int scalingFactor = 1;
    int addressSize;
    String endianness;
    int milliseconds;


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
        this.addressSize = ((int) Double.parseDouble(parameters.get(6).getValue().trim()));
        this.functionCode = parameters.get(7).getValue().trim();
        this.endianness = parameters.get(8).getValue().trim();
        this.milliseconds = Integer.parseInt(parameters.get(9).getValue().trim());
        if(scalingFactor == 0)
            scalingFactor = 1;
        TimeUnit.MILLISECONDS.sleep(milliseconds);
        launchServer(address, port, reference, addressSize, functionCode, endianness ,result);

        return result;
    }
    private void launchServer(InetAddress address, int port, int reference, int addressSize, String functionCode, String endianness, Result result) {

        try{
        //2. Open the connection
        connection = new TCPMasterConnection(address);
        connection.setPort(port);
        connection.connect();

        switch (functionCode)
        {
            case "15": case "15.0": case "0x0F": case "0x0f":
                //3. Prepare the request
                coilsRequest = new ReadCoilsRequest(reference, 1);

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
                    result.setComment("Sent: " +value + "\n" + "Found: " + receivedValue.charAt(7)+"\nReading Register: "+reference);
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
                    result.setComment("Forced to set 0\nMissmatch \n" + "Sent: " +value + "\n" + "Found: " + receivedValue.charAt(7)+"\nReading Register: "+reference);
                }
                break;

            case "16": case "16.0": case "0x10":
                //3. Prepare the request
                multipleRegistersRequest = new ReadMultipleRegistersRequest(reference, 1);

                //4. Prepare tht transaction
                transaction = new ModbusTCPTransaction(connection);
                for (int i = reference; i <= reference + addressSize/16 - 1; ++i)
                {
                    multipleRegistersRequest.setReference(i);
                    transaction.setRequest(multipleRegistersRequest);
                    //5. Execute the transaction
                    transaction.execute();
                    multipleRegistersResponse = (ReadMultipleRegistersResponse) transaction.getResponse();
                    if (endianness.toLowerCase().equals("big"))
                        receivedValue += StringUtils.leftPad(Integer.toHexString(multipleRegistersResponse.getRegisterValue(0)),4,"0");
                    else if (endianness.toLowerCase().equals("little"))
                        receivedValue = StringUtils.leftPad(Integer.toHexString(multipleRegistersResponse.getRegisterValue(0)),4,"0")+receivedValue;
                    }

                if (receivedValue.equals(StringUtils.leftPad(Integer.toHexString((int) (Double.parseDouble(value)*scalingFactor)),8,"0")))
                {
                    result.setResult("OK");
                    result.setComment("Sent: " + value + "\nScaling Factor: " + scalingFactor
                            + "\nActual value Sent: " + (int) (Double.parseDouble(value)*scalingFactor) + "\nFound: " + Integer.parseInt(receivedValue,16)
                            + "\nActual Value Sent (hex): " + StringUtils.leftPad(Integer.toHexString((int) (Double.parseDouble(value)*scalingFactor)),8,"0")+ "\nFound (hex): "+ receivedValue
                            + "\nReading Register : "+reference);
                }
                else
                {
                    result.setComment("Missmatch \n" + "Sent : " + value + "\nScaling Factor: " + scalingFactor
                            + "\nActual value Sent: " + (int) (Double.parseDouble(value)*scalingFactor) + "\nFound: " + Integer.parseInt(receivedValue,16)
                            + "\nActual Value Sent (hex): " + StringUtils.leftPad(Integer.toHexString((int) (Double.parseDouble(value)*scalingFactor)),8,"0")+ "\nFound (hex): "+ receivedValue
                            + "\nReading Register : "+reference);
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
