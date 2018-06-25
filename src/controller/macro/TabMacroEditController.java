/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import DB.Script;
import DBcontroller.MacroDB;
import DBcontroller.sessionFactorySingleton;
import controller.macroActions.PreviewMacro;
import controller.macroActions.ScriptLineTableMacroController;
import controller.macroActions.TableActionCreationController;
import controller.util.CommonFunctions;
import java.net.URL;
import java.text.DateFormat;
import java.util.ResourceBundle;

import controller.tabtestcase.TabTestCaseEditController;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabMacroEditController implements Initializable {

    @FXML
    private AnchorPane anchorPanelEditMacro;
    @FXML
    private GridPane gridPaneMacroEdit;
    @FXML
    private Button buttonValid;
    @FXML
    private GridPane gridPaneTableAction;
    @FXML
    private Text labelActionTitle;
    @FXML
    private Button buttonAddAction;
    @FXML
    private GridPane gridPaneLabelCaseEdit;
    @FXML
    private Label labelMacroNameEdit;
    @FXML
    private Label labelMacroEditionDate;
    @FXML
    private Label labelMacroCreationDate;
    @FXML
    private Label labelMacroVersion;
    @FXML
    private Label labelMacroObjectives;
    @FXML
    private TextField jtextfieldMacroNameEdit;
    @FXML
    private TextField jtextfieldMacroEdit;
    @FXML
    private TextField jtextfieldMacroCreationDateEdit;
    @FXML
    private TextField jtextfieldTypeMacroEditionDateEdit;
    @FXML
    private TextArea jtextareaObjectivesMacroEdit;
    @FXML
    private ScrollPane scrollPanePreview;
    @FXML
    private Label labelIsStimuli;
    @FXML
    private CheckBox stimuliCheckBoxEdit;
    @FXML
    private AnchorPane anchorHeader;
    @FXML
    private Text labelPreview;

    private Alert alert;

    private static TabMacroMainViewController mainController;

    private TableActionCreationController controllerTableAction;

    private final PreviewMacro controllerPreviewMacro = new PreviewMacro();

    private final boolean canBeValidate = false;

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    private final int textfieldMacroNameMaxLength = 60;

    private MacroDB controllerMacroDB = new MacroDB();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initButtons();
        this.constructTableStep();
        this.loadPreviewMacro();

        jtextfieldMacroNameEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorTextMacroName(newValue.trim().isEmpty());
            activationValidButton();
            if (CommonFunctions.displayWarningIncorrectInputFormat("Macro name", textfieldMacroNameMaxLength, newValue.length() > textfieldMacroNameMaxLength)) {
                jtextfieldMacroNameEdit.setText(oldValue);
            }
        });

        jtextareaObjectivesMacroEdit.textProperty().addListener((obsevable, oldValue, newValue) -> {
            changeColorTextDescription(newValue.trim().isEmpty());
            activationValidButton();
        });

        this.controllerTableAction.collectionControllerScript.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                activationValidButton();
            }
        });
    }

    /**
     *
     * @param mainController
     */
    public void init(TabMacroMainViewController mainController) {
        TabMacroEditController.mainController = mainController;
        controllerTableAction.initMacroEdit(this);
    }

    /**
     *
     */
    public void initButtons() {
        this.buttonValid.setDisable(true);
        this.buttonAddAction.setOnAction((ActionEvent e) -> {
            controllerTableAction.addAction();
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            try {
                this.editMacro();
            } catch (ParseException ex) {
                Logger.getLogger(TabMacroEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    /**
     *
     * @return
     */
    public PreviewMacro getControllerPreview() {
        return this.controllerPreviewMacro;
    }

    private void editMacro() throws ParseException {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ObservableList<ScriptLineTableMacroController> observableScripts = controllerTableAction.getCollectionControllerScript();
        int numberScript = observableScripts.size();
        System.out.println("Size of ObservableScripts Object: " + numberScript);
        Script macro = constructMacro();
        session.save(macro);
        int i = 0;
        boolean missingPurpose = false;
        //This part is responsible for saving new macro object. Correct. 
        while (i < numberScript && missingPurpose == false) {
            System.out.println(observableScripts.get(i).toString());
            System.out.println(observableScripts.get(i).getScriptControllerAction().toString());
            System.out.println(observableScripts.get(i).getScriptControllerAction().getHashParamScriptMacro().get(0).getValue());  //This will give the i script in this macro's parameter 0 value.
            if ("".equals(observableScripts.get(i).getScriptControllerAction().getHashParamScriptMacro().get(0).getValue())) {   //Each script should has purpose. First parameter of each script. If satisfied, save it.
                CommonFunctions.displayAlert(AlertType.ERROR, "Missing Purpose", "There is something wrong with script" + observableScripts.get(i).getScriptControllerAction().getCurrentScript().getName(), "A script purpose is missing in your macro");
                missingPurpose = true;
            } else {
                //Save each script to sesson. Set parameters of each observableScript. 
                observableScripts.get(i).getScriptControllerAction().getScriptMacro().setScriptByScriptIdScript(macro);
                observableScripts.get(i).getScriptControllerAction().getScriptMacro().setScriptOrder((byte) i);
                observableScripts.get(i).getScriptControllerAction().getScriptMacro().setScriptByScriptIdScript1(observableScripts.get(i).getScriptControllerAction().getCurrentScript());
                session.save(observableScripts.get(i).getScriptControllerAction().getScriptMacro());
                i++;
            }
        }
        if (!missingPurpose) {
            session.beginTransaction().commit();
            mainController.updateRepository();
            mainController.closeTab();
            mainController.focusLibrary();
        }
        session.close();
    }

    private Script constructMacro() throws ParseException {
        Script editScript = new Script();
        editScript.setDesciption(jtextareaObjectivesMacroEdit.getText());
        editScript.setName(jtextfieldMacroNameEdit.getText());
        editScript.setScriptVersion(1);
        editScript.setCreationDate(df.format(new Date()));
        editScript.setEditionDate(df.format(new Date()));
        editScript.setIsMacro((byte) (1));
        editScript.setIsStimuli((byte) (stimuliCheckBoxEdit.isSelected() ? 1 : 0));
        return editScript;
    }

    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneTableAction.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/macroActions/tableActionCreation.fxml").openStream()), 0, 1, 1, 5);
        } catch (IOException ex) {
            Logger.getLogger(TabMacroEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerTableAction = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/macroActions/headerTableAction.fxml").openStream());
            this.gridPaneTableAction.add(paneTest, 0, 0, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabMacroEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Init settings for TextAreas & CheckBoxes
     *
     * @param script
     */
    public void displayMacro(Script script) {
        buttonAddAction.setVisible(true);
        buttonValid.setVisible(true);

        jtextareaObjectivesMacroEdit.setText(script.getDesciption());
        jtextfieldMacroEdit.setId("displayStyle");
        jtextfieldMacroNameEdit.setText(script.getName());
        jtextfieldMacroNameEdit.setDisable(false);
        jtextfieldMacroNameEdit.setEditable(true);
        jtextfieldMacroEdit.setId("displayStyle");
        jtextfieldMacroEdit.setText(script.getScriptVersion().toString());
        jtextfieldMacroEdit.setDisable(false);
        jtextfieldMacroEdit.setEditable(true);
        jtextfieldMacroCreationDateEdit.setText(script.getCreationDate());
        jtextfieldMacroCreationDateEdit.setDisable(false);
        jtextfieldMacroCreationDateEdit.setEditable(true);
        jtextfieldTypeMacroEditionDateEdit.setText(script.getEditionDate());
        jtextfieldTypeMacroEditionDateEdit.setDisable(false);
        jtextfieldTypeMacroEditionDateEdit.setEditable(true);
        stimuliCheckBoxEdit.setSelected(script.getIsStimuli() != 0);

        controllerTableAction.displayScriptAndStepEdit(script);     //Contruct most of the view. 
        controllerPreviewMacro.updateGridPaneCreationView(script);
        this.anchorPanelEditMacro.getStylesheets().add("/view/testcampaign/cssViewCampaign.css");

    }

    private void loadPreviewMacro() {
        System.out.println("Here Edit header load");

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/macroActions/headerPreviewMacro.fxml").openStream());
            AnchorPane.setTopAnchor(paneTest, 0.0);
            AnchorPane.setRightAnchor(paneTest, 0.0);
            AnchorPane.setLeftAnchor(paneTest, 0.0);
            AnchorPane.setBottomAnchor(paneTest, 0.0);
            this.anchorHeader.getChildren().add(paneTest);
            //this.hBoxHeader.getChildren().add(paneTest);
            //this.gridPaneLabelCaseNew.add(paneTest, 5, 0, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseEditController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.controllerPreviewMacro.initialize(scrollPanePreview);
        } catch (Exception e) {
            System.out.println("exception tab marco = " + e);
        }
    }

    private void changeColorTextMacroName(boolean color) {
        if (color) {
            labelMacroNameEdit.setTextFill(Color.RED);
        } else {
            labelMacroNameEdit.setTextFill(Color.BLACK);
        }
    }

    private void changeColorTextDescription(boolean color) {
        if (color) {
            labelMacroObjectives.setTextFill(Color.RED);
        } else {
            labelMacroObjectives.setTextFill(Color.BLACK);
        }
    }

    private void activationValidButton() {
        if (!jtextareaObjectivesMacroEdit.getText().isEmpty() && !jtextfieldMacroNameEdit.getText().isEmpty() && this.controllerTableAction.getCollectionControllerScript().size() != 0) {
            this.buttonValid.setDisable(false);
        } else {
            this.buttonValid.setDisable(true);
        }
    }
}
