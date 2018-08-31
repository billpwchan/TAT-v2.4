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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;
import javax.imageio.ImageIO;
import static main.Main.HMIs;
import model.Classe;

import model.HMI;
import model.Position;
import model.equipment;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.StyledRectangleTarget;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

/**
 *
 * @author T0155040
 */
public class TriggerEquipment implements InterfaceScript {

    private String nameClasse, nameEquipment, indexNameReturn, nameHMI;

    private final Mouse mouse = new DesktopMouse();

    private ScreenLocation locationUp, locationDown, locationLeft, locationRight, locationPlus, locationMinus, locationRescale;

    private ImageTarget targetUp, targetDown, targetPlus, targetMinus, targetCenter, targetLeft, targetRight, targetBack, targetFront, targetLogout;

    private final static double minScore = 0.999;

    /**
     *
     */
    public ArrayList<String> pathUtilitise = new ArrayList<>(Arrays.asList("", "", "", "", "", "", "", "", "", "", ""));

    // private boolean lookAndFeelExist = false;
    // private final Screen screen = new Screen();
    DateFormat df = new SimpleDateFormat("hh-mm-ss");

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {

        boolean foundEQP = false;
        Thread.sleep(3000);
        nameHMI = parameters.get(1).getValue();
        nameClasse = parameters.get(2).getValue();
        nameEquipment = parameters.get(3).getValue();
        //positionEquipment = parameters.get(4).getValue();
        indexNameReturn = parameters.get(4).getValue();

        ArrayList<ScreenRegion> toClick = new ArrayList<>();
        ArrayList<String> actionButton = new ArrayList<>();
        Result result = new Result();

        ArrayList<HMI> zobi = HMIs;
        HMI hmifound = null;
        System.out.println("name to find : " + nameHMI);
        for (HMI h : zobi) {
            System.out.println("name hmi : " + h.getName());
            if (h.getName().equals(this.nameHMI)) {
                hmifound = h;
                System.out.println("HMI found and name is : " + hmifound.getName());
                break;
            }
        }

        if (hmifound != null) {
            test(hmifound);
        }

        Classe classFound = null;
        ArrayList<Classe> classe = hmifound.getClasses();

        for (Classe c : classe) {
            if (c.getClasseName().equals(nameClasse)) {
                classFound = c;
                break;
            }
        }
        equipment currentEquipment = null;
        for (equipment e : classFound.getEquipments()) {
            if (e.getEquipmentName().equals(nameEquipment)) {
                currentEquipment = e;
                foundEQP = true;
                break;
            }
        }

        Position currentPosition = currentEquipment.getPositions().iterator().next();
//        for(Position pos: currentEquipment.getPositions()){
//            if()
//        }
        if (!foundEQP) {
            result.setResult("NOK");
            result.setComment("Equipment Not found in the HMI mapping");
            return result;
        }
        Path path = currentPosition.getPath();
        String tamp = path.getFileName().toString().replace("IMAGE", "");
        tamp = tamp.replace(".png", "");
        actionButton = new ArrayList(Arrays.asList(tamp.split("||")));
//        for (String s : actionButton) {
//            System.out.println("Value : " + s);
//        }
        path = path.getParent();
        //String toBeEquals =main.Main.pathForFunctional+"\\testLibrary"; 
        boolean found = false;
        while (!found) {
            if ("HMI-Mapping".equals(path.getFileName().toString())) {
                found = true;
            } else {

                try (BufferedReader br = new BufferedReader(new FileReader(hmifound.getPathHMI() + path.toString() + "\\goto.txt"))) {
                    //System.out.println("Path is : " + main.Main.pathForFunctional + path.toString() + "\\goto.txt");
                    String sCurrentLine;

                    while ((sCurrentLine = br.readLine()) != null) {
                        if (!sCurrentLine.isEmpty()) {
                            String[] ScreenRegion = sCurrentLine.split(",");
                            System.out.println(sCurrentLine);
                            System.out.println("toto " + Double.valueOf(ScreenRegion[0]).intValue());
//                                Double.valueOf(ScreenRegion[1]).intValue()+
//                                Double.valueOf(ScreenRegion[2]).intValue()+
//                                Double.valueOf(ScreenRegion[3]).intValue());
                            ScreenRegion sr = new DesktopScreenRegion(
                                    Double.valueOf(ScreenRegion[0]).intValue(),
                                    Double.valueOf(ScreenRegion[1]).intValue(),
                                    Double.valueOf(ScreenRegion[2]).intValue(),
                                    Double.valueOf(ScreenRegion[3]).intValue());

                            toClick.add(sr);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                toClick.stream().forEach((s) -> {
//                    //System.out.println("Value : " + s);
//                });
                //System.out.println("path is :" + path);
                path = path.getParent();
            }
        }

        for (int i = toClick.size() - 1; i >= 0; i--) {
            clickOnTarget(toClick.get(i));
        }

        InitializeButton();
        prepareLayout(actionButton);
        //ScreenRegion sr =  ;
        System.out.println("EQP : " + currentPosition);
        Thread.sleep(2500);

        hashMap.put(this.indexNameReturn, currentPosition);
        System.out.println("HASH SIZE = " + hashMap.size());
        //File outputfile = new File("C:\\Users\\tmartinez\\Desktop\\imagesFunctional\\Equipment" +currentEquipment.getEquipmentName()+"-Date-" +df.format(Calendar.getInstance().getTime()) + ".png");
        //ImageIO.write(currentPosition.getScreenRegion().capture(), "png", outputfile);
        //this.clickOnTarget(currentEquipment.getScreenRegion());
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

    private void clickOnTarget(ScreenRegion screen) {
        try {
            DesktopScreenRegion s = new DesktopScreenRegion();
            s.setBounds(new java.awt.Rectangle(0, 0, 1680, 1050));

            mouse.click(screen.getCenter());
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TriggerEquipment.class.getName()).error("", ex);
        }
    }

    private void InitializeButton() throws IOException {

        if (!pathUtilitise.get(0).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(0)));
            targetPlus = new ImageTarget(toBufferedImage(t));
            targetPlus.setMinScore(minScore);
        }
        if (!pathUtilitise.get(1).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(1)));
            targetMinus = new ImageTarget(toBufferedImage(t));
            targetMinus.setMinScore(minScore);
        }
        if (!pathUtilitise.get(2).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(2)));
            targetBack = new ImageTarget(toBufferedImage(t));
        }
        if (!pathUtilitise.get(3).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(3)));
            targetCenter = new ImageTarget(toBufferedImage(t));
            targetCenter.setMinScore(minScore);
        }
        if (!pathUtilitise.get(4).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(4)));
            targetFront = new ImageTarget(toBufferedImage(t));
        }
        if (!pathUtilitise.get(5).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(5)));
            targetLogout = new ImageTarget(toBufferedImage(t));
        }
        if (!pathUtilitise.get(7).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(7)));
            targetUp = new ImageTarget(toBufferedImage(t));
            targetUp.setMinScore(minScore);
        }
        if (!pathUtilitise.get(8).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(8)));
            targetDown = new ImageTarget(toBufferedImage(t));
            targetDown.setMinScore(minScore);
        }
        if (!pathUtilitise.get(9).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(9)));
            targetLeft = new ImageTarget(toBufferedImage(t));
            targetLeft.setMinScore(minScore);
        }
        if (!pathUtilitise.get(10).isEmpty()) {
            Image t = ImageIO.read(new File(pathUtilitise.get(10)));
            targetRight = new ImageTarget(toBufferedImage(t));
            targetRight.setMinScore(minScore);

        }

        DesktopScreenRegion desktop = new DesktopScreenRegion();
        desktop.setBounds(new java.awt.Rectangle(0, 0, 1680, 1050));
        locationUp = desktop.find(targetUp).getCenter();
        locationDown = desktop.find(targetDown).getCenter();
        locationLeft = desktop.find(targetLeft).getCenter();
        locationRight = desktop.find(targetRight).getCenter();
        locationPlus = desktop.find(targetPlus).getCenter();
        locationMinus = desktop.find(targetMinus).getCenter();
        locationRescale = desktop.find(targetCenter).getCenter();
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    private void prepareLayout(ArrayList<String> actionButton) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        actionButton.stream().forEach((s) -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(TriggerEquipment.class.getName()).error("", ex);
            }
            System.out.println("J'appuie sur " + s);
            switch (s) {
                case "P":
                    plus();
                    break;
                case "M":
                    minus();
                    break;
                case "L":
                    goLeft();
                    break;
                case "R":
                    goRight();
                    break;
                case "U":
                    goUp();
                    break;
                case "D":
                    goDown();
                    break;
                case "C":
                    center();
                    break;
                default:
                    System.out.println("Key non recognized");
                    break;
            }
        });

    }

    private void goUp() {
        mouse.click(locationUp);
    }

    private void goDown() {
        mouse.click(locationDown);

    }

    private void goLeft() {
        mouse.click(locationLeft);
    }

    private void goRight() {
        mouse.click(locationRight);
    }

    private void center() {
        mouse.click(locationRescale);
    }

    private void minus() {
        mouse.click(locationMinus);
    }

    private void plus() {
        mouse.click(locationPlus);
    }

    private void test(HMI currentHMI) {
        String settingsPath = currentHMI.getPathHMI().toString();
        File[] directories = new File(settingsPath + "\\Settings").listFiles();
        for (File fileImage : directories) {
            //System.out.println("File path is : " + fileImage.getAbsolutePath());
            if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\plus.png")) {
                pathUtilitise.set(0, settingsPath + "\\Settings\\plus.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\minus.png")) {
                pathUtilitise.set(1, settingsPath + "\\Settings\\minus.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\back.png")) {
                pathUtilitise.set(2, settingsPath + "\\Settings\\back.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\center.png")) {
                pathUtilitise.set(3, settingsPath + "\\Settings\\center.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\front.png")) {
                pathUtilitise.set(4, settingsPath + "\\Settings\\front.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\logout.png")) {
                pathUtilitise.set(5, settingsPath + "\\Settings\\logout.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\up.png")) {
                pathUtilitise.set(7, settingsPath + "\\Settings\\up.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\down.png")) {
                pathUtilitise.set(8, settingsPath + "\\Settings\\down.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\left.png")) {
                pathUtilitise.set(9, settingsPath + "\\Settings\\left.png");
            } else if (fileImage.getAbsolutePath().equals(settingsPath + "\\Settings\\right.png")) {
                pathUtilitise.set(10, settingsPath + "\\Settings\\right.png");
            }

        }
    }

//    private ScreenRegion getScreenRegion(Screen screen, ScreenLocation sr) {
//        ScreenRegion r;
//        //r = new DesktopScreenRegion(sr.);//new DefaultScreenRegion(screen,currentEquipment.getScreenRegion().getBounds().x)
//        return r;
//    }
}
