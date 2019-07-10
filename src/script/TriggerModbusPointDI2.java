/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author t0155041
 */
public class TriggerModbusPointDI2 implements InterfaceScript {

    /**
     * Message to display with the Echo.
     */
    private int register;

    private double value;

    private String stringValueBit1, stringValueBit2;

    /**
     * Params of the test.
     */
    String params;

    /**
     * Result of the test.
     */
    String result = "Failed";

    /**
     *
     * @param processImage
     * @param register
     * @param newValue
     * @throws InterruptedException
     */
    public void updateHoldingRegister(SimpleProcessImage processImage, int register, double newValue) throws InterruptedException {

        processImage.setRegister(register, new SimpleRegister((int) newValue));

        //CommonFunctions.debugLog.error("Register is : " + newValue + " value is :" + register);
        Thread.sleep(500);
    }

    private void updateInputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setInputRegister(register, new SimpleRegister((int) value));
        Thread.sleep(500);
    }

    public void updateDigiatalOutputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setDigitalOut((int) register, new SimpleDigitalOut(value!=0));

//      CommonFunctions.debugLogd.error("updateinputReg = "+ register);
        Thread.sleep(500);
    }

    public void updateDigiatalInputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setDigitalIn((int) register, new SimpleDigitalIn (value!=0));

//      CommonFunctions.debugLogd.error("updateinputReg = "+ register);
        Thread.sleep(500);
    }

    /**
     *
     */
    public void result() {
        this.result = "success";
    }

    public void close() {
    }

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
       //WARNING ! inverse values

        //this.typeOfPoint = (String) parameters.get(0).getParameter();
        this.register = ((int) Double.parseDouble(parameters.get(1).getValue().trim()));
        this.stringValueBit1 = (parameters.get(2).getValue().trim());
        this.stringValueBit2 = (parameters.get(3).getValue().trim());

        // this.stringValue = this.stringValue.trim();
//        if (this.stringValue.length() == 2) {
        this.value = 2 * Integer.valueOf(this.stringValueBit1) + 1 * Integer.valueOf(this.stringValueBit2);
        CommonFunctions.debugLog.error("THIS.VALUE= " + this.value);
        //this.value = Math.pow(2, Integer.valueOf(this.stringValueBit2)) + Math.pow(2, Integer.valueOf(this.stringValueBit1));
//        } else {
        //this.value = Math.pow(2,Integer.valueOf(this.stringValue)) ;
//        }

        switch (LaunchTCPServerModbus.functionCode)
        {
            case "03": case "0x03":
            try {
                SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                updateHoldingRegister(instance, register,(int) value);
            } catch (Exception ex) {
                Logger.getLogger(TriggerModbusPointDI.class.getName()).error("", ex);
            }
            break;
            case "04": case "0x04":
            try {
                SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                updateInputRegister(instance, register,(int) value);
            } catch (Exception ex) {
                Logger.getLogger(TriggerModbusPointDI.class.getName()).error("", ex);
            }
            break;
            case "01": case "0x01":
            try {
                SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                updateDigiatalOutputRegister(instance, register, value);
            } catch (Exception ex) {
                Logger.getLogger(TriggerModbusPointDI.class.getName()).error("", ex);
            }
            break;
            case "02": case "0x02":
            try {
                SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                updateDigiatalInputRegister(instance, register, value);
            } catch (Exception ex) {
                Logger.getLogger(TriggerModbusPointDI.class.getName()).error("", ex);
            }
            break;
            default:
                //CommonFunctions.debugLog.error("Error, neither AI,DI or DI2 where found !");
                break;
        }
        return new Result();
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
