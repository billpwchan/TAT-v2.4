/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcampaign;

import DB.Iterations;
import DB.TestCampaign;
import DB.TestCase;
import DBcontroller.IterationDB;
import DBcontroller.TestCampaignDB;
import DBcontroller.TestCaseDB;
import controller.tabtestcase.TabTestCaseLibraryController;
import controller.util.CommonFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;
import model.setCursorOnComponent;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabTestCampaignRepositoryController implements Initializable {

    /**
     *
     */
    public static final ObservableList<TestCampaign> observableListTestCampaign = FXCollections.observableArrayList();
    private static final TestCampaignDB testCampaignHandler = new TestCampaignDB();
    private static final IterationDB iterationHandler = new IterationDB();
    /**
     *
     */
    public static TabTestCampaignMainViewController main;
    private final ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();
    private final TestCaseDB testCaseHandler = new TestCaseDB();
    @FXML
    private AnchorPane anchorPaneRepositoryCampaign;
    @FXML
    private GridPane gridPaneCampaignRepository;
    @FXML
    private TableView<TestCase> tableViewTestCase;
    @FXML
    private TableView<TestCampaign> tableViewTestCampaign;
    @FXML
    private Button buttonNew;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonRefreshCampaignRepository;
    @FXML
    private TextField fieldFilter;
    private TestCampaign campaignSelected = new TestCampaign();

    /**
     * Update of the campaign repository
     */
    public static void updateRepository() {
        observableListTestCampaign.setAll(testCampaignHandler.getAllCampaigns());
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Fill the observable list of test campaign with all the campaign get from the DB
            observableListTestCampaign.setAll(testCampaignHandler.getAllCampaigns());
        } catch (Exception ex) {
            Logger.getLogger(TabTestCaseLibraryController.class.getName()).error("", ex);
        }

        FilteredList<TestCampaign> filteredData = new FilteredList<>(observableListTestCampaign, p -> true);

        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tCampaign -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (tCampaign.getCampaignVersion() != null && tCampaign.getCampaignVersion().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (tCampaign.getComments() != null && tCampaign.getComments().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getCreationDate() != null && tCampaign.getCreationDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getDescription() != null && tCampaign.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getEditionDate() != null && tCampaign.getEditionDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getIdtestCampaign() != null && tCampaign.getIdtestCampaign().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getReference() != null && tCampaign.getReference().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getSoftwareSutRelease() != null && tCampaign.getSoftwareSutRelease().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getSystem() != null && tCampaign.getSystem().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCampaign.getWriterEmail() != null && tCampaign.getWriterEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<TestCampaign> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewTestCampaign.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewTestCampaign.setItems(sortedData);

        //tableViewTestCampaign.setItems(observableListTestCampaign);
        tableViewTestCampaign.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableViewTestCase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableViewTestCampaign.setTableMenuButtonVisible(true);
        tableViewTestCase.setTableMenuButtonVisible(true);

        tableViewTestCase.setPlaceholder(new Label(""));
        tableViewTestCampaign.setPlaceholder(new Label(""));

        initColumn campaignColumnInit = new initColumn();

        campaignColumnInit.initColumnCampaign(tableViewTestCampaign);

        campaignColumnInit.initColumnCase(tableViewTestCase);

        this.initContextMenu();
        this.initButtons();

        /* Listener in order to perform actions after mousePressed on the table assets.view testCases.
         * if 2 click, open the assets.view of the test case selected in a tab.
         */
        this.tableViewTestCase.setOnMousePressed(
                (MouseEvent event) -> {
                    if (event.getClickCount() == 2 && tableViewTestCase.getSelectionModel().getSelectedItem() != null) {
                        main.viewTestCase(tableViewTestCase.getSelectionModel().getSelectedItem());
                    }
                }
        );

        /* Listener in order to perform actions after mousePressed on the table assets.view testCampaign.
         * if only 1click, display in the table assets.view case, the test cases of the selected campaign.
         * if 2 click, open the assets.view of the test campaign in a tab.
         */
        this.tableViewTestCampaign.setOnMousePressed(
                (MouseEvent event) -> {
                    if (tableViewTestCampaign.getSelectionModel().getSelectedItem() != null) {
                        if (event.getClickCount() == 2) {
                            TestCampaign CampaignSelected = tableViewTestCampaign.getSelectionModel().getSelectedItem();
                            viewTestCampaign(CampaignSelected);
                        } else if (event.getClickCount() == 1) {
                            TestCampaign displayFromThisCampaign = tableViewTestCampaign.getSelectionModel().getSelectedItem();
                            observableListTestCase.setAll(testCaseHandler.getTestCasesFromTestCampaign(displayFromThisCampaign));
                            tableViewTestCase.setItems(observableListTestCase);
                            this.campaignSelected = displayFromThisCampaign;
                        }
                    }
                }
        );

        //Define a new cursor when an action is available for the user
        defineCursor();
    }

    /**
     * method to open the assets.view of a campaign in a new tab
     *
     * @param Campaign the campaign to display
     */
    private void viewTestCampaign(TestCampaign Campaign) {
        TabTestCampaignRepositoryController.main.displayViewTab(Campaign);
    }

    /**
     * Method to open the tab to create a new campaign
     */
    private void newTestCampaign() {
        TabTestCampaignRepositoryController.main.displayNewTestCampaign();
    }

    /**
     * initialize the reference of the class parent
     * TabTestCampaignMainViewController to this controller.
     *
     * @param mainController the reference to the parent class
     */
    public void init(TabTestCampaignMainViewController mainController) {
        TabTestCampaignRepositoryController.main = mainController;
    }

    /**
     * Method in order to call the method assets.view test case from the mainFrame
     *
     * @param testCase the test case to display
     */
    public void callViewToTestCase(TestCase testCase) {
        TabTestCampaignRepositoryController.main.viewTestCase(testCase);
    }

    /**
     * define a new cursor for buttons where an action is possible
     */
    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonNew);
        //nodeHand.add(this.buttonRefreshCampaignRepository);
        nodeHand.add(this.buttonDelete);
//        nodeHand.add(this.buttonEdit);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     * initialize the context menu in the table assets.view test campaign
     */
    private void initContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem viewCampaign = new MenuItem("View");
//        MenuItem deleteCampaign = new MenuItem("Delete");
//        MenuItem editCampaign = new MenuItem("Edit");
        menu.getItems().add(viewCampaign);
//        menu.getItems().add(deleteCampaign);
//        menu.getItems().add(editCampaign);
        tableViewTestCampaign.setContextMenu(menu);

        viewCampaign.setOnAction((ActionEvent event) -> {
            TestCampaign CampaignSelected = tableViewTestCampaign.getSelectionModel().getSelectedItem();
            viewTestCampaign(CampaignSelected);
        });

        //        deleteCampaign.setOnAction((ActionEvent event) -> {
//
//        });
//
//        editCampaign.setOnAction((ActionEvent event) -> {
//        });
    }

    /**
     * initialize the buttons of the assets.view
     */
    private void initButtons() {
        this.buttonDelete.setDisable(false);
        this.buttonEdit.setDisable(true);
        this.buttonEdit.setVisible(false);
        this.buttonRefreshCampaignRepository.setVisible(false);
        buttonRefreshCampaignRepository.setOnAction((ActionEvent e) -> {
            TabTestCampaignRepositoryController.updateRepository();
        });

        buttonNew.setOnAction((ActionEvent e) -> {
            newTestCampaign();
        });

//        buttonEdit.setOnAction((ActionEvent e) -> {
//        });

        buttonDelete.setOnAction((ActionEvent e) -> {
            if (this.campaignSelected != null) {
                ArrayList<Iterations> baselines = iterationHandler.getIterationsFromCampaign(campaignSelected);
                if (baselines.size() > 0) {
                    String baselineNames = "Baselines to delete : \n";
                    for (Iterations baseline : baselines) {
                        baselineNames = baselineNames + "\t--> " + baseline.getBaselineId() + "\n";
                    }
                    CommonFunctions.displayAlert(AlertType.WARNING, "Warning", "Impossible to delete, the campaign has already been baselines. Please delete these baslines and executions first: ", baselineNames);
                    e.consume();
                } else {
                    testCampaignHandler.deleteCampaignNotExecuted(campaignSelected);
                    TabTestCampaignRepositoryController.updateRepository();
                    tableViewTestCase.setItems(null);
                    CommonFunctions.reportLog.info("User deleted test campaign: " + campaignSelected.getReference());
                }
            }
        });
    }

}
