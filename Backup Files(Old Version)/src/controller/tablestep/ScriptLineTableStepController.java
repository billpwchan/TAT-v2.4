/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tablestep;

import DB.Script;
import DB.ScriptExecutions;
import DB.TestStep;
import DB.TestStepHasScript;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.setCursorOnComponent;

/**
 * FXML Controller class
 *
 * @author Martinez Thibault
 */
public class ScriptLineTableStepController implements Initializable {

    @FXML
    private AnchorPane anchroPaneScript;
    @FXML
    private GridPane gridPaneScript;
    @FXML
    private Label labelAction;
    @FXML
    private Label labelVerif;
    @FXML
    private Label labelCommentStep;
    @FXML
    private ImageView imageUp;
    @FXML
    private ImageView imageDown;
    @FXML
    private AnchorPane paneVerif;
    @FXML
    private AnchorPane paneAction;
    @FXML
    private Label labelID;
    @FXML
    private ImageView paramAction;
    @FXML
    private ImageView paramVerif;
    @FXML
    private ListView listRequirements;
    @FXML
    private ImageView imageViewTrash;

    private StepLineTableStepController controllerStepParent;

    private ViewScriptController scriptControllerAction;

    private ViewScriptController scriptControllerVerif;

    private int idStepFather;

    private final static int constanteSize = 25;

    private final Image delete = new Image("images/trash.png");

    private final static Image imd = new Image("images/imageDown.png");

    private final static Image imu = new Image("images/imageUp.png");

    FXMLLoader fxmlLoader = new FXMLLoader();// FXMLLoader.load(getClass().getResource("/view/stepcreation/viewScript.fxml"));//InputStream anchorVerifStream = getClass().getResource("/view/stepcreation/viewScript.fxml").openStream();

    FXMLLoader fxmlLoader2 = new FXMLLoader();

    AnchorPane anchorAction = new AnchorPane();

    AnchorPane anchorVerif = new AnchorPane();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     *
     */
    public void setScriptView() {

    }

