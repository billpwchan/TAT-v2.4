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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IEC104InitOutputConnection implements InterfaceScript {
    static Process process;
    static BufferedReader br;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
//        String ip = parameters.get(1).getValue().trim().replace(',','.');    //Not used for simulator monitor process
//        int port = ((int) Double.parseDouble(parameters.get(2).getValue().trim()));
//        int asduAddress = (int) Double.parseDouble(parameters.get(3).getValue().trim());

        int port = 2404;
        int asduAddress = 7;
        IEC104InitConnection.initIEC104ConfigFile(port, asduAddress);

        Runtime.getRuntime().exec("attrib +H IEC104slave.ini");     //Hide the .ini file from the user.
        process = new ProcessBuilder("src\\script\\IEC104Simulator\\20171117_104Slave.exe").start();

        br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String consoleLine;
        Instant before = Instant.now();
        while ((consoleLine = br.readLine()) != null && !consoleLine.contains("Supervisory") && Duration.between(before, Instant.now()).toMillis() < 10000) {
            //Should be finished initialization
            CommonFunctions.debugLog.debug(consoleLine);
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

    public static void main(String[] args) {
        IEC104InitOutputConnection temp = new IEC104InitOutputConnection();
        try {
            temp.run(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
