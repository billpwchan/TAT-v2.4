package controller.tabtestcase;

import DB.ScriptHasBeenConfigured;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import DBcontroller.TestCaseDB;
import DBcontroller.sessionFactorySingleton;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.ScriptLineTableStepController;
import controller.tablestep.StepLineTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import controller.util.CommonFunctions;
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
import model.setCursorOnComponent;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabTestCaseEditController implements Initializable {

    private static TabTestCaseMainViewController main;
    private final TestCaseDB testCaseHandler = new TestCaseDB();
    //Maximum Allowed TextField Length
    private final int textfieldCaseIDMaxLength = 30;
    private final int textfieldCaseVersionMaxLength = 10;
    private final int textfieldProjectMaxLength = 10;
    private final int textfieldTypeTestMaxLength = 20;
    private final int textfieldTestCategoryMaxLength = 20;
    private final int textfieldLocationMaxLength = 20;
    private final int textfieldCaseTitleMaxLength = 60;
    private final int textfieldWriterMaxLength = 20;
    private final int textfieldWriterEmailMaxLength = 50;
    private final int textfieldCaseSourceMaxLength = 50;
    private final int textfieldTestEnvironementMaxLength = 20;
    @FXML
    private AnchorPane anchorPanelNewTestCase;
    @FXML
    private GridPane gridPaneCaseNew;
    @FXML
    private GridPane gridPaneLabelCaseNew;
    @FXML
    private Label labelCaseIDNew;
    @FXML
    private Label labelTypeTestCaseNew;
    @FXML
    private Label labelProjectCaseNew;
    @FXML
    private Label labelCaseVersionNew;
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
    private Label labelDescriptionCaseNew;
    @FXML
    private Label labelCommentsCaseNew;
    @FXML
    private Label labelListViewCaseNew;
    @FXML
    private Label labelListViewCaseNew1;
    @FXML
    private TextField jtextfieldCaseIDEdit;
    @FXML
    private TextField jtextfieldCaseVersionEdit;
    @FXML
    private TextField jtextfieldProjectCaseEdit;
    @FXML
    private TextField jtextfieldTypeTestCaseEdit;
    @FXML
    private TextField jtextfieldTestCategoryCaseEdit;
    @FXML
    private TextField jtextfieldLocationCaseEdit;
    @FXML
    private TextField jtextfieldCaseTitleEdit;
    @FXML
    private ChoiceBox<String> choiceboxTestMethodAddCase;
    @FXML
    private ChoiceBox<String> choiceboxWritingStatusAddCase;
    @FXML
    private TextField jtextfieldWriterCaseEdit;
    @FXML
    private TextField jtextfieldWriterEmailCaseEdit;
    @FXML
    private TextField jtextfieldCaseSourceEdit;
    @FXML
    private TextField jtextfieldTestEnvironementCaseEdit;
    @FXML
    private TextField jtextfieldEditionDateCaseEdit;
    @FXML
    private TextField jtextfieldCreationDateCaseEdit;
    @FXML
    private TextField jtextfieldInternalVersionCaseEdit;
    @FXML
    private TextField jtextfieldNumberStepCaseEdit;
    @FXML
    private CheckBox checkBoxBlockingCaseEdit;
    @FXML
    private TextArea jtextareaCaseObjectivesEdit;
    @FXML
    private TextArea jtextareaInternalCommentsCaseEdit;
    @FXML
    private ListView<String> listViewPCRCaseNew;
    @FXML
    private ListView<String> listViewRequirementCaseNew;
    @FXML
    private Button buttonValid;
    @FXML
    private GridPane gridPaneTableStep;
    @FXML
    private Button button1;
    @FXML
    private Button buttonAddStep;
    @FXML
    private Button buttonAddScript;
    private boolean canBeValidate = false;
    private Stage dialogStage;
    private TableStepScriptCreationController controllerTableStep;
    private int testcaseID = -1;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeHeaderInformationCase();
        defineCursor();
        initializeButtonAction();
    }

    void constructInformation(TestCase testCase) {

        /**
         * Set all the field of the assets.view form the information in the variable
         * test case
         */
        this.jtextfieldCaseIDEdit.setText(testCase.getTestCaseIdentification());
        this.jtextfieldCaseIDEdit.setDisable(false);
        this.jtextfieldCaseVersionEdit.setText(testCase.getTestCaseVersion());
        this.jtextfieldProjectCaseEdit.setText(testCase.getProject());
        this.jtextfieldTypeTestCaseEdit.setText(testCase.getTypeOfTest());
        this.jtextfieldTestCategoryCaseEdit.setText(testCase.getCategoryOfTest());
        this.jtextfieldLocationCaseEdit.setText(testCase.getLocation());
        this.jtextfieldCaseTitleEdit.setText(testCase.getTestCaseTitle());
        this.choiceboxTestMethodAddCase.getSelectionModel().select(testCase.getTestMethodIadt());
        this.choiceboxWritingStatusAddCase.getSelectionModel().select(testCase.getWritingStatus());
        this.jtextfieldWriterCaseEdit.setText(testCase.getWritter());
        this.jtextfieldWriterEmailCaseEdit.setText(testCase.getWritterEmail());
        this.jtextfieldTestEnvironementCaseEdit.setText(testCase.getEnvironment());
        this.jtextfieldCreationDateCaseEdit.setText(testCase.getCreationDate());
        this.jtextfieldInternalVersionCaseEdit.setText(String.valueOf(testCase.getCaseInternalVersion() + 1));
        this.jtextfieldNumberStepCaseEdit.setText(String.valueOf(testCase.getTotalSteps()));
        this.jtextareaCaseObjectivesEdit.setText(testCase.getTestObjective());
        this.jtextareaInternalCommentsCaseEdit.setText(testCase.getInternalComments());
        this.checkBoxBlockingCaseEdit.setSelected(testCase.getBlocking() != 0);
        this.jtextfieldCaseSourceEdit.setText(testCase.getTestCaseSource());

        initializeFieldListener();

        controllerTableStep.displayScriptAndStepEdit(testCase);

        /**
         * Query the DB and get all the test step and script of this particular
         * test case.
         */
        //Need to create a assets.view mode fo the table test step;
        //displayTestStepToTreeTable(testCaseHandler.getAllFromCase(testCase.getIdtestCase()));
        //controllerTableStep.addStepAndScriptView(testCaseHandler.getAllFromCase(testCase.getIdtestCase()));
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
        constructTableStep();
        TabTestCaseEditController.main = mainController;
        controllerTableStep.getCollectionTestStep().addListener((ListChangeListener) change -> {
            jtextfieldNumberStepCaseEdit.setText(String.valueOf(controllerTableStep.getCollectionTestStep().size()));
        });
        controllerTableStep.initTestCaseEdit(this);
    }

    private void initializeHeaderInformationCase() {
        this.choiceboxWritingStatusAddCase.getItems().addAll("Being written", "Written & not reviewed", "Written & reviewed NOK", "Written & reviewed OK");
        this.choiceboxTestMethodAddCase.getItems().addAll("Inspection", "Analysis", "Demonstration", "Test");
        this.jtextfieldEditionDateCaseEdit.setText(Main.df.format(new Date()));
        this.buttonValid.setDisable(false);
        this.buttonAddScript.setDisable(true);
    }

    private void initializeFieldListener() {
        this.jtextfieldCaseIDEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelCaseIDNew, newValue);
            if (CommonFunctions.displayWarningIncorrectInputFormat("Case ID", textfieldCaseIDMaxLength, newValue.length() > textfieldCaseIDMaxLength)) {
                jtextfieldCaseIDEdit.setText(oldValue);
            }
        });
        this.jtextfieldTestCategoryCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelTestCategoryCaseNew, newValue);
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                jtextfieldTestCategoryCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldTestEnvironementCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelTestEnvironementCaseNew, newValue);
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Environement", textfieldTestEnvironementMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                jtextfieldTestEnvironementCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldCaseTitleEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Title", textfieldCaseTitleMaxLength, newValue.length() > textfieldCaseTitleMaxLength)) {
                jtextfieldCaseTitleEdit.setText(oldValue);
            }
        });
        this.jtextfieldCaseVersionEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Case Version", textfieldCaseVersionMaxLength, newValue.length() > textfieldCaseVersionMaxLength)) {
                jtextfieldCaseVersionEdit.setText(oldValue);
            }
        });
        this.jtextfieldProjectCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Project", textfieldProjectMaxLength, newValue.length() > textfieldProjectMaxLength)) {
                jtextfieldProjectCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldTypeTestCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Project", textfieldTypeTestMaxLength, newValue.length() > textfieldTypeTestMaxLength)) {
                jtextfieldTypeTestCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldTestCategoryCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            changeColorLabel(labelTestCategoryCaseNew, newValue);
            if (CommonFunctions.displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                jtextfieldTestCategoryCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldLocationCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Location", textfieldLocationMaxLength, newValue.length() > textfieldLocationMaxLength)) {
                jtextfieldLocationCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldWriterCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Writer", textfieldWriterMaxLength, newValue.length() > textfieldWriterMaxLength)) {
                jtextfieldWriterCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldWriterEmailCaseEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Writer Email", textfieldWriterEmailMaxLength, newValue.length() > textfieldWriterEmailMaxLength)) {
                jtextfieldWriterEmailCaseEdit.setText(oldValue);
            }
        });
        this.jtextfieldCaseSourceEdit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (CommonFunctions.displayWarningIncorrectInputFormat("Case Source", textfieldCaseSourceMaxLength, newValue.length() > textfieldCaseSourceMaxLength)) {
                jtextfieldCaseSourceEdit.setText(oldValue);
            }
        });
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
     * Check the state of the button validate, enable it when all of these 4
     * field are fill.
     *
     * @return canBeValidate boolean of button valid disable.
     */
    private void minimumInformationTestCase() {
        canBeValidate = !jtextfieldCaseIDEdit.getText().isEmpty()
                && !jtextfieldTestCategoryCaseEdit.getText().isEmpty()
                && !jtextfieldTestEnvironementCaseEdit.getText().isEmpty()
                && !(jtextfieldWriterEmailCaseEdit.getText().length() > 50);
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

    private void initializeButtonAction() {
        buttonAddStep.setOnAction((ActionEvent e) -> {
            controllerTableStep.addStepinVbox(null);
            controllerTableStep.configurationOnForStep();   //Add a step already

            ObservableList<StepLineTableStepController> observableTestStep = controllerTableStep.getCollectionTestStep();   //Try to select this new step
            controllerTableStep.updateCurrentStep(observableTestStep.get(observableTestStep.size() - 1));
            controllerTableStep.addScriptToSelectStep();

//            CommonFunctions.reportLog.info("Add a new step to test case (Test Case Edit)");
        });

        buttonAddScript.setOnAction((ActionEvent e) -> {
            controllerTableStep.addScriptToSelectStep();
//            CommonFunctions.reportLog.info("Add a new script to test step (Test Case Edit)");
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            if (this.createUpdateTestCase()) {
                main.updateLibrary();
                main.closeTab();
                main.focusOnLast();
            }
        });
    }

    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneTableStep.add(fxmlLoader.load(getClass().getResource("/assets/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 0, 1, 1, 5);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = fxmlLoader2.load(getClass().getResource("/assets/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneTableStep.add(paneTest, 0, 0, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        HeaderTableStepController controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeaderTableStep);
    }

    /**
     * Construct an object test case from the field in the assets.view
     *
     * @return testCaseFromNew the test case created.
     */
    private TestCase constructTestCase() throws ParseException {
        return new TestCase(
                jtextfieldCaseTitleEdit.getText(),
                jtextfieldWriterCaseEdit.getText(),
                new Byte(jtextfieldInternalVersionCaseEdit.getText()),
                Main.df.format(new Date()),
                null,
                jtextfieldCaseIDEdit.getText(),
                jtextfieldCaseVersionEdit.getText(),
                jtextfieldProjectCaseEdit.getText(),
                jtextfieldTypeTestCaseEdit.getText(),
                jtextfieldTestCategoryCaseEdit.getText(),
                jtextfieldLocationCaseEdit.getText(),
                jtextfieldTestEnvironementCaseEdit.getText(),
                jtextareaCaseObjectivesEdit.getText(),
                new Byte(jtextfieldNumberStepCaseEdit.getText()),
                jtextfieldWriterEmailCaseEdit.getText(),
                choiceboxWritingStatusAddCase.getSelectionModel().getSelectedItem(),
                choiceboxTestMethodAddCase.getSelectionModel().getSelectedItem(),
                (byte) (checkBoxBlockingCaseEdit.isSelected() ? 1 : 0),
                jtextareaInternalCommentsCaseEdit.getText(),
                jtextfieldCaseSourceEdit.getText());
    }


    /**
     * Create the object test case linked to his set of test steps, which each
     * steps linked to the his set of script.
     */
    private boolean createUpdateTestCase() {
        //Validate each test stpe to see if no step description is found.
        if (CommonFunctions.validateUpdateTestCase(controllerTableStep.getCollectionTestStep())) {
            return false;
        }
        String reportLogMsg = "";
        ObservableList<StepLineTableStepController> observableTestStep = controllerTableStep.getCollectionTestStep();

        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        int numberOfTestStep = observableTestStep.size();
        TestCase thisTestCase = null;
        try {
            thisTestCase = this.constructTestCase();        //Construct a new test case with info inside textarea & Combo boxes.
        } catch (ParseException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        session.save(thisTestCase);
        session.beginTransaction().commit();        //This can generate a new ID for reference
        reportLogMsg += "After Editing Test Case :" + Objects.requireNonNull(thisTestCase).getTestCaseTitle() + System.lineSeparator();
        StringBuilder reportLogMsgBuilder = new StringBuilder(reportLogMsg);
        for (int i = 0; i < numberOfTestStep; i++) {
            StepLineTableStepController current = observableTestStep.get(i);
            TestStep step = new TestStep(current.getTestStep());
            step.setStepOrder((byte) i);
            step.setTestCase(thisTestCase); //At this stage, the problem is TestStep's ScriptHasBeenConfigured Object is not yet saved. As well as TestStepHasScripts
            step.setTestStepHasScripts(new HashSet<>());    //Cuz the TestStephasSripts is not yet saved.
            session.save(step);
            session.beginTransaction().commit();
            thisTestCase.addStep(step);
            reportLogMsgBuilder.append("\tStep ").append(i).append(": ").append(step.getHumanStimuli()).append(System.lineSeparator());
            int numberOfScript = current.getCollectionScript().size();
            int executionOrderScript = 0;

            for (int j = 0; j < numberOfScript; j++) {
                //This while-loop is for the Step description (Left part of the Test steps). Only consider isScript = 1 case.
                ScriptLineTableStepController currentScript = current.getCollectionScript().get(j);

                if (currentScript.getScriptAction() != null) {
                    currentScript.getScriptAction().setExecutionOrder((byte) executionOrderScript);
                    TestStepHasScript newTSHS = new TestStepHasScript(currentScript.getScriptAction());
                    newTSHS.setTestStep(step);
                    executionOrderScript++;
                    step.addTestStephasScript(newTSHS);
                    Iterator<ScriptHasBeenConfigured> itScriptHBC = currentScript.getScriptAction().getScriptHasBeenConfigureds().iterator();
                    Set<ScriptHasBeenConfigured> setSHBC = new HashSet<>();
                    while (itScriptHBC.hasNext()) {
                        ScriptHasBeenConfigured nextSHBC = new ScriptHasBeenConfigured(itScriptHBC.next(), newTSHS);
                        setSHBC.add(nextSHBC);
                    }
                    newTSHS.setScriptHasBeenConfigureds(setSHBC);
                    reportLogMsgBuilder.append("\t\tScript ").append(j + 1).append(" Step Description: ").append(newTSHS.getScript().getName()).append(System.lineSeparator());
                }
                //This while-loop is for Expected result (Right part of the Test steps). Onlye consider isMacro = 1 case.
                if (currentScript.getScriptVerif() != null) {
                    currentScript.getScriptVerif().setExecutionOrder((byte) executionOrderScript);
                    TestStepHasScript newTSHS = new TestStepHasScript(currentScript.getScriptVerif());
                    newTSHS.setTestStep(step);
                    executionOrderScript++;
                    //System.out.println("ScriptVerif, TSHS ID: " + currentScript.getScriptAction().getIdtestStepHasScript() + currentScript.getScriptAction());
                    step.addTestStephasScript(currentScript.getScriptVerif());
//                    System.out.println("ID TEST STEP HAS SCRIPT = " + currentScript.getScriptVerif().getIdtestStepHasScript());
                    Iterator<ScriptHasBeenConfigured> itScriptHBC = currentScript.getScriptVerif().getScriptHasBeenConfigureds().iterator();
                    Set<ScriptHasBeenConfigured> setSHBC = new HashSet<>();

                    while (itScriptHBC.hasNext()) {
                        ScriptHasBeenConfigured nextSHBC = new ScriptHasBeenConfigured(itScriptHBC.next(), newTSHS);
                        setSHBC.add(nextSHBC);
                    }
                    newTSHS.setScriptHasBeenConfigureds(setSHBC);
                    reportLogMsgBuilder.append("\t\tScript ").append(j + 1).append(" Expected Result: ").append(newTSHS.getScript().getName()).append(System.lineSeparator());
                }
            }
        }
        reportLogMsg = reportLogMsgBuilder.toString();
        try {
            session.beginTransaction().commit();        //Cause TestStepHasScript exception (need to save it before commit).
            CommonFunctions.reportLog.info(reportLogMsg);
            CommonFunctions.reportLog.info("Successfully saved test case: " + Objects.requireNonNull(thisTestCase).getTestCaseIdentification());
        } catch (TransientObjectException ex) {     //Need to close the session regardless the exception occured.
            CommonFunctions.debugLog.error("Error in saving edited test case", ex);
        }
        session.close();
        return true;
    }

    /**
     * @return
     */
    public AnchorPane getAnchorPane() {
        return this.anchorPanelNewTestCase;
    }
}
