/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import com.jcraft.jsch.JSchException;
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

        //System.out.println("TO SEARCH = " + this.toCompare);
        //System.out.println("REGULAR EX = " + this.regularExpression);
        //System.out.println("Taille est de : " + this.arrayToSearchIn.size());
        for (int i = 0; i < this.arrayToSearchIn.size(); i++) {
            try {
                //System.out.println("LINE = " + this.arrayToSearchIn.get(i));
                Pattern p = Pattern.compile(this.regularExpression);
                //System.out.println("P= " + p);
                Matcher m = p.matcher(this.arrayToSearchIn.get(i));
                //System.out.println("m= " + m);
                String compare = "";
                try {
                    m.find();
                    //System.out.println("FIND");
                    compare = m.group(1);
                    //System.out.println("COMPARE = " + compare);
                } catch (Exception e) {
                    System.out.println("No match found");
                }
                
                this.toCompare = this.toCompare.trim();
                //System.out.println("TO COMPARE = " + compare);
                compare = compare.replace("|", "");
                compare = compare.trim();
//                try {
//                    System.out.println("TO COMPARE = " + compare);
//                } catch (Exception e) {
//                    System.out.println("Exception = " + e);
//                }
                if (compare.equals(this.toCompare)) {
                    toReturn.add(this.arrayToSearchIn.get(i));
                    //System.out.println("j'ai trouve que "+compare+"="+this.toCompare);
                    //System.out.println("FIND=  "+this.arrayToSearchIn.get(i));
                }
            } catch (Exception e) {
                   System.out.println("Exception e= "+e);
            }
        }
        hashMap.put(this.indexNameReturn, toReturn);
        return null;
    }

    public void close() {

    }
}
