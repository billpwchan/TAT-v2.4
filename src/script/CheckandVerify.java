package script;

import DB.ParametersExecution;
import controller.util.CommonFunctions;
import engine.Result;
import etherip.EtherNetIP;
import etherip.data.CipException;
import etherip.types.CIPData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CheckandVerify {

    String result = "Failed";
    String address;
    int slot;
    String tag0;
    String tag1;
    double value;
    int milliseconds;

    public void close() {
    }

    public void result() {
        this.result = "success";
    }

    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        Result result = new Result();
        result.setResult("NOK");
        this.address = parameters.get(1).getValue().trim().replace(',','.');
        this.slot = Integer.parseInt(parameters.get(2).getValue().trim());
        this.tag0 = parameters.get(3).getValue().trim();
        this.tag1 = parameters.get(4).getValue().trim();
        this.value = Double.parseDouble(parameters.get(5).getValue().trim());
        this.milliseconds = Integer.parseInt(parameters.get(6).getValue().trim());

        if(value == -1)
        {
            result.setResult("OS");
            result.setComment("The Control Value is blank.\nSkip");
        }
        else
            {
                TimeUnit.MILLISECONDS.sleep(milliseconds);
                LaunchCIPConnection(address, slot, "Program:" + tag0 + "." + tag1, value, result);
        }

        return result;

    }

    public void LaunchCIPConnection(String address, int slot, String tag, double value, Result result) throws Exception{

        try (EtherNetIP plc = new EtherNetIP(address,slot)){
            plc.connectTcp();
            CIPData readValue = plc.readTag(tag);

            switch (readValue.getType())
            {
                default:
                case DINT:
                    if(readValue.getNumber(0).intValue() == (int) value){
                        result.setResult("OK");
                        result.setComment("Sent: " +value + "\nFound: " + readValue.getNumber(0)+"\nReading Tag: " + tag);
                    }
                    else{
                        result.setComment("Force to set 0\nSent: " +value + "\nFound: " + readValue.getNumber(0)+"\nReading Tag: " + tag);
                    }
                    break;
                case BOOL:
                    if(readValue.getNumber(0).byteValue() == (int) value){
                        result.setResult("OK");
                        result.setComment("Sent: " +value + "\nFound: " + readValue.getNumber(0)+"\nReading Tag: " + tag);
                    }
                    else{
                        result.setComment("Force to set 0\nSent: " +value + "\nFound: " + readValue.getNumber(0)+"\nReading Tag: " + tag);
                    }
                    break;
                case REAL:
                    if(readValue.getNumber(0).doubleValue() == value){
                        result.setResult("OK");
                        result.setComment("Sent: " +value + "\nFound: " + readValue.getNumber(0)+"\nReading Tag: " + tag);
                    }
                    else{
                        result.setComment("Force to set 0\nSent: " +value + "\nFound: " + readValue.getNumber(0)+"\nReading Tag: " + tag);
                    }
                    break;
            }

        } catch (final CipException e) {
            CommonFunctions.debugLog.info(e.getMessage());
            CommonFunctions.debugLog.info("Failed with CipException");
        }
    }
}
