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
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;

/**
 *
 * @author tmartinez
 */
public class TakeScreenshot implements InterfaceScript {

    DateFormat df = new SimpleDateFormat("hh-mm-ss");
    
    private String pathToSave;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        pathToSave = parameters.get(1).getValue();
        ScreenRegion desktop = new DesktopScreenRegion();
        File outputfile = new File(pathToSave+"\\-Date-" +df.format(Calendar.getInstance().getTime())+".png");
        ImageIO.write(desktop.capture(), "png", outputfile);
        return null;
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
