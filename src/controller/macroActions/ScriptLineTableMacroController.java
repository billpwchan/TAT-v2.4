/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macroActions;

import DB.Macro;
import DB.Script;
import controller.util.CommonFunctions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.setCursorOnComponent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class ScriptLineTableMacroController implements Initializable {

    private final static Image delete = new Image("assets/images/trash.png");
    private final static Image imd = new Image("assets/images/imageDown.png");
    private final static Image imu = new Image("assets/images/imageUp.png");
    private final static int constanteSize = 25;
    AnchorPane anchorAction = new AnchorPane();
    @FXML
    private AnchorPane anchroPaneScript;
    @FXML
    private GridPane gridPaneScript;
    @FXML
    private Label labelID;
    @FXML
    private ImageView imageViewTrash;
    @FXML
    private ImageView imageUp;
    @FXML
    private ImageView imageDown;
    @FXML
    private Label labelAction;
    private TableActionCreationController controllerViewGlobal;
    private int personalID;
    private ViewScriptMacroController scriptControllerAction;

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
     * Initialize the reference of the object tableStepScript in the object
     * controllerViewGlobal.
     *
     * @param controllerGLobal
     */
    void initControllerTable(TableActionCreationController controllerGLobal) {
        controllerViewGlobal = controllerGLobal;
    }

    /**
     * @return
     */
    public TableActionCreationController controllerViewGlobal() {
        return this.controllerViewGlobal;
    }

    /**
     * @return
     */
    public ViewScriptMacroController getScriptControllerAction() {
        return this.scriptControllerAction;
    }

    /**
     * Put the ID label of the Action based on the ranked number in the
     * controller list.
     *
     * @param scriptID
     */
    public void constructInformation(int scriptID) {
        this.labelID.setText(String.valueOf(scriptID));
        this.personalID = scriptID;
    }

    /**
     * return the reference of this anchorpane
     *
     * @return anchorpane
     */
    public AnchorPane getAnchorPane() {
        return this.anchroPaneScript;
    }

    /**
     * return the id of this step (i.e the position of this script into the
     * collection of scripts).
     *
     * @return personalID
     */
    public int getIDScript() {
        return this.personalID;
    }

    /**
     * Set the ID of this script with the id given in parameters.
     *
     * @param id id of this test step.
     */
    public void setID(int id) {
        constructInformation(id);
    }

    /**
     * @param actions
     * @param operationType
     */
    public void setScriptCreation(HashSet<Script> actions, String operationType) {
        HashSet<Script> thisActions = new HashSet<>(actions);
        this.loadViewAction();
        this.imageDown.setImage(imd);
        this.imageUp.setImage(imu);
        this.imageViewTrash.setImage(delete);

        imageUp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.goUp(ScriptLineTableMacroController.this);
            event.consume();
        });

        imageDown.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.goDown(ScriptLineTableMacroController.this);
            event.consume();
        });

        this.imageViewTrash.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.deleteSelectedAction(ScriptLineTableMacroController.this);
            event.consume();
        });
        System.out.println("SCRIPT = " + actions.size());
        switch (operationType) {
            case "New":
                scriptControllerAction.loadScripts(thisActions);        //Load all available scripts in the checkbox.
                break;
            case "Edit":
                scriptControllerAction.loadScriptsEdit(thisActions);
                break;
            default:
                CommonFunctions.displayAlert(Alert.AlertType.ERROR, "Unsupported Action", "This function is not yet supported.", "Plase choose another operation.");
                break;
        }
        defineCursor();
    }

    private void loadViewAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/assets/view/macroActions/viewScript.fxml"));
        this.scriptControllerAction = new ViewScriptMacroController();
        fxmlLoader.setController(scriptControllerAction);
        try {
            anchorAction = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(ScriptLineTableMacroController.class.getName()).error("", ex);
        }
        this.gridPaneScript.add(anchorAction, 2, 0, 1, 1);
        scriptControllerAction.init(this);
    }

    /**
     *
     */
    public void updateSizeGrid() {

        int NbrParamAction = 0;
        int nbrParamVerif = 0;
        if (scriptControllerAction != null) {
            NbrParamAction = scriptControllerAction.getNumberOfParam();
        }
        if (NbrParamAction * constanteSize >= 50 || nbrParamVerif * constanteSize >= 50) {
            if (NbrParamAction >= nbrParamVerif) {
                this.anchroPaneScript.setMinHeight(30 + NbrParamAction * constanteSize);
                this.gridPaneScript.setMinHeight(30 + NbrParamAction * constanteSize);
                this.anchroPaneScript.setMaxHeight(30 + NbrParamAction * constanteSize);
                this.gridPaneScript.setMaxHeight(30 + NbrParamAction * constanteSize);
            } else if ((NbrParamAction < nbrParamVerif)) {
                this.anchroPaneScript.setMinHeight(30 + nbrParamVerif * constanteSize);
                this.gridPaneScript.setMinHeight(30 + nbrParamVerif * constanteSize);
                this.anchroPaneScript.setMaxHeight(30 + NbrParamAction * constanteSize);
                this.gridPaneScript.setMaxHeight(30 + NbrParamAction * constanteSize);
            }

        }
        GridPane.setHalignment(labelAction, HPos.CENTER);
        GridPane.setValignment(labelAction, VPos.CENTER);

    }

    /**
     * Display a different cursor for each node given in the arraylist.
     */
    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.imageDown);
        nodeHand.add(this.imageUp);
        nodeHand.add(this.imageViewTrash);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    void setScriptandParamAction(Macro macro) {
        this.loadViewAction();
        scriptControllerAction.updateScriptViewDisplay(macro);
    }

    void setScriptandParamActionEdit(Macro macro) {
        this.loadViewAction();
        scriptControllerAction.updateScriptEditDisplay(macro);
    }
}
