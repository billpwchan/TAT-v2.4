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

public class CIPWriteTag {

    String result = "Failed";
    String address;
    int slot;
    String tag0;
    String tag1;
    double value;

    public void close() {
    }

    public void result() {
        this.result = "success";
    }

    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {

        this.address = parameters.get(1).getValue().trim().replace(',','.');
        this.slot = Integer.parseInt(parameters.get(2).getValue().trim());
        this.tag0 = parameters.get(3).getValue().trim();
        this.tag1 = parameters.get(4).getValue().trim();
        this.value = Double.parseDouble(parameters.get(5).getValue().trim());

        LaunchCIPConnection(address, slot, "Program:" + tag0 + "." + tag1, value);
        TimeUnit.MILLISECONDS.sleep(500);

        return new Result();
    }

    public void LaunchCIPConnection(String address, int slot, String tag, double value) throws Exception{

        try (EtherNetIP plc = new EtherNetIP(address,slot)){
            plc.connectTcp();
            CommonFunctions.debugLog.info("Tag"+ tag);
            CIPData readValue = plc.readTag(tag);
            CommonFunctions.debugLog.info("Read Value: "+readValue);

            if((int) value == -1){
                for (int i = 0; i <= 7; ++i){
                 readValue.set(0, i);
                 plc.writeTag(tag, readValue);
                }
            } else {
                readValue.set(0,value);
                CommonFunctions.debugLog.info("Value Sent: "+readValue);

                plc.writeTag(tag, readValue);
                CommonFunctions.debugLog.info("Value after Sent: "+readValue);
            }

        } catch (final CipException e) {
            CommonFunctions.debugLog.info(e.getMessage());
            CommonFunctions.debugLog.info("Failed with CipException");
        }
    }
}
