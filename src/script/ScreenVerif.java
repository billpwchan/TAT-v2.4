package script;

import DB.ParametersExecution;
import engine.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class which verify a picture. this is a check script for the TAT
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class ScreenVerif {

    /**
     * the length of the picture.
     */
    private int x;

    /**
     * the width of the picture.
     */
    private int y;

    /**
     * the path of the picture.
     */
    private String path;

    /**
     * the name of the picture.
     */
    private String name;

    /**
     * the result of the check.
     */
    private Result result;

    /**
     * constructor of a screen verification.
     */
    public ScreenVerif() {
    }

    /**
     * run method for the screen verification.
     *
     * @param parameters
     * @param hashmap
     * @return
     * @throws IOException
     * @throws AWTException
     */
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashmap) throws IOException, AWTException {
        this.result = new Result();
        this.result.setResult("Failed");
        this.x = (int) Double.parseDouble(parameters.get(1).getValue());
        this.y = (int) Double.parseDouble(parameters.get(2).getValue());
        this.path = parameters.get(3).getValue().trim();
        this.name = parameters.get(4).getValue().trim();
        boolean error = false;
        try {
            final BufferedImage image = ImageIO.read(new File(this.path + "/" + this.name + ".png"));
            if (image.getWidth() != this.x) {
                error = true;
                this.result.setComment("Width Problem");
                this.result.setResult("NOK");
            }
            if (image.getHeight() != this.y) {
                error = true;
                this.result.setResult("NOK");
                this.result.setComment("Height Problem");
            }
            if (!error) {
                this.result.setResult("OK");

            }

        } catch (Exception e) {
            this.result.setResult("NOK");
            this.result.setComment("Failed, file not found");
        }
        return result;
    }

    /**
     * set a new length to the verification.
     *
     * @param x the new length
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * set a new width to the verification.
     *
     * @param y the new width
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * set a new path to the verification.
     *
     * @param path the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * set a new name for the verification.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter of the result for the verification.
     *
     * @return the result as a string
     */
    public Result result() {
        return this.result;
    }

    /**
     * treat the parameters of the script.
     *
     * @param params the paramters of the script
     */
//	public void treatParam(String params)
//	{
//		final String[] parameters=params.split(",");
//		this.x=Integer.parseInt(parameters[0]);
//		this.y=Integer.parseInt(parameters[1]);
//		this.path=parameters[2];
//		this.name=parameters[3];
//	}
}
