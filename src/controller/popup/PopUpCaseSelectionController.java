/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.TestCase;
import controller.tabtestcampaign.TabTestCampaignNewController;
import controller.util.CommonFunctions;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class Controller of the popUp to select a case
 *
 * @author tmorin
 */
public class PopUpCaseSelectionController implements Initializable {

    /**
     *
     */
    public static TabTestCampaignNewController controllerNewCampaign;
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
        this.anchorPane.getStylesheets().add("/assets/view/CustomeStyle.css");
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

        CommonFunctions.AddListener(fieldFilter, filteredData);
    }

    /**
     * @param controllerNewCampaign
     */
    public void init(TabTestCampaignNewController controllerNewCampaign) {
        PopUpCaseSelectionController.controllerNewCampaign = controllerNewCampaign;
    }

    /**
     * @param casesInDB
     */
    public void setTable(ObservableList<TestCase> casesInDB) {
        observableListTestCase = casesInDB;
        //tableViewCasePopUpAddCase.setItems(casesInDB);
        initColumn campaignColumnInit = new initColumn();
        campaignColumnInit.initColumnCase(tableViewCasePopUpAddCase);

        filteredData = new FilteredList<>(observableListTestCase, p -> true);

        // 3. Wrap the FilteredList in a SortedList. 
        sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewCasePopUpAddCase.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewCasePopUpAddCase.setItems(sortedData);
    }

}
