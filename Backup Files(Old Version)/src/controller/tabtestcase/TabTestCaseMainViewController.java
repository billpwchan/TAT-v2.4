/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcase;

import DB.TestCase;
import controller.TATFrameController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import model.ObjectCopy;
import model.currentTab;
import org.apache.log4j.Logger;

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

    /**
     *
     */
    @FXML
    public AnchorPane anchorPanelViewTestCase;
    @FXML
    private TabPane tabPaneTestCase;

    private Tab tabLibrary;

    private ArrayList<currentTab> currentEditTab = new ArrayList<>();

    private ArrayList<currentTab> currentViewTab = new ArrayList<>();

    private static TabTestCaseLibraryController libraryController;

    private static TabTestCaseEditController editController;

    private TabTestCaseViewController viewController;

    private TabTestCaseNewController newController;

    private TATFrameController mainFrameController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.tabLibrary = new Tab("Library");
        this.tabPaneTestCase.getTabs().add(0, tabLibrary);
        this.tabLibrary.setClosable(false);
        this.tabLibrary.setStyle("-fx-background-color : #00bfa5;"
                + "-fx-background-insets : transparent;");
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane libraryPane = fxmlLoader.load(getClass().getResource("/view/testcase/TabTestCaseLibrary.fxml").openStream());
            this.tabLibrary.setContent(libraryPane);
            libraryController = (TabTestCaseLibraryController) fxmlLoader.getController();
            libraryController.init(this);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseMainViewController.class.getName()).error("", ex);
        }

        this.tabPaneTestCase.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

//        this.tabPaneTestCase.getSelectionModel().selectedItemProperty().addListener(
//                new ChangeListener<Tab>() {
//
//                    @Override
//                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
//                        System.out.println(newValue + ":" + newValue.getId() + ":" + newValue.getText());
//                        if (newValue == tabLibrary) {
//                            
//                        }
//                    }
//
//                }
//        );
        /**
         * Update the library when focus on the library panel.
         */
//        this.tabLibrary.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
//                if (newPropertyValue) {
//                    updateLibrary();
//                }
//            }
//        }
//        );
//        
//                this.tabPaneTestCase.getSelectionModel().selectedItemProperty().addListener(
//                new ChangeListener<Tab>() {
//
//                    @Override
//                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
//
//                        if (newValue == tabLibrary) {
//                            //System.out.println("UPDATE !");
//                            updateLibrary();
//                        }
//                    }
//
//                }
//        );
    }

    /**
     * Create a tab edit for the test case put in parameter,
     *
     * @param testcaseEdit test case to create edition tab for.
     */
    public void displayEditTab(TestCase testcaseEdit) {
        if (!searchPanel(currentEditTab, testcaseEdit.getIdtestCase())) {
            try {
                Tab edit = new Tab();
                FXMLLoader fxmlLoader = new FXMLLoader();
                AnchorPane editPane = fxmlLoader.load(getClass().getResource("/view/testcase/TabTestCaseEdit.fxml").openStream());
                edit.setContent(editPane);
                editController = (TabTestCaseEditController) fxmlLoader.getController();
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
                edit.setOnClosed(new EventHandler<Event>() {
                    @Override
                    public void handle(Event t) {
                        t.consume();
                        currentEditTab.remove(editCurrent);
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(TabTestCaseMainViewController.class.getName()).error("", ex);
            }
        }
    }

    /**
     * Create a new tab.
     */
    public void displayNewTestCase() {
//        createOrchestra test = new createOrchestra();
//        TestCampaignDB test2 = new TestCampaignDB();
//        
//        test.generateExcelRapport(test2.getTestCampaignFromID(13), "base-V1", 3);
        try {

            Tab newTestCase = new Tab();
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane newPane = fxmlLoader.load(getClass().getResource("/view/testcase/TabTestCaseNew.fxml").openStream());
            newTestCase.setContent(newPane);
            newController = (TabTestCaseNewController) fxmlLoader.getController();
            newController.init(this);
            newTestCase.setText("New Test Case");
            this.tabPaneTestCase.getTabs().add(newTestCase);
            newTestCase.setClosable(true);
            this.tabPaneTestCase.getSelectionModel().select(newTestCase);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseMainViewController.class.getName()).error("", ex);
        }
    }

    /**
     *
     */
    public void deleteTestCase() {
        System.out.println("Not implemented yet ");
    }

    /**
     * Display a new tab view for the test case given in parameter.
     *
     * @param testCaseView test case to view in new tab.
     */
    public void displayViewTab(TestCase testCaseView) {
        if (!searchPanel(currentViewTab, testCaseView.getIdtestCase())) {
            try {
                Tab view = new Tab();
                FXMLLoader fxmlLoader = new FXMLLoader();
                AnchorPane editPane = fxmlLoader.load(getClass().getResource("/view/testcase/TabTestCaseView.fxml").openStream());
                view.setContent(editPane);
                viewController = (TabTestCaseViewController) fxmlLoader.getController();
                viewController.init(this);
                view.setText("View " + testCaseView.getTestCaseIdentification());
                this.tabPaneTestCase.getTabs().add(view);
                viewController.constructInformation(testCaseView);
                view.setClosable(true);
                currentTab viewCurrent = new currentTab(testCaseView.getIdtestCase(), view);
                currentViewTab.add(viewCurrent);
                this.tabPaneTestCase.getSelectionModel().select(view);
                view.setOnClosed(new EventHandler<Event>() {
                    @Override
                    public void handle(Event t) {
                        t.consume();
                        currentViewTab.remove(viewCurrent);
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(TabTestCaseMainViewController.class.getName()).error("", ex);
            }
        }
    }

    /**
     * Search if the id of the object is already display in either the edit
     * view(Arraylist edit) or in the view mode (arrayList view).
     *
     * @param currentTab arraylist of the tab to search into.
     * @param tabID id of the object to lookfor.
     * @return
     */
    private boolean searchPanel(ArrayList<currentTab> currentTab, int tabID) {
        boolean found = false;
        int i = 0;
        while (i < currentTab.size() && found == false) {
            if (currentTab.get(i).getID() == tabID) {
                found = true;
                this.tabPaneTestCase.getSelectionModel().select(currentTab.get(i).getTab());
            }
            i++;
        }
        return found;
    }

    /**
     *
     * @param mainController
     */
    public void init(TATFrameController mainController) {
        this.mainFrameController = mainController;
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
