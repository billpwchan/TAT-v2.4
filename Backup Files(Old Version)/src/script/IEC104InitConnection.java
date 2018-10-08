package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IEC104InitConnection implements InterfaceScript {
    String filepath;
    static List<String> lines;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        String ip = parameters.get(1).getValue().trim();    //Not used for simulator monitor process
        int port = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));
//        int port = 2404;
        int asduAddress = ((int) Double.parseDouble(parameters.get(3).getValue().trim()));
//        int asduAddress = 7;
        CommonFunctions.debugLog.debug("Working Directory = " + System.getProperty("user.dir"));
        Path inputPath = Paths.get("src\\script\\IEC104Simulator\\IEC104slavetemplate.ini");
        Path outputPath = Paths.get("IEC104slave.ini");
        try {
            //Read Template File
            lines =
                    Files.readAllLines(inputPath);
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
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
//        process.destroy();
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

    public static void main(String[] args) {
        IEC104InitConnection temp = new IEC104InitConnection();
        try {
            temp.run(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
