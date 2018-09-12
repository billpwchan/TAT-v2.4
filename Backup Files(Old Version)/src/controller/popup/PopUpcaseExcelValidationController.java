/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import controller.tabtestexecution.TabTestCampaignExecutionBaselineCampaignController;
import controller.util.CommonFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class PopUpcaseExcelValidationController implements Initializable {

    private final static String Separator = ("" + ((char) 7));
    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField fieldColumnPositionLocation;
    @FXML
    private TextField fieldRowPositionLocation;
    @FXML
    private TextField fieldColumnPositionCategory;
    @FXML
    private TextField fieldRowPositionCategory;
    @FXML
    private CheckBox checkboxLocation;
    //private RadioButton radioLocation;
    @FXML
    private CheckBox checkBoxCategory;
    //private RadioButton radioCategory;
    @FXML
    private TextField fieldRange;
    @FXML
    private Label labelRowCategory;
    @FXML
    private Label labelRowLocation;
    @FXML
    private Label labelColumnCategory;
    @FXML
    private Label labelColumnLocation;
    private TabTestCampaignExecutionBaselineCampaignController mainController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.buttonOk.setDisable(true);
        this.fieldColumnPositionLocation.setDisable(true);
        this.fieldRowPositionLocation.setDisable(true);
        this.fieldColumnPositionCategory.setDisable(true);
        this.fieldRowPositionCategory.setDisable(true);

        buttonOk.setOnAction((ActionEvent event) -> {
            okClick();
        });

        buttonCancel.setOnAction((ActionEvent event) -> {
            mainController.cancelPopUp();
        });

    }

    /**
     * @param mainController
     */
    public void init(TabTestCampaignExecutionBaselineCampaignController mainController) {
        this.mainController = mainController;
    }

    /**
     *
     */
    public void start() {
        fieldRange.textProperty().addListener((observable, oldValue, newValue) -> {
            buttonOk.setDisable(newValue.trim().isEmpty());
        });

        checkboxLocation.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                fieldColumnPositionLocation.setDisable(false);
                fieldRowPositionLocation.setDisable(false);
                setColorsLocation();
            } else {
                fieldColumnPositionLocation.setDisable(true);
                fieldRowPositionLocation.setDisable(true);
                labelColumnLocation.setTextFill(Color.BLACK);
                labelRowLocation.setTextFill(Color.BLACK);
            }
        });

        checkBoxCategory.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                fieldColumnPositionCategory.setDisable(false);
                fieldRowPositionCategory.setDisable(false);
                setColorsCategory();
            } else {
                fieldColumnPositionCategory.setDisable(true);
                fieldRowPositionCategory.setDisable(true);
                labelColumnCategory.setTextFill(Color.BLACK);
                labelRowCategory.setTextFill(Color.BLACK);
            }
        });

        fieldColumnPositionLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            setColorsLocation();
        });

        fieldRowPositionLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            setColorsLocation();
        });

        fieldColumnPositionCategory.textProperty().addListener((observable, oldValue, newValue) -> {
            setColorsCategory();
        });

        fieldRowPositionCategory.textProperty().addListener((observable, oldValue, newValue) -> {
            setColorsCategory();
        });

    }

    private void setColorsLocation() {
        if (fieldColumnPositionLocation.getText().isEmpty()) {
            labelColumnLocation.setTextFill(Color.RED);
        } else {
            labelColumnLocation.setTextFill(Color.BLACK);
        }
        if (fieldRowPositionLocation.getText().isEmpty()) {
            labelRowLocation.setTextFill(Color.RED);
        } else {
            labelRowLocation.setTextFill(Color.BLACK);
        }
    }

    private void setColorsCategory() {
        if (fieldColumnPositionCategory.getText().isEmpty()) {
            labelColumnCategory.setTextFill(Color.RED);
        } else {
            labelColumnCategory.setTextFill(Color.BLACK);
        }
        if (fieldRowPositionCategory.getText().isEmpty()) {
            labelRowCategory.setTextFill(Color.RED);
        } else {
            labelRowCategory.setTextFill(Color.BLACK);
        }
    }

    private void okClick() {
        if (checkBoxCategory.isSelected()) {
            this.mainController.setExcelCategoryInstantiation((this.fieldColumnPositionCategory.getText() + Separator + this.fieldRowPositionCategory.getText()));
        }
        if (checkboxLocation.isSelected()) {
            this.mainController.setExcelLocationInstantiation((this.fieldColumnPositionLocation.getText() + Separator + this.fieldRowPositionLocation.getText()));
        }

        int numFieldRange = 0;
        boolean isInteger = true;

        try {
            numFieldRange = Integer.valueOf(this.fieldRange.getText());
        } catch (NumberFormatException e) {
            isInteger = false;
            CommonFunctions.displayAlert(AlertType.ERROR, "Invalid Input", "Number of Lines inputed is not an Integer", "Number of Lines inputed is not an Integer. Please input a valid integer.");
        } catch (Exception ex) {
            CommonFunctions.debugLog.error("Invlaid Num Field Range in Excel Validation", ex);
        }
        if (isInteger) {
            this.mainController.setRange(Integer.parseInt(this.fieldRange.getText())); //sets range
            this.mainController.setOnAction();
        }

    }

}
