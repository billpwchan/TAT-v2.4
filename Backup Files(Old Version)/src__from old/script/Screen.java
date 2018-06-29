package script;

import DB.ParametersExecution;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Class which perform a screenshot of the screen. this is a stimuli script for
 * the TAT
 *
 * @version 1.0
 * @author Thomas Morin
 */
public class Screen {

    /**
     * length of the screenshot.
     */
    private int x;

    /**
     * width of the screenshot.
     */
    private int y;

    /**
     * path where the screenshot should be stored.
     */
    private String path;

    /**
     * name of the screenshot.
     */
    private String name;

    /**
     * Constructor of a screenshot. get the parameters and run the script.
     *
     * @param parameters the parameters of the script
     * @throws IOException
     * @throws AWTException
     */
    public Screen() {

    }

    /**
     * Take a screenshot.
     *
     * @throws IOException
     * @throws AWTException
     */
    public void run(ArrayList<ParametersExecution> parameters, HashMap hashmap) throws IOException, AWTException {
        this.x = (int) Double.parseDouble(parameters.get(1).getValue());
        this.y = (int) Double.parseDouble(parameters.get(3).getValue());
        this.path = parameters.get(3).getValue();
        this.name = parameters.get(4).getValue();
        //this.treatParam(parameters);
        final BufferedImage image = new Robot().createScreenCapture(new Rectangle(this.x, this.y));
        ImageIO.write(image, "png", new File(this.path + "/" + this.name + ".png"));
    }

    /**
     * set a new length of the screenshot.
     *
     * @param x the new length
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * set a new width of the screenshot.
     *
     * @param y the new width
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * set a new path for the screenshot.
     *
     * @param path the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * set a new name for the screenshot.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void close() {

    }

    /**
     * set the parameters of the screenshot.
     *
     * @param params the new parameters of the screenshot
     */
//	public void treatParam(int x,int y,String path,String name)
//	{
//		final String[] parameters=params.split(",");
//		this.x=Integer.parseInt(parameters[0]);
//		this.y=Integer.parseInt(parameters[1]);
//		this.path=parameters[2];
//		this.name=parameters[3];
//	}
}
