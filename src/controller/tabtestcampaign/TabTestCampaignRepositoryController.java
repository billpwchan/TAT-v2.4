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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;
import java.util.ArrayList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.setCursorOnComponent;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabTestCampaignRepositoryController implements Initializable {

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

    /**
     *
     */
    public static TabTestCampaignMainViewController main;

    /**
     *
     */
    public static final ObservableList<TestCampaign> observableListTestCampaign = FXCollections.observableArrayList();

    private final ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();

    private final TestCaseDB testCaseHandler = new TestCaseDB();
    private TestCampaign campaignSelected = new TestCampaign();

    private static final TestCampaignDB testCampaignHandler = new TestCampaignDB();
    private static final IterationDB iterationHandler = new IterationDB();

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
            Logger.getLogger(TabTestCaseLibraryController.class.getName()).log(Level.SEVERE, null, ex);
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

        System.out.println("filtered data campaign= " + filteredData.size());

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

        /* Listener in order to perform actions after mousePressed on the table view testCases.
         * if 2 click, open the view of the test case selected in a tab.
         */
        this.tableViewTestCase.setOnMousePressed(
                (MouseEvent event) -> {
                    if (event.getClickCount() == 2 && tableViewTestCase.getSelectionModel().getSelectedItem() != null) {
                        main.viewTestCase(tableViewTestCase.getSelectionModel().getSelectedItem());
                    }
                }
        );

        /* Listener in order to perform actions after mousePressed on the table view testCampaign.
         * if only 1click, display in the table view case, the test cases of the selected campaign.
         * if 2 click, open the view of the test campaign in a tab.
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
     * method to open the view of a campaign in a new tab
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
     *
     * Method in order to call the method view test case from the mainFrame
     *
     * @param testCase the test case to display
     */
    public void callViewToTestCase(TestCase testCase) {
        TabTestCampaignRepositoryController.main.viewTestCase(testCase);
    }

    /**
     * Update of the campaign repository
     */
    public static void updateRepository() {
        observableListTestCampaign.setAll(testCampaignHandler.getAllCampaigns());
    }

    /**
     * define a new cursor for buttons where an action is possible
     */
    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonNew);
        //nodeHand.add(this.buttonRefreshCampaignRepository);
        nodeHand.add(this.buttonDelete);
        nodeHand.add(this.buttonEdit);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     * initialize the context menu in the table view test campaign
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
     * initialize the buttons of the view
     */
    private void initButtons() {
        this.buttonDelete.setDisable(false);
        this.buttonEdit.setDisable(true);
        this.buttonRefreshCampaignRepository.setVisible(false);
        buttonRefreshCampaignRepository.setOnAction((ActionEvent e) -> {
            this.updateRepository();
        });

        buttonNew.setOnAction((ActionEvent e) -> {
            newTestCampaign();
        });

        buttonEdit.setOnAction((ActionEvent e) -> {
        });

        buttonDelete.setOnAction((ActionEvent e) -> {
            if (this.campaignSelected != null) {
                ArrayList<Iterations> baselines = iterationHandler.getIterationsFromCampaign(campaignSelected);
                if (baselines.size() > 0) {
                    String baselineNames = "Baselines to delete : \n";
                    for (int i = 0; i < baselines.size(); i++) {
                        baselineNames = baselineNames + "\t--> " + baselines.get(i).getBaselineId() + "\n";
                    }
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Impossible to delete, the campaign has already been baselined. Please delete these baselines and executions first :");
                    alert.setContentText(baselineNames);
                    alert.showAndWait();
                    e.consume();
                } else {
                    testCampaignHandler.deleteCampaignNotExecuted(campaignSelected);
                    this.updateRepository();
                    tableViewTestCase.setItems(null);
                }
            }
        });
    }

}
