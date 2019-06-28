/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import engine.Result;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Thomas Morin
 */
public class Concat implements InterfaceScript {

    private String string1;
    private String string2;
    private String stringToReturn;
    private String bufferName;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        this.string1 = parameters.get(1).getValue().trim();
        if (this.string1.contains("@&Buffer_")) {
            this.string1 = (String) hashMap.get(this.string1);
        }
        this.string2 = parameters.get(2).getValue().trim();
        if (this.string2.contains("@&Buffer_")) {
            this.string2 = (String) hashMap.get(this.string2);
        }
        this.bufferName = parameters.get(3).getValue().trim();
        this.stringToReturn = string1.concat(" " + string2);
        hashMap.put(bufferName, this.stringToReturn);
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
