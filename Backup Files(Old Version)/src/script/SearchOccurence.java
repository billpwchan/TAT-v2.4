/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.jcraft.jsch.JSchException;
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
public class SearchOccurence {

    ArrayList<String> arrayToSearchIn;
    String hashMapIndex;
    String regularExpression;
    String toCompare;
    String indexNameReturn;

    /**
     * Constructor of SearchOccurence.
     */
    public void SearchOccurence() {
    }

    /**
     *
     * @param parameters
     * @param hashMap
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws JSchException, IOException {
        Result result = new Result();
        result.setResult("NOK");
        System.out.println("enter search occurence");
        this.toCompare = parameters.get(1).getValue().trim();
        if (this.toCompare.contains("@&Buffer_")) {
            this.toCompare = (String) ((ArrayList) hashMap.get(this.toCompare)).get(0);         //Special case
        }
        this.regularExpression = parameters.get(2).getValue().trim();
        this.hashMapIndex = parameters.get(3).getValue().trim();
        this.arrayToSearchIn = (ArrayList<String>) hashMap.get(this.hashMapIndex);
        //System.out.println("ARRAY = " + this.arrayToSearchIn);
        System.out.println("buffer size= " + this.arrayToSearchIn.size());
        if (this.arrayToSearchIn.isEmpty()) {
            result.setComment("No lines found");
        } else {
            if (!"".equals(this.regularExpression) && !"null".equals(this.regularExpression)) {
                String compare = "";
                Pattern p = Pattern.compile(this.regularExpression);
                Matcher m = p.matcher(this.arrayToSearchIn.get(0));
                try {
                    m.find();
                    /*
                     Double severityDoub = Double.parseDouble(this.toCompare);
                     if ((severityDoub % 1 == 0) && (severityDoub / 4 == 0) && !this.toCompare.contains("[a-zA-Z]+")) {
                     this.toCompare = String.valueOf((int) Math.round(severityDoub)); 
                     }
                     this.toCompare = this.toCompare.trim();
                     */
                    compare = m.group(1);

                    compare = compare.replace("|", "");
                    compare = compare.trim();

                    if (isInteger(this.toCompare)) {
                        this.toCompare += ".0";
                    }

                    if (isInteger(compare)) {
                        compare = compare + ".0";
                    }

                    System.out.println("Compare: " + compare);

                    if (compare.equals(this.toCompare)) {
                        result.setResult("OK");
                    } else {
                        result.setComment("Missmatch \n" + "Searched : " + this.toCompare + "\n" + "Found = " + compare);
                    }
                } catch (Exception e) {
                    System.out.println("in catch = " + e);
                    result.setComment("Mismatch \n" + "Searched: " + this.toCompare + "\n" + "Found = " + "ErrorInVerificationOfHMI");
                }

            } else {
                //Scanner txtscan = new Scanner(this.arrayToSearchIn.get(0));
                if (this.arrayToSearchIn.get(0).indexOf(this.toCompare) != -1) {
                    result.setResult("OK");
                }
            }
//            System.out.println("COMPARE = " + compare);
//            System.out.println("COMPARE = " + toCompare);
//            System.out.println("EQUALS = " + compare.equals(this.toCompare));
        }
        System.out.println("FIN SearchOccurence");
        return result;
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     *
     */
    public void close() {

    }
}
