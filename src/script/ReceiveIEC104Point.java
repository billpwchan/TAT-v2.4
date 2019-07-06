package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


public class ReceiveIEC104Point implements InterfaceScript {

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(IEC104InitConnection.process.getInputStream()));
        String line123;
        Instant before = Instant.now();
        while ((line123 = br.readLine()) != null && Duration.between(before, Instant.now()).toMillis() < 10000) {
            //Should be finished initialization
            CommonFunctions.debugLog.debug(line123);
            System.out.println(line123);
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
        //Must initiate IEC104 connection to keep lines of IEC104Template as static.
        IEC104InitConnection temp = new IEC104InitConnection();
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

