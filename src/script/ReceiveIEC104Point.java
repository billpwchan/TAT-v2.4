package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;

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

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(IEC104InitConnection.process.getInputStream()));
        String line123;
        System.out.println("Receiving Points Now");
        while ((line123 = br.readLine()) != null && !line123.contains("Single Command Response")) {
            //Should be finished initialization
            CommonFunctions.debugLog.debug(line123);
            System.out.println(line123);
        }
        System.out.println("Received DO Command with message: " + line123);
        for (int i = 0; i < 8; i++) {
            line123 = br.readLine();
            System.out.println("Line " + ++i + ": " + line123);
        }
        line123 = br.readLine();  //This is the line containing information;
        System.out.println("Received DO Code Line: " + line123);
        System.out.println("Last 2 digits: " + line123.substring(line123.length()-3));
        System.out.println("Central 2 digits: " + line123.substring(line123.length() - DOCodeOffset1, line123.length() - DOCodeOffset1 + 2));
        System.out.println("Right 2 digits: " + line123.substring(line123.length() - DOCodeOffset2, line123.length() - DOCodeOffset2 + 2));
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
        Path inputPath = Paths.get("src\\script\\IEC104Simulator\\IEC104slavetemplate.ini");
        Path outputPath = Paths.get("IEC104slave.ini");
        try {
            //Read Template File
            List<String> lines = Files.readAllLines(inputPath);
            int port = 2404;
            int asduAddress = 7;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i) != null && (lines.get(i)).startsWith("Port")) {
                    lines.set(i, "Port=" + port);
                } else if (lines.get(i) != null && (lines.get(i)).startsWith("AsduAddress")) {
                    lines.set(i, "AsduAddress=" + asduAddress);
                }
            }
            //Write adjusted configuration to OutputFile.
            Files.write(outputPath, lines, Charset.defaultCharset());
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("Cannot locate the file specified. Please check the IEC104Simulator Folder inside /scripts.", ex);
        }
        Runtime.getRuntime().exec("attrib +H IEC104slave.ini");     //Hide the .ini file from the user.
        Process process = new ProcessBuilder("src\\script\\IEC104Simulator\\20171117_104Slave.exe").start();
        IEC104InitConnection.process = process;
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line123;
        Instant before = Instant.now();
        try {
            while (!((line123 = br.readLine()) != null && !line123.contains("Supervisory") && Duration.between(before, Instant.now()).toMillis() < 10000))
                CommonFunctions.debugLog.debug(line123);
        } catch (IOException e) {
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

