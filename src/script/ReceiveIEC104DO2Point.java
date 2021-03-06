package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;


public class ReceiveIEC104DO2Point implements InterfaceScript {

    private int DOCodeOffset1 = 9;
    private int DOCodeOffset2 = 12;
    private int DOHexOffset = 8;

    public static void main(String[] args) {
    }

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        String consoleLine;
        Result result = new Result();
        result.setResult("NOK");

        int pointAddressExcel = ((int) Double.parseDouble(parameters.get(1).getValue().trim()));

        if (parameters.get(2).getValue().trim().equals("-1")) {
            result.setResult("OS");
            return result;
        }
        int addressSizeExcel = (int) Double.parseDouble(parameters.get(2).getValue().trim());
        Instant before = Instant.now();
        System.out.println("Receiving Points Now");
        while ((consoleLine = IEC104InitOutputConnection.br.readLine()) != null && !consoleLine.contains("Double Command Response")) {
            //Should be finished initialization
            CommonFunctions.debugLog.debug(consoleLine);
            System.out.println(consoleLine);
            if (Duration.between(before, Instant.now()).toMillis() > 10000) {
                StringBuilder resultComment = new StringBuilder();
                resultComment.append("Result Mismatch \n Received Incorrect value. ");
                result.setComment(resultComment.toString());
                return result;
            } else if (consoleLine.contains("Unknown Sector")) {
                StringBuilder resultComment = new StringBuilder();
                resultComment.append("Result MisMatch \n Received Incorrect Point Address. ");
                result.setComment(resultComment.toString());
                return result;
            }
        }
        System.out.println("Received DO Command with message: " + consoleLine);
        for (int i = 0; i < DOHexOffset; i++) {
            consoleLine = IEC104InitOutputConnection.br.readLine();
            CommonFunctions.debugLog.info("Line " + ++i + ": " + consoleLine);
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

        if (pointAddressReceived == pointAddressExcel && addressSizeReceived == addressSizeExcel) {
            result.setResult("OK");
            StringBuilder resultComment = new StringBuilder();
            resultComment.append("Result OK \n Received Point Address: ").append(pointAddressReceived);
            resultComment.append("\n Actual Point Address: ").append(pointAddressExcel);
            resultComment.append("\n Received Address Size: ").append(addressSizeReceived);
            resultComment.append("\n Actual Address Size: ").append(addressSizeExcel);
            result.setComment(resultComment.toString());
        } else {
            StringBuilder resultComment = new StringBuilder();
            resultComment.append("Result MisMatch \n Received Point Address: ").append(pointAddressReceived);
            resultComment.append("\n Actual Point Address: ").append(pointAddressExcel);
            resultComment.append("\n Received Address Size: ").append(pointAddressExcel);
            resultComment.append("\n Actual Address Size: ").append(addressSizeExcel);
            result.setComment(resultComment.toString());
        }
        return result;
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
}

