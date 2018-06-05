package script;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import engine.Result;
//import configuration.ParametersScript;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.procimg.SimpleInputRegister;
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

    public void updateHoldingRegister(SimpleProcessImage processImage, int register, double newValue) throws InterruptedException {

        processImage.setRegister((int) register, new SimpleRegister((int) newValue));
        //System.out.println("updateHoldingReg = "+ register);

        //System.out.println("Register is : " + newValue + " value is :" + register);
        Thread.sleep(500);
    }

    private void updateInputRegister(SimpleProcessImage instance, int register, double value) throws InterruptedException {

        instance.setInputRegister((int) register, new SimpleRegister((int) value));
        System.out.println("Number of inputRegCount: " + instance.getInputRegisterCount());

        System.out.println("reg " + (int) register + " SimRegValue " + (int) value);

//      System.out.println("updateinputReg = "+ register);
        Thread.sleep(500);
    }

    public void treatParameters() {
        final String[] parameter = this.params.split(",");
        this.value = Integer.parseInt(parameter[0]);
        this.register = Integer.parseInt(parameter[1]);
//        System.out.println("value = " + this.value);
//        System.out.println("register = "+this.register);
    }

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
        System.out.println("0 ?"+ this.stringValue.equals("0"));
        int val = (int) Double.parseDouble(this.stringValue);
        //System.out.println("VALUE AFTER CAST" + val);
        this.value = (int) Math.pow(2, val);
//this.value = Math.pow(2,Integer.valueOf(this.stringValue)) ;
//        }
        switch (LaunchTCPServerModbus.serverType) {
            case "HR":
                try {
                    if (!this.stringValue.equals("0")) {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateHoldingRegister(instance, register, value);
                    } else {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        updateInputRegister(instance, register, 0);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TriggerModbusPointDI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "AI":
                try {
                    if (!this.stringValue.equals("0")) {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        System.out.println("Register: " + register + " value: " + value);
                        updateInputRegister(instance, register, value);
                    } else {
                        SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                        System.out.println("Register: " + register + " value: " + value);
                        updateInputRegister(instance, register, 0);
                    }
                } catch (Exception ex) {
                    System.out.println("Exception caught.");
                    ex.printStackTrace();
                    Logger.getLogger(TriggerModbusPointDI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                //System.out.println("Error, neither AI,DI or DI2 where found !");
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
