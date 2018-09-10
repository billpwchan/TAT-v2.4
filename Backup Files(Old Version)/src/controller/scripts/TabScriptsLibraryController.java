/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.scripts;

import DB.Script;
import DBcontroller.ScriptDB;
import controller.macro.TabMacroLibraryController;
import controller.macroActions.PreviewMacro;
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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class TabScriptsLibraryController implements Initializable {

    private static TabScriptsMainViewController scriptMainViewController;
    private final ObservableList<Script> observableListScripts = FXCollections.observableArrayList();
    private final ScriptDB scriptHandler = new ScriptDB();
    @FXML
    private AnchorPane anchorPanelLibraryMacro;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private TableView<Script> tableViewScript;
    @FXML
    private GridPane gridPaneMacroLibrary;
    @FXML
    private ScrollPane scrollPanePreview;
    @FXML
    private TextField fieldFilter;
    private Script currentScriptSelected;
    private PreviewMacro previewScript;

    private TableParamCreationController tableParamController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previewScript = new PreviewMacro();
        this.observableListScripts.setAll(this.scriptHandler.getScriptListAndParameters());
        //this.tableViewScript.setItems(observableListScripts);
        this.tableViewScript.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initColumn columnInitialisation = new initColumn();
        columnInitialisation.initColumnMacros(this.tableViewScript, true);
        constructTableMacro();
        initButtons();
        this.tableViewScript.setOnMousePressed((MouseEvent event) -> {
            if (tableViewScript.getSelectionModel().getSelectedItem() != null) {
                if (event.getClickCount() == 2) {
                    currentScriptSelected = tableViewScript.getSelectionModel().getSelectedItem();
                    //displayScript(currentScriptSelected);
                }
            }
        });

        /**
         * Add listener to selected object in the tableViewMacro.
         */
        this.tableViewScript.getSelectionModel()
                .selectedItemProperty().addListener((ObservableValue<? extends Script> observable, Script oldValue, Script newValue) -> {
                    if (newValue != null) {
                        currentScriptSelected = newValue;
                        tableParamController.displayParams(currentScriptSelected);
                        //previewScript.updateGridPaneScriptView(currentScriptSelected);
                        //buttonEdit.setDisable(false);

                    } else {
                        //buttonEdit.setDisable(true);
                    }
                }
        );

        FilteredList<Script> filteredData = new FilteredList<>(observableListScripts, p -> true);

        CommonFunctions.addListenerScript(filteredData, fieldFilter);

        //System.out.println("filtered data= " + filteredData.size());
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Script> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableViewScript.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableViewScript.setItems(sortedData);

    }

    private void initButtons() {
        this.buttonDelete.setDisable(true);
        this.buttonEdit.setDisable(true);

        buttonAdd.setOnAction((ActionEvent e) -> {
            newScript();
        });

        buttonEdit.setOnAction((ActionEvent e) -> {

        });
        buttonDelete.setOnAction((ActionEvent e) -> {
        });

    }

    /**
     * @param aThis
     */
    public void init(TabScriptsMainViewController aThis) {
        TabScriptsLibraryController.scriptMainViewController = aThis;
    }

    private void constructTableMacro() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneMacroLibrary.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/scriptmanagement/tableScriptCreation.fxml").openStream()), 0, 2, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
        tableParamController = fxmlLoader.getController();

        //this.previewScript.initialize(scrollPanePreview);
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/scriptmanagement/headerParameters.fxml").openStream());
            this.gridPaneMacroLibrary.add(paneTest, 0, 1, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }
    }

    private void newScript() {
        TabScriptsLibraryController.scriptMainViewController.displayNewScript();
    }

    /**
     *
     */
    public void updateLibrary() {
        this.observableListScripts.setAll(this.scriptHandler.getScriptListAndParameters());
        //this.tableViewScript.setItems(observableListScripts);
    }

}
