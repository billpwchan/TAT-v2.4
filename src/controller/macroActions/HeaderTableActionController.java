/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macroActions;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author T0155041, Bill Chan
 */
public class HeaderTableActionController implements Initializable {

    @FXML
    private AnchorPane anchorPanInVbox;
    @FXML
    private GridPane gridPanInAnchor;
    @FXML
    private Label labelReadyAction;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.anchorPanInVbox.getStylesheets().add("/view/CustomeStyle.css");
        this.anchorPanInVbox.getStyleClass().add("header-custom");
        this.anchorPanInVbox.getStyleClass().add("header-custom2");
    }

}
