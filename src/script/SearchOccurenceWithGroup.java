/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.jcraft.jsch.JSchException;
import controller.util.CommonFunctions;
import engine.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class SearchOccurenceWithGroup {

    ArrayList<String> arrayToSearchIn;
    String hashMapIndex;
    String regularExpression;
    String toCompare;
    String indexNameReturn;
    int group;


    /**
     * @param parameters
     * @param hashMap
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws JSchException, IOException {
        Result result = new Result();
        result.setResult("NOK");
        CommonFunctions.debugLog.error("enter search occurence");
        this.toCompare = parameters.get(1).getValue().trim();
        if (this.toCompare.contains("@&Buffer_")) {
            this.toCompare = (String) hashMap.get(this.toCompare);         //Special case
        }
        this.regularExpression = parameters.get(2).getValue().trim();
        this.hashMapIndex = parameters.get(3).getValue().trim();
        this.arrayToSearchIn = (ArrayList<String>) hashMap.get(this.hashMapIndex);
        this.group = Integer.parseInt(parameters.get(4).getValue().trim());

        CommonFunctions.debugLog.debug("INPUT TIMESTAMP: " + this.toCompare + "\nEVENT TIMESTAMP: " + this.arrayToSearchIn);
        if (this.arrayToSearchIn.isEmpty()) {
            result.setComment("No lines found");
        } else {
            if (!"".equals(this.regularExpression) && !"null".equals(this.regularExpression)) {
                String compare = "";
                Pattern p = Pattern.compile(this.regularExpression);
                Matcher m = p.matcher(this.arrayToSearchIn.get(0));
                try {
                    m.find();
                    compare = m.group(this.group);
                    compare = compare.trim();
                    CommonFunctions.debugLog.error("Compare: " + compare);
                    if (this.toCompare.equals("-1.0")) {
                        System.out.println("Search OS");
                        result.setResult("OS");
                    } else if (compare.equals(this.toCompare)) {
                        result.setResult("OK");
                    } else {
                        result.setComment("Missmatch \n" + "Searched : " + this.toCompare + "\n" + "Found = " + compare);
                    }
                } catch (Exception e) {
                    CommonFunctions.debugLog.error("in catch = " + e);
                    result.setComment("Mismatch \n" + "Searched: " + this.toCompare + "\n" + "Found = " + "ErrorInVerificationOfHMI");
                }

            } else {
                //Scanner txtscan = new Scanner(this.arrayToSearchIn.get(0));
                if (this.arrayToSearchIn.get(0).contains(this.toCompare)) {
                    result.setResult("OK");
                }
            }
        }
        return result;
    }

    /**
     *
     */
    public void close() {

    }
}
