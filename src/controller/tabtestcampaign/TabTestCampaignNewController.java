/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcampaign;

import DB.TestCampaign;
import DB.TestCase;
import DBcontroller.TestCampaignDB;
import DBcontroller.TestCaseDB;
import controller.popup.PopUpCaseSelectionController;
import controller.util.CommonFunctions;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Main;
import model.initColumn;
import model.setCursorOnComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabTestCampaignNewController implements Initializable {

    private final ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();
    private final TestCaseDB testCaseHandler = new TestCaseDB();
    private final TestCampaignDB testCampaignHandler = new TestCampaignDB();
    /**
     *
     */
    public TabTestCampaignMainViewController main;
    @FXML
    private AnchorPane anchorPanelNewTestCampaign;
    @FXML
    private GridPane gridPaneAddCampaign;
    @FXML
    private TableView<TestCase> TableViewTestCasesAdded;
    @FXML
    private GridPane gridPaneButtonAddCampaign;
    @FXML
    private Button buttonAddCase;
    @FXML
    private Button buttonValidCampaign;
    @FXML
    private GridPane gridPaneFieldsAddCampaign;
    @FXML
    private Label labelReferenceAddCampaign;
    @FXML
    private Label labelSystemAddCampaign;
    @FXML
    private Label labelWriterAddCampaign;
    @FXML
    private Label labelSUTReleaseAddCampaign;
    @FXML
    private Label labelRegressionThreadAddCampaign;
    @FXML
    private Label labelVersionAddCampaign;
    @FXML
    private Label labelCreationDateAddCampaign;
    @FXML
    private Label labelEditionDateAddCampaign;
    @FXML
    private Label labelNumberCasesAddCampaign;
    @FXML
    private Label labelWriterMailAddCampaign;
    @FXML
    private Label labelDescriptionAddCampaign;
    @FXML
    private Label labelCommentsAddCampaign;
    @FXML
    private TextField jtextfieldReferenceAddCampaign;
    @FXML
    private TextField jtextfieldSystemAddCampaign;
    @FXML
    private TextField jtextfieldWriterAddCampaign;
    @FXML
    private TextField jtextfieldVersionAddCampaign;
    @FXML
    private TextField jtextfieldCreationDateAddCampaign;
    @FXML
    private TextField jtextfieldEditionDateAddCampaign;
    @FXML
    private TextField jtextfieldSUTReleaseAddCampaign;
    @FXML
    private TextField jtextfieldCasesNumberAddCampaign;
    @FXML
    private TextField jtextfieldWriterMailAddCampaign;
    @FXML
    private TextArea textareaCommentsAddCampaign;
    @FXML
    private CheckBox checkboxRegressionThreadAddCampaign;
    @FXML
    private Button buttonDeleteCase;
    @FXML
    private Button buttonUp;
    @FXML
    private Button buttonDown;
    private Stage dialogStage;

    // index of the current test case selected
    private int index;

    private boolean popUpOpen = false;

    private boolean canBeValidate = false;

    private Alert alert;

    //TextField Max Length
    private int textfieldReferenceCampaignMaxLength = 11;
    private int textfieldSystemCampaignMaxLength = 20;
    private int textfieldWriterCampaignMaxLength = 20;
    private int textfieldSUTReleaseCampaignMaxLength = 20;
    private int textfieldWritermailCampaignMaxLength = 50;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // System.out.println("init");
        this.initButtons();
        this.fillNonEditableFields();
        this.loadCSS();
        this.initContextMenu();
        this.jtextfieldWriterAddCampaign.setText(Main.currentUser.getName());
        this.jtextfieldWriterMailAddCampaign.setText(Main.currentUser.getEmail());
        // init of the TableView
        initColumn campaignColumnInit = new initColumn();
        campaignColumnInit.initColumnCase(TableViewTestCasesAdded);
        TableViewTestCasesAdded.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableViewTestCasesAdded.setTableMenuButtonVisible(true);
        TableViewTestCasesAdded.setItems(observableListTestCase);

        /*
         * Listener in order to perform actions after mousePressed on the table assets.view
         * test cases if only 1click, get the index of the case selected and
         * Disable/Enable the buttons up and down if needed if 2 click, open the test
         * case selected in a assets.view tab.
         */
        this.TableViewTestCasesAdded.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 1 && TableViewTestCasesAdded.getSelectionModel().getSelectedItem() != null) {
                index = TableViewTestCasesAdded.getSelectionModel().getSelectedIndex();
                buttonDeleteCase.setDisable(false);
                buttonUpDownManagement();

            } else if (event.getClickCount() == 2
                    && TableViewTestCasesAdded.getSelectionModel().getSelectedItem() != null) {
                viewTestCase(TableViewTestCasesAdded.getSelectionModel().getSelectedItem());
            }
        });

        this.jtextfieldReferenceAddCampaign.textProperty().addListener(
                (final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
                    if (CommonFunctions.displayWarningIncorrectInputFormat("Campaign ID", textfieldReferenceCampaignMaxLength,
                            newValue.length() > textfieldReferenceCampaignMaxLength)) {
                        this.jtextfieldReferenceAddCampaign.setText(oldValue);
                    }
                    changeColorLabel(labelReferenceAddCampaign, newValue);
                });

        this.jtextfieldSystemAddCampaign.textProperty().addListener(
                (final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
                    if (CommonFunctions.displayWarningIncorrectInputFormat("System", textfieldSystemCampaignMaxLength,
                            newValue.length() > textfieldSystemCampaignMaxLength)) {
                        this.jtextfieldSystemAddCampaign.setText(oldValue);
                    }
                });

        this.jtextfieldWriterAddCampaign.textProperty().addListener(
                (final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
                    if (CommonFunctions.displayWarningIncorrectInputFormat("Writer", textfieldWriterCampaignMaxLength,
                            newValue.length() > textfieldWriterCampaignMaxLength)) {
                        this.jtextfieldWriterAddCampaign.setText(oldValue);
                    }
                    changeColorLabel(this.labelWriterAddCampaign, newValue);
                });

        this.jtextfieldSUTReleaseAddCampaign.textProperty().addListener(
                (final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
                    if (CommonFunctions.displayWarningIncorrectInputFormat("SUT Release", textfieldSUTReleaseCampaignMaxLength,
                            newValue.length() > textfieldSUTReleaseCampaignMaxLength)) {
                        this.jtextfieldSUTReleaseAddCampaign.setText(oldValue);
                    }
                });

        this.jtextfieldWriterMailAddCampaign.textProperty().addListener(
                (final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
                    if (CommonFunctions.displayWarningIncorrectInputFormat("Writer Email", textfieldWritermailCampaignMaxLength,
                            newValue.length() > textfieldWritermailCampaignMaxLength)) {
                        this.jtextfieldWriterMailAddCampaign.setText(oldValue);
                    }
                });
        // Define a new cursor when an action is available for the user
        defineCursor();

    }

    /**
     * Open a popUp in order to add Test Case in the campaign
     */
    private void addCases() {
        try {
            ObservableList<TestCase> CasesInDB = FXCollections
                    .observableArrayList(new ArrayList<TestCase>(testCaseHandler.getAllTestCases()));
            FXMLLoader fxmlLoader = new FXMLLoader();
            AnchorPane editPane = fxmlLoader
                    .load(getClass().getResource("/assets/view/popup/popUpCaseSelection.fxml").openStream());
            dialogStage = new Stage();
            dialogStage.setTitle("Case Selection");
            dialogStage.initOwner(Main.primaryStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(editPane);
            dialogStage.setScene(scene);

            PopUpCaseSelectionController controller = fxmlLoader.getController();
            controller.init(this);
            controller.setTable(CasesInDB);

            dialogStage.setOnCloseRequest((WindowEvent event) -> {
                this.closePopUp();
            });
            dialogStage.show();
            dialogStage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - dialogStage.getWidth() / 2);
            dialogStage
                    .setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - dialogStage.getHeight() / 2);
            popUpOpen = true;
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

    /*
     * Action performed after closing the popUp to add a case. The actions are:
     * Adding the cases selected in the tableView case and update the number of
     * cases in the right field.
     *
     * @param CasesSelected
     */
    public void setAction(ObservableList<TestCase> CasesSelected) {
        observableListTestCase.addAll(CasesSelected);
        this.jtextfieldCasesNumberAddCampaign.setText(String.valueOf(observableListTestCase.size()));
        buttonUpDownManagement();
    }

    /**
     * initialize the reference of the class parent
     * TabTestCampaignMainViewController to this controller.
     *
     * @param mainController the reference to the parent class
     */
    public void init(TabTestCampaignMainViewController mainController) {
        this.main = mainController;

    }

    /**
     * Method in order to call the method assets.view test case from the mainFrame
     *
     * @param testCaseToView the test case to display
     */
    public void viewTestCase(TestCase testCaseToView) {
        this.main.viewTestCase(testCaseToView);
    }

    /**
     * Method called to close the popUp of case selection
     */
    public void closePopUp() {
        if (popUpOpen) {
            this.dialogStage.close();
            popUpOpen = false;
        }

    }

    /**
     * Method called to focus on the popUp of case selection
     */
    void focusOnPopUp() {
        if (popUpOpen) {
            dialogStage.requestFocus();
        }
    }

    /**
     * management in order to management the disable/enable of up and down
     * buttons
     */
    private void buttonUpDownManagement() {
        if (observableListTestCase.size() == 1 || index == -2) {
            this.buttonUp.setDisable(true);
            this.buttonDown.setDisable(true);
        } else if (index == 0) {
            this.buttonUp.setDisable(true);
            this.buttonDown.setDisable(false);
        } else if (index == observableListTestCase.size() - 1) {
            this.buttonDown.setDisable(true);
            this.buttonUp.setDisable(false);
        } else {
            this.buttonUp.setDisable(false);
            this.buttonDown.setDisable(false);
        }
    }

    /**
     * define a new cursor for buttons where an action is possible
     */
    private void defineCursor() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.buttonAddCase);
        nodeHand.add(this.buttonDeleteCase);
        nodeHand.add(this.buttonDown);
        nodeHand.add(this.buttonDown);
        nodeHand.add(this.buttonValidCampaign);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     * create the new campaign and call the method to put this campaign in DB
     */
    private void createNewCampaign() {
        TestCampaign newTestCampaign = new TestCampaign();
        newTestCampaign.setReference(this.jtextfieldReferenceAddCampaign.getText()); // CHANGE IN STRING
        newTestCampaign.setSystem(this.jtextfieldSystemAddCampaign.getText());
        newTestCampaign.setWritter(this.jtextfieldWriterAddCampaign.getText());
        newTestCampaign.setCampaignVersion(1);
        newTestCampaign.setComments(this.textareaCommentsAddCampaign.getText());
        newTestCampaign.setCreationDate(Main.df.format(new Date()));
        newTestCampaign.setSoftwareSutRelease(this.jtextfieldSUTReleaseAddCampaign.getText());
        newTestCampaign.setNumberTestCase(observableListTestCase.size());
        newTestCampaign.setRegressionThread((byte) (this.checkboxRegressionThreadAddCampaign.isSelected() ? 1 : 0));
        newTestCampaign.setWriterEmail(this.jtextfieldWriterMailAddCampaign.getText());
        ArrayList<TestCase> testCaseCampaign = new ArrayList<>(observableListTestCase);
        testCampaignHandler.CreateCampaign(newTestCampaign, testCaseCampaign);
    }

    /**
     * verify if the reference field of the new campaign is empty
     *
     * @return 0 is the reference field is empty
     */
    private boolean minimumInformationTestCampaign() {
        canBeValidate = !jtextfieldReferenceAddCampaign.getText().isEmpty() && !this.jtextfieldWriterAddCampaign.getText().isEmpty();
        return canBeValidate;
    }

    /**
     * change the color of the label put in parameter regarding the value of
     * newValue if new value is empty, label will be displayed in red. Otherwise
     * the label will be displayed in black
     *
     * @param label    the label for which change the color
     * @param newValue the string to check
     */
    private void changeColorLabel(Label label, String newValue) {
        label.setTextFill(newValue.isEmpty() ? Color.RED : Color.BLACK);
        minimumInformationTestCampaign();
        this.buttonValidCampaign.setDisable(!canBeValidate);
    }

    /**
     * initialize the buttons of the assets.view
     */
    private void initButtons() {

        this.buttonDeleteCase.setDisable(true);
        this.buttonDown.setDisable(true);
        this.buttonUp.setDisable(true);

        this.buttonValidCampaign.setDisable(true);

        buttonAddCase.setOnAction((ActionEvent event) -> {
            if (popUpOpen) {
                dialogStage.requestFocus();
            } else {
                popUpOpen = true;
                this.addCases();
            }
        });

        buttonUp.setOnAction((ActionEvent event) -> {
            Collections.swap(observableListTestCase, index, index - 1);
            index--;
            TableViewTestCasesAdded.getSelectionModel().select(index);
            buttonUpDownManagement();
        });

        buttonDown.setOnAction((ActionEvent event) -> {
            Collections.swap(observableListTestCase, index, index + 1);
            index++;
            TableViewTestCasesAdded.getSelectionModel().select(index);
            buttonUpDownManagement();
        });

        buttonDeleteCase.setOnAction((ActionEvent event) -> {
            observableListTestCase.remove(TableViewTestCasesAdded.getSelectionModel().getSelectedItem());
            buttonDeleteCase.setDisable(true);
            TableViewTestCasesAdded.getSelectionModel().select(null);
            index = -2;
            buttonUpDownManagement();
            this.jtextfieldCasesNumberAddCampaign.setText(String.valueOf(observableListTestCase.size()));
        });

        buttonValidCampaign.setOnAction((ActionEvent e) -> {
            if (minimumInformationTestCampaign()) {
                this.createNewCampaign();
                main.updateRepository();
                main.closeTab();
                main.focusRepository();
            }
        });
    }

    /**
     * automaticaly fill all the non editable fields of the assets.view
     */
    private void fillNonEditableFields() {
        int NumberOfCases = 0;
        this.jtextfieldCreationDateAddCampaign.setText(Main.df.format(new Date()));
        this.jtextfieldCreationDateAddCampaign.setId("displayStyle");
        this.jtextfieldCasesNumberAddCampaign.setText(String.valueOf(NumberOfCases));
        this.jtextfieldCasesNumberAddCampaign.setId("displayStyle");
        this.jtextfieldVersionAddCampaign.setText("1.0");
        this.jtextfieldVersionAddCampaign.setId("displayStyle");
        this.jtextfieldEditionDateAddCampaign.setId("displayStyle");

        this.labelReferenceAddCampaign.setTextFill(Color.RED);
        this.labelWriterAddCampaign.setTextFill(Color.RED);
    }

    /**
     * Add css sheet to the anchorPane of the assets.view
     */
    private void loadCSS() {
        this.anchorPanelNewTestCampaign.getStylesheets().add("/assets/view/testexecution/cssLibraryTestCase.css");
    }

    /**
     * initialize the context menu in the table assets.view test case
     */
    private void initContextMenu() {

        ContextMenu menu = new ContextMenu();
        MenuItem addCase = new MenuItem("Add Case");
        MenuItem deleteCase = new MenuItem("Delete Case");
        menu.getItems().add(addCase);
        menu.getItems().add(deleteCase);

        TableViewTestCasesAdded.setContextMenu(menu);

        deleteCase.setOnAction((ActionEvent event) -> {
            observableListTestCase.remove(TableViewTestCasesAdded.getSelectionModel().getSelectedItem());
            this.jtextfieldCasesNumberAddCampaign.setText(String.valueOf(observableListTestCase.size()));
        });

        addCase.setOnAction((ActionEvent event) -> {
            if (popUpOpen) {
                dialogStage.requestFocus();
            } else {
                this.addCases();
                popUpOpen = true;
            }
        });

    }
}
