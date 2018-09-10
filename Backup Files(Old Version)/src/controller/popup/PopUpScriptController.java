/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.Script;
import controller.tabtestcase.TabTestCaseNewController;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Martinez Thibault
 */
public class PopUpScriptController implements Initializable {

    @FXML
    private AnchorPane anchorPaneScriptPopUP;
    @FXML
    private TableView<Script> tableViewScript;
    @FXML
    private TableColumn<Script, String> userIDScript;
    @FXML
    private TableColumn<Script, String> scriptName;
    @FXML
    private TableColumn<Script, String> pathScript;
    @FXML
    private TableColumn<Script, String> descriptionScript;
    @FXML
    private TableColumn<Script, String> parameterListScript;
    @FXML
    private TableColumn<Script, String> writerScript;
    @FXML
    private TableColumn<Script, Integer> versionScript;
    @FXML
    private TableColumn<Script, Date> creationDateScript;
    @FXML
    private TableColumn<Script, Date> editionDateScript;
    @FXML
    private Button buttonValid;

    /**
     *
     */
    public static TabTestCaseNewController controllerNewCase;

    /**
     *
     */
    public boolean isStimuli;

    private Script scriptSelected;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableViewScript.setTableMenuButtonVisible(true);

        userIDScript.setCellValueFactory(new PropertyValueFactory<>("userId"));
        scriptName.setCellValueFactory(new PropertyValueFactory<>("name"));
        pathScript.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionScript.setCellValueFactory(new PropertyValueFactory<>("desciption"));
        parameterListScript.setCellValueFactory(new PropertyValueFactory<>("parameterList"));
        writerScript.setCellValueFactory(new PropertyValueFactory<>("writer"));
        versionScript.setCellValueFactory(new PropertyValueFactory<>("version"));
        creationDateScript.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        editionDateScript.setCellValueFactory(new PropertyValueFactory<>("editionDate"));

        tableViewScript.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Script> observable, Script oldValue, Script newValue) -> {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            this.scriptSelected = newValue;
        });

        buttonValid.setOnAction((ActionEvent event) -> {
            ///controllerNewCase.setAction(this.scriptSelected,isStimuli);
            controllerNewCase.closePopUp();
        });

    }

    /**
     *
     * @param scriptInDB
     */
    public void setTable(ObservableList<Script> scriptInDB) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        tableViewScript.setItems(scriptInDB);
    }

    /**
     *
     * @param Stimuli
     */
    public void setIsStimuli(boolean Stimuli) {
        this.isStimuli = Stimuli;
    }

    /**
     *
     * @param controllerNewCase
     */
    public void init(TabTestCaseNewController controllerNewCase) {
        PopUpScriptController.controllerNewCase = controllerNewCase;
    }

    /**
     *
     * @param newValue
     */
    public void displayCurrentScript(Script newValue) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

}
