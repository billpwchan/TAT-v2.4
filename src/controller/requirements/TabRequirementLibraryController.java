/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.requirements;

import DB.Requirement;
import DB.TestCase;
import DBcontroller.RequirementDB;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.initColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155040
 */
public class TabRequirementLibraryController implements Initializable {

    private static TabRequirementMainViewController requirementMainViewController;
    private final ObservableList<Requirement> observableListRequirement = FXCollections.observableArrayList();
    private final RequirementDB RequirementHandler = new RequirementDB();
    @FXML
    private AnchorPane anchorPanelLibraryTestCase;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private TableView<Requirement> tableViewRequirements;
    @FXML
    private GridPane gridPaneCaseLibrary;
    @FXML
    private TableView<TestCase> tableViewTestCaseLinked;
    @FXML
    private WebView webViewRequirement;
    @FXML
    private TextField fieldFilter;
    private Requirement selectedRequirement;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final WebEngine webEngine = webViewRequirement.getEngine();
        observableListRequirement.setAll(RequirementHandler.getAllRequirement());
        tableViewRequirements.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewTestCaseLinked.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewRequirements.setTableMenuButtonVisible(true);
        tableViewTestCaseLinked.setTableMenuButtonVisible(true);
        tableViewTestCaseLinked.setPlaceholder(new Label(""));
        tableViewRequirements.setPlaceholder(new Label(""));
        tableViewRequirements.setItems(observableListRequirement);
        initColumn requirementColumnInit = new initColumn();
        requirementColumnInit.initColumnRequirement(tableViewRequirements);
        requirementColumnInit.initColumnCase(tableViewTestCaseLinked);
        initButtons();
        this.tableViewRequirements.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Requirement>() {
            @Override
            public void changed(ObservableValue<? extends Requirement> observable, Requirement oldValue, Requirement newValue) {
                if (newValue != null) {
                    selectedRequirement = newValue;
                    String st = null;
                    if (selectedRequirement.getRequirementText().contains("contenteditable=\"true\"")) {
                        st = selectedRequirement.getRequirementText().replace("contenteditable=\"true\"", "contenteditable=\"false\"");
                    } else {
                        st = selectedRequirement.getRequirementText();
                    }
                    webEngine.loadContent(st);

                }
            }
        });

        FilteredList<Requirement> filteredData = new FilteredList<>(observableListRequirement, p -> true);

        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(requirement -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (requirement.getRequirementID() != null && requirement.getRequirementID().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (requirement.getCategory() != null && requirement.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (requirement.getWriter() != null && requirement.getWriter().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (requirement.getVersion() != null && requirement.getVersion().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (requirement.getRequirementText() != null && requirement.getRequirementText().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (requirement.getCoverage() != null && requirement.getCoverage().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (requirement.getIadt() != null && requirement.getIadt().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (requirement.getComment() != null && requirement.getComment().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Requirement> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewRequirements.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewRequirements.setItems(sortedData);

    }

    void init(TabRequirementMainViewController aThis) {
        TabRequirementLibraryController.requirementMainViewController = aThis;
    }

    private void initButtons() {
        this.buttonDelete.setDisable(true);
        this.buttonEdit.setDisable(true);

        buttonAdd.setOnAction((ActionEvent e) -> {
            newRequirement();
        });

        buttonEdit.setOnAction((ActionEvent e) -> {
        });

        buttonDelete.setOnAction((ActionEvent e) -> {
        });

    }

    private void newRequirement() {
        TabRequirementLibraryController.requirementMainViewController.displayNewRequirement();
    }

    void updateRepository() {
        observableListRequirement.setAll(RequirementHandler.getAllRequirement());
    }

}
