/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import main.Main;

import java.util.ArrayList;

/**
 * @author Martinez Thibault.
 */
public class setCursorOnComponent {

    /**
     * Default constructor.
     */
    public setCursorOnComponent() {

    }

    /**
     * Set the cursor Hand on every node given im the arraylist component when entering into this node and set it back to the default when it exit the node.
     *
     * @param component
     */
    public void setCursorHand(ArrayList<Node> component) {

        component.stream().map((component1) -> {
            component1.setOnMouseEntered((Event event) -> {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                Main.primaryStage.getScene().setCursor(Cursor.HAND); //Change cursor to hand
            });
            return component1;
        }).forEach((component1) -> {
            component1.setOnMouseExited((Event event) -> {
                if (Main.isSet) {
                    Image image = new Image("/assets/images/valid.png");
                    Main.primaryStage.getScene().setCursor(new ImageCursor(image));

                } else {
                    Main.primaryStage.getScene().setCursor(Cursor.DEFAULT);
                }
            });
        });

    }

}
