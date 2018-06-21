/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.TestCase;
import controller.tabtestcampaign.TabTestCampaignNewController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;
import javafx.scene.control.TextField;

/**
 * FXML Controller class Controller of the popUp to select a case
 *
 * @author tmorin
 */
public class PopUpCaseSelectionController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPanePopUpCase;
    @FXML
    private TableView<TestCase> tableViewCasePopUpAddCase;
    @FXML
    private Button buttonValidationPopUpAddCase;
    @FXML
    private TextField fieldFilter;

    /**
     *
     */
    public static TabTestCampaignNewController controllerNewCampaign;

    private ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();

    private ObservableList<TestCase> casesSelected = FXCollections.observableArrayList();

    private FilteredList<TestCase> filteredData;

    private SortedList<TestCase> sortedData;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.anchorPane.getStylesheets().add("/view/CustomeStyle.css");
        buttonValidationPopUpAddCase.setDisable(true);
        tableViewCasePopUpAddCase.setPlaceholder(new Label(""));
        tableViewCasePopUpAddCase.setTableMenuButtonVisible(true);
        tableViewCasePopUpAddCase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewCasePopUpAddCase.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        casesSelected = tableViewCasePopUpAddCase.getSelectionModel().getSelectedItems();
        buttonValidationPopUpAddCase.setOnAction((ActionEvent event) -> {
            controllerNewCampaign.setAction(this.casesSelected);
            controllerNewCampaign.closePopUp();
        });

        tableViewCasePopUpAddCase.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TestCase> observable, TestCase oldValue, TestCase newValue) -> {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            //System.out.println("IN ");
            buttonValidationPopUpAddCase.setDisable(false);
        });

        this.tableViewCasePopUpAddCase.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && tableViewCasePopUpAddCase.getSelectionModel().getSelectedItem() != null) {
                    casesSelected = tableViewCasePopUpAddCase.getSelectionModel().getSelectedItems();
                    controllerNewCampaign.setAction(casesSelected);
                    controllerNewCampaign.closePopUp();
                }
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem viewCase = new MenuItem("View");
        menu.getItems().add(viewCase);
        tableViewCasePopUpAddCase.setContextMenu(menu);

        viewCase.setOnAction((ActionEvent event) -> {
            if (tableViewCasePopUpAddCase.getSelectionModel().getSelectedItem() != null && casesSelected.size() == 1) {
                controllerNewCampaign.viewTestCase(casesSelected.get(0));
            }
        });

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
                } else if (tCase.getCreationDate() != null && tCase.getCreationDate().toLowerCase().contains(lowerCaseFilter)) {
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
    }

    /**
     *
     * @param controllerNewCampaign
     */
    public void init(TabTestCampaignNewController controllerNewCampaign) {
        PopUpCaseSelectionController.controllerNewCampaign = controllerNewCampaign;
    }

    /**
     *
     * @param casesInDB
     */
    public void setTable(ObservableList<TestCase> casesInDB) {
        observableListTestCase = casesInDB;
        System.out.println("observable size =  " + observableListTestCase.size());
        //tableViewCasePopUpAddCase.setItems(casesInDB);
        initColumn campaignColumnInit = new initColumn();
        campaignColumnInit.initColumnCase(tableViewCasePopUpAddCase);
        System.out.println("SIEZE= " + tableViewCasePopUpAddCase.getItems().size());

        filteredData = new FilteredList<>(observableListTestCase, p -> true);
        System.out.println("filtered data= " + filteredData.size());

        // 3. Wrap the FilteredList in a SortedList. 
        sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewCasePopUpAddCase.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewCasePopUpAddCase.setItems(sortedData);
    }

}
