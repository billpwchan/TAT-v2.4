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
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

/**
 *
 * @author T0155040
 */
public class clickSouris implements InterfaceScript{

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) {
        Mouse m = new DesktopMouse();
        m.click(m.getLocation());
        
        return null;
    }

    @Override
    public void close() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Parameters> parameters() {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Script scriptInfos() {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
