/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcase;

import DB.Iterations;
import DB.TestCampaign;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import model.initColumn;
import model.setCursorOnComponent;
import model.util;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabTestCaseLibraryController implements Initializable {

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

    /**
     *
     */
    public static TabTestCaseMainViewController main;

    private Stage popUpCampaignID;

    private Stage popUpBaselineID;

    private final ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();

    private TestCaseDB testCaseHandler;

    private TestCase currentTestCaseSelected;

    private TestCampaign campaignToBaseline;

    private TableStepScriptCreationController controllerTableStep;

    private HeaderTableStepController controllerHeaderTableStep;

    @FXML
    private TableView<String> tableViewRequirements;
    @FXML
    private TableView<String> tableViewTestCaseLinked;

    TestCampaignDB testCampaignHandler = new TestCampaignDB();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        FilteredList<TestCase> filteredData = new FilteredList<>(observableListTestCase, p -> true);

        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tCase -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (tCase.getTestCaseIdentification().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (tCase.getProject() != null && tCase.getProject().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTypeOfTest() != null && tCase.getTypeOfTest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getCategoryOfTest() != null && tCase.getCategoryOfTest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getLocation() != null && tCase.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestCaseTitle() != null && tCase.getTestCaseTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestCaseSource() != null && tCase.getTestCaseSource().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTotalSteps() != null && tCase.getTotalSteps().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getBlocking() != null && tCase.getBlocking().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getInternalComments() != null && tCase.getInternalComments().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getWritingStatus() != null && tCase.getWritingStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getWritter() != null && tCase.getWritter().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getCreationDate() != null && tCase.getCreationDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestObjective() != null && tCase.getTestObjective().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestMethodIadt() != null && tCase.getTestMethodIadt().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getEnvironment() != null && tCase.getEnvironment().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });

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
//            for (int i = 0; i < 100000; i++) {
//                TestCase testCase = new TestCase();
//                testCase.setTestCaseIdentification("ID = " + i);
//                this.observableListTestCase.add(testCase);
//            }
            //this.tableViewTestCase.setItems(observableListTestCase);
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
            Logger.getLogger(TabTestCaseLibraryController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        this.tableViewTestCase.getSelectionModel().getSelectedCells().addListener((ListChangeListener.Change<? extends TablePosition> c) -> {
//            label1.setText(String.valueOf(c.getList().get(0).getRow()));
//            //System.out.println(label1);
//        });

        /**
         * Bind the column stimuli and check to the model
         * testCaseTreeTableVIewObject
         */
//        TreeTableColumn<testCaseTreeTableViewObject, String> stimuliCol
//                = new TreeTableColumn<>("Stimuli");
//        stimuliCol.setPrefWidth(150);
//        stimuliCol.setCellValueFactory(
//                (TreeTableColumn.CellDataFeatures<testCaseTreeTableViewObject, String> param)
//                -> param.getValue().getValue().stimuliProperty()
//        );
//        TreeTableColumn<testCaseTreeTableViewObject, String> checkCol
//                = new TreeTableColumn<>("check");
//        checkCol.setPrefWidth(150);
//        checkCol.setCellValueFactory(
//                (TreeTableColumn.CellDataFeatures<testCaseTreeTableViewObject, String> param)
//                -> param.getValue().getValue().checkProperty()
//        );
        ContextMenu menu = new ContextMenu();

        MenuItem viewCase = new MenuItem("View..");
        //MenuItem newCase = new MenuItem("New..");
        MenuItem editCase = new MenuItem("Edit..");
        MenuItem executeCase = new MenuItem("Execute..");
        //MenuItem deleteCase = new MenuItem("Delete");
        this.buttonAdd.setOnAction((ActionEvent event) -> {
            newTestCase();
        });

        this.buttonDelete.setOnAction((ActionEvent event) -> {
            System.out.println("Campaign selected : ");
            if (this.currentTestCaseSelected != null) {
                ArrayList<TestCampaign> campaigns = testCampaignHandler.getCampaignsFromCases(this.currentTestCaseSelected);
                if (campaigns.size() > 0) {
                    String campaignNames = "Campaign to Delete : \n";
                    for (int i = 0; i < campaigns.size(); i++) {
                        campaignNames = campaignNames + "\t--> "+campaigns.get(i).getReference() + "\n";
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

        viewCase.setOnAction((ActionEvent event) -> {
            viewTestCase(currentTestCaseSelected);
        });

        this.executeButton.setOnAction((ActionEvent event) -> {
//            if (!controllerTableStep.isFullyConfigured()) {
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Case not fully configured");
//                alert.setHeaderText(null);
//                alert.setContentText("Impossible to execute a test case not fully configured");
//                alert.showAndWait();
//                event.consume();
//            } else {
            executeCaseGetCampaignID();
            //}
        });

        editCase.setOnAction((ActionEvent event) -> {
            //TestCase person = tableViewTestCase.getSelectoinModel().getSelectedItem();
            editTestCase(currentTestCaseSelected);
        });

        executeCase.setOnAction((ActionEvent event) -> {
            //To DO
        });

//        newCase.setOnAction((ActionEvent event) -> {
//            newTestCase();
//        });
//        deleteCase.setOnAction((ActionEvent event) -> {
//            deleteTestCase();
//        });
        menu.getItems().addAll(viewCase, editCase, executeCase);
        //menu.getItems().add(newCase);
        //menu.getItems().add(deleteCase);

        /**
         * Set the contextuel menu for the table TestCase.
         */
        this.tableViewTestCase.setContextMenu(menu);

        /**
         * Add listener to selected object in the tableViewTestCase.
         */
        this.tableViewTestCase.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TestCase>() {
            @Override
            public void changed(ObservableValue<? extends TestCase> observable, TestCase oldValue, TestCase newValue) {
                if (newValue != null) {
                    currentTestCaseSelected = newValue;
                    testCaseHandler.getAllFromCase(currentTestCaseSelected);
                    controllerTableStep.displayScriptAndStepView(currentTestCaseSelected);
                    tableViewTestCase.getSelectionModel().select(currentTestCaseSelected);
                    buttonEdit.setDisable(false);

                    if (controllerTableStep.isFullyConfigured()) {
                        executeButton.setDisable(false);
                    } else {
                        executeButton.setDisable(true);
                    }
                    controllerHeaderTableStep.loadImageExpand();

                } else {
                    buttonEdit.setDisable(true);
                }
            }
        });

        /**
         * Add mouse action on table test case. ! click : display the teststep/
         * test script 2 click : open the test case in a new tab.
         */
        this.tableViewTestCase.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (currentTestCaseSelected != null && event.getClickCount() == 2) {
                    viewTestCase(currentTestCaseSelected);
                }
            }
        });
        this.anchorPanelLibraryTestCase.getStylesheets().add("/view/testcase/cssLibraryTestCase.css");
        defineCursor();
    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void viewTestCase(TestCase person) {
        this.main.displayViewTab(person);
    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void editTestCase(TestCase person) {
        if (popUpEditCase(person)) {
            TabTestCaseLibraryController.main.displayEditTab(person);
        }
    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void newTestCase() {
        TabTestCaseLibraryController.main.displayNewTestCase();

    }

    /**
     * @see TabTestCaseMainViewController
     */
    private void deleteTestCase() {
        TabTestCaseLibraryController.main.deleteTestCase();
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
    public void updateLibrary() {
        this.observableListTestCase.setAll(testCaseHandler.getAllTestCases());
    }

    private void constructTableStep() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneCaseLibrary.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 0, 2, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerTableStep = fxmlLoader.getController();

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneCaseLibrary.add(paneTest, 0, 1, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeaderTableStep);

    }

    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonAdd);
        nodeHand.add(this.buttonEdit);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     *
     */
    public void focusOnLast() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.tableViewTestCase.getSelectionModel().selectLast();
    }

    private void executeCaseGetCampaignID() {
        try {
            PopUpCampaignCreationController dialogController;
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader.load(getClass().getResource("/view/popup/popUpCampaignCreation.fxml").openStream());
            dialogController = (PopUpCampaignCreationController) fxmlLoader.getController();
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
            Logger.getLogger(TabTestCaseNewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Close the PopUp of campaign selection
     */
    public void closePopUpCampaign() {
        this.popUpCampaignID.close();
        //popUpBaselineOpen = false;
    }

    /**
     * Close the PopUp of campaign selection
     */
    public void closePopUpBaseline() {
        this.popUpBaselineID.close();
        //popUpBaselineOpen = false;
    }

    /**
     *
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
            dialogController = (PopUpBaselineCreationController) fxmlLoader.getController();
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
            popUpBaselineID.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    deleteCampaign();
                }
            });
            //popUpBaselineOpen = true;
            popUpBaselineID.show();//.showAndWait();
            //popUpCampaignID.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - popUpCampaignStage.getWidth() / 2);
            //popUpCampaignID.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - popUpCampaignStage.getHeight() / 2);
            dialogController.setPrimaryStage(popUpBaselineID);

        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param baselineID
     */
    public void setOnActionBaselineID(String baselineID) {
        closePopUpBaseline();
        TabTestCampaignExecutionRepositoryBaselineController execController = new TabTestCampaignExecutionRepositoryBaselineController();
        Task<Void> task;
        Thread th;
        int range, sheetNumber;
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
            sheetNumber = -1;
            excelChoose = true;
        }
        if (excelChoose == true) {
            //tabBaseline.alertBox2();
            task = new Task<Void>() {
                @Override
                public Void call() throws IOException, FileNotFoundException, InterruptedException {
                    util.startTime();
                    try {
                        configDB.configureTestCase(baseline, currentTestCaseSelected, excelFile, range, tabBaseline.getSheetNumber(), 0, null, null);
                    } catch (Exception ex) {
                        Logger.getLogger(TabTestCaseLibraryController.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(TabTestCampaignExecutionBaselineCampaignController.class.getName()).log(Level.SEVERE, null, ex);
            }
            TabTestCampaignExecutionBaselineCampaignController.closeAlert(tabBaseline.getAlert());
            try {
                TabTestCampaignExecutionRepositoryBaselineController.UpdateTreeItem();
            } catch (ParseException ex) {
                Logger.getLogger(TabTestCaseLibraryController.class.getName()).log(Level.SEVERE, null, ex);
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
     *
     * @param testCase
     * @return
     */
    public boolean popUpEditCase(TestCase testCase) {
        if (testCaseHandler.getTestCaseUsed(testCase)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("This test case is already used in a campaign, impossible to edit it.");
            alert.setContentText("Do you want to create a new one from it ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                return true;
            } else {
                return false;
                // ... user chose CANCEL or closed the dialog
            }
        } else {
            return true;
        }

    }

}
