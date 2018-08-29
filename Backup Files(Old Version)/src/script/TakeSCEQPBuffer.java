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
import script.InterfaceScript;

/**
 *
 * @author tmartinez
 */
public class TakeSCEQPBuffer implements InterfaceScript {

    private Position position;

    private String pathSave, EQPName, indexNameReturn;

    private String bufferName;
    DateFormat df = new SimpleDateFormat("hh-mm-ss");

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        this.pathSave = parameters.get(1).getValue();
        this.EQPName = parameters.get(2).getValue();
        this.bufferName = parameters.get(3).getValue();
        if (this.bufferName.contains("@&Buffer_")) {
            this.position = (Position) hashMap.get(this.bufferName);
        }
        this.indexNameReturn = parameters.get(4).getValue().trim();
        String savedImagePath = this.pathSave + "\\" + this.EQPName + ".png";
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        File outputfile = new File(savedImagePath);
        ImageIO.write(position.getScreenRegion().capture(), "png", outputfile);
        hashMap.put(this.indexNameReturn, savedImagePath);
        System.out.println("Path of image is : "+savedImagePath);
        Thread.sleep(500);
        return null;
    }

    @Override
    public void close() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Parameters> parameters() {
        ArrayList<Parameters> params = new ArrayList<>();

        Parameters pathSave = new Parameters();
        pathSave.setName("Save path");
        pathSave.setParameterType("string");
        pathSave.setDescription("Folder path to save the image of the equipment");
        params.add(pathSave);

        Parameters paramEQP = new Parameters();
        paramEQP.setName("EQP name");
        paramEQP.setParameterType("string");
        paramEQP.setDescription("Name of the equipment");
        params.add(paramEQP);

        Parameters paramEquipment = new Parameters();
        paramEquipment.setName("Equipment");
        paramEquipment.setParameterType("buffer");
        paramEquipment.setDescription("Buffer of the equipment to take");
        params.add(paramEquipment);

        Parameters paramOutBuff = new Parameters();
        paramOutBuff.setName("Path buffer");
        paramOutBuff.setParameterType("buffer");
        paramOutBuff.setDescription("Buffer in which the path of the new image is saved");
        params.add(paramOutBuff);

        return params;
    }

    @Override
    public Script scriptInfos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
