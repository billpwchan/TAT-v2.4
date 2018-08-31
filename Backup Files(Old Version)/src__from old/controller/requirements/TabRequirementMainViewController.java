/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.requirements;

import controller.TATFrameController;
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
 * @author T0155040
 */
public class TabRequirementMainViewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelViewTestCase;
    @FXML
    private TabPane tabPaneRequirement;

    private Tab tabRequirementLibrary;

    private static TabRequirementLibraryController libraryController;

    private TabRequirementCreationController newController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tabRequirementLibrary = new Tab("Requirements");
        this.tabRequirementLibrary.setStyle("-fx-background-color :  #e91e63;"
                + "-fx-background-insets : transparent;");

        this.tabPaneRequirement.getTabs().add(0, this.tabRequirementLibrary);
        this.tabRequirementLibrary.setClosable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/view/requirements/TabRequirementLibrary.fxml").openStream());
            this.tabRequirementLibrary.setContent(libraryPane);
            libraryController = (TabRequirementLibraryController) fxmlLoader.getController();
            libraryController.init(this);
        } catch (IOException ex) {
            Logger.getLogger(TabRequirementMainViewController.class.getName()).error("", ex);
        }

        this.tabPaneRequirement.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    public void init(TATFrameController aThis) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void displayNewRequirement() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Tab newRequirement = new Tab("New Requirement");
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane addPane = fxmlLoader.load(getClass().getResource("/view/requirements/TabRequirementCreation.fxml").openStream());
            newRequirement.setContent(addPane);
        } catch (IOException ex) {
            Logger.getLogger(TabRequirementMainViewController.class.getName()).error("", ex);
        }
        newController = (TabRequirementCreationController) fxmlLoader.getController();
        newController.init(this);
        this.tabPaneRequirement.getTabs().add(newRequirement);
        newRequirement.setClosable(true);
        this.tabPaneRequirement.getSelectionModel().select(newRequirement);
    }

    public void updateRepository() {
        TabRequirementMainViewController.libraryController.updateRepository();
    }

    void closeTab() {
        this.tabPaneRequirement.getTabs().remove(this.tabPaneRequirement.getSelectionModel().getSelectedItem());
    }

}