    /**
     *
     * @param scriptStimuli
     * @param scriptCheck
     */
    public void setScriptCreation(HashSet<Script> scriptStimuli, HashSet<Script> scriptCheck) {

        this.loadViewCheck();
        this.loadViewStimuli();
        this.imageDown.setImage(imd);
        this.imageUp.setImage(imu);
        this.imageViewTrash.setImage(delete);

        imageUp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerStepParent.goUp(ScriptLineTableStepController.this);
            event.consume();
        });

        imageDown.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerStepParent.goDown(ScriptLineTableStepController.this);
            event.consume();
        });

        this.imageViewTrash.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerStepParent.deleteScript(ScriptLineTableStepController.this);
            event.consume();
        });

        TestStep currTestStep = this.controllerStepParent.getTestStep();
        scriptControllerAction.loadScripts(scriptStimuli, currTestStep);
        scriptControllerVerif.loadScripts(scriptCheck, currTestStep);
        defineCursor();
    }

    /**
     * Initialize the reference of the class step father into the object
     * controllerStepParents
     *
     * @param mainController
     */
    public void init(StepLineTableStepController mainController) {
        controllerStepParent = mainController;
    }

    /**
     * Return the reference of the anchore pane. (Needed to know the id of this
     * pane into the VBox).
     *
     * @return
     */
    public AnchorPane getPane() {
        return this.anchroPaneScript;
    }

    /**
     * set the ID of the step father at the creation of this script.
     *
     * @param idStepFather
     * @param personalID
     */
    public void setIDs(int idStepFather, int personalID) {
        this.idStepFather = idStepFather;
        this.labelID.setText(String.valueOf(this.idStepFather + "." + personalID));
    }

    /**
     * Return the script loaded into the panel action
     *
     * @return
     */
    public TestStepHasScript getScriptAction() {
        TestStepHasScript temp;
        try {
            temp = this.scriptControllerAction.getTestStepHasScript();
            System.out.println("TEMP " + temp);
        } catch (NullPointerException ex) {
            Logger.getLogger(ScriptLineTableStepController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return temp;
    }

    /**
     *
     * @return
     */
    public ViewScriptController getScriptActionController() {
        return this.scriptControllerAction;
    }

    /**
     *
     * @return
     */
    public ViewScriptController getScriptVerifController() {
        return this.scriptControllerVerif;
    }

    /**
     * Return the script loaded into the panel verif.
     *
     * @return
     */
    public TestStepHasScript getScriptVerif() {
        TestStepHasScript temp;
        try {
            temp = this.scriptControllerVerif.getTestStepHasScript();
            System.out.println("TEMP " + temp);
        } catch (NullPointerException ex) {
            Logger.getLogger(ScriptLineTableStepController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return temp;
    }

    /**
     * Hide/Unhide option available during the baseline.
     */
    void executionInstanceBaseline() {
        this.imageViewTrash.setVisible(false);
        this.scriptControllerAction.executionInstance();
        this.scriptControllerVerif.executionInstance();
        this.imageDown.setVisible(false);
        this.imageUp.setVisible(false);
    }

    /**
     * Hide/Unhide option available during the baseline.
     */
    void executionInstancePopUp() {
        executionInstanceBaseline();
        this.paramAction.setVisible(false);
        this.paramVerif.setVisible(false);
    }

    /**
     * Set the background of the pane given in parameters depending on the
     * label.
     *
     * @param okAction pane to set the background of.
     * @param newValue label to test.
     */
    private void changeColorCircle(Pane okAction, String newValue) {
        switch (newValue) {
            case "OK":
                okAction.setStyle("-fx-background-color: #00FF00;");
                break;
            case "OKWC":
                okAction.setStyle(" -fx-background-color: #FFC000;");
                break;
            case "Not testable":
                okAction.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%);");
                break;
            case "NOK":
                okAction.setStyle("-fx-background-color: #FF0000;");
                labelVerif.setStyle("-fx-text-fill: white");
                break;
            case "OS":
                okAction.setStyle("-fx-background-color: #8DB4E2;");
                break;
            case "Incomplete":
                okAction.setStyle("-fx-background-color: #D9D9D9;");
                break;
            default:
                okAction.setStyle("-fx-background-color: #D9D9D9;");
                break;
        }
    }

    /**
     * Update the image of the action view with the one given in parameter.
     *
     * @param image
     */
    public void updateImageConfigurationAction(Image image) {
        this.paramAction.setVisible(true);
        this.paramAction.setImage(image);
    }

    /**
     * Update the image of the verif view with the one given in parameter.
     *
     * @param image
     */
    public void updateImageConfigurationVerif(Image image) {
        this.paramVerif.setVisible(true);
        this.paramVerif.setImage(image);
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
        if (scriptControllerVerif != null) {
            nbrParamVerif = scriptControllerVerif.getNumberOfParam();
        }
        if (NbrParamAction * constanteSize >= 25 || nbrParamVerif * constanteSize >= 25) {
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
        GridPane.setHalignment(labelVerif, HPos.CENTER);
        GridPane.setValignment(labelVerif, VPos.CENTER);

        GridPane.setHalignment(labelAction, HPos.CENTER);
        GridPane.setValignment(labelAction, VPos.CENTER);

    }

//    /**
//     * set the parameters given for the baseline for the script action.
//     *
//     * @param paramScript
//     */
//    void setParametersBaselineAction(ArrayList<paramsForScript> paramScript) {
//        scriptControllerAction.constructGridBaseline(paramScript);
//    }
//
//    /**
//     * set the parameters given for the baseline for the script verif.
//     *
//     * @param paramScript
//     */
//    void setParametersBaselineVerif(ArrayList<paramsForScript> paramScript) {
//        scriptControllerVerif.constructGridBaseline(paramScript);
//    }
//
//    /**
//     * return the arraylist paramforscript with the configured parameters for
//     * the script action.
//     *
//     * @return
//     */
//    public ObservableList<paramsForScript> getParamsAction() {
//        return this.scriptControllerAction.getParams();
//    }
//
//    /**
//     * return the arraylist paramforscript with the configured parameters for
//     * the script verif.
//     *
//     * @return
//     */
//    public ObservableList<paramsForScript> getParamsVerif() {
//        return this.scriptControllerVerif.getParams();
//    }
    /**
     * Check if the script of check and script of stimuli are configured.
     *
     * @return
     */
    public boolean isFullyConfigured() {
        boolean conf = false;
        if (this.scriptControllerAction == null) {
            conf = this.scriptControllerVerif.getConfigured();
        } else if (this.scriptControllerVerif == null) {
            conf = this.scriptControllerAction.getConfigured();
        } else {
            conf = this.scriptControllerVerif.getConfigured() && this.scriptControllerAction.getConfigured();
        }
        return conf;
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

    /**
     * Attached a listener for the label action linked to a listener on the
     * script stimuli result
     *
     * @param scriptexe
     */
    void setLabelAction(ScriptExecutions scriptexe) {
        this.labelCommentStep.setVisible(true);
        this.labelCommentStep.setText(scriptexe.getScriptExecutionComment());
        this.labelAction.setVisible(true);
        labelAction.setText(scriptexe.getScriptExecutionResult());
        changeColorCircle(paneAction, scriptexe.getScriptExecutionResult());
//        scriptexe.resultProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                Platform.runLater(() -> {
//                    labelAction.setText((String) newValue);
//                    changeColorCircle(paneAction, (String) newValue);
//                }
//                );
//
//            }
//        });

    }

    /**
     * Attached a listener for the label action linked to a listener on the
     * script stimuli verif
     *
     * @param scriptexe
     */
    void setLabelVerif(ScriptExecutions scriptexe) {
        this.labelCommentStep.setVisible(true);
        this.labelCommentStep.setText(scriptexe.getScriptExecutionComment());
        this.labelVerif.setText(scriptexe.getScriptExecutionResult());
        //System.out.println("Le resultat du script est : " + scriptexe.getScriptExecutionResult());
        this.labelVerif.setVisible(true);
        changeColorCircle(paneVerif, scriptexe.getScriptExecutionResult());
//        scriptexe.resultProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                Platform.runLater(() -> {
//                    //System.out.println("My new value is :" + (String) newValue);
//                    labelVerif.setText((String) newValue);
//                    changeColorCircle(paneVerif, (String) newValue);
//                }
//                );
//
//            }
//        });
    }

    /**
     * Make the configuration impossible or script action and verif.
     */
    public void disableConfig() {

        if (this.scriptControllerAction == null) {

            this.scriptControllerVerif.disableParamConfiguration();
        } else if (this.scriptControllerVerif == null) {
            this.scriptControllerAction.disableParamConfiguration();
        } else {
            this.scriptControllerAction.disableParamConfiguration();
            this.scriptControllerVerif.disableParamConfiguration();
        }

    }

    void setScriptandParamAction(TestStepHasScript scHasParameters) {
        this.loadViewStimuli();
        scriptControllerAction.updateScriptViewDisplay(scHasParameters);
    }

    void setScriptandParamVerif(TestStepHasScript scHasParameters) {

        this.loadViewCheck();
        //System.out.println("sc= "+scHasParameters.getScriptHasBeenConfigureds().size());
        scriptControllerVerif.updateScriptViewDisplay(scHasParameters);
    }

    private void loadViewStimuli() {
        try {
            try (InputStream anchorActionStream = getClass().getResource("/view/stepcreation/viewScript.fxml").openStream()) {
                anchorAction = fxmlLoader.load(anchorActionStream);
                this.scriptControllerAction = (ViewScriptController) fxmlLoader.getController();
                this.gridPaneScript.add(anchorAction, 2, 0, 1, 1);
            }
            scriptControllerAction.init(this);
        } catch (IOException ex) {
            Logger.getLogger(ScriptLineTableStepController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

    }

    private void loadViewCheck() {
        try {

            try (InputStream anchorVerifStream = getClass().getResource("/view/stepcreation/viewScript.fxml").openStream()) {
                anchorVerif = fxmlLoader2.load(anchorVerifStream);
                this.scriptControllerVerif = (ViewScriptController) fxmlLoader2.getController();
                this.gridPaneScript.add(anchorVerif, 4, 0, 1, 1);
            }
            scriptControllerVerif.init(this);
        } catch (IOException ex) {
            Logger.getLogger(ScriptLineTableStepController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

    void setScriptandParamActionEdit(TestStepHasScript currentTSHS) {
        scriptControllerAction.updateGridPaneEdit(currentTSHS);
    }

    void setScriptandParamVerifEdit(TestStepHasScript currentTSHS) {
        scriptControllerVerif.updateGridPaneEdit(currentTSHS);
    }

    void setScriptandParamActionBaseline(TestStepHasScript currentTSHS) {
        this.loadViewStimuli();
        scriptControllerAction.updateGridPaneBaseline(currentTSHS);
    }

    void setScriptandParamVerifBaseline(TestStepHasScript currentTSHS) {
        this.loadViewCheck();
        scriptControllerVerif.updateGridPaneBaseline(currentTSHS);
    }

    void setScriptandParamActionExecuction(ScriptExecutions currentTSHS) {
        this.loadViewStimuli();
        this.setLabelAction(currentTSHS);
        this.scriptControllerAction.updateGridPaneExecution(currentTSHS);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setScriptandParamVerifExecuction(ScriptExecutions currentTSHS) {
        this.loadViewCheck();
        this.setLabelVerif(currentTSHS);
        this.scriptControllerVerif.updateGridPaneExecution(currentTSHS);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    public StepLineTableStepController getControllerStepParent() {
        return this.controllerStepParent;
    }

    /**
     *
     * @return
     */
    public ViewScriptController getScriptView() {
        return this.scriptControllerAction;
    }

}
