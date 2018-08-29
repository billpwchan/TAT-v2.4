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
import model.Position;

/**
 *
 * @author tmartinez
 */
public class TakeScreenShotEQP implements InterfaceScript {

    private Position position;

    private String pathSave;

    private String bufferName;
    DateFormat df = new SimpleDateFormat("hh-mm-ss");

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        this.pathSave = parameters.get(1).getValue();
        this.bufferName = parameters.get(2).getValue();
        if (this.bufferName.contains("@&Buffer_")) {
            this.position = (Position) hashMap.get(this.bufferName);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        File outputfile = new File(this.pathSave + "//Date-" + df.format(Calendar.getInstance().getTime()) + ".png");
        ImageIO.write(position.getScreenRegion().capture(), "png", outputfile);
        return null;
    }

    @Override
    public void close() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
