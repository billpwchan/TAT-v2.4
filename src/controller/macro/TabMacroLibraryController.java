/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import DB.Script;
import DBcontroller.MacroDB;
import controller.macroActions.PreviewMacro;
import controller.macroActions.TableActionCreationController;
import controller.tabtestcase.TabTestCaseNewController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabMacroLibraryController implements Initializable {

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

    private static TabMacroMainViewController macroMainViewController;

    private TableActionCreationController controllerTableMacro;

    private Script currentMacroSelected = new Script();

    private final MacroDB macroHandler = new MacroDB();

    private final ObservableList<Script> observableListMacro = FXCollections.observableArrayList();

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
         * Init the column of the tableVIewMacro
         */
        initColumn columnInitialisation = new initColumn();
        columnInitialisation.initColumnMacros(this.tableViewMacro,false);

        constructTableMacro();
        initButtons();

        this.tableViewMacro.setOnMousePressed((MouseEvent event) -> {
            if (tableViewMacro.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2) {
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

        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(script -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (script.getCreationDate() != null && script.getCreationDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (script.getEditionDate() != null && script.getEditionDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (script.getDesciption() != null && script.getDesciption().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (script.getName() != null && script.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (script.getScriptVersion() != null && script.getScriptVersion().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });

        //System.out.println("filtered data= " + filteredData.size());
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
        this.buttonDelete.setDisable(true);
        this.buttonEdit.setDisable(true);

        buttonAdd.setOnAction((ActionEvent e) -> {
            newMacro();
        });

        buttonEdit.setOnAction((ActionEvent e) -> {
            editMacro(currentMacroSelected);
        });
        buttonDelete.setOnAction((ActionEvent e) -> {
        });

    }

    private void newMacro() {
        TabMacroLibraryController.macroMainViewController.displayNewMacro();
    }
    
    private void editMacro(Script script) {
        TabMacroLibraryController.macroMainViewController.displayEditMacro(script);
    }

    private void displayMacro(Script script) {
        TabMacroLibraryController.macroMainViewController.displayViewMacro(script);
    }

    private void constructTableMacro() {
        try {
            this.preview.initialize(scrollPanePreview);
        } catch (Exception e) {
            System.out.println("exception e = " + e);
        }

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/macroActions/headerPreviewMacro.fxml").openStream());
            this.gridPaneMacroLibrary.add(paneTest, 1, 1, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    public void updateLibrary() {
        this.observableListMacro.setAll(this.macroHandler.getMacros());
        //this.tableViewMacro.setItems(observableListMacro);
    }

}
