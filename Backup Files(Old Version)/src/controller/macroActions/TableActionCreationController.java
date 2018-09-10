/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macroActions;

import DB.Macro;
import DB.ParamScriptMacro;
import DB.Script;
import DBcontroller.ScriptDB;
import DBcontroller.sessionFactorySingleton;
import controller.macro.TabMacroEditController;
import controller.macro.TabMacroNewController;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class TableActionCreationController implements Initializable {

    @FXML
    private AnchorPane mainAnchorPan;
    @FXML
    private ScrollPane scrollPan;
    @FXML
    private VBox vBox;

    private ScriptLineTableMacroController controllerScriptLine;

    private ScriptLineTableMacroController selectedScriptController;

    /**
     *
     */
    public final ObservableList<ScriptLineTableMacroController> collectionControllerScript = FXCollections.observableArrayList();

    private int scriptID = 1;

    private final ObservableList<Node> workingCollection = FXCollections.observableArrayList();

    private final HashSet<Script> scripts = new HashSet<>(0);

    private HashSet<Script> allScript = new HashSet<>(0);

    private TabMacroNewController controllerNewMacro;

    private TabMacroEditController controllerEditMacro;


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
    }

    /**
     *
     */
    public void addAction() {
        addActioninVbox();
        controllerScriptLine.setScriptCreation(scripts, "New");
    }

    /**
     *
     */
    public void addActionEdit() {
        addActioninVbox();
        controllerScriptLine.setScriptCreation(scripts, "Edit");
    }

    /**
     *
     * @return
     */
    public TabMacroNewController getControllerFather() {
        return this.controllerNewMacro;
    }

    /**
     *
     * @return
     */
    public TabMacroEditController getControllerFatherEdit() {
        return this.controllerEditMacro;
    }

    /**
     *
     * @return
     */
    public ObservableList<ScriptLineTableMacroController> getCollectionControllerScript() {
        return this.collectionControllerScript;
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
    public void addActioninVbox() {
        //Add an additional script to the vBox
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane scriptPane;
            try (InputStream streamViewScript = getClass().getResource("/view/macroActions/scriptLineTableMacro.fxml").openStream()) {
                scriptPane = fxmlLoader.load(streamViewScript);
                controllerScriptLine = (ScriptLineTableMacroController) fxmlLoader.getController();
            }
            controllerScriptLine.initControllerTable(this);
            controllerScriptLine.constructInformation(scriptID);

            if (this.selectedScriptController == null) {
                collectionControllerScript.add(controllerScriptLine);
                workingCollection.add(scriptPane);
            } else {
                int idVbox = workingCollection.indexOf(this.selectedScriptController.getAnchorPane());
                workingCollection.add(idVbox, scriptPane);
                int indexController = this.selectedScriptController.getIDScript() - 1;
                indexController += 1;
                collectionControllerScript.add(indexController, controllerScriptLine);
                updateScriptId(indexController);
            }
            scriptPane.setPrefWidth(vBox.getPrefWidth());
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
     * from.
     */
    private void updateScriptId(int startingID) {
        for (int i = startingID; i < collectionControllerScript.size(); i++) {
            collectionControllerScript.get(i).setID(i + 1);
        }
    }

    /**
     * Display the Vbox.
     */
    private void displayVbox() {
        vBox.getChildren().setAll(workingCollection);
    }

    //Load all existing scripts (Pre-defined) to a hashset
    private void loadScriptCheckAndStimuli() {
        ScriptDB scriptDBHandler = new ScriptDB();
        allScript = scriptDBHandler.getScriptListWithParameters();

        Iterator<Script> itScript = allScript.iterator();
        while (itScript.hasNext()) {
            Script script = itScript.next();
            if (script.getIsMacro() == 0) {
                scripts.add(script);
            }
        }
    }

    /**
     * Init the reference of the object controllerNew with the reference of the
     * object aThis.
     *
     * @param aThis
     */
    public void initMacroNew(TabMacroNewController aThis) {
        controllerNewMacro = aThis;
        this.loadScriptCheckAndStimuli();
    }

    /**
     *
     * @param aThis
     */
    public void initMacroEdit(TabMacroEditController aThis) {
        controllerEditMacro = aThis;
        this.loadScriptCheckAndStimuli();
    }

    /**
     * Swap the script given in reference with the one below, in the collection
     * of script.
     *
     * @param aThis
     */
    public void goDown(ScriptLineTableMacroController aThis) {
        int indexControllerScriptInCollection = collectionControllerScript.indexOf(aThis);
        if (indexControllerScriptInCollection < collectionControllerScript.size() - 1) {
            Collections.swap(collectionControllerScript, indexControllerScriptInCollection, indexControllerScriptInCollection + 1);
            this.swapScript(false, aThis);
        }
        updateScriptId(0);
    }

    /**
     * * Swap the script given in reference with the one above, in the
     * collection of script.
     *
     * @param aThis
     */
    public void goUp(ScriptLineTableMacroController aThis) {
        int indexControllerScriptInCOllection = collectionControllerScript.indexOf(aThis);
        if (indexControllerScriptInCOllection > 0) {
            Collections.swap(collectionControllerScript, indexControllerScriptInCOllection, indexControllerScriptInCOllection - 1);
            this.swapScript(true, aThis);
        }
        updateScriptId(0);
    }

    /**
     * Delete the Action given in parameters
     *
     * @param aThis step to delete
     * @see deleteSelectedStep();
     */
    public void deleteSelectedAction(ScriptLineTableMacroController aThis) {
        if (aThis != null) {
            workingCollection.remove(aThis.getAnchorPane());
            this.collectionControllerScript.remove(aThis);
            ObservableList<ParamScriptMacro> temp = aThis.getScriptControllerAction().getHashParamScriptMacro();
            SessionFactory factory = sessionFactorySingleton.getInstance();
            Session session = factory.openSession();
            //Assume macroID are inversed presented in here.
            temp.stream().forEach((ParamScriptMacro PSM) -> {
                session.delete(PSM);
            });
            session.beginTransaction().commit();
            session.close();
            scriptID--;
            updateScriptId(0);
            displayVbox();
        }

        //UI Update
        if (this.controllerScriptLine.controllerViewGlobal().getControllerFather() != null) {
            this.controllerScriptLine.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(this);
        } else {
            this.controllerScriptLine.controllerViewGlobal().getControllerFatherEdit().getControllerPreview().updateGridPaneCreation(this);
        }
    }

    /**
     * Swap the script given in parameters in the vBox, depending on the value
     * of the boolean b
     *
     * @param b way to swap, if true go up, else go down.
     * @param aThis script to swap in the vbox.
     */
    private void swapScript(boolean b, ScriptLineTableMacroController aThis) {
        int indexScriptInVbox = workingCollection.indexOf(aThis.getAnchorPane());
        if (b) {
            Collections.swap(workingCollection, indexScriptInVbox, indexScriptInVbox - 1);
        } else {
            Collections.swap(workingCollection, indexScriptInVbox, indexScriptInVbox + 1);
        }
        vBox.getChildren().setAll(workingCollection);
    }

    /**
     * Display each Script in this Macro
     *
     * @param macro
     */
    public void displayScriptAndStepEdit(Script macro) {    //This macro is selected macro by user (Click event)
        clearTable();
        Iterator<Macro> itScriptsMacro = macro.getMacrosForScriptIdScript().iterator();
        while (itScriptsMacro.hasNext()) {
            Macro macroScript = itScriptsMacro.next();
            this.addActioninVbox();     //Create a place for this script.
            this.controllerScriptLine.setScriptCreation(scripts, "Edit"); //Need to be after addActionVBox (Controller not defined). Global var "scripts" contain all the existing scripts (Jar only)
            this.controllerScriptLine.setScriptandParamActionEdit(macroScript);
        }
    }

    /**
     *
     * @param macro
     */
    public void displayScriptAndStepView(Script macro) {
        clearTable();
        Iterator<Macro> itScripts = macro.getMacrosForScriptIdScript().iterator();
        while (itScripts.hasNext()) {
            Macro macroScript = itScripts.next();
            this.addActioninVbox();
            this.controllerScriptLine.setScriptandParamAction(macroScript);
        }
        displayVbox();
    }

    /**
     * Clear the model of the table, but does not display it.
     */
    public void clearTable() {
        this.workingCollection.clear();
        this.collectionControllerScript.clear();
        this.scriptID = 1;
    }
}
