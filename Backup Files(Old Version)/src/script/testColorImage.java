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
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author tmartinez
 */
public class testColorImage implements InterfaceScript {

    private String color, imagePath;
    private Double miniRate;

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {

        color = parameters.get(2).getValue();
        miniRate = Double.valueOf(parameters.get(3).getValue());

        this.imagePath = parameters.get(1).getValue().trim();
        if (this.imagePath.contains("@&Buffer_")) {
            this.imagePath = (String) hashMap.get(this.imagePath);
        }
        System.out.println("Path is :" + this.imagePath.trim());
        DecimalFormat df = new DecimalFormat("###.###");

        int countPixel = 0;
        System.out.println("Color is : "+color.trim());
        Color col = Color.web(color.trim());
        File file = new File(imagePath.trim());
        URL url = file.toURI().toURL();
        Image img = new Image(url.toExternalForm());
//        Image img = new Image(imagePath.trim());
        //Image img = new Image(imagePath);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if (img.getPixelReader().getColor(j, i).equals(col)) {
                    countPixel++;
                }
            }
        }
        Result result = new Result();
        if (miniRate > (countPixel / (img.getHeight() * img.getWidth()))) {
            result.setResult("NOK");
        } else {
            result.setResult("OK");
        }
        result.setComment("Color pourcentage is " + df.format(countPixel / (img.getHeight() * img.getWidth())));
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
