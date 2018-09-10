/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcase;

import DB.Iterations;
import DB.TestCampaign;
import DB.TestCase;
import DBcontroller.ConfigurationDB;
import DBcontroller.TestCampaignDB;
import DBcontroller.TestCaseDB;
import controller.popup.PopUpBaselineCreationController;
import controller.popup.PopUpCampaignCreationController;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import controller.tabtestcampaign.TabTestCampaignRepositoryController;
import controller.tabtestexecution.TabTestCampaignExecutionBaselineCampaignController;
import controller.tabtestexecution.TabTestCampaignExecutionRepositoryBaselineController;
import controller.util.CommonFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import model.initColumn;
import model.setCursorOnComponent;
import model.util;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabTestCaseLibraryController implements Initializable {

    /**
     *
     */
    public static TabTestCaseMainViewController main;
    private final ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();
    TestCampaignDB testCampaignHandler = new TestCampaignDB();
    @FXML
    private AnchorPane anchorPanelLibraryTestCase;
    @FXML
    private GridPane gridPaneCaseLibrary;
    @FXML
    private TableView<TestCase> tableViewTestCase;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button executeButton;
    @FXML
    private TextField fieldFilter;
    private Stage popUpCampaignID;
    private Stage popUpBaselineID;
    private TestCaseDB testCaseHandler;
    private TestCase currentTestCaseSelected;
    private TestCampaign campaignToBaseline;
    private TableStepScriptCreationController controllerTableStep;
    private HeaderTableStepController controllerHeaderTableStep;
    @FXML
    private TableView<String> tableViewRequirements;
    @FXML
    private TableView<String> tableViewTestCaseLinked;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        FilteredList<TestCase> filteredData = new FilteredList<>(observableListTestCase, p -> true);

        CommonFunctions.AddListener(fieldFilter, filteredData);

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<TestCase> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewTestCase.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewTestCase.setItems(sortedData);

        //Label label1 = new Label();
        constructTableStep();
        try {
            /**
             * initialize the tableView test case and set the library to all
             * stored test case in library
             */
            this.testCaseHandler = new TestCaseDB();
            this.observableListTestCase.setAll(this.testCaseHandler.getAllTestCases());
            this.buttonDelete.setDisable(false);
            this.buttonEdit.setDisable(true);
            this.executeButton.setDisable(true);
            this.tableViewTestCase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            /**
             * Init the column of the tableVIewTestCase
             */
            initColumn columnInitialisation = new initColumn();
            columnInitialisation.initColumnCase(this.tableViewTestCase);

            //tableViewTestCase.setPlaceholder(new Label(""));
        } catch (Exception ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        ContextMenu menu = new ContextMenu();
        MenuItem viewCase = new MenuItem("View..");
        MenuItem editCase = new MenuItem("Edit..");
        MenuItem executeCase = new MenuItem("Execute..");
        this.buttonAdd.setOnAction((ActionEvent event) -> {
            newTestCase();
        });

        this.buttonDelete.setOnAction((ActionEvent event) -> {
            System.out.println("Campaign selected : ");
            if (this.currentTestCaseSelected != null) {
                ArrayList<TestCampaign> campaigns = testCampaignHandler.getCampaignsFromCases(this.currentTestCaseSelected);
                if (campaigns.size() > 0) {
                    String campaignNames = "Campaign to Delete : \n";
                    for (TestCampaign campaign : campaigns) {
                        campaignNames = campaignNames + "\t--> " + campaign.getReference() + "\n";
                    }
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Impossible to delete, the campaign has already been baselined. Please delete these Campaigns first :");
                    alert.setContentText(campaignNames);
                    alert.showAndWait();
                    event.consume();
                } else {
                    testCaseHandler.deleteTestCase(currentTestCaseSelected);
                    this.updateLibrary();
                    currentTestCaseSelected = this.observableListTestCase.get(0);
                    tableViewTestCase.getSelectionModel().select(currentTestCaseSelected);

                }
            }
        });

        this.buttonEdit.setOnAction((ActionEvent event) -> {
            editTestCase(currentTestCaseSelected);
        });

        editCase.setOnAction((ActionEvent event) -> {
            editTestCase(currentTestCaseSelected);
        });

        viewCase.setOnAction((ActionEvent event) -> {
            viewTestCase(currentTestCaseSelected);
        });

        this.executeButton.setOnAction((ActionEvent event) -> {
        });

        executeCase.setOnAction((ActionEvent event) -> {
            executeCaseGetCampaignID();
        });

        menu.getItems().addAll(viewCase, editCase, executeCase);
        /**
         * Set the contextuel menu for the table TestCase.
         */
        this.tableViewTestCase.setContextMenu(menu);

        /**
         * Add listener to selected object in the tableViewTestCase.
         */
        this.tableViewTestCase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentTestCaseSelected = newValue;
                testCaseHandler.getAllFromCase(currentTestCaseSelected);
                controllerTableStep.displayScriptAndStepView(currentTestCaseSelected);
                tableViewTestCase.getSelectionModel().select(currentTestCaseSelected);
                buttonEdit.setDisable(false);
                if (controllerTableStep.isFullyConfigured()) executeButton.setDisable(false);
                else executeButton.setDisable(true);
                controllerHeaderTableStep.loadImageExpand();
            } else {
                buttonEdit.setDisable(true);
            }
        });

        /**
         * Add mouse action on table test case. ! click : display the teststep/
         * test script 2 click : open the test case in a new tab.
         */
        this.tableViewTestCase.setOnMousePressed(event -> {
            if (currentTestCaseSelected != null && event.getClickCount() == 2) {
                viewTestCase(currentTestCaseSelected);
            }
        });
        this.anchorPanelLibraryTestCase.getStylesheets().add("/view/testcase/cssLibraryTestCase.css");
        defineCursor();
    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void viewTestCase(TestCase testCase) {
        TabTestCaseLibraryController.main.displayViewTab(testCase);
        CommonFunctions.reportLog.info("User Viewed the Test Case: " + testCase.getTestCaseIdentification());
    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void editTestCase(TestCase testCase) {
        if (popUpEditCase(testCase)) {
            TabTestCaseLibraryController.main.displayEditTab(testCase);
            CommonFunctions.reportLog.info("User Edited the Test Case: " + testCase.getTestCaseIdentification());
        }
    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void newTestCase() {
        TabTestCaseLibraryController.main.displayNewTestCase();
        CommonFunctions.reportLog.info("User created a new Test Case");
    }

    /**
     * Init the main controller with the reference of the object
     * tabtestcaseMainViewController.
     *
     * @param mainController
     */
    public void init(TabTestCaseMainViewController mainController) {
        main = mainController;
    }

    /**
     * Update the table with the value stored in the database.
     */
    void updateLibrary() {
        this.observableListTestCase.setAll(testCaseHandler.getAllTestCases());
    }

    private void constructTableStep() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneCaseLibrary.add(fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 0, 2, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        controllerTableStep = fxmlLoader.getController();

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = fxmlLoader2.load(getClass().getResource("/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneCaseLibrary.add(paneTest, 0, 1, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeaderTableStep);

    }

    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonAdd);
        nodeHand.add(this.buttonEdit);
        nodeHand.add(this.buttonDelete);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     *
     */
    void focusOnLast() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.tableViewTestCase.getSelectionModel().selectLast();
    }

    private void executeCaseGetCampaignID() {
        try {
            PopUpCampaignCreationController dialogController;
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/view/popup/popUpCampaignCreation.fxml").openStream());
            dialogController = fxmlLoader.getController();
            popUpCampaignID = new Stage();
            popUpCampaignID.setTitle("Campaign creation");
            popUpCampaignID.initOwner(Main.primaryStage);
            popUpCampaignID.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            popUpCampaignID.setScene(scene);
            PopUpCampaignCreationController controller = fxmlLoader.getController();
            controller.init(this);
            popUpCampaignID.setOnCloseRequest((WindowEvent event) -> {
                this.closePopUpCampaign();
            });
            //popUpBaselineOpen = true;
            popUpCampaignID.show();//.showAndWait();
            //popUpCampaignID.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - popUpCampaignStage.getWidth() / 2);
            //popUpCampaignID.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - popUpCampaignStage.getHeight() / 2);
            dialogController.setPrimaryStage(popUpCampaignID);

        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

    /**
     * Close the PopUp of campaign selection
     */
    public void closePopUpCampaign() {
        this.popUpCampaignID.close();
        CommonFunctions.reportLog.info("User Cancel Test Campaign PopUp.");
    }

    /**
     * Close the PopUp of campaign selection
     */
    public void closePopUpBaseline() {
        this.popUpBaselineID.close();
        CommonFunctions.reportLog.info("User Cancel Baseline PopUp.");
    }

    /**
     * @param campaignID
     */
    public void setOnActionCampaignID(String campaignID) {
        TestCampaign newTestCampaign = new TestCampaign();
        newTestCampaign.setReference(campaignID);
        newTestCampaign.setCampaignVersion(1);
        newTestCampaign.setCreationDate(Main.df.format(new Date()));
        newTestCampaign.setNumberTestCase(1);
        newTestCampaign.setRegressionThread((byte) 0);
        ArrayList<TestCase> testCaseCampaign = new ArrayList<>();
        testCaseCampaign.add(currentTestCaseSelected);
        this.campaignToBaseline = testCampaignHandler.CreateCampaign(newTestCampaign, testCaseCampaign);
        TabTestCampaignRepositoryController.updateRepository();
        this.closePopUpCampaign();
        this.executeCaseGetBaselineID();
    }

    private void executeCaseGetBaselineID() {
        try {
            PopUpBaselineCreationController dialogController;
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/view/popup/popUpBaselineCreation.fxml").openStream());
            dialogController = fxmlLoader.getController();
            popUpBaselineID = new Stage();
            popUpBaselineID.setTitle("BaselineCreation");
            popUpBaselineID.initOwner(Main.primaryStage);
            popUpBaselineID.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            popUpBaselineID.setScene(scene);
            PopUpBaselineCreationController controller = fxmlLoader.getController();
            controller.init(this);
            popUpBaselineID.setOnCloseRequest((WindowEvent event) -> {
                this.closePopUpBaseline();
            });
            popUpBaselineID.setOnCloseRequest(event -> deleteCampaign());
            //popUpBaselineOpen = true;
            popUpBaselineID.show();//.showAndWait();
            //popUpCampaignID.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - popUpCampaignStage.getWidth() / 2);
            //popUpCampaignID.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - popUpCampaignStage.getHeight() / 2);
            dialogController.setPrimaryStage(popUpBaselineID);

        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

    /**
     * @param baselineID
     */
    public void setOnActionBaselineID(String baselineID) {
        closePopUpBaseline();
        TabTestCampaignExecutionRepositoryBaselineController execController = new TabTestCampaignExecutionRepositoryBaselineController();
        Task<Void> task;
        Thread th;
        int range;
        TabTestCampaignExecutionBaselineCampaignController tabBaseline = new TabTestCampaignExecutionBaselineCampaignController();
        final ConfigurationDB configDB = new ConfigurationDB();
        File excelFile;
        boolean excelChoose = false;
        final Iterations baseline = configDB.createBaseline(baselineID, campaignToBaseline);
        if (tabBaseline.isExcelNeeded(currentTestCaseSelected)) {
            excelFile = tabBaseline.selectExcelFile();
            range = tabBaseline.getRange();
            if (excelFile != null) {
                excelChoose = true;

            }
        } else {
            excelFile = null;
            range = -1;
            excelChoose = true;
        }
        if (excelChoose) {
            task = new Task<Void>() {
                @Override
                public Void call() {
                    util.startTime();
                    try {
                        configDB.configureTestCase(baseline, currentTestCaseSelected, excelFile, range, tabBaseline.getSheetNumber(), 0, null, null);
                    } catch (Exception ex) {
                        Logger.getLogger(TabTestCaseLibraryController.class.getName()).error("", ex);
                    }
                    util.endTime();
                    return null;
                }
            };
            th = new Thread(task);
            th.setDaemon(true);
            th.start();
            try {
                th.join();
            } catch (InterruptedException ex) {
                CommonFunctions.debugLog.error("", ex);
            }
            TabTestCampaignExecutionBaselineCampaignController.closeAlert(tabBaseline.getAlert());
            try {
                TabTestCampaignExecutionRepositoryBaselineController.UpdateTreeItem();
            } catch (ParseException ex) {
                CommonFunctions.debugLog.error("", ex);
            }
            tabBaseline.notificationBaselinCase();
            //closeAlertBox(dialog);
        }
        execController.runCampaign(baselineID);
    }

    /**
     *
     */
    public void deleteCampaign() {
        testCampaignHandler.deleteCampaignNotExecuted(campaignToBaseline);
        TabTestCampaignRepositoryController.updateRepository();
    }

    /**
     * @param testCase
     * @return
     */
    private boolean popUpEditCase(TestCase testCase) {
        if (testCaseHandler.getTestCaseUsed(testCase)) {
            Optional<ButtonType> result = CommonFunctions.displayAlert(AlertType.WARNING, "Confirmation Dialog",
                    "This test case is already used in a campaign, impossible to edit it.", "Do you want to create a new one from it ?");
            return result.isPresent() && result.get() == ButtonType.OK; // ... user chose CANCEL or closed the dialog
        }
            return true;
        }
}
