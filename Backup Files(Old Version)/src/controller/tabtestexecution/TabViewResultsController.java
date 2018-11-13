
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestexecution;

import DB.CaseExecutions;
import DB.Iterations;
import DB.TestCampaign;
import DBcontroller.ResultsDB;
import DBcontroller.TestCampaignDB;
import controller.popup.PopUpChangeCaseCommentController;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import controller.tabtestcase.TabTestCaseNewController;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import model.TestCasesExecution;
import model.initColumn;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author tmorin
 */
public class TabViewResultsController implements Initializable {

    private final initColumn CaseToDisplay = new initColumn();
    @FXML
    private AnchorPane mainAnchorPopUp;
    @FXML
    private GridPane gridPanePopUpCase;
    @FXML
    private TableView<CaseExecutions> tableViewCampaignPopUpRun;
    @FXML
    private AnchorPane anchorPaneSteps;
    @FXML
    private PieChart pieChart;
    @FXML
    private TitledPane tPane;
    @FXML
    private Label labelReferenceCampaignView;
    @FXML
    private Label labelNameCampaignView;
    @FXML
    private Label labelSystemCampaignView;
    @FXML
    private Label labelWriterCampaignView;
    @FXML
    private Label labelVersionCampaignView;
    @FXML
    private Label labelCreationDateCampaignView;
    @FXML
    private Label labelEditionDateCampaignView;
    @FXML
    private Label labelSUTReleaseCampaignView;
    @FXML
    private Label labelNumberOfCaseCampaignView;
    @FXML
    private Label labelRegressionThreadCampaignView;
    @FXML
    private Label labelWriterMailCampaignView;
    @FXML
    private Label labelDescriptionCampaignView;
    @FXML
    private Label labelCommentsCampaignView;
    @FXML
    private TextField jtextfieldReferenceCampaignView;
    @FXML
    private TextField jtextfieldNameCampaignView;
    @FXML
    private TextField jtextfieldSystemCampaignView;
    @FXML
    private TextField jtextfieldWriterCampaignView;
    @FXML
    private TextField jtextfieldVersionCampaignView;
    @FXML
    private TextField jtextfieldCreationDateCampaignView;
    @FXML
    private TextField jtextfieldEditionDateCampaignView;
    @FXML
    private TextField jtextfieldSUTReleaseCampaignView;
    @FXML
    private TextField jtextfieldNumberCasesCampaignView;
    @FXML
    private TextField jtextfieldWriterEmailCampaignView;
    @FXML
    private TextArea jtextareaCommentsCampaignView;
    @FXML
    private CheckBox CheckboxRegressionThreadCampaignView;
    @FXML
    private TableStepScriptCreationController controllerTableStep;
    @FXML
    private Button buttonChangeResults;
    private CaseExecutions testCaseSelected;

    private TabTestCampaignExecutionMainViewController main;

    private int campaignID;

    private String baselineId;

    private ArrayList<CaseExecutions> testCasesToDisplay = new ArrayList();

    private ObservableList<CaseExecutions> testCases = FXCollections.observableArrayList();

    private Iterations iteration;

    private HeaderTableStepController controllerHeader;

    private ObservableList<PieChart.Data> pieChartData;

    private ObservableList<CaseExecutions> casesExecutionModified = FXCollections.observableArrayList();

    private Stage dialogStage;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResultsDB resultHandler = new ResultsDB();
        this.constructTableStep();
        this.iniTitlePane();
        buttonChangeResults.setDisable(true);
        tableViewCampaignPopUpRun.setTableMenuButtonVisible(true);
        tableViewCampaignPopUpRun.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //CaseToDisplay.setMainController(this);
        pieChartData = FXCollections.observableArrayList(new PieChart.Data("Not tested", 0), new PieChart.Data("OK", 0), new PieChart.Data("OKWC", 0), new PieChart.Data("NOK", 0), new PieChart.Data("Not Testable", 0), new PieChart.Data("Incomplete", 0), new PieChart.Data("OS", 0), new PieChart.Data("Total", 0));

        pieChart.setLegendVisible(true);
        pieChart.setData(pieChartData);

        buttonChangeResults.setOnAction((ActionEvent e) -> {
            resultHandler.validCaseResultsChange(casesExecutionModified);
        });

