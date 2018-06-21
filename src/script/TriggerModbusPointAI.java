package script;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DB.ParametersExecution;
import java.text.DecimalFormat;
//import configuration.ParametersScript;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 *
 * @author tmartinez
 */
public class TriggerModbusPointAI {

    /**
     * Message to display with the Echo.
     */
    private int register, lowValue, maxValue;

    private float scalingFactor;
    /**
     * Type of point to display
     */

    private String typeOfPoint, ip;
    /**
     * Params of the test.
     */
    String params, indexNameReturn;

    /**
     * Result of the test.
     */
    String result = "Failed";

    /**
     * Constructor of the class. Non instanciate.
     */
    public void UpdateServer() {

    }

    /**
     *
     * @param parameters
     * @param hashMap
     * @return
     */
    public String run(ArrayList<ParametersExecution> parameters, HashMap hashMap) {
        //WARNING ! inverse values

        //this.typeOfPoint = (String) parameters.get(0).getParameter();
        this.register = ((int) Double.parseDouble(parameters.get(1).getValue().trim()));
        this.lowValue = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));
        this.maxValue = ((int) Double.parseDouble(parameters.get(3).getValue().trim()));
        this.scalingFactor = ((float) Double.parseDouble(parameters.get(4).getValue().trim()));
        this.indexNameReturn = parameters.get(5).getValue().trim();

        int newInt = randInt(lowValue, maxValue);
        //System.out.println("Scaling factor is :" + scalingFactor + "\n scaling factor by 1 is : " + scalingFactor * 1);
        //double formatInt =Math.round(((newInt * scalingFactor)*100)/100);

        double formatIntTest = (newInt * scalingFactor);
        DecimalFormat df = new DecimalFormat("#####0.00");

        // round(formatInt,2);
        //System.out.println(" new int value is : " + newInt + "\n value with format is : " + formatInt);
        //String toReturn = String.format("%1.6e", formatInt);
        String toReturn = String.valueOf(df.format(formatIntTest));
        //System.out.println("String to return is :" + toReturn);
        switch (LaunchTCPServerModbus.serverType) {

            // System.out.println();
            case "HR":
                try {
                    SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                    updateHoldingRegister(instance, newInt, register);
                } catch (Exception ex) {
                    Logger.getLogger(TriggerModbusPointAI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "AI":
                try {
                    SimpleProcessImage instance = LaunchTCPServerModbus.getInstance();
                    updateInputRegister(instance, newInt, register);
                } catch (Exception ex) {
                    Logger.getLogger(TriggerModbusPointAI.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                System.out.println("Error, neither AI,DI or DI2 where found !");
                break;
        }
        hashMap.put(this.indexNameReturn, toReturn);
        return "";
    }

    /**
     *
     * @param processImage
     * @param value
     * @param register
     * @throws InterruptedException
     */
    public void updateHoldingRegister(SimpleProcessImage processImage, int value, int register) throws InterruptedException {

        processImage.setRegister((int) register, new SimpleRegister((int) value));

        //System.out.println("Register is : " + register + " value is :" + value);
        Thread.sleep(500);
    }

    private void updateInputRegister(SimpleProcessImage instance, int register, int value) throws InterruptedException {

        instance.setInputRegister((int) value, new SimpleRegister((int) register));

        Thread.sleep(500);
    }

    /**
     *
     */
    public void treatParameters() {
        final String[] parameter = this.params.split(",");
        //this.value = Integer.parseInt(parameter[0]);
        this.register = Integer.parseInt(parameter[1]);
    }

    /**
     *
     */
    public void result() {
        this.result = "success";
    }

    /**
     *
     */
    public void close() {
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
//    public void updateProcessImage(BasicProcessImage processImage,int register,short newValue) throws IllegalDataAddressException {
//        
//        //hsort hr1Value = ((Number) processImage.getHoldingRegister(80, DataType.TWO_BYTE_BCD)).shortValue();
//        processImage.setHoldingRegister(register, DataType.TWO_BYTE_BCD, newValue);
//    }
//    @Override
//    public void InitParametersType() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public ArrayList<ParametersScript> getParametersType() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
