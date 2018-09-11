/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import DB.Script;
import DB.TestCase;
import DBcontroller.MacroDB;
import DBcontroller.TestCaseDB;
import controller.macroActions.PreviewMacro;
import controller.macroActions.TableActionCreationController;
import controller.tabtestcase.TabTestCaseNewController;
import controller.util.CommonFunctions;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;
import model.setCursorOnComponent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabMacroLibraryController implements Initializable {

    private static TabMacroMainViewController macroMainViewController;
    private final MacroDB macroHandler = new MacroDB();
    private final TestCaseDB testCaseHandler = new TestCaseDB();
    private final ObservableList<Script> observableListMacro = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorPanelLibraryMacro;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private TableView<Script> tableViewMacro;
    @FXML
    private GridPane gridPaneMacroLibrary;
    @FXML
    private ScrollPane scrollPanePreview;
    @FXML
    private TextField fieldFilter;
    private TableActionCreationController controllerTableMacro;
    private Script currentMacroSelected = new Script();
    private PreviewMacro preview;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preview = new PreviewMacro();
        this.observableListMacro.setAll(this.macroHandler.getMacros());
        //this.tableViewMacro.setItems(observableListMacro);
        this.tableViewMacro.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /**
         * Init the column of the tableViewMacro
         */
        initColumn columnInitialisation = new initColumn();
        columnInitialisation.initColumnMacros(this.tableViewMacro, false);

        constructTableMacro();
        initButtons();
        defineCursor();

        this.tableViewMacro.setOnMousePressed((MouseEvent event) -> {
            if (tableViewMacro.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2) {   //Open View Tab.
                currentMacroSelected = tableViewMacro.getSelectionModel().getSelectedItem();
                macroHandler.getAllFromMacro(currentMacroSelected);
                displayMacro(currentMacroSelected);
            }
        });

        /**
         * Add listener to selected object in the tableViewMacro.
         */
        this.tableViewMacro.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Script> observable, Script oldValue, Script newValue) -> {
            if (newValue != null) {
                currentMacroSelected = newValue;
                macroHandler.getAllFromMacro(currentMacroSelected);
                preview.updateGridPaneCreationView(currentMacroSelected);
                buttonEdit.setDisable(false);
            } else {
                buttonEdit.setDisable(true);
            }
        });

        FilteredList<Script> filteredData = new FilteredList<>(observableListMacro, p -> true);

        CommonFunctions.addListenerScript(filteredData, fieldFilter);

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Script> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewMacro.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewMacro.setItems(sortedData);

    }

    void init(TabMacroMainViewController aThis) {
        TabMacroLibraryController.macroMainViewController = aThis;
    }

    @SuppressWarnings("empty-statement")
    private void initButtons() {
        this.buttonDelete.setDisable(false);
        this.buttonEdit.setDisable(true);

        buttonAdd.setOnAction((ActionEvent e) -> {
            newMacro();
        });

        buttonEdit.setOnAction((ActionEvent e) -> {
            editMacro(currentMacroSelected);
        });
        buttonDelete.setOnAction((ActionEvent e) -> {
            if (this.currentMacroSelected != null) {
                ArrayList<TestCase> testCases = testCaseHandler.getTestCasesFromMacros(this.currentMacroSelected);
                if (testCases.size() > 0) {
                    String testCaseNames = "Test Case to Delete : \n";
                    testCaseNames = testCases.stream().map((testCase) -> "\t--> " + testCase.getTestCaseTitle() + "\n").reduce(testCaseNames, String::concat);
                    CommonFunctions.displayAlert(Alert.AlertType.WARNING, "Warning", "Impossible to delete, the test case has already created. Please delete these test cases first: ", testCaseNames);
                    e.consume();
                } else {
                    macroHandler.deleteMacro(this.currentMacroSelected);        //Need to also delete in ParamScriptMacro & Macro database.
                    this.updateLibrary();
                    currentMacroSelected = this.observableListMacro.get(0);    //If there's no macro left. Need to consider the case!
                    if (currentMacroSelected != null) {
                        tableViewMacro.getSelectionModel().select(currentMacroSelected);
                    }
                }
            }
        });

    }

    private void newMacro() {
        TabMacroLibraryController.macroMainViewController.displayNewMacro();
        CommonFunctions.reportLog.info("User create a new Macro.");
    }

    private void editMacro(Script script) {
        TabMacroLibraryController.macroMainViewController.displayEditMacro(script);
        CommonFunctions.reportLog.info("User edited the Macro(" + script.getIdScript() + "): " + currentMacroSelected.getName());
    }

    private void displayMacro(Script script) {
        TabMacroLibraryController.macroMainViewController.displayViewMacro(script);
        CommonFunctions.reportLog.info("User edited the Macro(" + script.getIdScript() + "): " + currentMacroSelected.getName());
    }

    private void constructTableMacro() {
        try {
            this.preview.initialize(scrollPanePreview);
        } catch (Exception ex) {
            Logger.getLogger(TabMacroLibraryController.class.getName()).error("Cannot construct table Macro: ", ex);
        }

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = fxmlLoader2.load(getClass().getResource("/view/macroActions/headerPreviewMacro.fxml").openStream());
            this.gridPaneMacroLibrary.add(paneTest, 1, 1, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("Cannot construct table Macro: ", ex);
        }
    }

    /**
     *
     */
    public void updateLibrary() {
        this.observableListMacro.setAll(this.macroHandler.getMacros());
        //this.tableViewMacro.setItems(observableListMacro);
    }

    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonAdd);
        nodeHand.add(this.buttonEdit);
        nodeHand.add(this.buttonDelete);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

}
