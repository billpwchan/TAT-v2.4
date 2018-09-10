/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.TestCampaign;
import DBcontroller.TestCaseDB;
import controller.tabtestexecution.TabTestCampaignExecutionRepositoryBaselineController;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.initColumn;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.tabtestcampaign.TabTestCampaignRepositoryController.observableListTestCampaign;

/**
 * FXML Controller class Controller of the popUp to select a campaign for
 * baseline
 *
 * @author tmorin
 */
public class PopUpCampaignSelectionController implements Initializable {

    /**
     *
     */
    public static TabTestCampaignExecutionRepositoryBaselineController executionMainViewController;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPanePopUpCase;
    @FXML
    private TableView<TestCampaign> tableViewCampaignPopUpAddCampaign;
    @FXML
    private Button buttonBaselineCampaign;
    @FXML
    private Text textEmptyCampaign;
    @FXML
    private TextField fieldFilter;
    private Stage dialogStage;

    private TestCampaign campaignSelected;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textEmptyCampaign.setVisible(false);
        TestCaseDB testCaseHandler = new TestCaseDB();
        this.anchorPane.getStylesheets().add("/view/CustomeStyle.css");
        tableViewCampaignPopUpAddCampaign.setPlaceholder(new Label(""));
        tableViewCampaignPopUpAddCampaign.setTableMenuButtonVisible(true);
        tableViewCampaignPopUpAddCampaign.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buttonBaselineCampaign.setDisable(true);
        tableViewCampaignPopUpAddCampaign.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TestCampaign> observable, TestCampaign oldValue, TestCampaign newValue) -> {
            this.campaignSelected = newValue;
            if (testCaseHandler.numberoOfCasesInCampaign(this.campaignSelected.getIdtestCampaign()) != 0) {
                buttonBaselineCampaign.setDisable(false);
                textEmptyCampaign.setVisible(false);
            } else {
                buttonBaselineCampaign.setDisable(true);
                textEmptyCampaign.setVisible(true);

            }
        });

        this.tableViewCampaignPopUpAddCampaign.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableViewCampaignPopUpAddCampaign.getSelectionModel().getSelectedItem() != null) {
                campaignSelected = tableViewCampaignPopUpAddCampaign.getSelectionModel().getSelectedItem();
                if (testCaseHandler.numberoOfCasesInCampaign(campaignSelected.getIdtestCampaign()) != 0) {
                    executionMainViewController.setAction(campaignSelected);
                    executionMainViewController.closePopUp();
                } else {
                    buttonBaselineCampaign.setDisable(true);
                    textEmptyCampaign.setVisible(true);

                }
                //controllerNewCampaign.viewTestCase(casesSelected.get(0));
            }
        });

        buttonBaselineCampaign.setOnAction((ActionEvent event) -> {
            if (this.campaignSelected != null) {
                executionMainViewController.setAction(this.campaignSelected);
                executionMainViewController.closePopUp();
            }
        });
    }

//    public void displayAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.show();
//        alert.setX(this.dialogStage.getX() + this.dialogStage.getWidth() / 2 - alert.getWidth() / 2);
//        alert.setY(this.dialogStage.getY() + this.dialogStage.getHeight() / 2 - alert.getHeight() / 2);
//    }

    /**
     * @param controllerBaselineCampaign
     */
    public void init(TabTestCampaignExecutionRepositoryBaselineController controllerBaselineCampaign) {
        PopUpCampaignSelectionController.executionMainViewController = controllerBaselineCampaign;
    }

    /**
     * @param campaignInDB
     */
    public void setTable(ObservableList<TestCampaign> campaignInDB) {
        //tableViewCampaignPopUpAddCampaign.setItems(campaignInDB);
        initColumn campaignColumnInit = new initColumn();
        campaignColumnInit.initColumnCampaign(tableViewCampaignPopUpAddCampaign);

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
        sortedData.comparatorProperty().bind(tableViewCampaignPopUpAddCampaign.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewCampaignPopUpAddCampaign.setItems(sortedData);

    }

    /**
     * @param stage
     */
    public void setPrimaryStage(Stage stage) {
        this.dialogStage = stage;
    }
}
