/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import DB.Script;
import controller.TATFrameController;
import controller.requirements.TabRequirementMainViewController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabMacroMainViewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelViewMacro;
    @FXML
    private TabPane tabPaneMacro;

    private Tab tabMacroLibrary;

    private static TabMacroLibraryController libraryController;

    private static TabMacroNewController newMacroController;

    private TabMacroNewController viewMacroController;

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
            AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/view/macro/TabMacroLibrary.fxml").openStream());
            this.tabMacroLibrary.setContent(libraryPane);
            libraryController = (TabMacroLibraryController) fxmlLoader.getController();
            libraryController.init(this);
        } catch (IOException ex) {
            Logger.getLogger(TabRequirementMainViewController.class.getName()).error("", ex);
        }

        this.tabPaneMacro.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    public void init(TATFrameController aThis) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void displayViewMacro(Script macro) {

        Tab viewMacro = new Tab("view Macro");
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane addPane = fxmlLoader.load(getClass().getResource("/view/macro/TabMacroNew.fxml").openStream());
            viewMacro.setContent(addPane);
        } catch (IOException ex) {
            Logger.getLogger(TabMacroMainViewController.class.getName()).error("", ex);
        }
        viewMacroController = (TabMacroNewController) fxmlLoader.getController();
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
            AnchorPane addPane = fxmlLoader.load(getClass().getResource("/view/macro/TabMacroNew.fxml").openStream());
            newMacro.setContent(addPane);
        } catch (IOException ex) {
            Logger.getLogger(TabMacroMainViewController.class.getName()).error("", ex);
        }
        newMacroController = (TabMacroNewController) fxmlLoader.getController();
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
        this.tabPaneMacro.getTabs().remove(this.tabPaneMacro.getSelectionModel().getSelectedItem());
    }

    public void focusLibrary() {
        this.tabPaneMacro.getSelectionModel().select(0);
    }

    public void updateRepository() {
        this.libraryController.updateLibrary();
    }

}
