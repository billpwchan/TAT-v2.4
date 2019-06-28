/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DBcontroller.TestCampaignDB;
import controller.tabtestcase.TabTestCaseLibraryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class PopUpCampaignCreationController implements Initializable {

    @FXML
    private AnchorPane anchorPaneCampaignCreation;
    @FXML
    private TextField textFieldID;
    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonCancel;
    @FXML
    private Label labelCampaignAlreadyExists;

    private Stage dialogStage;

    private TabTestCaseLibraryController mainController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.labelCampaignAlreadyExists.setVisible(false);
        this.buttonOk.setDisable(true);
        this.buttonOk.setOnAction((ActionEvent event) -> {
            this.getCampaignExistance(this.textFieldID.getText());
        });

        textFieldID.textProperty().addListener((observable, oldValue, newValue) -> {
            this.buttonOk.setDisable(newValue.trim().isEmpty());
            this.labelCampaignAlreadyExists.setVisible(false);
        });

        this.buttonCancel.setOnAction((ActionEvent event) -> {
            this.mainController.closePopUpCampaign();
        });
    }

    /**
     * @param mainController
     */
    public void init(TabTestCaseLibraryController mainController) {
        this.mainController = mainController;
    }

    /**
     * @param stage
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * @param campaigID
     */
    public void getCampaignExistance(String campaigID) {
        TestCampaignDB testCampaignHandler = new TestCampaignDB();
        if (testCampaignHandler.checkCampaignExistence(campaigID) >= 1) {
            labelCampaignAlreadyExists.setVisible(true);
        } else {
            this.mainController.setOnActionCampaignID(this.textFieldID.getText());
        }
    }

}
