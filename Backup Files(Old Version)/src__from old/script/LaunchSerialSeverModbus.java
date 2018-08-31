package script;

import DB.ParametersExecution;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package testmodbus;
//
//import com.serotonin.modbus4j.BasicProcessImage;
//import com.serotonin.modbus4j.ModbusFactory;
//import com.serotonin.modbus4j.ModbusSlaveSet;
//import com.serotonin.modbus4j.ProcessImage;
//import com.serotonin.modbus4j.ProcessImageListener;
//import com.serotonin.modbus4j.code.DataType;
//import com.serotonin.modbus4j.exception.IllegalDataAddressException;
//import com.serotonin.modbus4j.exception.ModbusInitException;
//import com.serotonin.modbus4j.ip.IpParameters;
//
///**
// *Class of modbus server (slave) TCP protocol.
// * @author Martinez Thibault
// * @version 0.1
// */
//public class ModbusServerTCP
//{
//    String ip = "localhost";
//    int port = 502;
//    private static ModbusSlaveSet instance;
//
//    /**
//     * Constructor of the ModbusServerTCP class.
//     */
//            private ModbusServerTCP() throws Exception 
//	{
//            launchModbusServerTCP();
//	}
//    
//    /**
//     * Method to launch The tcp server.
//     * @param port listening port of the modbus server.
//     * @param ipAddrServer ip address of the modbus server.
//     */
//    public void launchModbusServerTCP()
//    {
//        
//     
//    }
//
//    
//        static BasicProcessImage getModscanProcessImage(int slaveId) {
//        BasicProcessImage processImage = new BasicProcessImage(slaveId);
//        processImage.setAllowInvalidAddress(false);
//        processImage.setInvalidAddressValue(Short.MIN_VALUE);
//
//
//
//        final long tStart,tEnd,tDelta;
//        final double elapsedSeconds;
//                tStart= System.currentTimeMillis();
//        for(int i=0;i<10000;i++){
//             processImage.setHoldingRegister(i, (short) 0);
//             //processImage.setRegister(i, i, slaveId, i);
//        }
//        tEnd = System.currentTimeMillis();
//        tDelta = tEnd - tStart;
//        elapsedSeconds = tDelta / 1000.0;
//        System.out.println(elapsedSeconds);
//        processImage.setExceptionStatus((byte) 151);
//        // Add an image listener.
//        //BasicProcessImageListener imageListener = new BasicProcessImageListener();
//        processImage.addListener(new BasicProcessImageListener());
//
//        return processImage;
//    }
//        
//        
//static class BasicProcessImageListener implements ProcessImageListener {
//        public void coilWrite(int offset, boolean oldValue, boolean newValue) {
//            System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
//        }
//
//        public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
//            System.out.println("HR at " + offset + " was set from " + oldValue + " to " + newValue);
//        }
//        
//        public void setHoldingRegister(int offset, short oldValue, short newValue) {
//            System.out.println("HR at " + offset + " was set from " + oldValue + " to " + newValue);
//        }
//    }
//
//	/**
//	 * Singleton of the class.
//	 * 
//	 * @return instance of the class
//	 * @throws Exception
//	 *             exception
//	 */
//	public static ModbusSlaveSet getInstance() throws Exception 
//	{
//
//		if (instance == null) {
//                      ModbusFactory modbusFactory = new ModbusFactory();
//       IpParameters params = new IpParameters();
//       params.setHost("localhost"); 
//       params.setPort(502);
//
//        instance = modbusFactory.createTcpSlave(false);
//        instance.addProcessImage(getModscanProcessImage(1));
//        
//        new Thread(new Runnable() { 
//           public void run() {
//                try {
//                    instance.start();
//                    //instance.setExceptionHandler(instance);
//                }
//                catch (ModbusInitException e) {
//                }
//            }
//        }).start();
//		}
//		return instance;
//	}
//    }
/* The important instances and variables */
public class LaunchSerialSeverModbus {

    static ModbusTCPListener listener = null;
    private static SimpleProcessImage instance = null;
    //int port = Modbus.DEFAULT_PORT;
    //private String ip;

    private String ip;
    public static String serverType;
    private int port, slave;

    public String run(ArrayList<ParametersExecution> parameters, HashMap<String, Object> test) throws InterruptedException {

        this.ip = (String) parameters.get(1).getValue();
        this.port = ((int) Double.parseDouble(parameters.get(2).getValue()));
        this.slave = ((int) Double.parseDouble(parameters.get(3).getValue()));
        serverType = (String) parameters.get(4).getValue();
        launchServer(ip, port, slave, serverType);
        Thread.sleep(3000);

        return null;
    }

    public void launchServer(String ip, int portDeServer, int SlaveID, String serString) {

//2. Prepare a process image
        instance = new SimpleProcessImage();
        switch (serString) {
            case "HR":
                for (int i = 0; i < 10000; i++) {
                    instance.addRegister(new SimpleRegister(0));
                    //instance.addInputRegister(new SimpleRegister(0));

                }
                break;
            case "AI":
                for (int i = 0; i < 10000; i++) {
                    instance.addInputRegister(new SimpleRegister(0));
                }
                break;
        }
        System.out.println("Register : " + instance.getRegisterCount() + " ,Input register : " + instance.getInputRegisterCount());
//3. Set the image on the coupler9
        ModbusCoupler.getReference().setProcessImage(instance);
        ModbusCoupler.getReference().setMaster(false);
        ModbusCoupler.getReference().setUnitID(SlaveID);

//4. Create a listener with 2 threads in pool
        listener = new ModbusTCPListener(1);
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(LaunchTCPServerModbus.class.getName()).error("", ex);
        }
        listener.setAddress(localhost);
        listener.setPort(portDeServer);
        listener.start();
    }

    /**
     * Singleton of the class.
     *
     * @param ip
     * @return instance of the class
     * @throws Exception exception
     */
    public static SimpleProcessImage getInstance() throws Exception {
        return instance;
    }

    public static void close() {
        listener.stop();
        instance = null;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (int i = 0; i < threadArray.length; i++) {
            //System.out.println("Thread1 = " + threadArray[i]);
            if(threadArray[i].getName().contains("Modbus_Pool")){
                threadArray[i].stop();
            }
        }
    }
}
