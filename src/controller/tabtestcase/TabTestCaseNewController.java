/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcase;

import DB.Requirement;
import DB.TestCase;
import DB.TestStep;
import DBcontroller.sessionFactorySingleton;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.ScriptLineTableStepController;
import controller.tablestep.StepLineTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import controller.util.CommonFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import model.initColumn;
import model.setCursorOnComponent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static controller.util.CommonFunctions.displayWarningIncorrectInputFormat;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabTestCaseNewController implements Initializable {

    private static TabTestCaseMainViewController main;
    //Maximum Allowed TextField Length
    private final int textfieldCaseIDMaxLength = 30;
    private final int textfieldCaseVersionMaxLength = 10;
    private final int textfieldProjectMaxLength = 10;
    private final int textfieldTypeTestMaxLength = 20;
    private final int textfieldTestCategoryMaxLength = 20;
    private final int textfieldLocationMaxLength = 20;
    private final int textfieldCaseTitleMaxLength = 30;
    private final int textfieldWriterMaxLength = 20;
    private final int textfieldWriterEmailMaxLength = 50;
    private final int textfieldCaseSourceMaxLength = 50;
    private final int textfieldTestEnvironementMaxLength = 20;
    @FXML
    private AnchorPane anchorPanelNewTestCase;
    @FXML
    private GridPane gridPaneCaseNew;
    @FXML
    private AnchorPane anchorPaneStepTable;
    @FXML
    private Button buttonAddStep;
    @FXML
    private Button buttonAddScript;
    @FXML
    private GridPane gridPaneLabelCaseNew;
    @FXML
    private Label labelCaseIDNew;
    @FXML
    private Label labelTypeTestCaseNew;
    @FXML
    private Label labelProjectCaseNew;
    @FXML
    private Label labelVersionCaseNew;
    @FXML
    private Label labelTestSheetIDCaseNew;
    @FXML
    private Label labelTestSheetVersionCaseNew;
    @FXML
    private Label labelCaseTitleNew;
    @FXML
    private Label labelWriterCaseNew;
    @FXML
    private Label labelWritingStatusCaseNew;
    @FXML
    private Label labelWriterEmailCaseNew;
    @FXML
    private Label labelTestCategoryCaseNew;
    @FXML
    private Label labelTestEnvironementCaseNew;
    @FXML
    private TextField jtextfieldCaseIDNew;
    @FXML
    private TextField jtextfieldInternalVersionCaseNew;
    @FXML
    private TextField jtextfieldCaseTitleNew;
    @FXML
    private TextField jtextfieldWriterCaseNew;
    @FXML
    private TextField jtextfieldTypeTestCaseNew;
    @FXML
    private TextField jtextfieldCaseSourceNew;
    @FXML
    private TextField jtextfieldWriterEmailCaseNew;
    @FXML
    private TextField jtextfieldProjectCaseNew;
    @FXML
    private TextField jtextfieldCaseVersionNew;
    @FXML
    private TextField jtextfieldTestCategoryCaseNew;
    @FXML
    private TextField jtextfieldLocationCaseNew;
    @FXML
    private CheckBox checkBoxBlockingCaseNew;
    @FXML
    private TextField jtextfieldTestEnvironementCaseNew;
    @FXML
    private TextArea jtextareaObjectivesCaseNew;
    @FXML
    private Label labelDescriptionCaseNew;
    @FXML
    private Label labelCommentsCaseNew;
    @FXML
    private TextArea jtextareaCommentsCaseNew;
    @FXML
    private Button buttonValid;
    @FXML
    private ChoiceBox<String> choiceboxWritingStatusAddCase;
    @FXML
    private ChoiceBox<String> choiceboxTestMethodAddCase;
    @FXML
    private TextField jtextfieldCreationDateCaseNew;
    @FXML
    private TextField jtextfieldNumberStepCaseNew;
    @FXML
    private GridPane gridPaneTableStep;
    @FXML
    private ListView listViewRequirementCaseNew;
    private Stage dialogStage;
    private TableStepScriptCreationController controllerTableStep;
    private boolean canBeValidate = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Initialize the information of the header (color label, text in combobox,...)
        this.initializeHeaderInformationCase();

        //Initialize the actions of the buttons.
        this.initializeButtonAction();

        //Initialize the property listener on the different field of the test case, use mainly to check the disable property of the valid button.
        this.initializeFieldListener();

        //Construct the table for the step configuration
        this.constructTableStep();

        //Define the cursor which should change when entering node.
        defineCursor();

        initColumn.initTableViewRequirement(listViewRequirementCaseNew);

    }

    /**
     * Reference the mainview controller into the object main and init the
     * tablestep with the reference of this object. Add a listener to the size
     * of the step collection (in order to know how many steps there are in the
     * case).
     *
     * @param mainController
     */
    public void init(TabTestCaseMainViewController mainController) {
        TabTestCaseNewController.main = mainController;
        jtextfieldWriterCaseNew.setText(Main.currentUser.getName());
        jtextfieldWriterEmailCaseNew.setText(Main.currentUser.getEmail());
        controllerTableStep.getCollectionTestStep().addListener((ListChangeListener) change -> {
            jtextfieldNumberStepCaseNew.setText(String.valueOf(controllerTableStep.getCollectionTestStep().size()));
        });
        controllerTableStep.initTestCaseNew(this);
    }

    /**
     * Construct an object test case from the field in the assets.view
     *
     * @return testCaseFromNew the test case created.
     */
    private TestCase constructTestCase() {
        return new TestCase(
                jtextfieldCaseTitleNew.getText(),
                jtextfieldWriterCaseNew.getText(),
                new Byte(jtextfieldInternalVersionCaseNew.getText()),
                Main.df.format(new Date()),
                null,
                jtextfieldCaseIDNew.getText(),
                jtextfieldCaseVersionNew.getText(),
                jtextfieldProjectCaseNew.getText(),
                jtextfieldTypeTestCaseNew.getText(),
                jtextfieldTestCategoryCaseNew.getText(),
                jtextfieldLocationCaseNew.getText(),
                jtextfieldTestEnvironementCaseNew.getText(),
                jtextareaObjectivesCaseNew.getText(),
                new Byte(jtextfieldNumberStepCaseNew.getText()),
                jtextfieldWriterEmailCaseNew.getText(),
                choiceboxWritingStatusAddCase.getSelectionModel().getSelectedItem(),
                choiceboxTestMethodAddCase.getSelectionModel().getSelectedItem(),
                (byte) (checkBoxBlockingCaseNew.isSelected() ? 1 : 0),
                jtextareaCommentsCaseNew.getText(),
                jtextfieldCaseSourceNew.getText());
    }

    /**
     * Close the pop up.
     */
    public void closePopUp() {
        this.dialogStage.close();
    }

    /**
     * Load the assets.view
     */
    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneTableStep.add(fxmlLoader.load(getClass().getResource("/assets/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 0, 1, 1, 5);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = fxmlLoader2.load(getClass().getResource("/assets/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneTableStep.add(paneTest, 0, 0, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        HeaderTableStepController controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
    }


    /**
     * Create the object test case linked to his set of test steps, which each
     * steps linked to the his set of script.
     */
    private boolean createNewTestCase() {
        //Validate the user input first
        if (CommonFunctions.validateUpdateTestCase(controllerTableStep.getCollectionTestStep())) {
            return false;
        }
        String reportLogMsg = "";
        ObservableList<StepLineTableStepController> observableTestStep = controllerTableStep.getCollectionTestStep();
        int numberOfTestStep = observableTestStep.size();
        TestCase thisTestCase = constructTestCase();
        reportLogMsg += "After Creating Test Case :" + Objects.requireNonNull(thisTestCase).getTestCaseTitle() + System.lineSeparator();
        StringBuilder reportLogMsgBuilder = new StringBuilder(reportLogMsg);
        for (int i = 0; i < numberOfTestStep; i++) {
            StepLineTableStepController current = observableTestStep.get(i);
            TestStep step = current.getTestStep();
            step.setStepOrder((byte) i);
            thisTestCase.addStep(step);
            reportLogMsgBuilder.append("\tStep ").append(i).append(": ").append(step.getHumanStimuli()).append(System.lineSeparator());
            int numberOfScript = current.getCollectionScript().size();
            int executionOrderScript = 0;
            for (int j = 0; j < numberOfScript; j++) {
                ScriptLineTableStepController currentScript = current.getCollectionScript().get(j);
                if (currentScript.getScriptAction() != null) {
                    currentScript.getScriptAction().setExecutionOrder((byte) executionOrderScript);
                    executionOrderScript++;
                    step.addTestStephasScript(currentScript.getScriptAction());
                    reportLogMsgBuilder.append("\t\tScript ").append(0).append(": ").append(currentScript.getScriptAction().getScript().getName()).append(System.lineSeparator());
                }
                if (currentScript.getScriptVerif() != null) {
                    currentScript.getScriptVerif().setExecutionOrder((byte) executionOrderScript);
                    executionOrderScript++;
                    step.addTestStephasScript(currentScript.getScriptVerif());
                    reportLogMsgBuilder.append("\t\tScript ").append(0).append(": ").append(currentScript.getScriptVerif().getScript().getName()).append(System.lineSeparator());
                }
            }

        }
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.save(thisTestCase);
        reportLogMsg = reportLogMsgBuilder.toString();
        try {
            session.beginTransaction().commit();        //Cause TestStepHasScript exception (need to save it before commit).
            CommonFunctions.reportLog.info(reportLogMsg);
            CommonFunctions.reportLog.info("Successfully saved test case: " + Objects.requireNonNull(thisTestCase).getTestCaseIdentification());
        } catch (TransientObjectException ex) {     //Need to close the session regardless the exception occured.
            CommonFunctions.debugLog.error("Error in saving new generated test case", ex);
        }
        session.close();
        return true;
    }

    /**
     * Check the state of the button validate, enable it when all of these 4
     * field are fill.
     *
     * @return canBeValidate boolean of button valid disable.
     */
    private boolean minimumInformationTestCase() {

        canBeValidate = !jtextfieldCaseIDNew.getText().isEmpty()
                && !jtextfieldTestCategoryCaseNew.getText().isEmpty()
                && !jtextfieldTestEnvironementCaseNew.getText().isEmpty()
                && !jtextareaObjectivesCaseNew.getText().isEmpty();
        return canBeValidate;

    }

    /**
     * Change the color of the label depending on if the string is empty or not.
     *
     * @param labelTestCaseObjectives
     * @param newValue
     */
    private void changeColorLabel(Label labelTestCaseObjectives, String newValue) {
        if (newValue.isEmpty()) {
            labelTestCaseObjectives.setTextFill(Color.RED);
        } else {
            labelTestCaseObjectives.setTextFill(Color.BLACK);
        }
        minimumInformationTestCase();
        this.buttonValid.setDisable(!canBeValidate);
    }

    /**
     * CHange the type of cursor for the nodes put in this method.
     */
    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonAddScript);
        nodeHand.add(this.buttonAddStep);
        nodeHand.add(this.buttonValid);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     * @param b
     */
    public void setStateButtonScript(boolean b) {
        this.buttonAddScript.setDisable(b);
    }

    private void initializeHeaderInformationCase() {
        this.choiceboxWritingStatusAddCase.getItems().addAll("Being written", "Written & not reviewed", "Written & reviewed NOK", "Written & reviewed OK");
        this.choiceboxWritingStatusAddCase.getSelectionModel().selectFirst();
        this.choiceboxTestMethodAddCase.getItems().addAll("Inspection", "Analysis", "Demonstration", "Test");
        jtextfieldNumberStepCaseNew.setText("0");
        this.jtextfieldCreationDateCaseNew.setText(Main.df.format(new Date()));
        this.jtextfieldInternalVersionCaseNew.setText("1");

        this.labelTestCategoryCaseNew.setTextFill(Color.RED);
        this.labelTestEnvironementCaseNew.setTextFill(Color.RED);
        this.labelCaseIDNew.setTextFill(Color.RED);
        this.labelDescriptionCaseNew.setTextFill(Color.RED);

        this.buttonValid.setDisable(true);
        this.buttonAddScript.setDisable(true);
    }

    private void initializeButtonAction() {
        buttonAddStep.setOnAction((ActionEvent e) -> {
            controllerTableStep.addStepinVbox(null);
            controllerTableStep.configurationOnForStep();
        });

//        buttonDeleteStep.setOnAction((ActionEvent e) -> {
//            if(controllerTableStep.deleteSelectedStep()){
//                 jtextfieldNumberStepCaseNew.setText(String.valueOf(counter.decrementAndGet()));
//            }
//           
//        });
        buttonAddScript.setOnAction((ActionEvent e) -> {
            controllerTableStep.addScriptToSelectStep();
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            if (this.createNewTestCase()) {
                main.updateLibrary();
                main.closeTab();
                main.focusOnLast();
            }
        });
    }

    private void initializeFieldListener() {
        this.jtextfieldCaseIDNew.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelCaseIDNew, newValue);
            if (displayWarningIncorrectInputFormat("Case ID", textfieldCaseIDMaxLength, newValue.length() > textfieldCaseIDMaxLength)) {
                jtextfieldCaseIDNew.setText(oldValue);
            }
        });
        this.jtextfieldTestCategoryCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelTestCategoryCaseNew, newValue);
            if (displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                jtextfieldTestCategoryCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldTestEnvironementCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelTestEnvironementCaseNew, newValue);
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Environement", textfieldTestEnvironementMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                jtextfieldTestEnvironementCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldCaseTitleNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Title", textfieldCaseTitleMaxLength, newValue.length() > textfieldCaseTitleMaxLength)) {
                jtextfieldCaseTitleNew.setText(oldValue);
            }
        });
        this.jtextfieldCaseVersionNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Case Version", textfieldCaseVersionMaxLength, newValue.length() > textfieldCaseVersionMaxLength)) {
                jtextfieldCaseVersionNew.setText(oldValue);
            }
        });
        this.jtextfieldProjectCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Project", textfieldProjectMaxLength, newValue.length() > textfieldProjectMaxLength)) {
                jtextfieldProjectCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldTypeTestCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Project", textfieldTypeTestMaxLength, newValue.length() > textfieldTypeTestMaxLength)) {
                jtextfieldTypeTestCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldTestCategoryCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelTestCategoryCaseNew, newValue);
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                jtextfieldTestCategoryCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldLocationCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Location", textfieldLocationMaxLength, newValue.length() > textfieldLocationMaxLength)) {
                jtextfieldLocationCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldWriterCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Writer", textfieldWriterMaxLength, newValue.length() > textfieldWriterMaxLength)) {
                jtextfieldWriterCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldWriterEmailCaseNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Writer Email", textfieldWriterEmailMaxLength, newValue.length() > textfieldWriterEmailMaxLength)) {
                jtextfieldWriterEmailCaseNew.setText(oldValue);
            }
        });
        this.jtextfieldCaseSourceNew.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Case Source", textfieldCaseSourceMaxLength, newValue.length() > textfieldCaseSourceMaxLength)) {
                jtextfieldCaseSourceNew.setText(oldValue);
            }
        });
        this.jtextareaObjectivesCaseNew.textProperty().addListener((Observable, oldValue, newValue) -> changeColorLabel(labelDescriptionCaseNew, newValue));
    }

    /**
     *
     */
    public void updateListRequirement() {
        HashSet<Requirement> requirementsCase = new HashSet<>();
        for (int i = 0; i < this.controllerTableStep.getCollectionTestStep().size(); i++) {
            requirementsCase.addAll(this.controllerTableStep.getCollectionTestStep().get(i).getRequirements());
        }
        //this.controllerTableStep.getCollectionTestStep().get(0).getRequirements()
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //System.out.println("Jupdate ma liste de rek");
        listViewRequirementCaseNew.setItems(FXCollections.observableArrayList(requirementsCase));
    }

    /**
     * @return AnchorPane
     */
    public AnchorPane getAnchorPane() {
        return this.anchorPanelNewTestCase;
    }
}
