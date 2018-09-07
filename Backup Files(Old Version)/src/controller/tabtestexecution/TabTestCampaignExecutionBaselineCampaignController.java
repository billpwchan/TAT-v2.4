/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestexecution;

import DB.Iterations;
import DB.ScriptHasBeenConfigured;
import DB.TestCampaign;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import DBcontroller.ConfigurationDB;
import DBcontroller.TestCaseDB;
import configuration.settings;
import controller.popup.PopUpcaseExcelValidationController;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import controller.tabtestcase.TabTestCaseNewController;
import controller.util.CommonFunctions;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import model.initColumn;
import model.util;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class For Baseline Creation ====> Validate campaign steps and
 * generate validated baseline.
 *
 * @author tmorin
 */
public class TabTestCampaignExecutionBaselineCampaignController implements Initializable {

    @FXML
    private AnchorPane anchorPanel;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane gridPaneCampaignView;
    @FXML
    private GridPane gridPaneLabel;
    @FXML
    private Label labelUserID;
    @FXML
    private Label labelName;
    @FXML
    private Label labelSystem;
    @FXML
    private Label labelWriter;
    @FXML
    private Label labelVersion;
    @FXML
    private Label labelCreationDate;
    @FXML
    private Label labelEditionDate;
    @FXML
    private Label labelSUTRelease;
    @FXML
    private Label labelNumberOf;
    @FXML
    private Label labelRegressionThread;
    @FXML
    private Label labelWriterMail;
    @FXML
    private Label labelRequirements;
    @FXML
    private Label labelComments;
    @FXML
    private TextField jtextfieldUserID;
    @FXML
    private TextField jtextfieldName;
    @FXML
    private TextField jtextfieldSystem;
    @FXML
    private TextField jtextfieldWriter;
    @FXML
    private TextField jtextfieldVersion;
    @FXML
    private TextField jtextfieldCreation;
    @FXML
    private TextField jtextfieldEdition;
    @FXML
    private TextField jtextfieldSUTRelease;
    @FXML
    private TextField jtextfieldNumberCases;
    @FXML
    private TextField jtextfieldWriterEmail;
    @FXML
    private ListView requirementsList;
    @FXML
    private TextArea jtextareaComments;
    @FXML
    private CheckBox CheckboxRegressionThread;
    @FXML
    private Text textTestCaseAddCampaign;
    @FXML
    private TreeTableView treeTableViewCases;
    @FXML
    private TableView<TestCase> tableViewCases;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private final TableView<ObservableList<StringProperty>> tableViewExcelFile = new TableView<>();
    @FXML
    private Button validTestCase;
    @FXML
    private TextField baselineName;
    @FXML
    private Button validConfiguration;
    @FXML
    private Text validOtherCasesBefore;
    @FXML
    private Text alreadyValidated;
    @FXML
    private Label labelBaselineName;

    private final ObservableList<TestCase> observableListTestCases = FXCollections.observableArrayList();

    private TestCase selected;

    /**
     *
     */
    public TabTestCampaignExecutionMainViewController main;

    private TestCampaign campaignToBaseline = new TestCampaign();

    private final TestCaseDB testCaseHandler = new TestCaseDB();

    private String BaselineName;

    private Stage dialogStage;

    /**
     *
     */
    public boolean save;

    private TableStepScriptCreationController controllerTableStep;

    private int range;

    private Iterations baseline;

    private String sheetNumber;

    private int numberOfCases = 0;

    private boolean baselineNameInput = true;

    private int numberValidatedCase;

    private int indexCaseSelected = 0;

    Task<Void> task;

    /**
     *
     */
    public static Stage dialog;

    /**
     *
     */
    public static Alert alert;

    private HeaderTableStepController controllerHeaderTableStep;

    Thread th;

    private String excelLocationInstantiation = null;

    private String excelCategoryInstantiation = null;

    private PopUpcaseExcelValidationController popUpChooseExcel;

    private Stage instantiateCase;

    private boolean closePopUp = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        prepareDisplayButtonsAndLabels();

