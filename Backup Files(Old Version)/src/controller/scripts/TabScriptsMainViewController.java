/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.scripts;

import controller.TATFrameController;
import controller.requirements.TabRequirementMainViewController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class TabScriptsMainViewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelViewMacro;
    @FXML
    private TabPane tabPaneMacro;

    private Tab tabScriptsLibrary;

    private TabScriptsLibraryController libraryController;

    private TabScriptNewController newScriptController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tabScriptsLibrary = new Tab("Library");
        this.tabScriptsLibrary.setStyle("-fx-background-color :  #ba68c8;"
                + "-fx-background-insets : transparent;");

        this.tabPaneMacro.getTabs().add(0, this.tabScriptsLibrary);
        this.tabScriptsLibrary.setClosable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/view/scriptmanagement/TabScriptsLibrary.fxml").openStream());
            //AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/view/scriptmanagement/TabScriptsLibrary.fxml").openStream());
            this.tabScriptsLibrary.setContent(libraryPane);
            libraryController = (TabScriptsLibraryController) fxmlLoader.getController();
            libraryController.init(this);
        } catch (IOException ex) {
            Logger.getLogger(TabRequirementMainViewController.class.getName()).error("", ex);
        }

        this.tabPaneMacro.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    /**
     *
     * @param aThis
     */
    public void init(TATFrameController aThis) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void displayNewScript() {
        Tab newScript = new Tab("New Script");
        this.tabPaneMacro.getTabs().add(newScript);
        AnchorPane addPane = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            addPane = fxmlLoader.load(getClass().getResource("/view/scriptmanagement/TabScriptNew.fxml").openStream());
        } catch (IOException ex) {
            Logger.getLogger(TabScriptsMainViewController.class.getName()).error("", ex);
        }
        newScript.setContent(addPane);
        newScriptController = (TabScriptNewController) fxmlLoader.getController();

        newScriptController.init(this);
        newScriptController.initNewScript();
        newScript.setClosable(true);
        this.tabPaneMacro.getSelectionModel().select(newScript);
    }

    /**
     * Methode to remove a tab from the list of tabpane when the tab should be
     * closed
     */
    public void closeTab() {
        this.tabPaneMacro.getTabs().remove(this.tabPaneMacro.getSelectionModel().getSelectedItem());
    }

    /**
     *
     */
    public void updateRepository() {
        this.libraryController.updateLibrary();
    }

    /**
     *
     */
    public void focusLibrary() {
        this.tabPaneMacro.getSelectionModel().select(0);
    }
}
