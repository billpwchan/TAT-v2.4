package controller.tabtestcase;

import main.Main;
import DB.ScriptHasBeenConfigured;
import DB.TestCase;
import DB.TestStep;
import DBcontroller.TestCaseDB;
import DBcontroller.sessionFactorySingleton;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.ScriptLineTableStepController;
import controller.tablestep.StepLineTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogEvent;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.ObjectCopy;
import model.setCursorOnComponent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.GenericJDBCException;

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
    final int textfieldCaseTitleMaxLength = 30;
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
        // TODO
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
                if (displayWarningIncorrectInputFormat("Case ID", textfieldCaseIDMaxLength, newValue.length() > textfieldCaseIDMaxLength)) {
                    jtextfieldCaseIDEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldTestCategoryCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelTestCategoryCaseNew, newValue);
                if (displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                    jtextfieldTestCategoryCaseEdit.setText(oldValue);
                }
            }

        });
        this.jtextfieldTestEnvironementCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelTestEnvironementCaseNew, newValue);
                if (displayWarningIncorrectInputFormat("Test Environement", textfieldTestEnvironementMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                    jtextfieldTestEnvironementCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldCaseTitleEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Test Title", textfieldCaseTitleMaxLength, newValue.length() > textfieldCaseTitleMaxLength)) {
                    jtextfieldCaseTitleEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldCaseVersionEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Test Case Version", textfieldCaseVersionMaxLength, newValue.length() > textfieldCaseVersionMaxLength)) {
                    jtextfieldCaseVersionEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldProjectCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Project", textfieldProjectMaxLength, newValue.length() > textfieldProjectMaxLength)) {
                    jtextfieldProjectCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldTypeTestCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Project", textfieldTypeTestMaxLength, newValue.length() > textfieldTypeTestMaxLength)) {
                    jtextfieldTypeTestCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldTestCategoryCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                changeColorLabel(labelTestCategoryCaseNew, newValue);
                if (displayWarningIncorrectInputFormat("Test Category", textfieldTestCategoryMaxLength, newValue.length() > textfieldTestCategoryMaxLength)) {
                    jtextfieldTestCategoryCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldLocationCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Location", textfieldLocationMaxLength, newValue.length() > textfieldLocationMaxLength)) {
                    jtextfieldLocationCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldWriterCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Writer", textfieldWriterMaxLength, newValue.length() > textfieldWriterMaxLength)) {
                    jtextfieldWriterCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldWriterEmailCaseEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Writer Email", textfieldWriterEmailMaxLength, newValue.length() > textfieldWriterEmailMaxLength)) {
                    jtextfieldWriterEmailCaseEdit.setText(oldValue);
                }
            }
        });
        this.jtextfieldCaseSourceEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                if (displayWarningIncorrectInputFormat("Case Source", textfieldCaseSourceMaxLength, newValue.length() > textfieldCaseSourceMaxLength)) {
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
            controllerTableStep.configurationOnForStep();
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
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerTableStep = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneTableStep.add(paneTest, 0, 0, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TabTestCaseEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < numberOfTestStep; i++) {
            StepLineTableStepController current = observableTestStep.get(i);
            TestStep step = current.getTestStep();
            step.setStepOrder((byte) i);
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
                    executionOrderScript++;
                    //System.out.println("ScriptAction, TSHS ID: " + currentScript.getScriptAction().getIdtestStepHasScript());
                    step.addTestStephasScript(currentScript.getScriptAction());

//                    Iterator<ScriptHasBeenConfigured> itScriptHBC = currentScript.getScriptAction().getScriptHasBeenConfigureds().iterator();
//                    while (itScriptHBC.hasNext()) {
//                        currentScript.getScriptAction().setScriptHasBeenConfigureds(itScriptHBC.next());
//                        System.out.println("SCRIPT HAS BEEN CONFIGURED :" + itScriptHBC.next().getTestStepHasScript());
//                        session.save(itScriptHBC.next().getTestStepHasScript());
//                    }
                    System.out.println("Script has been configured set : " + currentScript.getScriptAction().getScriptHasBeenConfigureds());
                    System.out.println("Number of parameters : " + currentScript.getScriptAction().getScriptHasBeenConfigureds().size());
                    System.out.println("currentScript = " + currentScript.getScriptAction().getScript().getName());
                }
                //This while-loop is for Expected result (Right part of the Test steps). Onlye consider isMacro = 1 case.
                if (currentScript.getScriptVerif() != null) {
                    currentScript.getScriptVerif().setExecutionOrder((byte) executionOrderScript);
                    executionOrderScript++;
                    //System.out.println("ScriptVerif, TSHS ID: " + currentScript.getScriptAction().getIdtestStepHasScript() + currentScript.getScriptAction());
                    step.addTestStephasScript(currentScript.getScriptVerif());
                    System.out.println("ID TEST STEP HAS SCRIPT = " + currentScript.getScriptVerif().getIdtestStepHasScript());
//                    Iterator<ScriptHasBeenConfigured> itScriptHBC = currentScript.getScriptVerif().getScriptHasBeenConfigureds().iterator();
//                    while (itScriptHBC.hasNext()) {
//                        System.out.println("SCRIPT HAS BEEN CONFIGURED :" + itScriptHBC.next().getTestStepHasScript());
//                        session.save(itScriptHBC.next().getTestStepHasScript());
//                    }
                    System.out.println("Script has been configured set : " + currentScript.getScriptVerif().getScriptHasBeenConfigureds());
                    System.out.println("Number of parameters : " + currentScript.getScriptVerif().getScriptHasBeenConfigureds().size());
                    System.out.println("currentScript = " + currentScript.getScriptVerif().getScript().getName());
                }
//                session.saveOrUpdate(step);
            }
