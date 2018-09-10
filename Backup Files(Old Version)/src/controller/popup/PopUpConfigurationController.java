/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import configuration.settings;
import controller.TATFrameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static main.Main.primaryStage;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class PopUpConfigurationController implements Initializable {

    @FXML
    private Button buttonSaveParams;
    @FXML
    private TextField textFieldPath;
    @FXML
    private Button pathChooseScripts;

    private TATFrameController maincontroller;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     *
     * @param mainController
     */
    public void init(TATFrameController mainController) {
        maincontroller = mainController;

        pathChooseScripts.setOnAction((ActionEvent event) -> {
            directoryChooserPathScripts();
            maincontroller.focusOnSettings();
        });

        buttonSaveParams.setOnAction((ActionEvent event) -> {
            maincontroller.setOnValidParams();
        });

    }

    /**
     *
     */
    public void constructInformation() {
        textFieldPath.setText(settings.scriptsPaht);
    }

    /**
     *
     */
    public void directoryChooserPathScripts() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Directory Scripts");
        File selectedDirectory = chooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            textFieldPath.setText(selectedDirectory.getAbsolutePath());
            settings.scriptsPaht = selectedDirectory.getAbsolutePath();
        }

    }

}
