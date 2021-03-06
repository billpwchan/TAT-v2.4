/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestexecution;

import DB.Iterations;
import DB.TestCampaign;
import controller.TATFrameController;
import controller.util.CommonFunctions;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import model.currentTab;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author tmorin
 */
public class TabTestCampaignExecutionMainViewController implements Initializable {

    private final ArrayList<currentTab> currentBaselineTab = new ArrayList<>();
    private final Hashtable<Tab, TabTestCampaignExecutionBaselineCampaignController> storeBaselineControler = new Hashtable<>();
    @FXML
    private TabPane tabPaneTestCampaignExecution;
    private Tab campaigns;
    private TabTestCampaignExecutionRepositoryBaselineController repositoryBaselineController;
    private TATFrameController mainFrameController;
    private TabTestCampaignExecutionBaselineCampaignController baselineController;
    private TabViewResultsController viewController;
    private int index;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.campaigns = new Tab("Baselined test campaigns");
        this.campaigns.setStyle("-fx-background-color : #ffee58;"
                + "-fx-background-insets : transparent;");
        this.tabPaneTestCampaignExecution.getTabs().add(0, campaigns);
        this.campaigns.setClosable(false);
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            AnchorPane campaignsPane = fxmlLoader.load(getClass().getResource("/assets/view/testexecution/TabTestCampaignExecutionRepositoryBaseline.fxml").openStream());
            this.campaigns.setContent(campaignsPane);
            repositoryBaselineController = fxmlLoader.getController();
            repositoryBaselineController.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        this.tabPaneTestCampaignExecution.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        this.tabPaneTestCampaignExecution.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            //if (newValue == campaigns) {
            //   updateRepository();
            // }
        });

    }

    /**
     * Open a new tab to create a baseline of a particular campaign
     *
     * @param campaign the campaign to baseline
     */
    void createBaseline(TestCampaign campaign) {
        if (!searchPanel(currentBaselineTab, campaign.getIdtestCampaign())) {
            try {
                CommonFunctions.reportLog.info("User open baseline: " + campaign.getReference());
                Tab view = new Tab();
                FXMLLoader fxmlLoader = new FXMLLoader();
                AnchorPane baselinePane = fxmlLoader.load(getClass().getResource("/assets/view/testexecution/TabTestCampaignExecutionBaselineCampaign.fxml").openStream());
                view.setContent(baselinePane);
                baselineController = fxmlLoader.getController();
                baselineController.init(this);
                view.setText("Baseline : " + campaign.getReference());
                this.tabPaneTestCampaignExecution.getTabs().add(view);
                baselineController.constructInformation(campaign);
                view.setClosable(true);
                currentTab BaselineCurrent = new currentTab(campaign.getIdtestCampaign(), view);
                currentBaselineTab.add(BaselineCurrent);
                storeBaselineControler.put(view, baselineController);
                this.tabPaneTestCampaignExecution.getSelectionModel().select(view);
                view.setOnClosed((Event t) -> {
                    t.consume();
                    storeBaselineControler.get(view).closeWithoutValid();
                    currentBaselineTab.remove(BaselineCurrent);
                });
            } catch (IOException ex) {
                CommonFunctions.debugLog.error("", ex);
            }
        }
    }

    /**
     * Open a tab to assets.view the result of an execution
     *
     * @param iteration
     */
    void displayViewTab(Iterations iteration) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                viewController.setParameters(iteration);
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        Tab view = new Tab();
        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane editPane = null;
        try {
            editPane = fxmlLoader.load(getClass().getResource("/assets/view/testexecution/ViewResults.fxml").openStream());
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        view.setContent(editPane);
        viewController = fxmlLoader.getController();
        viewController.init(this);
        CommonFunctions.reportLog.info("User assets.view Iteration: " + iteration.getBaselineId());
        view.setText("View : " + iteration.getBaselineId());
        this.tabPaneTestCampaignExecution.getTabs().add(view);
        th.start();
        view.setClosable(true);
        this.tabPaneTestCampaignExecution.getSelectionModel().select(view);
    }

    /**
     * Method to search if the campaign given in parameter is already open for
     * viewing
     *
     * @param currentTab the arraylist of tab opened
     * @param ID         the ID of the campaign to search in the arraylist
     * @return if the campaign is in the tab
     */
    private boolean searchPanel(ArrayList<currentTab> currentTab, int ID) {
        boolean found = false;
        int i = 0;
        while (i < currentTab.size() && !found) {
            if (currentTab.get(i).getID() == ID) {
                found = true;
                index = i;
                this.tabPaneTestCampaignExecution.getSelectionModel().select(currentTab.get(i).getTab());
            }
            i++;
        }
        return found;
    }

    /**
     * update the repository of test executions
     *
     * @throws ParseException
     */
    public void updateBaselineTree() throws ParseException {
        this.repositoryBaselineController.updateRepository();
        //this.repositoryBaselineController.UpdateTreeItem(campaignID);
    }

    /**
     * close of a baseline tab, remove the tab from the arrayList where baseline
     * tabs are stored
     *
     * @param ID the id of the campaign for which close the tab
     */
    public void closeTab(int ID) {
        searchPanel(currentBaselineTab, ID);
        boolean j = this.currentBaselineTab.remove(this.currentBaselineTab.get(index));
        this.tabPaneTestCampaignExecution.getTabs().remove(this.tabPaneTestCampaignExecution.getSelectionModel().getSelectedItem());
        CommonFunctions.reportLog.info("User close Baseline Tab.");
    }

    /**
     * Method to initialize the global variable mainController with the
     * TATFrameController given as param
     *
     * @param mainController the TATFrameController to associate
     */
    public void init(TATFrameController mainController) {
        this.mainFrameController = mainController;
    }

    /**
     * Call from the parent controller the method to open a campaign assets.view tab
     *
     * @param testCampaign the campaign to assets.view
     */
    void viewTestCampaign(TestCampaign testCampaign) {
        this.mainFrameController.callViewToTestCampaign(testCampaign);
    }

    /**
     * getter of the parent controller
     *
     * @return the parent controller
     */
    public TATFrameController getMainController() {
        return this.mainFrameController;
    }
}