        this.casesExecutionModified.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                buttonChangeResults.setDisable(casesExecutionModified.size() == 0);
            }
        });

        this.tableViewCampaignPopUpRun.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                testCaseSelected = tableViewCampaignPopUpRun.getSelectionModel().getSelectedItem();
                long tempsDebut3 = System.currentTimeMillis();
                DisplaySteps(testCaseSelected);
                long tempsFin3 = System.currentTimeMillis();
                float seconds3 = (tempsFin3 - tempsDebut3) / 1000F;
                System.out.println("test case selected result = " + testCaseSelected.simpleStringResultProperty());
                //System.out.println("ESSAI RESULT = " + this.tableViewCampaignPopUpRun.getColumns().get(this.tableViewCampaignPopUpRun.getColumns().size() - 1).getCellData(tableViewCampaignPopUpRun.getSelectionModel().getSelectedIndex()));
                //System.out.println("Case display= " + Float.toString(seconds3));
            }
        });
        loadCss();
    }

    /**
     * initialize the reference of the class parent
     * TabTestCampaignExecutionMainViewController to this controller.
     *
     * @param mainController the reference to the parent class
     */
    void init(TabTestCampaignExecutionMainViewController mainController) {
        this.main = mainController;
    }

    /**
     * Initialize all the parameters and variables for the assets.view
     *
     * @param stepExecution the campaign to display results
     * @throws InterruptedException
     */
    public void setParameters(Iterations iteration) throws InterruptedException {
        this.iteration = iteration;
        ArrayList<CaseExecutions> caseExecutions;
        this.campaignID = this.iteration.getTestCampaign().getIdtestCampaign();
        this.baselineId = iteration.getBaselineId();
        TestCasesExecution testCaseExecution = new TestCasesExecution();
        //System.out.println("ITERATION NUMBER= " + this.iteration.getIterationNumber());

        //gets all case executioms
        caseExecutions = testCaseExecution.PrepareCaseDisplayResults(baselineId, this.iteration.getIterationNumber());
        testCasesToDisplay = caseExecutions;
        testCases = FXCollections.observableArrayList(caseExecutions);
        tableViewCampaignPopUpRun.setItems(testCases);
        //By Default: test case 0 is selected since at top
        testCaseSelected = testCases.get(0);
        tableViewCampaignPopUpRun.getSelectionModel().select(testCaseSelected);
        if (this.iteration.getIterationNumber() != 0) {
            this.tableViewCampaignPopUpRun.setEditable(true);
            CaseToDisplay.setMainController(this);
            CaseToDisplay.initColumnCaseToExecute(tableViewCampaignPopUpRun);
            Platform.runLater(() -> {
                preparePieGraph(caseExecutions);
                DisplaySteps(testCaseSelected);
                constructCampaignInformation(campaignID);
            });
        } else {
            CaseToDisplay.initColumnCaseToExecute(tableViewCampaignPopUpRun);
            this.buttonChangeResults.setVisible(false);
//            System.out.println("COLUMNS = " + this.tableViewCampaignPopUpRun.getColumns());
//            System.out.println("COLUMNS = " + this.tableViewCampaignPopUpRun.getColumns().get(this.tableViewCampaignPopUpRun.getColumns().size() - 1));
//            this.tableViewCampaignPopUpRun.getColumns().get(this.tableViewCampaignPopUpRun.getColumns().size() - 1);
            pieChart.setVisible(false);
            Platform.runLater(() -> {
                DisplaySteps(testCaseSelected);
                constructCampaignInformation(campaignID);
            });
        }

    }

    /**
     * Display the step and scripts of a case
     *
     * @param testCasesAndSteps the ArrayList of testCasesAndSteps where search
     *                          the test case to display
     * @param testCaseId        the ID of the case to display
     * @param caseOrder
     */
    public void DisplaySteps(CaseExecutions caseExecution) {
        TestCasesExecution testCaseExecution = new TestCasesExecution();
        long tempsDebut3 = System.currentTimeMillis();
        testCaseExecution.PrepareStepsScriptsParametersDisplayResults(caseExecution, iteration);
        long tempsFin3 = System.currentTimeMillis();
        //float seconds3 = (tempsFin3 - tempsDebut3) / 1000F;
        //System.out.println("Step preparation= " + Float.toString(seconds3));
        //long tempsDebut = System.currentTimeMillis();
        controllerTableStep.displayScriptAndStepExecution(testCaseSelected);
        //long tempsFin = System.currentTimeMillis();
        //float seconds = (tempsFin - tempsDebut) / 1000F;
        //System.out.println("AFFICHAGE EFFECTUE EN= " + Float.toString(seconds));
    }

    /**
     * Prepare the assets.view of the table of step
     */
    private void constructTableStep() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPanePopUpCase.add((AnchorPane) fxmlLoader.load(getClass().getResource("/assets/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 1, 4, 3, 3);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/assets/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPanePopUpCase.add(paneTest, 1, 3, 3, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/assets.view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();
        controllerHeader = fxmlLoader2.getController();
        controllerHeader.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeader);
        controllerHeader.setRestults();
    }

    /**
     * prepare the display of the pieGrah of the campaign
     *
     * @param testCasesToDisplay all the test cases of the campaign
     */
    public void preparePieGraph(ArrayList<CaseExecutions> caseExecutions) {
        //System.out.println("PIE CHART PREPARATION");
        int NOK = 0;
        int notExecuted = 0;
        int OK = 0;
        int okWithComment = 0;
        int outOfScope = 0;
        int notTestable = 0;
        int incomplete = 0;
        for (int i = 0; i < caseExecutions.size(); i++) {
            //System.out.println("CASE EXECUTION RESULT = " + caseExecutions.get(i).getCaseExecutionResult());
            switch (caseExecutions.get(i).getCaseExecutionResult()) {
                case "NOK":
                    NOK = NOK + 1;
                    break;
                case "OK":
                    OK = OK + 1;
                    break;
                case "NExec":
                    notExecuted = notExecuted + 1;
                    break;
                case "OKWC":
                    okWithComment++;
                    break;
                case "OS":
                    outOfScope++;
                    break;
                case "Not testable":
                    notTestable++;
                    break;
                case "incomplete":
                    incomplete++;
                    break;
                default:
                    break;
            }
        }

        pieChartData.get(0).setPieValue(notExecuted);
        pieChartData.get(0).setName("Not tested\t" + notExecuted);
        pieChartData.get(1).setPieValue(OK);
        pieChartData.get(1).setName("OK\t\t\t" + OK);
        pieChartData.get(2).setPieValue(okWithComment);
        pieChartData.get(2).setName("OKWC\t\t" + okWithComment);
        pieChartData.get(3).setPieValue(NOK);
        pieChartData.get(3).setName("NOK\t\t\t" + NOK);
        pieChartData.get(4).setName("Not testable\t" + notTestable);
        pieChartData.get(4).setPieValue(notTestable);
        pieChartData.get(5).setName("Incomplete\t" + incomplete);
        pieChartData.get(5).setPieValue(incomplete);
        pieChartData.get(6).setName("OS\t\t\t" + outOfScope);
        pieChartData.get(6).setPieValue(outOfScope);
        pieChartData.get(7).setName("Total\t\t" + (notExecuted + OK + NOK + okWithComment + outOfScope + notTestable + incomplete));

    }

    /**
     * Load the CSS sheet of the assets.view
     */
    private void loadCss() {
        this.mainAnchorPopUp.getStylesheets().add("/assets/view/popup/tablestep.css");
    }

    /**
     * construct the campaign information to put in the title Pane
     *
     * @param campaignID
     */
    public void constructCampaignInformation(int campaignID) {
        //System.out.println("CONSTRUC INFORMATION");
        TestCampaignDB testCampaignHandler = new TestCampaignDB();
        TestCampaign testCampaign = testCampaignHandler.getTestCampaignFromID(campaignID);
        this.jtextfieldReferenceCampaignView.setText(String.valueOf(testCampaign.getReference()));
        this.jtextfieldSystemCampaignView.setText(testCampaign.getSystem());
        this.jtextfieldWriterCampaignView.setText(testCampaign.getWritter());
        this.jtextfieldVersionCampaignView.setText(String.valueOf(testCampaign.getCampaignVersion()));
        this.jtextfieldCreationDateCampaignView.setText(testCampaign.getCreationDate());
        this.jtextfieldEditionDateCampaignView.setText(testCampaign.getEditionDate());
        this.jtextfieldSUTReleaseCampaignView.setText(testCampaign.getSoftwareSutRelease());
        this.jtextfieldNumberCasesCampaignView.setText(String.valueOf(testCampaign.getNumberTestCase()));
        this.CheckboxRegressionThreadCampaignView.setSelected(testCampaign.getRegressionThread() != 0);
        this.CheckboxRegressionThreadCampaignView.setDisable(true);
        this.jtextfieldWriterEmailCampaignView.setText(testCampaign.getWriterEmail());
        this.jtextareaCommentsCampaignView.setText(testCampaign.getComments());
        tPane.setText("Information of baseline : " + baselineId);
    }

    /**
     * Display of the TitlePane
     */
    private void iniTitlePane() {
        tPane.setAnimated(false);
        tPane.expandedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Platform.runLater(() -> {
                        if (!tPane.isExpanded()) {
                            tPane.getChildrenUnmodifiable().get(0).setVisible(false);
                            gridPanePopUpCase.getRowConstraints().get(0).setMaxHeight(30);
                            gridPanePopUpCase.getRowConstraints().get(0).setPrefHeight(30);
                            gridPanePopUpCase.getRowConstraints().get(0).setMinHeight(30);
                        } else {
                            tPane.getChildrenUnmodifiable().get(0).setVisible(true);
                            gridPanePopUpCase.getRowConstraints().get(0).setMaxHeight(250);
                            gridPanePopUpCase.getRowConstraints().get(0).setPrefHeight(250);
                            gridPanePopUpCase.getRowConstraints().get(0).setMinHeight(250);
                        }
                    }
            );
        });
    }

    /**
     * @param caseExecModified
     */
    public void addModifiedCaseExecution(CaseExecutions caseExecModified) {
        if (this.casesExecutionModified.contains(caseExecModified)) {
            this.casesExecutionModified.remove(caseExecModified);
            //this.testCases.get(this.testCases.indexOf(caseExecModified)).getCaseExecutionResultObj().setNewComment("");
        }
        if (this.testCases.get(this.testCases.indexOf(caseExecModified)).getOriginalResult() == null) {

        } else {
            if (caseExecModified.simpleStringResultProperty().getValue().compareTo(this.testCases.get(this.testCases.indexOf(caseExecModified)).getOriginalResult()) != 0) {
                String comment = popUpChangeResult();
                if (comment != null) {
                    comment = Main.df.format(Calendar.getInstance().getTime()).concat(": " + comment + " (old result= " + this.testCases.get(this.testCases.indexOf(caseExecModified)).getOriginalResult() + ")");
                    this.casesExecutionModified.add(caseExecModified);
                    this.testCases.get(this.testCases.indexOf(caseExecModified)).getCaseExecutionResultObj().setNewComment(comment);
                } else {
                    this.testCases.get(this.testCases.indexOf(caseExecModified)).setCaseExecutionResult(this.testCases.get(this.testCases.indexOf(caseExecModified)).getOriginalResult());
                    this.testCases.get(this.testCases.indexOf(caseExecModified)).getCaseExecutionResultObj().setNewComment(" ");
                }
            } else {
                this.testCases.get(this.testCases.indexOf(caseExecModified)).getCaseExecutionResultObj().setNewComment(" ");
            }
            System.out.println("SIZE = " + this.casesExecutionModified);
            Platform.runLater(() -> {
                preparePieGraph(testCasesToDisplay);
            });
        }
    }

    private String popUpChangeResult() {
        String comment = null;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Comment");
        dialog.setHeaderText("A comment is mandatory when a result is manually modified");
        dialog.setContentText("Please enter your comment:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            comment = result.get();
        }
        return comment;
    }

    /**
     * @param caseExecu
     */
    public void popUpChangeComment(CaseExecutions caseExecu) {
        try {
            //toto.set(p.getValue().getCaseExecutionResultObj().getComment() + "\n");
            //return toto.concat(p.getValue().getCaseExecutionResultObj().simpleStringCommentProperty());
            //String comment = caseExecu.getCaseExecutionResultObj().getComment();
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/assets/view/popup/popUpChangeCaseComment.fxml").openStream());
            dialogStage = new Stage();
            dialogStage.setTitle("Change Comment");
            dialogStage.initOwner(Main.primaryStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            dialogStage.setScene(scene);

            PopUpChangeCaseCommentController controller = fxmlLoader.getController();
            controller.init(this);
            controller.setComment(caseExecu);

            dialogStage.setOnCloseRequest((WindowEvent event) -> {
                this.closePopUp();
            });
            dialogStage.show();
            dialogStage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - dialogStage.getWidth() / 2);
            dialogStage.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - dialogStage.getHeight() / 2);
//            dialogStage.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - dialogStage.getWidth() / 2);
//            dialogStage.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - dialogStage.getHeight() / 2);
        } catch (IOException ex) {
            Logger.getLogger(TabViewResultsController.class.getName()).error("", ex);
        }
    }

    /**
     *
     */
    public void closePopUp() {
        this.dialogStage.close();
    }

    /**
     * @param caseExecu
     * @param comment
     */
    public void validCommentChange(CaseExecutions caseExecu, String comment) {
        caseExecu.getCaseExecutionResultObj().setComment(comment);
        this.addCaseCommentModified(caseExecu);
    }

    /**
     * @param caseExecu
     */
    public void addCaseCommentModified(CaseExecutions caseExecu) {
        if (this.casesExecutionModified.contains(caseExecu)) {
            this.casesExecutionModified.remove(caseExecu);
        }
        this.casesExecutionModified.add(caseExecu);
        this.closePopUp();
        //this.testCases.get(this.testCases.indexOf(caseExecModified)).getCaseExecutionResultObj().setNewComment("");
    }
}
