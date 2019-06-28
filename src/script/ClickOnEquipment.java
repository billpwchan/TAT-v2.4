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
import model.Position;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author tmartinez
 */
public class ClickOnEquipment implements InterfaceScript {

    private Position position;
    
    private String bufferName;
    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Result result = new Result();
        result.setResult("NOK");
        this.bufferName = parameters.get(1).getValue();
        if (this.bufferName.contains("@&Buffer_")) {
            this.position = (Position) hashMap.get(this.bufferName);
        }
        CommonFunctions.debugLog.error("EQp : " + this.position);
        Mouse mouse =  new DesktopMouse();
        Thread.sleep(800);
        mouse.click(this.position.getScreenRegion().getCenter());
        
        return result;
    } 

    @Override
    public void close() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
