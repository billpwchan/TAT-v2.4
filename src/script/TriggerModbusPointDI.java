package script;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

//import configuration.ParametersScript;
//import net.wimpi.modbus.debug;

/**
 *
 * @author tmartinez
 */
public class TriggerModbusPointDI implements InterfaceScript {

    /**
     * Message to display with the Echo.
     */
    private int register;

    private double value;

    private String stringValue;

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
        //CommonFunctions.debugLog.error("updateHoldingReg = "+ register);

        //CommonFunctions.debugLog.error("Register is : " + newValue + " value is :" + register);
        Thread.sleep(500);
    }

    private void updateInputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setInputRegister(register, new SimpleRegister((int) value));
        CommonFunctions.debugLog.error("Number of inputRegCount: " + instance.getInputRegisterCount());

        CommonFunctions.debugLog.error("reg " + register + " SimRegValue " + (int) value);

//      CommonFunctions.debugLogd.error("updateinputReg = "+ register);
        Thread.sleep(500);
    }

    public void updateDigiatalOutputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setDigitalOut(register, new SimpleDigitalOut(value!=0));

//      CommonFunctions.debugLogd.error("updateinputReg = "+ register);
        Thread.sleep(500);
    }

    public void updateDigiatalInputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setDigitalIn(register, new SimpleDigitalIn (value!=0));

//      CommonFunctions.debugLogd.error("updateinputReg = "+ register);
        Thread.sleep(500);
    }

    /**
     *
     */
    public void result() {
        this.result = "success";
    }

    @Override
    public void close() {
    }

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
       //WARNING ! inverse values

        //this.typeOfPoint = (String) parameters.get(0).getParameter();
        this.register = ((int) Double.parseDouble(parameters.get(1).getValue().trim()));
        this.stringValue = (parameters.get(2).getValue().trim());

        this.stringValue = this.stringValue.trim();
//        if (this.stringValue.length() == 2) {
//            this.value = Math.pow(2, (int) this.stringValue.charAt(1)) + Math.pow(2, (int) this.stringValue.charAt(0));
//        } else {
        int val = (int) Double.parseDouble(this.stringValue);
        //CommonFunctions.debugLog.error("VALUE AFTER CAST" + val);
        this.value = (int) Math.pow(2, val);
//this.value = Math.pow(2,Integer.valueOf(this.stringValue)) ;
//        }
        try {
            switch (LaunchTCPServerModbus.functionCode) {
                case "fc03":
                    if (!this.stringValue.equals("0")) {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateHoldingRegister(instance, register, value);
                    } else {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateInputRegister(instance, register, 0);
                    }
                    break;
                case "fc04":
                    if (!this.stringValue.equals("0")) {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateInputRegister(instance, register, value);
                    } else {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateInputRegister(instance, register, 0);
                    }
                    break;
                case "fc01":
                    if (!this.stringValue.equals("0")) {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateDigiatalOutputRegister(instance, register, value);
                    } else {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateDigiatalOutputRegister(instance, register, 0);
                    }
                    break;
                case "fc02":
                    if (!this.stringValue.equals("0")) {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateDigiatalInputRegister(instance, register, value);
                    } else {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateDigiatalInputRegister(instance, register, 0);
                    }
                    break;
                default:
                        break;
            }
        } catch(Exception ex){
                Logger.getLogger(TriggerModbusPointDI.class.getName()).error("", ex);
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
