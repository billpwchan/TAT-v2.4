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
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabTestCaseEditController implements Initializable {

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

    private HeaderTableStepController controllerHeaderTableStep;

    SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");

    private boolean canBeValidate = false;

    private static TabTestCaseMainViewController main;

    private Stage dialogStage;

    private TableStepScriptCreationController controllerTableStep;

    private final TestCaseDB testCaseHandler = new TestCaseDB();

    private int testcaseID = -1;

    private Alert alert;

    //Maximum Allowed TextField Length
    final int textfieldCaseIDMaxLength = 30;
    final int textfieldCaseVersionMaxLength = 10;
    final int textfieldProjectMaxLength = 10;
    final int textfieldTypeTestMaxLength = 20;
    final int textfieldTestCategoryMaxLength = 20;
    final int textfieldLocationMaxLength = 20;
    final int textfieldCaseTitleMaxLength = 60;
    final int textfieldWriterMaxLength = 20;
    final int textfieldWriterEmailMaxLength = 50;
    final int textfieldCaseSourceMaxLength = 50;
    final int textfieldTestEnvironementMaxLength = 20;

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
         * Set all the field of the view form the information in the variable
         * test case
         */
        this.jtextfieldCaseIDEdit.setText(testCase.getTestCaseIdentification());
        this.jtextfieldCaseIDEdit.setDisable(true);
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
        //Need to create a view mode fo the table test step;
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
        controllerTableStep.getCollectionTestStep().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                jtextfieldNumberStepCaseEdit.setText(String.valueOf(controllerTableStep.getCollectionTestStep().size()));
            }
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
        this.jtextfieldCaseIDEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelCaseIDNew, newValue);
                if (CommonFunctions.displayWarningIncorrectInputFormat("Case ID", textfieldCaseIDMaxLength, newValue.length() > textfieldCaseIDMaxLength)) {
                    jtextfieldCaseIDEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldTestCategoryCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelTestCategoryCaseNew, newValue);
                if (CommonFunctions.displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                    jtextfieldTestCategoryCaseEdit.setText(oldValue);
                }
            }

        });
        this.jtextfieldTestEnvironementCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelTestEnvironementCaseNew, newValue);
                if (CommonFunctions.displayWarningIncorrectInputFormat("Test Environement", textfieldTestEnvironementMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                    jtextfieldTestEnvironementCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldCaseTitleEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Test Title", textfieldCaseTitleMaxLength, newValue.length() > textfieldCaseTitleMaxLength)) {
                    jtextfieldCaseTitleEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldCaseVersionEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Test Case Version", textfieldCaseVersionMaxLength, newValue.length() > textfieldCaseVersionMaxLength)) {
                    jtextfieldCaseVersionEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldProjectCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Project", textfieldProjectMaxLength, newValue.length() > textfieldProjectMaxLength)) {
                    jtextfieldProjectCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldTypeTestCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Project", textfieldTypeTestMaxLength, newValue.length() > textfieldTypeTestMaxLength)) {
                    jtextfieldTypeTestCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldTestCategoryCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelTestCategoryCaseNew, newValue);
                if (CommonFunctions.displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                    jtextfieldTestCategoryCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldLocationCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Location", textfieldLocationMaxLength, newValue.length() > textfieldLocationMaxLength)) {
                    jtextfieldLocationCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldWriterCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Writer", textfieldWriterMaxLength, newValue.length() > textfieldWriterMaxLength)) {
                    jtextfieldWriterCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldWriterEmailCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Writer Email", textfieldWriterEmailMaxLength, newValue.length() > textfieldWriterEmailMaxLength)) {
                    jtextfieldWriterEmailCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldCaseSourceEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (CommonFunctions.displayWarningIncorrectInputFormat("Case Source", textfieldCaseSourceMaxLength, newValue.length() > textfieldCaseSourceMaxLength)) {
                    jtextfieldCaseSourceEdit.setText(oldValue);
                }
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
    private boolean minimumInformationTestCase() {

        canBeValidate = !jtextfieldCaseIDEdit.getText().isEmpty()
                && !jtextfieldTestCategoryCaseEdit.getText().isEmpty()
                && !jtextfieldTestEnvironementCaseEdit.getText().isEmpty()
                && !(jtextfieldWriterEmailCaseEdit.getText().length() > 50);
        return canBeValidate;
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
     *
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
        });

        buttonAddScript.setOnAction((ActionEvent e) -> {
            controllerTableStep.addScriptToSelectStep();
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            //if (displayWarningDeleteCase()) {
            //alertBox2();
            this.createUpdateTestCase();
            //this.closeAlert();
            main.updateLibrary();
            main.closeTab();
            main.focusOnLast();
            // }
        });
    }

    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneTableStep.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 0, 1, 1, 5);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneTableStep.add(paneTest, 0, 0, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeaderTableStep);

    }

    /**
     * Construct an object test case from the field in the view
     *
     * @return testCaseFromNew the test case created.
     */
    private TestCase constructTestCase() throws ParseException {

        TestCase testCaseFromNew = new TestCase(
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
        return testCaseFromNew;
    }

    /**
     * Create the object test case linked to his set of test steps, which each
     * steps linked to the his set of script.
     */
    private void createUpdateTestCase() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        ObservableList<StepLineTableStepController> observableTestStep = controllerTableStep.getCollectionTestStep();
        int numberOfTestStep = observableTestStep.size();
        TestCase thisTestCase = null;

        try {
            thisTestCase = this.constructTestCase();        //Construct a new test case with info inside textarea & Combo boxes.
        } catch (ParseException ex) {
            Logger.getLogger(TabTestCaseEditController.class.getName()).error("", ex);
        }
        session.save(thisTestCase);
        session.beginTransaction().commit();        //This can generate a new ID for reference.
        for (int i = 0; i < numberOfTestStep; i++) {
            StepLineTableStepController current = observableTestStep.get(i);
            TestStep step = new TestStep(current.getTestStep());
            step.setStepOrder((byte) i);
            step.setTestCase(thisTestCase); //At this stage, the problem is TestStep's ScriptHasBeenConfigured Object is not yet saved. As well as TestStepHasScripts
            step.setTestStepHasScripts(new HashSet<>());    //Cuz the TestStephasSripts is not yet saved.
            session.save(step);
            session.beginTransaction().commit();

            System.out.println("StepID: " + step.getIdtestStep() + " , Steporder: " + step.getStepOrder());
            if (thisTestCase != null) {
                thisTestCase.addStep(step);
            }
            int numberOfScript = current.getCollectionScript().size();
            int executionOrderScript = 0;
            //System.out.println("NUMBER OF SCRIPT = "+numberOfScript);

            for (int j = 0; j < numberOfScript; j++) {
                //This while-loop is for the Step description (Left part of the Test steps). Only consider isScript = 1 case.
                ScriptLineTableStepController currentScript = current.getCollectionScript().get(j);

                if (currentScript.getScriptAction() != null) {
                    currentScript.getScriptAction().setExecutionOrder((byte) executionOrderScript);
                    TestStepHasScript newTSHS = new TestStepHasScript(currentScript.getScriptAction());
                    newTSHS.setTestStep(step);

                    executionOrderScript++;
                    //System.out.println("ScriptAction, TSHS ID: " + currentScript.getScriptAction().getIdtestStepHasScript());

                    step.addTestStephasScript(newTSHS);

                    Iterator<ScriptHasBeenConfigured> itScriptHBC = currentScript.getScriptAction().getScriptHasBeenConfigureds().iterator();
                    Set<ScriptHasBeenConfigured> setSHBC = new HashSet<>();
                    while (itScriptHBC.hasNext()) {
                        ScriptHasBeenConfigured nextSHBC = new ScriptHasBeenConfigured(itScriptHBC.next(), newTSHS);
                        setSHBC.add(nextSHBC);
                    }
                    newTSHS.setScriptHasBeenConfigureds(setSHBC);
//                    session.save(newTSHS);
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
                }
            }
        }
        try {
            session.beginTransaction().commit();        //Cause TestStepHasScript exception (need to save it before commit). 
        } catch (TransientObjectException ex) {     //Need to close the session regardless the exception occured.
            Logger.getLogger(TabTestCaseEditController.class.getName()).error("", ex);
        }
        session.close();
    }

    /**
     *
     */
    public void closeAlert() {
        try {
            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent t) {
                    alert.close();
                }
            });
            alert.close();
        } catch (Exception e) {
            System.out.println("Exception close alert = " + e);
        }
    }

    /**
     *
     */
    public void alertBox2() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Please wait");
        alert.setHeaderText("Please wait until the edition is finished.");
        Node NOKButton = alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0));
        NOKButton.setVisible(false);
        alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent t) {
                t.consume();
            }
        });
        alert.show();
    }

    /**
     *
     * @return
     */
    public AnchorPane getAnchorPane() {
        return this.anchorPanelNewTestCase;
    }
}
