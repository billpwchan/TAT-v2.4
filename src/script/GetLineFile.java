/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import controller.util.CommonFunctions;
import engine.Result;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Thomas Morin
 */
public class GetLineFile implements InterfaceScript {

    private String FilePath;
    private String bufferName;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        String stringToReturn = null;
        ArrayList<String> tableToReturn = new ArrayList();
        this.FilePath = parameters.get(1).getValue().trim();
        this.bufferName = parameters.get(2).getValue().trim();
        try {
            String currentLine;
            BufferedReader br = new BufferedReader(new FileReader(this.FilePath));
            while ((currentLine = br.readLine()) != null) {
                stringToReturn = currentLine;
            }
        } catch (Exception e) {
            CommonFunctions.debugLog.error("Exception e= " + e);
        }
        tableToReturn.add(stringToReturn);
        hashMap.put(bufferName, tableToReturn);
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<Parameters> parameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Script scriptInfos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
