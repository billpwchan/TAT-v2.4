package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;
import org.hibernate.tool.hbm2x.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


public class ReceiveIEC104Point implements InterfaceScript {

    private int DOCodeOffset1 = 9;
    private int DOCodeOffset2 = 12;
    private int DOHexOffset = 8;


    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        String consoleLine;
        Result result = new Result();
        result.setResult("NOK");

        int pointAddressExcel = ((int) Double.parseDouble(parameters.get(0).getValue().trim()));
        int addressSizeExcel = (int) Double.parseDouble(parameters.get(1).getValue().trim());
        System.out.println("Receiving Points Now");
        while ((consoleLine = IEC104InitOutputConnection.br.readLine()) != null && !consoleLine.contains("Single Command Response")) {
            //Should be finished initialization
            CommonFunctions.debugLog.debug(consoleLine);
            System.out.println(consoleLine);
        }
        System.out.println("Received DO Command with message: " + consoleLine);
        for (int i = 0; i < DOHexOffset; i++) {
            consoleLine = IEC104InitOutputConnection.br.readLine();
            System.out.println("Line " + ++i + ": " + consoleLine);
        }
        consoleLine = IEC104InitOutputConnection.br.readLine();  //This is the line containing information;

        CommonFunctions.debugLog.info("Received DO Code Line: " + consoleLine);
        CommonFunctions.debugLog.info("Last 2 digits: " + consoleLine.substring(consoleLine.length() - 3).trim());
        CommonFunctions.debugLog.info("Right 2 digits: " + consoleLine.substring(consoleLine.length() - DOCodeOffset1, consoleLine.length() - DOCodeOffset1 + 2));
        CommonFunctions.debugLog.info("Central 2 digits: " + consoleLine.substring(consoleLine.length() - DOCodeOffset2, consoleLine.length() - DOCodeOffset2 + 2));

        int pointAddressReceived = Integer.parseInt(
                consoleLine.substring(consoleLine.length() - DOCodeOffset1, consoleLine.length() - DOCodeOffset1 + 2) +
                        consoleLine.substring(consoleLine.length() - DOCodeOffset2, consoleLine.length() - DOCodeOffset2 + 2), 16);
        int addressSizeReceived = Integer.parseInt(consoleLine.substring(consoleLine.length() - 2).trim());
        System.out.println("pointAddress is: " + pointAddressReceived);
        System.out.println("addressSize: " + addressSizeReceived);

        if (pointAddressReceived == pointAddressExcel) {
            result.setResult("OK");
            StringBuilder resultComment = new StringBuilder();
            resultComment.append("Result OK \n Received Point Address: ");
            resultComment.append(pointAddressReceived);
            resultComment.append("\n Actual Point Address: ");
            resultComment.append(pointAddressExcel);
            result.setComment(resultComment.toString());
        } else {
            StringBuilder resultComment = new StringBuilder();
            resultComment.append("Result MisMatch \n Received Point Address: ");
            resultComment.append(pointAddressReceived);
            resultComment.append("\n Actual Point Address: ");
            resultComment.append(pointAddressExcel);
            result.setComment(resultComment.toString());
        }
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<Parameters> parameters() {
        return null;
    }

    @Override
    public Script scriptInfos() {
        return null;
    }

    public static void main(String[] args) throws IOException {
        //Must initiate IEC104 connection to keep lines of IEC104Template as static.
//        Path inputPath = Paths.get("src\\script\\IEC104Simulator\\IEC104slavetemplate.ini");
//        Path outputPath = Paths.get("IEC104slave.ini");
//        try {
//            //Read Template File
//            List<String> lines = Files.readAllLines(inputPath);
//            int port = 2404;
//            int asduAddress = 7;
//            for (int i = 0; i < lines.size(); i++) {
//                if (lines.get(i) != null && (lines.get(i)).startsWith("Port")) {
//                    lines.set(i, "Port=" + port);
//                } else if (lines.get(i) != null && (lines.get(i)).startsWith("AsduAddress")) {
//                    lines.set(i, "AsduAddress=" + asduAddress);
//                }
//            }
//            //Write adjusted configuration to OutputFile.
//            Files.write(outputPath, lines, Charset.defaultCharset());
//        } catch (IOException ex) {
//            CommonFunctions.debugLog.error("Cannot locate the file specified. Please check the IEC104Simulator Folder inside /scripts.", ex);
//        }
//        Runtime.getRuntime().exec("attrib +H IEC104slave.ini");     //Hide the .ini file from the user.
//        Process process = new ProcessBuilder("src\\script\\IEC104Simulator\\20171117_104Slave.exe").start();
//        IEC104InitConnection.process = process;
//        InputStream is = process.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//        String consoleLine;
//        Instant before = Instant.now();
//        try {
//            while (!((consoleLine = br.readLine()) != null && !consoleLine.contains("Supervisory") && Duration.between(before, Instant.now()).toMillis() < 10000))
//                CommonFunctions.debugLog.debug(consoleLine);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        IEC104InitOutputConnection temp = new IEC104InitOutputConnection();

        try {
            temp.run(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReceiveIEC104Point temp1 = new ReceiveIEC104Point();
        try {
            temp1.run(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