//            session.save(step);
//            session.evict(step);

        }
        //Trying to overcome TransientObjectException. Need to save DB.TestStepHasScript before saving thisTestCase.
//        Iterator<TestStep> itTS = thisTestCase.getTestSteps().iterator();
//        while(itTS.hasNext()){
//            TestStep setTestStep= itTS.next();
//            Iterator<TestStepHasScript> itTSHS = setTestStep.getTestStepHasScripts().iterator();
//            while(itTSHS.hasNext()){
//                TestStepHasScript setTSHS = itTSHS.next();
//                session.save(setTSHS);
//            }
//        }
        try {
            session.save(thisTestCase);
            session.beginTransaction().commit();        //Cause TestStepHasScript exception (need to save it before commit). 
        } catch (TransientObjectException ex) {     //Need to close the session regardless the exception occured.
            Logger.getLogger(TabTestCaseEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.close();

//                ObservableList<StepLineTableStepController> observableTestStep = controllerTableStep.getCollectionTestStep();
//        int numberOfTestStep = observableTestStep.size();
//        TestCase thisTestCase = constructTestCase();
//        for (int i = 0; i < numberOfTestStep; i++) {
//            StepLineTableStepController current = observableTestStep.get(i);
//            TestStep step = current.getTestStep();
//            step.setStepOrder((byte) i);
//            thisTestCase.addStep(step);
//            int numberOfScript = current.getCollectionScript().size();
//            int executionOrderScript = 0;
//
//            for (int j = 0; j < numberOfScript; j++) {
//                ScriptLineTableStepController currentScript = current.getCollectionScript().get(j);
//                if (currentScript.getScriptAction() != null) {
//                    currentScript.getScriptAction().setExecutionOrder((byte) executionOrderScript);
//                    executionOrderScript++;
//                    step.addTestStephasScript(currentScript.getScriptAction());
//
//                }
//                if (currentScript.getScriptVerif() != null) {
//                    currentScript.getScriptVerif().setExecutionOrder((byte) executionOrderScript);
//                    executionOrderScript++;
//                    step.addTestStephasScript(currentScript.getScriptVerif());
//                }
//            }
//
//        }
//        ObservableList<StepLineTableStepController> observableTestStep = controllerTableStep.getCollectionTestStep();
//        ObservableList<ScriptLineTableStepController> observableScript;
//        int numberOfTestStep = observableTestStep.size();
//
//        SessionFactory factory = sessionFactorySingleton.getInstance();
//        Session session = factory.openSession();
//        Transaction tx;
//        HashSet setStep = new HashSet();
//        HashSet setStepHasScript = new HashSet();
//        HashSet setScrHasBeenConfigured = new HashSet();
//        setScrHasBeenConfigured.clone()
//
//        //Loop on all test step
//        for (int i = 0; i < numberOfTestStep; i++) {
//            StepLineTableStepController current = observableTestStep.get(i);
//            observableScript = current.getCollectionScript();
//
//            //Create test step number i
//            TestStep step = new TestStep(current.getTestStep());
//            System.out.println("Check is :" + current.getTestStep().getHumanCheck());
//            System.out.println("Stimuli is :" + current.getTestStep().getHumanStimuli());
//            step.setStepOrder((byte) i);
//            step.setCreationDate(new java.util.Date());
//            setStep.add(step);
//            //Add it to the collection
//            //Create arraylist of testStepHasScriptID for all of the script in the next step
//            HashSet setScript = new HashSet();
//            //Loop on all script of this test step
//            byte executionOrderScript = 0;
//            for (ScriptLineTableStepController observableScript1 : observableScript) {
//                // System.out.println("Is script null " + observableScript1.getScriptAction());
//                if (observableScript1.getScriptAction() != null) {
//                    Script testScriptTGetFromDB = (Script) session.get(Script.class, observableScript1.getScriptAction().getScript().getIdScript());
//                    //testScriptTGetFromDB = 
//                    tx = session.beginTransaction();
//                    //TestStepHasScriptId stepHasScriptID = new TestStepHasScriptId(observableScript1.getScriptAction().getIdScript(), step.getIdtestStep(), executionOrderScript, true);
//                    TestStepHasScript stepHasScript = new TestStepHasScript(testScriptTGetFromDB, step, executionOrderScript);
//                    setScript.add(stepHasScript);
//                    session.save(stepHasScript);
//                    tx.commit();
//                    executionOrderScript++;
//                    HashSet setConf = new HashSet();
//                    for (int index = 0; index < observableScript1.getParamControllerAction().getParams().size(); index++) {
//                        paramsForScript params = observableScript1.getParamControllerAction().getParams().get(index);
//                       // if (params.getConfigured()) {
//
//                        //System.out.println("Script ID :" + stepHasScript.getScript().getIdScript() + "Script ID in ID :" + stepHasScript.getId().getScriptIdScript());
//                        //System.out.println("Test Step ID :" + stepHasScript.getTestStep().getIdtestStep() + "Test Step ID in ID :" + stepHasScript.getId().getTestStepIdtestStep());
//                        paramsForScript currentParamForScript = observableScript1.getParamControllerAction().getParams().get(index);
//                        tx = session.beginTransaction();
//                        //ScriptHasParameters script = new ScriptHasParameters();
//                        ParametersDB paramHandler = new ParametersDB();
//                        ArrayList<ScriptHasParameters> ParametersFromScript = paramHandler.getParametersFromScript(params.getScript().getIdScript());
//                        ScriptHasParametersConfiguredId test2 = new ScriptHasParametersConfiguredId(ParametersFromScript.get(index), stepHasScript);
//                        ScriptHasParametersConfigured shpc = new ScriptHasParametersConfigured(test2, ParametersFromScript.get(index), stepHasScript,
//                                currentParamForScript.getConfigured(), currentParamForScript.getValue(), currentParamForScript.getPathToVariable());
////                        for(int k = 0;k<ParametersFromScript.size();k++){
////                            shpc.getScriptHasParameters().getScriptHasParametersConfigureds();
////                        }
//                        //System.out.println("The order is " + shpc.getOrder() + "test parameter value is " + shpc.getValue());
//                        setConf.add(shpc);
//                        session.save(shpc);
//                        tx.commit();
//                        //}
//                    }
//                    stepHasScript.setScriptHasParametersConfigureds(setConf);
//                }
//                if (observableScript1.getScriptrVerif() != null) {
//                    tx = session.beginTransaction();
//                    TestStepHasScriptId stepHasScriptID = new TestStepHasScriptId(observableScript1.getScriptrVerif().getIdScript(), step.getIdtestStep(), executionOrderScript, false);
//                    TestStepHasScript stepHasScript = new TestStepHasScript(stepHasScriptID, observableScript1.getScriptrVerif(), step);
//                    setScript.add(stepHasScript);
//                    session.save(stepHasScript);
//                    tx.commit();
//                    executionOrderScript++;
//                    HashSet setConf = new HashSet();
//                    for (int index = 0; index < observableScript1.getParamControllerVerif().getParams().size(); index++) {
//                        paramsForScript params = observableScript1.getParamControllerVerif().getParams().get(index);
//                        //if (params.getConfigured()) {
//                        paramsForScript currentParamForScript = observableScript1.getParamControllerVerif().getParams().get(index);
//                        tx = session.beginTransaction();
//                        //ScriptHasParameters script = new ScriptHasParameters();
//                        ParametersDB paramHandler = new ParametersDB();
//                        ArrayList<ScriptHasParameters> ParametersFromScript = paramHandler.getParametersFromScript(params.getScript().getIdScript());
//                        ScriptHasParametersConfiguredId test2 = new ScriptHasParametersConfiguredId(ParametersFromScript.get(index), stepHasScript);
//                        ScriptHasParametersConfigured shpc = new ScriptHasParametersConfigured(test2, ParametersFromScript.get(index), stepHasScript,
//                                currentParamForScript.getConfigured(), currentParamForScript.getValue(), currentParamForScript.getPathToVariable());
//                        setConf.add(shpc);
//                        session.save(shpc);
//                        tx.commit();
//                        // }
//                    }
//                    stepHasScript.setScriptHasParametersConfigureds(setConf);
//                }
//
//            }
//            step.setTestStepHasScripts(setScript);
//        }
//        TestCase thisTestCase = new TestCase();
//
//        try {
//            thisTestCase = constructTestCase();
//        } catch (ParseException ex) {
//            Logger.getLogger(TabTestCaseEditController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        thisTestCase.setTestSteps(setStep);
//        tx = session.beginTransaction();
//        session.save(thisTestCase);
//        tx.commit();
//        session.close();
    }

    private boolean displayWarningDeleteCase() {
        boolean ok = false;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Updating this test case will delete all related test campaigns and baselines !");
        alert.setContentText("Do you want to edit ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ok = true;
        }
        return ok;
    }

    private boolean displayWarningIncorrectInputFormat(String fieldName, Integer maxLength, boolean identifier) {
        if (!identifier) {
            return false;
        }
        boolean ok = false;
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning. ");
        alert.setHeaderText("Incorect Input Format in Field \"" + fieldName + "\": ");
        alert.setContentText(fieldName + " exceeds maximum characters allowed (" + maxLength.toString() + " characters). Please use another value, or only part of your input will be recorded.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ok = true;
        }
        return ok;
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
