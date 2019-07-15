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
 * @author Thomas Morin
 */
public class launchSoftware implements InterfaceScript {

    static Process process;
    private String pathSoftware;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        this.pathSoftware = parameters.get(1).getValue().trim();
        if (this.pathSoftware.contains("@&Buffer_")) {
            this.pathSoftware = (String) hashMap.get(this.pathSoftware);
        }

        String OS = System.getProperty("os.name");
        if (OS.trim().toLowerCase().contains("windows")) {
            process = new ProcessBuilder(this.pathSoftware).start();
        }
        return null;
    }

    @Override
    public void close() {
        process.destroyForcibly();
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
