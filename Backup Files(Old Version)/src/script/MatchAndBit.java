/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
//import com.mchange.io.FileUtils;
import engine.Result;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author T0155040
 */
public class MatchAndBit implements InterfaceScript {

    private String image1, image2, savePath;
    private double seuil;

    private static final String LIB_BIN = "/src/file/";

//    static {
//        System.loadLibrary("opencv_java2411");
//    }
    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) {
        System.out.println("1");
        long tempsDebut3 = System.currentTimeMillis();

        loadFromJar();

        DecimalFormat df = new DecimalFormat("###.#####");
//
        //System.load("C:\\opencv-2\\build\\java\\x64\\opencv_java2411.dll");
        // System.load("opencv_java2411");

        image1 = parameters.get(2).getValue();
        image2 = parameters.get(1).getValue();
        seuil = Double.valueOf(parameters.get(3).getValue());
        int count = 0;
        int match_method = Imgproc.TM_CCOEFF_NORMED;
//
        //Mat templ = Imgcodecs.imread(image1);
        Mat templ = Highgui.imread(image1);
        Mat img = Highgui.imread(image2);
        //Mat img = Imgcodecs.imread(image2);
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(img, templ, result, match_method);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        System.out.println("MMR max value : " + mmr.maxVal);
        System.out.println("MMR max value : " + mmr.minVal);
        //accuracy = mmr.maxVal;

        Point point = mmr.maxLoc;
        System.out.println("Point : " + point);
        //Imgproc.rectangle(img, point, new Point(point.x + templ.cols(), point.y + templ.rows()), new Scalar(0, 255, 0));
        for (int x = 0; x < templ.cols(); x++) {
            for (int y = 0; y < templ.rows(); y++) {
                // Compare the pixels for equality.
                int x_img = (int) (point.x + x);
                int y_img = (int) (point.y + y);
                //System.out.println("(x,y) tempalte : ("+x+","+y+"); (x,y) Image : ("+y_img+","+x_img+")");
                System.out.println("Value template : " + Arrays.toString(templ.get(y, x)) + " Value img : " + Arrays.toString(img.get(y_img, x_img)));

                if (Arrays.equals(img.get(y_img, x_img), templ.get(y, x))) {
                    count++;
                }
            }
        }

        Result returnResult = new Result();
        float resultPourcen = ((float) count / (float) (templ.cols() * templ.rows()));
        System.out.println("Count value : " + count + " Number of pixel tested : " + (templ.cols() * templ.rows()));
        // System.out.println("Result : "+resultPourcen);
        System.out.println("Matching value : " + df.format(resultPourcen));
        returnResult.setComment("Matching value : " + df.format(resultPourcen));
        if (resultPourcen >= seuil) {
            returnResult.setResult("OK");
            Core.rectangle(img, point, new Point(point.x + templ.cols(), point.y + templ.rows()), new Scalar(0, 255, 0));
            //Imgproc.rectangle(img, point, new Point(point.x + templ.cols(), point.y + templ.rows()), new Scalar(0, 255, 0));
        } else {
            returnResult.setResult("NOK");
        }
        Core.rectangle(img, point, new Point(point.x + templ.cols(), point.y + templ.rows()), new Scalar(0, 255, 0));
        //Imgproc.rectangle(img, point, new Point(point.x + templ.cols(), point.y + templ.rows()), new Scalar(0, 255, 0));
        //Core.imwrite("D:\\Users\\t0155040\\Desktop\\Result.png", img);
        long tempsFin3 = System.currentTimeMillis();
        float seconds3 = (tempsFin3 - tempsDebut3) / 1000F;
        System.out.println("Comparison time= " + Float.toString(seconds3));
        return returnResult;
    }

    @Override
    public void close() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Parameters> parameters() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }

    @Override
    public Script scriptInfos() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }


    /**
     * When packaged into JAR extracts DLLs, places these into
     */
    private static void loadFromJar() {
        // we need to put both DLLs to temp dir
        //String path = "AC_" + new Date().getTime();
        loadLib("opencv_java2411");
    }

    /**
     * Puts library to temp dir and loads to memory
     */
    private static void loadLib( String name) {
        System.out.println("String loaded");
        name = "/file/opencv_java2411.dll";
        try {
            File temp;
            try ( // have to use a stream
                    InputStream in = MatchAndBit.class.getResourceAsStream( name)) {
                System.out.println(" try"+in.toString());
                byte[] buffer = new byte[1024];
                int read = -1;
                 temp = File.createTempFile(name, "");//new File(new File(System.getProperty("java.io.tmpdir")), name); //temp = File.createTempFile(name, "");
                try (FileOutputStream fos = new FileOutputStream(temp)) {
                    while ((read = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                    }
                }
            }
            // always write to different location
//            File fileOut = new File(System.getProperty("java.io.tmpdir") + "/" + path + LIB_BIN + name);
//            //logger.info("Writing dll to: " + fileOut.getAbsolutePath());
//            //File temp = File.createTempFile(name, "");
//            fileOut.mkdirs();
//           OutputStream out = new FileOutputStream(fileOut);
//           
////  OutputStream out = FileUtils.openOutputStream(fileOut);          
//            IOUtils.copy(in, out);
//            in.close();
//            out.close();
            System.load(temp.getAbsolutePath());
            System.out.println("String loaded");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MatchAndBit.class.getName()).error("", ex);
        } catch (IOException ex) {
            Logger.getLogger(MatchAndBit.class.getName()).error("", ex);
        }
    }

}
