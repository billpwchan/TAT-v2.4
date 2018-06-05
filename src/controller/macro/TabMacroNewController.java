/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import DB.Script;
import DBcontroller.sessionFactorySingleton;
import controller.macroActions.PreviewMacro;
import controller.macroActions.ScriptLineTableMacroController;
import controller.macroActions.TableActionCreationController;
import controller.tabtestcase.TabTestCaseNewController;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabMacroNewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelNewTestCase;
    @FXML
    private GridPane gridPaneCaseNew;
    @FXML
    private Button buttonValid;
    @FXML
    private GridPane gridPaneTableAction;
    @FXML
    private Text labelActionTitle;
    @FXML
    private Button buttonAddAction;
    @FXML
    private GridPane gridPaneLabelCaseNew;
    @FXML
    private Label labelMacroName;
    @FXML
    private Label labelMacroEditionDate;
    @FXML
    private Label labelMacroCreationDate;
    @FXML
    private Label labelMacroVersion;
    @FXML
    private Label labelMacroObjectives;
    @FXML
    private TextField jtextfieldMacroName;
    @FXML
    private TextField jtextfieldMacroNew;
    @FXML
    private TextField jtextfieldMacroCreationDate;
    @FXML
    private TextField jtextfieldTypeMacroEditionDate;
    @FXML
    private TextArea jtextareaObjectivesMacro;
    @FXML
    private AnchorPane anchorPanePreview;
    @FXML
    private ScrollPane scrollPanePreview;
    @FXML
    private CheckBox stimuliCheckBox;
    @FXML
    private Label labelIsStimuli;
    @FXML
    private AnchorPane anchorHeader;

    private static TabMacroMainViewController mainController;

    private TableActionCreationController controllerTableAction;

    private final PreviewMacro controllerPreviewMacro = new PreviewMacro();

    private final boolean canBeValidate = false;
    
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

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
        changeColorTextMacroName(true);
        changeColorTextDescription(true);

        jtextfieldMacroName.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorTextMacroName(newValue.trim().isEmpty());
            activationValidButton();
        });

        jtextareaObjectivesMacro.textProperty().addListener((observable, oldValue, newValue) -> {
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

    public void init(TabMacroMainViewController mainController) {
        TabMacroNewController.mainController = mainController;
        controllerTableAction.initMacroNew(this);
    }

    public void initNewMacro() {
        this.loadCSS();
        this.jtextfieldMacroCreationDate.setText(df.format(new Date()));
        this.jtextfieldMacroCreationDate.setId("displayStyle");
        this.jtextfieldMacroNew.setText("1");
        this.jtextfieldMacroNew.setId("displayStyle");
        this.jtextfieldTypeMacroEditionDate.setId("displayStyle");
    }

    public void initButtons() {
        this.buttonValid.setDisable(true);
        this.buttonAddAction.setOnAction((ActionEvent e) -> {
            controllerTableAction.addAction();
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            try {
                this.createMacro();
            } catch (ParseException ex) {
                Logger.getLogger(TabMacroNewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public PreviewMacro getControllerPreview() {
        return this.controllerPreviewMacro;
    }

    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneTableAction.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/macroActions/tableActionCreation.fxml").openStream()), 0, 1, 1, 5);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerTableAction = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/macroActions/headerTableAction.fxml").openStream());

            this.gridPaneTableAction.add(paneTest, 0, 0, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void createMacro() throws ParseException {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ObservableList<ScriptLineTableMacroController> observableScripts = controllerTableAction.getCollectionControllerScript();
        int numberScript = observableScripts.size();
        Script macro = constructMacro();
        session.save(macro);
        int i = 0;
        boolean missingPurpose = false;
        while (i < numberScript && missingPurpose == false) {
            if ("".equals(observableScripts.get(i).getScriptControllerAction().getHashParamScriptMacro().get(0).getValue())) {
                this.displayAlert();
                missingPurpose = true;
            } else {
                observableScripts.get(i).getScriptControllerAction().getScriptMacro().setScriptByScriptIdScript(macro);
                observableScripts.get(i).getScriptControllerAction().getScriptMacro().setScriptOrder((byte) i);
                session.save(observableScripts.get(i).getScriptControllerAction().getScriptMacro());
                i++;
            }
        }
        if (missingPurpose == false) {
            session.beginTransaction().commit();
            mainController.updateRepository();
            mainController.closeTab();
            mainController.focusLibrary();
        }

        session.close();
    }

    private Script constructMacro() throws ParseException {
        Script newScript = new Script();
        newScript.setDesciption(jtextareaObjectivesMacro.getText());
        newScript.setName(jtextfieldMacroName.getText());
        newScript.setScriptVersion(1);
        newScript.setCreationDate(df.format(new Date()));
        newScript.setIsMacro((byte) 1);
        newScript.setIsStimuli((byte) (stimuliCheckBox.isSelected()? 1 : 0));
        return newScript;
    }

    private void loadPreviewMacro() {
        System.out.println("HERE header load");
        //        System.out.println("SCROLLPANE = " + scrollPanePreview);
        //        System.out.println("CONTROLLER MACRO = " + controllerPreviewMacro);
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
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.controllerPreviewMacro.initialize(scrollPanePreview);
        } catch (Exception e) {
            System.out.println("exception tab marco = " + e);
        }
    }

    private void displayAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Missing Purpose");
        alert.setHeaderText(null);
        alert.setContentText("A script purpose is missing in your macro");
        alert.showAndWait();
    }

    public void displayMacro(Script script) {
        this.anchorPanelNewTestCase.getStylesheets().add("/view/testcampaign/cssViewCampaign.css");
        buttonAddAction.setVisible(false);
        buttonValid.setVisible(false);
        //MacroDB macroHandler=new MacroDB();
        //macroHandler.getAllFromMacro(script);
        jtextareaObjectivesMacro.setText(script.getDesciption());
        jtextareaObjectivesMacro.setDisable(true);
        jtextfieldMacroName.setText(script.getName());
        jtextfieldMacroName.setEditable(false);
        //jtextfieldMacroName.setDisable(true);
        jtextfieldMacroNew.setText(script.getScriptVersion().toString());
        jtextfieldMacroNew.setEditable(false);
        //jtextfieldMacroNew.setDisable(true);
        jtextfieldMacroCreationDate.setText(script.getCreationDate().toString());
        jtextfieldMacroCreationDate.setEditable(false);
        //jtextfieldMacroCreationDate.setDisable(true);
        //jtextfieldTypeMacroEditionDate.setDisable(true);
        stimuliCheckBox.setSelected(script.getIsStimuli() != 0);
        stimuliCheckBox.setDisable(true);
        controllerTableAction.displayScriptAndStepView(script);
        controllerPreviewMacro.updateGridPaneCreationView(script);
    }

    /**
     * Add css sheet to the anchorPane of the view
     */
    private void loadCSS() {
        this.anchorPanelNewTestCase.getStylesheets().add("/view/testexecution/cssLibraryTestCase.css");
    }

    private void changeColorTextMacroName(boolean color) {
        if (color) {
            labelMacroName.setTextFill(Color.RED);
        } else {
            labelMacroName.setTextFill(Color.BLACK);
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
        if (!jtextareaObjectivesMacro.getText().isEmpty() && !jtextfieldMacroName.getText().isEmpty() && this.controllerTableAction.getCollectionControllerScript().size() != 0) {
            this.buttonValid.setDisable(false);
        } else {
            this.buttonValid.setDisable(true);
        }
    }

}