        //Baseline Name will not be changed after validate one test case. Set Text Field to disabled.
        this.baselineName.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (selected != null) {
                        this.manageButtonFromBaselineNameListener(newValue);
                    }
                }
                );

        //If user decides to skip this one test case, pop-up this window to notify.
        this.tableViewCases.getSelectionModel()
                .selectedItemProperty().addListener((ObservableValue<? extends TestCase> observable, TestCase oldValue, TestCase newValue) -> {
                    //System.out.println("NEW VALUE = " + newValue + "OLD VALUE = " + oldValue);
                    if (newValue != null && newValue != oldValue && oldValue != null && oldValue.getSimpleStringConfiguredProperty().equals("NConfigured")) {
                        Alert dlg = new Alert(AlertType.WARNING);
                        dlg.setTitle("You do want dialogs right?");
                        dlg.getDialogPane().setContentText("I was a bit worried that you might not want them, so I wanted to double check.");
                        dlg.showAndWait();
                    }
                    selected = newValue;
                    indexCaseSelected = observableListTestCases.indexOf(selected);
                    testCaseHandler.getAllFromCase(selected);
                    controllerTableStep.displayScriptAndStepBaseline(selected);
                    this.manageButtonFromTableViewCase(newValue);
                }
                );

        validConfiguration.setOnAction(
                (ActionEvent event) -> {
                    try {
                        this.main.updateBaselineTree();
                    } catch (ParseException ex) {
                        Logger.getLogger(TabTestCampaignExecutionBaselineCampaignController.class.getName()).error("", ex);
                    }
                    TabTestCampaignExecutionRepositoryBaselineController execController = new TabTestCampaignExecutionRepositoryBaselineController();
                    execController.runCampaign(BaselineName);
                    this.main.closeTab(this.campaignToBaseline.getIdtestCampaign());
                }
        );

        validTestCase.setOnAction(
                (ActionEvent event) -> {
                    final ConfigurationDB configDB = new ConfigurationDB();
                    File excelFile;
                    boolean excelChoose = false;
                    if (!controllerTableStep.isFullyConfigured()) {
                        CommonFunctions.displayAlert(AlertType.WARNING, "Case not fully configured", null, "Please configure the entire testCase before validate");
                        event.consume();
                    } else {
                        if (!this.baselineName.isDisable() && !this.verifyBaselineExistence(this.baselineName.getText())) {
                            BaselineName = this.baselineName.getText();
                            baseline = configDB.createBaseline(BaselineName, campaignToBaseline);
                            baselineName.setDisable(true);
                        }
                        if (this.baselineName.isDisable()) {
                            if (isExcelNeeded(selected)) {
                                excelFile = selectExcelFile();
                                if (excelFile != null) {
                                    excelChoose = true;
                                    this.storeExcel(excelFile);
                                }
                            } else {
                                excelFile = null;
                                range = -1;
                                sheetNumber = null;
                                excelChoose = true;
                            }
                            if (excelChoose == true) {
                                alertBox2();
                                //Connect to Database for operation. Intended to add an additional layer of validation upfront.
                                try {
                                    util.startTime();
                                    try {
                                        numberOfCases = configDB.configureTestCase(baseline, selected, excelFile, range, sheetNumber, numberOfCases, excelCategoryInstantiation, excelLocationInstantiation);
                                    } catch (NullPointerException ex) {
                                        CommonFunctions.debugLog.error("Cannot find the specified sheet in Excel File", ex);
                                        throw ex;
                                    } catch (Exception ex) {
                                        CommonFunctions.debugLog.error("General Exception when opening Excel File", ex);
                                        throw ex;
                                    }
                                    util.endTime();
                                    th = new Thread(task);
                                    th.setDaemon(true);
                                    th.start();
                                    try {
                                        th.join();
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(TabTestCampaignExecutionBaselineCampaignController.class.getName()).error("", ex);
                                    }
                                    closeAlert(alert);
                                    //this.notificationBaselinCase();
                                    if (numberOfCases != -1) {
                                        this.selected.setConfigured("configured");
                                        this.setButtonAndLabelsVisible(false, true, true);
                                        validConfiguration(numberValidatedCase++);  //Enable ValidateBaseline button when consists enough cases
                                        controllerTableStep.disableConfiguration();
                                        numberOfCases = 0;
                                    } else {
                                        numberOfCases = 0;  //To restore numOfCases back to origina state (If exception is encountered)
                                    }
                                } catch (Exception ex) {
                                    CommonFunctions.displayAlert(AlertType.ERROR, "Exeption Found when opening Excel File",
                                            "Invalid Excel Configuration", "Please refer to the log to adjust Excel Configurations");
                                }
                            }
                        } else {
                            event.consume();
                        }

                    }
//            if (CaseID != -1 && exceFile != null) {
//                ConfigurationDB configDB = new ConfigurationDB();
//                configDB.configureTestCase(campaignToBaseline, testCases, params, this.BaselineName);
//                selected.setHasBeenConfiguredCase(true);
//                TreeItem testcase = new TreeItem<>(selected);
//                configured.getChildren().add(testcase);
//            }
                }
        );
        constructTableStep();

        loadCSS();
    }

    /**
     * initialize the reference of the class parent
     * TabTestCampaignExecutionMainViewController to this controller.
     *
     * @param mainController the reference to the parent class
     */
    void init(TabTestCampaignExecutionMainViewController mainController
    ) {
        this.main = mainController;

    }

    /**
     * Construct all the information to display in the tab view
     *
     * @param testCampaign the campaign to display
     */
    void constructInformation(TestCampaign testCampaign
    ) {
        this.jtextfieldUserID.setText(testCampaign.getReference());
        this.jtextfieldUserID.setId("displayStyle");
        this.jtextfieldSystem.setText(testCampaign.getSystem());
        this.jtextfieldSystem.setId("displayStyle");
        this.jtextfieldWriter.setText(testCampaign.getWritter());
        this.jtextfieldWriter.setId("displayStyle");
        this.jtextfieldVersion.setText(String.valueOf(testCampaign.getCampaignVersion()));
        this.jtextfieldVersion.setId("displayStyle");
        this.jtextfieldCreation.setText(testCampaign.getCreationDate());
        this.jtextfieldCreation.setId("displayStyle");
        this.jtextfieldEdition.setText(testCampaign.getEditionDate());
        this.jtextfieldEdition.setId("displayStyle");
        this.jtextfieldSUTRelease.setText(testCampaign.getSoftwareSutRelease());
        this.jtextfieldSUTRelease.setId("displayStyle");
        this.jtextfieldNumberCases.setText(String.valueOf(testCampaign.getNumberTestCase()));
        this.jtextfieldNumberCases.setId("displayStyle");
        this.CheckboxRegressionThread.setSelected(testCampaign.getRegressionThread() != 0);
        this.CheckboxRegressionThread.setDisable(true);
        this.jtextfieldWriterEmail.setText(testCampaign.getWriterEmail());
        this.jtextfieldWriterEmail.setId("displayStyle");
        this.jtextareaComments.setId("areaStyle");
        this.jtextareaComments.setText(testCampaign.getComments());
        this.campaignToBaseline = testCampaign;
        this.PrepareDisplayConfiguration(testCampaign);
    }

    /**
     * Prepare the view of the table of step
     */
    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPane.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 1, 1, 3, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPane.add(paneTest, 1, 0, 3, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeaderTableStep);

    }

    /**
     * verify if an excelFile is needed in a case
     *
     * @param testCaseToCheck
     * @return a boolean found=1 if an excel file is required
     */
    public boolean isExcelNeeded(TestCase testCaseToCheck) {
        String splitOn = ((char) 007) + "";
        boolean found = false;

        Iterator<TestStep> itSteps = testCaseToCheck.getTestSteps().iterator();
        while (itSteps.hasNext() && found == false) {
            TestStep testStep = itSteps.next();
            Iterator<TestStepHasScript> itTestStepHasScript = testStep.getTestStepHasScripts().iterator();
            while (itTestStepHasScript.hasNext() && found == false) {
                TestStepHasScript testStepHasScriptObject = itTestStepHasScript.next();
                Iterator<ScriptHasBeenConfigured> itScHasBeenConfigured = testStepHasScriptObject.getScriptHasBeenConfigureds().iterator();
                while (itScHasBeenConfigured.hasNext() && found == false) {
                    ScriptHasBeenConfigured ScHasBeenConfigured = itScHasBeenConfigured.next();
                    if (ScHasBeenConfigured.getValuePath().equals("Excel file")) {
                        found = true;
                        String[] paramsForExcel = ScHasBeenConfigured.getValue().split(splitOn);
                        //range = Integer.parseInt(paramsForExcel[4]);
                        sheetNumber = paramsForExcel[1] + paramsForExcel[2];
                    }
                }
            }
        }
        return found;
    }

    /**
     * verify if the baseline already exists for the campaign selected
     *
     * @param baselineName the name of the baseline to check
     * @return if a baseline exists
     */
    public boolean verifyBaselineExistence(String baselineName) {
        ConfigurationDB configurationHandler = new ConfigurationDB();
        boolean exist = false;
        long count;
        BaselineName = this.baselineName.getText();
        count = configurationHandler.checkConfigurationExistence(baselineName);
        if (count != 0) {
            CommonFunctions.displayAlert(AlertType.ERROR, "Duplicate Baseline Found", "Baseline Name already used", "Please choose an other name/ID for the Baseline (baseline ID already exists)");
            exist = true;
        }
        return exist;
    }

    /**
     * open a dialog box to tell the user to select an excel file
     *
     * @return the excel file
     */
    public File selectExcelFile() {
        File excelFile = null;
        showWizard();
        if (closePopUp) { // was if (closePopUp = true) ---->making it always true
            save = false;
            excelFile = FileChooser(save);
            //popUpChooseExcel
        }

        this.closePopUp = false;
        return excelFile;
    }

    /**
     * open a file chooser in oder to chose an excel file
     *
     * @param save a boolean to choose if open a file saver or a file chooser
     * @return the excel file
     */
    private File FileChooser(boolean save) {
        File selectedFile;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Excel Files", "*.xls"),
                new ExtensionFilter("Excel Files (New Ver.)", "*.xlsx")
        );
        if (save == false) {
            selectedFile = fileChooser.showOpenDialog(dialogStage);
        } else {
            selectedFile = fileChooser.showSaveDialog(dialogStage);
        }
        return selectedFile;
    }

    /**
     * change the color of the label put in parameter regarding the value of
     * newValue if new value is empty, label will be displayed in red. Otherwise
     * the label will be displayed in black
     *
     * @param label the label for which change the color
     * @param newValue the string to check
     */
    private void changeColorLabel(Label label, Boolean empty) {
        if (empty) {
            label.setTextFill(Color.RED);
        } else {
            label.setTextFill(Color.BLACK);
        }
    }

    /**
     * decide if enable or disable the button to valid the configuration
     *
     * @param numberCase the number of case to configure for this campaign
     */
    private void validConfiguration(int numberCase) {
        if (numberCase == observableListTestCases.size() - 1) {
            validConfiguration.setDisable(false);
        }
    }

    /**
     * get case to configure in the selected campaign
     *
     * @param testCampaign test campaign to configure
     * @return an arrayList of testCase to configure in the campaign
     */
    public ArrayList<TestCase> getCasesToConfigure(TestCampaign testCampaign) {
        ArrayList<TestCase> testCases = testCaseHandler.getTestCasesFromTestCampaign(testCampaign);
        //ArrayList<TestCase> testCasesDifferent = new ArrayList<>();
        testCases.stream().forEach((testCase) -> {
            testCase.setConfigured("NConfigured");
        });
        return testCases;
    }

    /**
     * Prepare the display of the view
     *
     * @param testCampaign the test campaign to baseline
     */
    public void PrepareDisplayConfiguration(TestCampaign testCampaign) {
        observableListTestCases.setAll(this.getCasesToConfigure(testCampaign));
        tableViewCases.setItems(observableListTestCases);
        this.tableViewCases.getSelectionModel().select(observableListTestCases.get(0));
        TestCase firstCase = observableListTestCases.get(0);
        testCaseHandler.getAllFromCase(firstCase);
        //testCaseAndSteps.setStepAndScripts(testCaseHandler.getAllFromCase(observableListTestCases.get(0).getIdtestCase()));
        //testCaseAndSteps.setTestCase(observableListTestCases.get(0));
        //controllerTableStep.addStepAndScriptBaseline(testCaseAndSteps);
        initColumn initColumnCase = new initColumn();
        initColumnCase.initColumnCaseForBaseline(tableViewCases);
    }

    /**
     * Prepare the display of the buttons and labels of the view
     */
    public void prepareDisplayButtonsAndLabels() {

        validConfiguration.setDisable(true);
        validOtherCasesBefore.setVisible(false);
        alreadyValidated.setVisible(false);
        validTestCase.setDisable(true);
        changeColorLabel(labelBaselineName, true);
    }

    /**
     * Load the CSS sheet for the view
     */
    public void loadCSS() {
        this.anchorPanel.getStylesheets().add("/view/testexecution/cssLibraryTestCase.css");
    }

    /**
     * Set the buttons and label visibility
     *
     * @param validOtherCasesLabel if enable the label "COnfigure other cases
     * before"
     * @param alreadyValidatedLabel if enable the label "Already validated"
     * @param buttonValid if enable the button valid test case
     */
    public void setButtonAndLabelsVisible(boolean validOtherCasesLabel, boolean alreadyValidatedLabel, boolean buttonValid) {
        validOtherCasesBefore.setVisible(validOtherCasesLabel);
        //alreadyValidated.setVisible(alreadyValidatedLabel);
        validTestCase.setDisable(buttonValid);
    }

    /**
     * method if the user close the tab without validate the baseline
     */
    public void closeWithoutValid() {
        if (observableListTestCases.get(0).getSimpleStringConfiguredProperty().equals("configured")) {
            ConfigurationDB configurationHandler = new ConfigurationDB();
            configurationHandler.deleteConfiguration(baseline);
            this.deleteFolderConfiguration();
        }
    }

    /**
     * Manage the button and labels from the listener of the baseline name
     *
     * @param newValue the name of the baseline
     */
    private void manageButtonFromBaselineNameListener(String newValue) {
        if (newValue.trim().isEmpty()) {
            setButtonAndLabelsVisible(false, false, true);
            baselineNameInput = newValue.trim().isEmpty();
            changeColorLabel(labelBaselineName, newValue.trim().isEmpty());
        } else if (indexCaseSelected != 0 && observableListTestCases.get(indexCaseSelected - 1).getSimpleStringConfiguredProperty().equals("NConfigured")) {
            setButtonAndLabelsVisible(true, false, true);
            baselineNameInput = false;
            changeColorLabel(labelBaselineName, newValue.trim().isEmpty());
        } else {
            validTestCase.setDisable(false);
            baselineNameInput = false;
            changeColorLabel(labelBaselineName, newValue.trim().isEmpty());
        }
    }

    /**
     * Manage the button and labels from the listener of table case
     *
     * @param newValue the current selected test case
     */
    private void manageButtonFromTableViewCase(TestCase newValue) {
        if ((indexCaseSelected == 0 || observableListTestCases.get(indexCaseSelected - 1).getSimpleStringConfiguredProperty().equals("configured")) && baselineNameInput == false && selected.getSimpleStringConfiguredProperty().equals("NConfigured")) {
            setButtonAndLabelsVisible(false, false, false);
        } else if (selected.getSimpleStringConfiguredProperty().equals("configured")) {
            setButtonAndLabelsVisible(false, true, true);
        } else if (indexCaseSelected != 0 && !observableListTestCases.get(indexCaseSelected - 1).getSimpleStringConfiguredProperty().equals("configured")) {
            setButtonAndLabelsVisible(true, false, true);
        } else if (baselineNameInput == true) {
            setButtonAndLabelsVisible(false, false, true);
        } else {
            validTestCase.setDisable(true);
        }
    }

    /**
     *
     */
    public void alertBox2() {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Please wait");
        alert.setHeaderText("Please wait until the complete validation of your test case");
        Node NOKButton = alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0));
        NOKButton.setVisible(false);
        alert.setOnCloseRequest((DialogEvent t) -> {
            if (th.getState() != Thread.State.TERMINATED) {
                t.consume();
            } else {
                alert.close();
            }
        });
        alert.show();
    }

    /**
     *
     * @param alert
     */
    public static void closeAlert(Alert alert) {
        try {
            alert.close();
            alert.hide();
        } catch (Exception e) {
            System.out.println("Exception close alerte = " + e);
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    public static void errorBox(int x, int y) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("The cell x= " + x + " y= " + y + " has a problem");
        alert.setContentText("Click on OK, check your excel File and try again");
        alert.showAndWait();
    }

    /**
     *
     * @param errorTitle
     * @param errorHeaderText
     * @param errorMessage
     */
    public static void errorBox(String errorTitle, String errorHeaderText, String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(errorTitle);
        alert.setHeaderText(errorHeaderText);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /**
     *
     */
    public void notificationBaselinCase() {
        Notifications notificationBuilder = Notifications.create()
                .title("Baseline")
                .text("Test Case validation complete")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
//                .onAction((ActionEvent arg0) -> {
//                    //System.out.println("Notification clicked on!");
//                });
        notificationBuilder.showInformation();
    }

    /**
     *
     */
    public void notificationBaseline() {
        Notifications notificationBuilder = Notifications.create()
                .title("Baseline")
                .text("BaseLine validation complete")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notificationBuilder.showInformation();
    }

    /**
     *
     * @return
     */
    public int getRange() {
        return this.range;
    }

    /**
     *
     * @return
     */
    public String getSheetNumber() {
        return this.sheetNumber;
    }

    /**
     *
     * @return
     */
    public Alert getAlert() {
        return TabTestCampaignExecutionBaselineCampaignController.alert;
    }

    private void storeExcel(File excelFile) {
        boolean success = (new File(settings.scriptsPaht + "\\" + baseline.getTestCampaign().getReference() + "\\" + baseline.getBaselineId())).mkdirs();
        if (success) {
            Path pathToCopy = Paths.get(settings.scriptsPaht + "\\" + baseline.getTestCampaign().getReference() + "\\" + baseline.getBaselineId() + "\\" + excelFile.toPath().getFileName());
            try {
                Files.copy(excelFile.toPath(), pathToCopy);
            } catch (IOException ex) {
                Logger.getLogger(TabTestCampaignExecutionBaselineCampaignController.class.getName()).error("", ex);
            }
        }
    }

    /**
     *
     */
    public void deleteFolderConfiguration() {
        File index = new File(settings.scriptsPaht + "\\" + baseline.getTestCampaign().getReference() + "\\" + baseline.getBaselineId());
        String[] entries = index.list();
        if (entries != null) {
            for (String s : entries) {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            }
            index.delete();
        }
    }

    private void showWizard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane instantiatePane = fxmlLoader.load(getClass().getResource("/view/popup/popUpcaseExcelValidation.fxml").openStream());
            popUpChooseExcel = (PopUpcaseExcelValidationController) fxmlLoader.getController();
            instantiateCase = new Stage();
            instantiateCase.setTitle("Value Instantiation");
            instantiateCase.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(instantiatePane);
            instantiateCase.setScene(scene);
            instantiateCase.setResizable(false);
            instantiateCase.initOwner(Main.primaryStage);
            //popUpInstantiateController.setPrimaryStage(instantiateCase);
            popUpChooseExcel.init(this);
            popUpChooseExcel.start();
            instantiateCase.showAndWait();
            instantiateCase.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - instantiateCase.getWidth() / 2);
            instantiateCase.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - instantiateCase.getHeight() / 2);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCampaignExecutionBaselineCampaignController.class.getName()).error("", ex);
        }
    }

    /**
     *
     * @param excelLocationInstantiation
     */
    public void setExcelLocationInstantiation(String excelLocationInstantiation) {
        this.excelLocationInstantiation = excelLocationInstantiation;
    }

    /**
     *
     * @param excelCategoryInstantiation
     */
    public void setExcelCategoryInstantiation(String excelCategoryInstantiation) {
        this.excelCategoryInstantiation = excelCategoryInstantiation;
    }

    /**
     *
     * @param range
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     *
     */
    public void setOnAction() {
        this.instantiateCase.close();
        this.closePopUp = true; //added this to get ExcelFile when user presses "ButtonOK" in PopUpExcelValidationController
    }

    /**
     *
     */
    public void cancelPopUp() {
        this.instantiateCase.close();
        this.closePopUp = false; //was true before, changing this to false and adding true for the OK button

        System.out.println("CancelPopUp was accessed and closePopup=false");
        System.out.println("close popUp in void cancelPopUp = " + this.closePopUp);
    }
}
