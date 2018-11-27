/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcase;

import DB.TestCase;
import controller.TATFrameController;
import controller.util.CommonFunctions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import model.ObjectCopy;
import model.currentTab;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Martinez Thibaukt
 */
public class TabTestCaseMainViewController implements Initializable {

    private static TabTestCaseLibraryController libraryController;
    /**
     *
     */
    @FXML
    public AnchorPane anchorPanelViewTestCase;
    @FXML
    private TabPane tabPaneTestCase;
    private ArrayList<currentTab> currentEditTab = new ArrayList<>();
    private ArrayList<currentTab> currentViewTab = new ArrayList<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Tab tabLibrary = new Tab("Library");
        this.tabPaneTestCase.getTabs().add(0, tabLibrary);
        tabLibrary.setClosable(false);
        tabLibrary.setStyle("-fx-background-color : #00bfa5;"
                + "-fx-background-insets : transparent;");
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/assets/view/testcase/TabTestCaseLibrary.fxml").openStream());
            tabLibrary.setContent(libraryPane);
            libraryController = fxmlLoader.getController();
            libraryController.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        this.tabPaneTestCase.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    /**
     * Create a tab edit for the test case put in parameter,
     *
     * @param testcaseEdit test case to create edition tab for.
     */
    void displayEditTab(TestCase testcaseEdit) {
        if (searchPanel(currentEditTab, testcaseEdit.getIdtestCase())) {
            try {
                Tab edit = new Tab();
                FXMLLoader fxmlLoader = new FXMLLoader();
                AnchorPane editPane = fxmlLoader.load(getClass().getResource("/assets/view/testcase/TabTestCaseEdit.fxml").openStream());
                edit.setContent(editPane);
                TabTestCaseEditController editController = fxmlLoader.getController();
                editController.init(this);
                edit.setText("Edit " + testcaseEdit.getTestCaseIdentification());
                this.tabPaneTestCase.getTabs().add(edit);
                ObjectCopy copyHandler = new ObjectCopy();      //Copy content before constructing object.
                TestCase tc = copyHandler.copyCompleteTestCase(testcaseEdit);
                editController.constructInformation(tc);
                edit.setClosable(true);
                currentTab editCurrent = new currentTab(testcaseEdit.getIdtestCase(), edit);
                currentEditTab.add(editCurrent);
                this.tabPaneTestCase.getSelectionModel().select(edit);
                edit.setOnClosed(t -> {
                    t.consume();
                    currentEditTab.remove(editCurrent);
                    CommonFunctions.reportLog.info("User cancel editing " + testcaseEdit.getTestCaseIdentification());
                });
            } catch (IOException ex) {
                CommonFunctions.debugLog.error("", ex);
            }
        }
    }

    /**
     * Create a new tab.
     */
    void displayNewTestCase() {
        try {
            Tab newTestCase = new Tab();
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane newPane = fxmlLoader.load(getClass().getResource("/assets/view/testcase/TabTestCaseNew.fxml").openStream());
            newTestCase.setContent(newPane);
            TabTestCaseNewController newController = fxmlLoader.getController();
            newController.init(this);
            newTestCase.setText("New Test Case");
            this.tabPaneTestCase.getTabs().add(newTestCase);
            newTestCase.setClosable(true);
            this.tabPaneTestCase.getSelectionModel().select(newTestCase);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

    /**
     * Display a new tab assets.view for the test case given in parameter.
     *
     * @param testCaseView test case to assets.view in new tab.
     */
    public void displayViewTab(TestCase testCaseView) {
        if (searchPanel(currentViewTab, testCaseView.getIdtestCase())) {
            try {
                Tab view = new Tab();
                FXMLLoader fxmlLoader = new FXMLLoader();
                AnchorPane editPane = fxmlLoader.load(getClass().getResource("/assets/view/testcase/TabTestCaseView.fxml").openStream());
                view.setContent(editPane);
                TabTestCaseViewController viewController = fxmlLoader.getController();
                viewController.init(this);
                view.setText("View " + testCaseView.getTestCaseIdentification());
                this.tabPaneTestCase.getTabs().add(view);
                viewController.constructInformation(testCaseView);
                view.setClosable(true);
                currentTab viewCurrent = new currentTab(testCaseView.getIdtestCase(), view);
                currentViewTab.add(viewCurrent);
                this.tabPaneTestCase.getSelectionModel().select(view);
                view.setOnClosed(t -> {
                    t.consume();
                    currentViewTab.remove(viewCurrent);
                    CommonFunctions.reportLog.info("User cancel viewing " + testCaseView.getTestCaseIdentification());
                });
            } catch (IOException ex) {
                CommonFunctions.debugLog.error("", ex);
            }
        }
    }

    /**
     * Search if the id of the object is already display in either the edit
     * assets.view(Arraylist edit) or in the assets.view mode (arrayList assets.view).
     *
     * @param currentTab arraylist of the tab to search into.
     * @param tabID      id of the object to lookfor.
     * @return
     */
    private boolean searchPanel(ArrayList<currentTab> currentTab, int tabID) {
        boolean found = false;
        int i = 0;
        while (i < currentTab.size() && !found) {
            if (currentTab.get(i).getID() == tabID) {
                found = true;
                this.tabPaneTestCase.getSelectionModel().select(currentTab.get(i).getTab());
            }
            i++;
        }
        return !found;
    }

    /**
     * @param mainController
     */
    public void init(TATFrameController mainController) {
    }

    /**
     * Call the function updateLibrary.
     *
     * @see TabTestCaseLibraryController
     */
    public void updateLibrary() {
        libraryController.updateLibrary();
    }

    /**
     * Close the tab currently displayed to the user.
     */
    public void closeTab() {
        this.tabPaneTestCase.getTabs().remove(this.tabPaneTestCase.getSelectionModel().getSelectedItem());
    }

    void focusOnLast() {
        this.tabPaneTestCase.getSelectionModel().select(0);
        libraryController.focusOnLast();
    }

}
