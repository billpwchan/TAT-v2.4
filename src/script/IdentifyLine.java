/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.jcraft.jsch.JSchException;
import controller.util.CommonFunctions;

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
public class IdentifyLine {

    ArrayList<String> arrayToSearchIn;
    String hashMapIndex;
    String regularExpression;
    String toCompare;
    String indexNameReturn;

    /**
     * Constructor of IdentifyLine.
     */
    public void IdentifyLine() {
    }

    /**
     *
     * @param parameters
     * @param hashMap
     * @return
     * @throws JSchException
     */
    public String run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws JSchException {
        ArrayList<String> toReturn = new ArrayList<>();
        this.toCompare = parameters.get(1).getValue().trim();
        if (this.toCompare.contains("@&Buffer_")){
            this.toCompare=(String) hashMap.get(this.toCompare);
        }
        this.regularExpression = parameters.get(2).getValue().trim();
        this.hashMapIndex = parameters.get(3).getValue().trim();
        this.arrayToSearchIn = (ArrayList<String>) hashMap.get(this.hashMapIndex);
        this.indexNameReturn = parameters.get(4).getValue().trim();
        if (hashMap.get(this.indexNameReturn) != null) {
            hashMap.remove(this.indexNameReturn);
        }

        //CommonFunctions.debugLog.error("TO SEARCH = " + this.toCompare);
        //CommonFunctions.debugLog.error("REGULAR EX = " + this.regularExpression);
        //CommonFunctions.debugLog.error("Taille est de : " + this.arrayToSearchIn.size());
        for (int i = 0; i < this.arrayToSearchIn.size(); i++) {
            try {
                //CommonFunctions.debugLog.error("LINE = " + this.arrayToSearchIn.get(i));
                Pattern p = Pattern.compile(this.regularExpression);
                //CommonFunctions.debugLog.error("P= " + p);
                Matcher m = p.matcher(this.arrayToSearchIn.get(i));
                //CommonFunctions.debugLog.error("m= " + m);
                String compare = "";
                try {
                    m.find();
                    //CommonFunctions.debugLog.error("FIND");
                    compare = m.group(1);
                    //CommonFunctions.debugLog.error("COMPARE = " + compare);
                } catch (Exception e) {
                    CommonFunctions.debugLog.error("No match found");
                }

                this.toCompare = this.toCompare.trim();
                //CommonFunctions.debugLog.error("TO COMPARE = " + compare);
                compare = compare.replace("|", "");
                compare = compare.trim();
//                try {
//                    CommonFunctions.debugLog.error("TO COMPARE = " + compare);
//                } catch (Exception e) {
//                    CommonFunctions.debugLog.error("Exception = " + e);
//                }
                if (compare.equals(this.toCompare)) {
                    toReturn.add(this.arrayToSearchIn.get(i));
                    //CommonFunctions.debugLog.error("j'ai trouve que "+compare+"="+this.toCompare);
                    //CommonFunctions.debugLog.error("FIND=  "+this.arrayToSearchIn.get(i));
                }
            } catch (Exception e) {
                CommonFunctions.debugLog.error("Exception e= " + e);
            }
        }
        hashMap.put(this.indexNameReturn, toReturn);
        return null;
    }

    /**
     *
     */
    public void close() {

    }
}
