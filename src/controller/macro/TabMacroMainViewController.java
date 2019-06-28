/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import DB.Script;
import controller.TATFrameController;
import controller.util.CommonFunctions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import model.ObjectCopy;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabMacroMainViewController implements Initializable {

    private static TabMacroLibraryController libraryController;
    private static TabMacroNewController newMacroController;
    @FXML
    private AnchorPane anchorPanelViewMacro;
    @FXML
    private TabPane tabPaneMacro;
    private Tab tabMacroLibrary;
    private TabMacroNewController viewMacroController;

    private TabMacroEditController editMacroController;

    private TATFrameController mainFrameController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tabMacroLibrary = new Tab("Library");
        this.tabMacroLibrary.setStyle("-fx-background-color :  #2196f3;"
                + "-fx-background-insets : transparent;");

        this.tabPaneMacro.getTabs().add(0, this.tabMacroLibrary);
        this.tabMacroLibrary.setClosable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/assets/view/macro/TabMacroLibrary.fxml").openStream());
            this.tabMacroLibrary.setContent(libraryPane);
            libraryController = fxmlLoader.getController();
            libraryController.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("Cannot initialize MacroView: ", ex);
        }

        this.tabPaneMacro.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    /**
     * @param aThis
     */
    public void init(TATFrameController aThis) {
        this.mainFrameController = aThis;
    }

    void displayEditMacro(Script macro) {
        Tab editMacro = new Tab("Edit Macro: " + macro.getName());
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane addPane = fxmlLoader.load(getClass().getResource("/assets/view/macro/TabMacroEdit.fxml").openStream());
            editMacro.setContent(addPane);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("Cannot edit Macro: ", ex);
        }
        editMacroController = fxmlLoader.getController();
        editMacroController.init(this);

        ObjectCopy copyHandler = new ObjectCopy();
        Script sc = copyHandler.copyCompleteScript(macro);

        editMacroController.displayMacro(sc);
        this.tabPaneMacro.getTabs().add(editMacro);
        editMacro.setClosable(true);
        this.tabPaneMacro.getSelectionModel().select(editMacro);
    }

    void displayViewMacro(Script macro) {
        Tab viewMacro = new Tab("View Macro: " + macro.getName());
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane addPane = fxmlLoader.load(getClass().getResource("/assets/view/macro/TabMacroNew.fxml").openStream());
            viewMacro.setContent(addPane);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("Cannot assets.view Macro: ", ex);
        }
        viewMacroController = fxmlLoader.getController();
        viewMacroController.init(this);
        viewMacroController.displayMacro(macro);
        this.tabPaneMacro.getTabs().add(viewMacro);
        viewMacro.setClosable(true);
        this.tabPaneMacro.getSelectionModel().select(viewMacro);
    }

    void displayNewMacro() {
        Tab newMacro = new Tab("New Macro");
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane addPane = fxmlLoader.load(getClass().getResource("/assets/view/macro/TabMacroNew.fxml").openStream());
            newMacro.setContent(addPane);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("Cannot create new Macro: ", ex);
        }
        newMacroController = fxmlLoader.getController();
        newMacroController.init(this);
        newMacroController.initNewMacro();
        this.tabPaneMacro.getTabs().add(newMacro);
        newMacro.setClosable(true);
        this.tabPaneMacro.getSelectionModel().select(newMacro);
    }

    /**
     * Close the tab currently displayed to the user.
     */
    public void closeTab() {
        try {
            this.tabPaneMacro.getTabs().remove(this.tabPaneMacro.getSelectionModel().getSelectedItem());
        } catch (Exception ex) {
            CommonFunctions.debugLog.error("Cannot close Macro tab: ", ex);
        }
    }

    /**
     * Select first Macro displayed in the library.
     */
    void focusLibrary() {
        try {
            this.tabPaneMacro.getSelectionModel().select(0);
        } catch (Exception ex) {
            CommonFunctions.debugLog.error("Cannot focus Macro lib: ", ex);
        }
    }

    /**
     * Update Macros in Library.
     */
    public void updateRepository() {
        TabMacroMainViewController.libraryController.updateLibrary();
    }

}
