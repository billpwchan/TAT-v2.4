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
import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author tmartinez
 */
public class MoveMouseCoordinates implements InterfaceScript {


    private int x, y;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        x = Integer.valueOf(parameters.get(1).getValue());
        y = Integer.valueOf(parameters.get(2).getValue());

        ScreenRegion s = new DesktopScreenRegion();
        ScreenRegion test = new DefaultScreenRegion(s, x, y, 1, 1);
        Mouse m = new DesktopMouse();
        m.drop(test.getCenter());
        //m.click(m.getLocation());

        return null;
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
