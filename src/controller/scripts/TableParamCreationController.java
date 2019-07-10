/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.scripts;

import DB.Parameters;
import DB.Script;
import DB.ScriptHasParameters;
import DBcontroller.ParametersDB;
import controller.scriptParameters.ScriptLineParameterController;
import controller.tablestep.TableStepScriptCreationController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class TableParamCreationController implements Initializable {

    /**
     *
     */
    public final ObservableList<ScriptLineParameterController> collectionControllerParam = FXCollections.observableArrayList();
    private final ObservableList<Node> workingCollection = FXCollections.observableArrayList();
    @FXML
    private AnchorPane mainAnchorPan;
    @FXML
    private ScrollPane scrollPan;
    @FXML
    private VBox vBox;
    private ScriptLineParameterController controllerParamLine;
    private ScriptLineParameterController selectedParamController;
    private int scriptID = 1;
    private ArrayList<Parameters> params = new ArrayList<>();

    private TabScriptNewController controllerNewScript;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.scrollPan.widthProperty().addListener((ObservableValue<? extends Number> arg0, Number arg1, Number arg2) -> {
            double newValue = arg2.doubleValue() - 15;
            vBox.setPrefWidth(newValue);
            updateAllChildren(newValue);
        });
        //this.addParaminVbox();

    }

    /**
     *
     */
    public void addParam() {
        addParaminVbox();
        controllerParamLine.setParamsCreation(params);
    }

    /**
     * @return
     */
    public TabScriptNewController getControllerFather() {
        return this.controllerNewScript;
    }

    /**
     * @return
     */
    public ObservableList<ScriptLineParameterController> getCollectionControllerParam() {
        return this.collectionControllerParam;
    }

    /*
     Bind each script and step AnchorPane to the width of the scrollPane.
     */
    private void updateAllChildren(double value) {
        for (int i = 0; i < vBox.getChildren().size(); i++) {
            ((AnchorPane) vBox.getChildren().get(i)).setPrefWidth(value);
        }
    }

    /**
     *
     */
    public void addParaminVbox() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane ParamPane;
            //this..getStylesheets().add("/assets.view/testcampaign/cssViewCampaign.css");
            try (InputStream streamViewScript = getClass().getResource("/assets/view/scriptmanagement/scriptLineParameter.fxml").openStream()) {
                ParamPane = fxmlLoader.load(streamViewScript);
                controllerParamLine = fxmlLoader.getController();
            }
            controllerParamLine.initControllerTable(this);
            controllerParamLine.constructInformation(scriptID);

            if (this.selectedParamController == null) {
                collectionControllerParam.add(controllerParamLine);
                workingCollection.add(ParamPane);

            } else {
                int idVbox = workingCollection.indexOf(this.selectedParamController.getAnchorPane());
                workingCollection.add(idVbox, ParamPane);
                int indexController = this.selectedParamController.getIDParam() - 1;
                indexController += 1;
                collectionControllerParam.add(indexController, controllerParamLine);
                updateScriptId(indexController);
            }
            ParamPane.setPrefWidth(vBox.getPrefWidth());
        } catch (IOException ex) {
            Logger.getLogger(TableStepScriptCreationController.class.getName()).error("", ex);
        }
        scriptID++;
        displayVbox();
    }

    /**
     * Update the id a each test step starting from the Id given in parameters
     *
     * @param startingID position of the test steps to start to modify the id
     *                   from.
     */
    private void updateScriptId(int startingID) {
        for (int i = startingID; i < collectionControllerParam.size(); i++) {
            collectionControllerParam.get(i).setID(i + 1);
        }
    }

    /**
     * Display the Vbox.
     */
    private void displayVbox() {
        vBox.getChildren().setAll(workingCollection);
    }

    private void loadParams() {
        //System.out.println("Je get tt les script");
        ParametersDB paramDBHandler = new ParametersDB();
        params = paramDBHandler.getAllParams();
        Parameters addParam = new Parameters();
        addParam.setName("Add new parameter");
        params.add(addParam);
        //System.out.println("J ai " + scripts.size() + " script de stimuli");
    }

    /**
     * Init the reference of the object controllerNew with the reference of the
     * object aThis.
     *
     * @param aThis
     */
    public void initScriptNew(TabScriptNewController aThis) {
        controllerNewScript = aThis;
        this.loadParams();
        this.addParaminVbox();
        controllerParamLine.setParamsCreation(this.params);
        this.controllerParamLine.setScriptPurpose();

    }

    /**
     * Swap the script given in reference with the one below, in the collection
     * of script.
     *
     * @param aThis
     */
    public void goDown(ScriptLineParameterController aThis) {
        int indexControllerScriptInCollection = collectionControllerParam.indexOf(aThis);
        if (indexControllerScriptInCollection < collectionControllerParam.size() - 1) {
            Collections.swap(collectionControllerParam, indexControllerScriptInCollection, indexControllerScriptInCollection + 1);
            this.swapParam(false, aThis);
        }
        updateScriptId(0);
    }

    /**
     * * Swap the script given in reference with the one above, in the
     * collection of script.
     *
     * @param aThis
     */
    public void goUp(ScriptLineParameterController aThis) {
        int indexControllerScriptInCOllection = collectionControllerParam.indexOf(aThis);
        if (indexControllerScriptInCOllection > 1) {
            Collections.swap(collectionControllerParam, indexControllerScriptInCOllection, indexControllerScriptInCOllection - 1);
            this.swapParam(true, aThis);
        }
        updateScriptId(0);
    }

    /**
     * Delete the Action given in parameters
     *
     * @param aThis step to delete
     * @see deleteSelectedStep();
     */
    public void deleteSelectedAction(ScriptLineParameterController aThis) {
        if (aThis != null) {
            workingCollection.remove(aThis.getAnchorPane());
            this.collectionControllerParam.remove(aThis);
            scriptID--;
            updateScriptId(0);
            displayVbox();
        }
        //this.controllerScriptLine.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(this);
    }

    /**
     * Swap the script given in parameters in the vBox, depending on the value
     * of the boolean b
     *
     * @param b     way to swap, if true go up, else go down.
     * @param aThis script to swap in the vbox.
     */
    void swapParam(boolean b, ScriptLineParameterController aThis) {
        int indexScriptInVbox = workingCollection.indexOf(aThis.getAnchorPane());
        if (b) {
            Collections.swap(workingCollection, indexScriptInVbox, indexScriptInVbox - 1);
        } else {
            Collections.swap(workingCollection, indexScriptInVbox, indexScriptInVbox + 1);
        }
        vBox.getChildren().setAll(workingCollection);
    }

    /**
     * @param script
     */
    public void displayParams(Script script) {
        clearTable();
        Iterator<ScriptHasParameters> itParams = script.getScriptHasParameterses().iterator();
        while (itParams.hasNext()) {
            Parameters param = itParams.next().getParameters();
            this.addParaminVbox();
            this.controllerParamLine.setParam(param);
        }
        displayVbox();
    }

    /**
     * Clear the model of the table, but does not display it.
     */
    public void clearTable() {
        this.workingCollection.clear();
        this.collectionControllerParam.clear();
        this.scriptID = 1;
    }

    /**
     * @param params
     */
    public void PrefilParams(ArrayList<Parameters> params) {
        clearTable();
        this.addParaminVbox();
        controllerParamLine.setParamsCreation(this.params);
        this.controllerParamLine.setScriptPurpose();
        for (Parameters param : params) {
            this.addParaminVbox();
            controllerParamLine.setParamsCreation(this.params);
            this.controllerParamLine.setPreFilledParams(param);
        }
        displayVbox();
    }
}
