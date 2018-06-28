/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tablestep;

import DB.Requirement;
import DB.StepExecutions;
import DB.TestStep;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import model.setCursorOnComponent;

/**
 * FXML Controller class Class handling all the action happening the the view
 * test step.
 *
 * @author Martinez Thibault
 */
public class StepLineTableStepController implements Initializable {

    @FXML
    private AnchorPane anchorPaneStep;
    @FXML
    private GridPane gridPaneStep;
    @FXML
    private Label idStep;
    @FXML
    private TextArea textAreaAction;
    @FXML
    private TextArea textAreaVerif;
    @FXML
    private CheckBox blockingStep;
    @FXML
    private ImageView imageUp;
    @FXML
    private ImageView imageDown;
    @FXML
    private ImageView imageExpand;
    @FXML
    private Pane paneAction;
    @FXML
    private Pane paneVerif;
    @FXML
    private TableView tableRequirements;
    @FXML
    private Label labelVerif;
    @FXML
    private Label labelAction;
    @FXML
    private ListView listRequirement;
    @FXML
    private ListView listPCR;
    @FXML
    private Label labelCommentStep;
    @FXML
    private ImageView imageTrash;
    @FXML
    private AnchorPane anchorPaneVerif;
    @FXML
    private AnchorPane anchorPaneAction;
    @FXML
    private Label labelActionArea;
    @FXML
    private Label labelVerifArea;

    private static final Image imd = new Image("images/imageDown.png");

    private static final Image ime = new Image("images/arrowDown.png");

    private static final Image deilete = new Image("images/trash.png");

    private static final Image imu = new Image("images/imageUp.png");

    private boolean isExpand = true;

    /**
     *
     */
    public int numberOfScript = 0;

    private final TestStep personalTespStep = new TestStep();

    private TableStepScriptCreationController controllerViewGlobal;

    ObservableList<ScriptLineTableStepController> collectionControllerScript = FXCollections.observableArrayList();

    ObservableList<Requirement> requirementsLinked = FXCollections.observableArrayList();

    private int personalID;

    private boolean isSelected = false;

    private ArrayList<Requirement> allReq;

    ObservableList<String> test = FXCollections.observableArrayList();

    HashSet<Requirement> requirementLinked = new HashSet(0);

    private boolean clicked = false;

    private HeaderTableStepController controllerHeader;

    private ScrollBar scrollBarv = null;// = (ScrollBar) textAreaAction.lookup(".scroll-bar:vertical");
    private ScrollBar scrollBarverif = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.anchorPaneStep.getStylesheets().add("/view/CustomeStyle.css");
        //this.listRequirement.getStyleClass().addAll("DeselectEven", "DeselectOdd");
        this.listRequirement.setCellFactory(new Callback() {

            @Override
            public Object call(Object listView) {
                ListCell<Requirement> cell = new ListCell<Requirement>() {
                    @Override
                    public void updateItem(Requirement item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getRequirementID());
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });
        //Set the cursor action for the node define in this method.
        //Attached an event handler on the image expand, fire the method expand when the arrow is clicked.

    }

    /**
     *
     * @param testStep
     */
    public void setStepView(TestStep testStep) {

        this.loadImagesView();
        this.defineCursorView();
        if (testStep != null) {
            this.anchorPaneStep.getStylesheets().add("view/CustomeStyle.css");
            this.anchorPaneStep.setId("text-ar");
            this.textAreaAction.setText(testStep.getHumanStimuli());
            this.textAreaAction.setVisible(true);
            this.textAreaVerif.setText(testStep.getHumanCheck());
            this.textAreaVerif.setVisible(true);
            this.textAreaVerif.setDisable(false);
            this.textAreaVerif.setEditable(false);
            this.textAreaAction.setEditable(false);
            this.labelActionArea.setVisible(false);
            this.labelVerifArea.setVisible(false);
            this.personalTespStep.setIdtestStep(testStep.getIdtestStep());
            this.personalTespStep.setHumanCheck(testStep.getHumanCheck());
            this.personalTespStep.setStepOrder(testStep.getStepOrder());
            this.personalTespStep.setHumanStimuli(testStep.getHumanStimuli());
            this.personalTespStep.setTestStepHasScripts(testStep.getTestStepHasScripts());
            this.listRequirement.setItems(FXCollections.observableArrayList(testStep.getRequirements()));
            this.requirementsLinked.addAll(testStep.getRequirements());
            this.listRequirement.getStyleClass().addAll("DeselectEven", "DeselectOdd");
            this.listRequirement.getStyleClass().removeAll("SelectEven", "SelectOdd");
//            scrollBarv = (ScrollBar) textAreaAction.lookup(".scroll-bar:vertical");
//            scrollBarverif = (ScrollBar) textAreaVerif.lookup(".scroll-bar:vertical");
//            System.out.println("SCROLL BAR = " + scrollBarv);
        }

        if (testStep != null) {
            this.imageExpand.setVisible(true);
            imageExpand.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                expandChildren();
                event.consume();
            });
        }

    }

    /**
     *
     * @param allReq
     */
    public void setStepCreation(ArrayList<Requirement> allReq) {
        this.allReq = allReq;
        this.loadImageCreation();
        //Implement listenern on field which compose a test step.
        this.implementListenerTestStepField();

        this.defineCursorCreation();
        this.anchorPaneStep.setOnMouseClicked((MouseEvent e) -> {
            clicked = true;
            if (!isSelected) {
                controllerViewGlobal.updateCurrentStep(StepLineTableStepController.this);
            }
        });

        this.listPCR.setOnMouseClicked((MouseEvent e) -> {
            if (!isSelected) {
                controllerViewGlobal.updateCurrentStep(StepLineTableStepController.this);
            }
        });

        this.listRequirement.setOnMouseClicked((MouseEvent e) -> {
            if (!isSelected) {
                controllerViewGlobal.updateCurrentStep(StepLineTableStepController.this);
            }
            if (e.getClickCount() == 2) {
                controllerViewGlobal.addRequirement(StepLineTableStepController.this);
            }
        });

        //Set the handler and listener for each compoent or node that need a listener, handler on this view.
        this.initializeHandler_Listener();

    }

    /**
     * Initialize the reference of the object tableStepScript in the object
     * controllerViewGlobal.
     *
     * @param controllerGLobal
     */
    void initControllerTable(TableStepScriptCreationController controllerGLobal) {
        controllerViewGlobal = controllerGLobal;
    }

    /**
     * Put the ID label of this test step based on the ranked number in the
     * controller list. This method is only triggered when a test step is
     * created, maybe replace it with setID.
     *
     * @param stepID
     */
    public void constructInformation(int stepID) {
        this.idStep.setText(String.valueOf(stepID));
        this.personalID = stepID;
    }

    /**
     * Set the ID of this test step with the id given in parameters.
     *
     * @param id id of this test step.
     */
    public void setID(int id) {
        constructInformation(id);
        updateNumberScript(id);
    }

    /**
     * return the id of this step (i.e the position of this step into the
     * collection of test step).
     *
     * @return personalID
     */
    public int getIDStep() {
        return this.personalID;
    }

    /**
     * Method called when this test step is selected in the table.
     */
    public void showArea() {
        clicked = true;
        isSelected = true;
        this.labelActionArea.setStyle("-fx-text-fill : white;");
        this.labelVerifArea.setStyle("-fx-text-fill : white;");
        this.setBackGroundAnchorPane("#3366CC");
        this.listRequirement.getStyleClass().removeAll("DeselectEven", "DeselectOdd");
        this.listRequirement.getStyleClass().addAll("SelectEven", "SelectOdd");
    }

    /**
     * Method called when the test step is deselected in the table.
     */
    public void hideArea() {
        System.out.println("HIDE AREA");
        isSelected = false;
        muteActionStep(false);
        muteVerifStep(false);
        this.labelActionArea.setStyle("-fx-text-fill : black;");
        this.labelVerifArea.setStyle("-fx-text-fill : black;");
        this.setBackGroundAnchorPane(" #CCFFCC");
        this.listRequirement.getStyleClass().addAll("DeselectEven", "DeselectOdd");
        this.listRequirement.getStyleClass().removeAll("SelectEven", "SelectOdd");
    }

    /**
     * Set the different component (node) define in this method to the color
     * given in parameter.
     *
     * @param color
     */
    public void setBackGroundAnchorPane(String color) {
        this.gridPaneStep.setStyle("-fx-background-color:" + color + ";");
        this.listRequirement.getStyleClass().addAll("DeselectEven", "DeselectOdd");
        this.listRequirement.getStyleClass().removeAll("SelectEven", "SelectOdd");
        this.listRequirement.setStyle("-fx-background-color:" + color + ";");
        this.listPCR.setStyle("-fx-background-color:" + color + ";");
        this.textAreaAction.setStyle("-fx-background-color:" + color + ";");
        this.textAreaVerif.setStyle("-fx-background-color:" + color + ";");
    }

    /**
     * return the reference of this anchorpane
     *
     * @return anchorpane
     */
    public AnchorPane getAnchorPane() {
        return this.anchorPaneStep;
    }

    /**
     * Add the script given in parameters as controllerScript at the position
     * number in the collection of script of this object.
     *
     * @param controllerScript
     * @param number
     */
    public void addScript(ScriptLineTableStepController controllerScript, int number) {
        controllerScript.init(this);
        controllerScript.setIDs(this.personalID, this.numberOfScript + 1);
        collectionControllerScript.add(controllerScript);
        this.numberOfScript += number;
    }

    /**
     * Return the number of script whom this step is the father.
     *
     * @return
     */
    public int getNumberOdScript() {
        return this.numberOfScript;
    }

    /**
     * Swap the script given in reference with the one below, in the collection
     * of script.
     *
     * @param aThis
     */
    public void goDown(ScriptLineTableStepController aThis) {
        int indexControllerScriptInCollection = collectionControllerScript.indexOf(aThis);
        if (indexControllerScriptInCollection < collectionControllerScript.size() - 1) {
            Collections.swap(collectionControllerScript, indexControllerScriptInCollection, indexControllerScriptInCollection + 1);
            controllerViewGlobal.swapScript(false, aThis);
        }
        updateNumberScript(this.personalID);
    }

    /**
     * * Swap the script given in reference with the one above, in the
     * collection of script.
     *
     * @param aThis
     */
    public void goUp(ScriptLineTableStepController aThis) {
        int indexControllerScriptInCOllection = collectionControllerScript.indexOf(aThis);
        if (indexControllerScriptInCOllection > 0) {
            Collections.swap(collectionControllerScript, indexControllerScriptInCOllection, indexControllerScriptInCOllection - 1);
            controllerViewGlobal.swapScript(true, aThis);
        }
        updateNumberScript(this.personalID);
    }

    /**
     * Update the id of the script whom this step is the father. Usually called
     * when the id of the step is changed.
     *
     * @param IDStep
     */
    public void updateNumberScript(int IDStep) {
        for (int i = 0; i < collectionControllerScript.size(); i++) {
            collectionControllerScript.get(i).setIDs(IDStep, i + 1);
        }
    }

    /**
     * Return the collection of the script, contains into this step.
     *
     * @return
     */
    public ObservableList<ScriptLineTableStepController> getCollectionScript() {
        return this.collectionControllerScript;
    }

    /**
     * Return the test step linked to this view.
     *
     * @return
     */
    public TestStep getTestStep() {
        //System.out.println("this.personal TestStep="+this.personalID + "personefpdijfaldfjkas "+ this.personalTespStep.getIdtestStep());
        return this.personalTespStep;

    }

    /**
     * Implement the listener fields in order to construct this test step.
     */
    private void implementListenerTestStepField() {

        this.blockingStep.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            personalTespStep.setBlockingStep(newValue);
        });

        textAreaVerif.textProperty().addListener((final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
            //System.out.println("Old value is :" + oldValue + " ,New value is : " + newValue);
            personalTespStep.setHumanCheck(newValue);
        });

        textAreaAction.textProperty().addListener((final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
            //System.out.println("Old value is :" + oldValue + " ,New value is : " + newValue);
            personalTespStep.setHumanStimuli(newValue);
        });

        personalTespStep.setRequirements(requirementLinked);
    }

    /**
     * Method called when the view should change form a creation test step to a
     * test step display.
     */
    public void executionInstance() {

        this.labelActionArea.setText(this.textAreaAction.getText());
        this.labelVerifArea.setText(this.textAreaVerif.getText());

        this.blockingStep.setDisable(true);

        this.imageDown.setVisible(false);
        this.imageUp.setVisible(false);
        this.imageTrash.setVisible(false);

    }

    /**
     * Method called when the view should change form a creation test step to a
     * popup execution.
     *
     * @param step
     */
    public void executionInstanceView(StepExecutions step) {
        executionInstance();
        this.labelCommentStep.setVisible(true);
        this.labelCommentStep.setText(step.getStepExecutionComment());
        String result = step.getStepExecutionResult();
        this.labelVerif.setVisible(true);
        labelVerif.setText(result);
        changeColorCircle(paneVerif, result);
    }

    /**
     * Set the background of the pane given in parameters depending on the
     * label.
     *
     * @param okAction pane to set the background of.
     * @param newValue label to test.
     */
    private void changeColorCircle(Pane okAction, String newValue) {
        switch (newValue) {
            case "OK":
                okAction.setStyle("-fx-background-color: #00FF00;");
                break;
            case "OKWC":
                okAction.setStyle(" -fx-background-color: #FFC000;");
                break;
            case "Not testable":
                okAction.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%);");
                break;
            case "NOK":
                okAction.setStyle("-fx-background-color: #FF0000;");
                labelVerif.setStyle("-fx-text-fill: white");
                break;
            case "OS":
                okAction.setStyle("-fx-background-color: #8DB4E2;");
                break;
            case "Incomplete":
                okAction.setStyle("-fx-background-color: #D9D9D9;");
                break;
            default:
                okAction.setStyle("-fx-background-color: #D9D9D9;");
                break;
        }
    }

    /**
     * Control image and the list of script depending on if the image expand is
     * clicked or not
     */
    public void expandChildren() {
        if (isExpand) {
            isExpand = false;
            imageExpand.setRotate(-90);
        } else {
            isExpand = true;
            imageExpand.setRotate(0);
        }
        controllerViewGlobal.expandChildren(StepLineTableStepController.this, isExpand);

        this.controllerViewGlobal.getControllerHeader().verifyExpand();
    }

    /**
     * Delete the script given in parameters from the view.
     *
     * @param aThis
     */
    void deleteScript(ScriptLineTableStepController aThis) {
        this.collectionControllerScript.remove(aThis);
        numberOfScript--;
        controllerViewGlobal.deleteScript(aThis);
        updateNumberScript(this.personalID);
    }

    /**
     * Change the cursor of the node given in the method
     */
    private void defineCursorView() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.imageExpand);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    private void defineCursorCreation() {
        ArrayList<Node> nodeHand = new ArrayList<>();
        nodeHand.add(this.imageDown);
        nodeHand.add(this.imageTrash);
        nodeHand.add(this.imageUp);
        setCursorOnComponent action = new setCursorOnComponent();
        action.setCursorHand(nodeHand);
    }

    /**
     * Activate or desactivate the text area action depending on the boolean
     * given in parameters.
     *
     * @param focusAction
     */
    private void muteActionStep(boolean focusAction) {
        System.out.println("MUTE ACTION");
        if (focusAction) {
            this.textAreaAction.setVisible(true);
            this.labelActionArea.setVisible(false);
            this.textAreaAction.requestFocus();
            //this.textAreaAction.positionCaret(this.textAreaAction.getText().length());
        } else {
            this.textAreaAction.setVisible(false);
            this.labelActionArea.setText(textAreaAction.getText());
            this.labelActionArea.setVisible(true);
            this.labelActionArea.setStyle("-fx-text-fill : white;");
        }

    }

    /**
     * Activate or desactivate the text area verif depending on the boolean
     * given in parameters.
     *
     * @param focusOnVerif
     */
    private void muteVerifStep(boolean focusOnVerif) {
        System.out.println("MUTE VERIF");
        if (focusOnVerif) {
            this.textAreaVerif.setVisible(true);
            this.labelVerifArea.setVisible(false);
            this.textAreaVerif.requestFocus();
            //this.textAreaVerif.positionCaret(this.textAreaVerif.getText().length());
        } else {
            this.textAreaVerif.setVisible(false);
            this.labelVerifArea.setText(textAreaVerif.getText());
            this.labelVerifArea.setVisible(true);
            this.labelVerifArea.setStyle("-fx-text-fill : white;");
        }

    }

    /**
     * Search for the scrollbar in the text area and initialize the method to
     * increase the height of the anchor pane.
     */
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            initStep();
            return null;
        }
    };

    /**
     *
     */
    public void initStep() {

        while (scrollBarv == null && scrollBarverif == null) {
            try {
                scrollBarv = (ScrollBar) textAreaAction.lookup(".scroll-bar:vertical");
                scrollBarverif = (ScrollBar) textAreaVerif.lookup(".scroll-bar:vertical");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(StepLineTableStepController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        scrollBarv.setOpacity(0.0);

        scrollBarv.visibleProperty().addListener((ObservableValue<? extends Boolean> source, Boolean wasVisible, Boolean isVisible) -> {
            if (isVisible && textAreaAction.getHeight() <= 180) {
                textAreaAction.setPrefRowCount(textAreaAction.getPrefRowCount() + 1);
                anchorPaneStep.setMinHeight(textAreaAction.getHeight() + 25);
                gridPaneStep.setMinHeight(textAreaAction.getHeight() + 25);
            } else {
                scrollBarv.setOpacity(1.0);
            }
        });

        scrollBarverif.setOpacity(0.0);
        scrollBarverif.visibleProperty().addListener((ObservableValue<? extends Boolean> source, Boolean wasVisible, Boolean isVisible) -> {
            if (isVisible && textAreaVerif.getHeight() <= 180) {
                textAreaVerif.setPrefRowCount(textAreaVerif.getPrefRowCount() + 1);
                anchorPaneStep.setMinHeight(textAreaVerif.getHeight() + 25);
                gridPaneStep.setMinHeight(textAreaVerif.getHeight() + 25);
            } else {
                scrollBarverif.setOpacity(1.0);
            }
        });

    }

    /**
     * Disable the configuration for each script into the collection of script.
     */
    public void disableScriptConfiguration() {
        collectionControllerScript.stream().forEach((collectionControllerScript1) -> {
            collectionControllerScript1.disableConfig();
        });
    }

    private void loadImagesView() {
        this.imageExpand.setImage(ime);
        this.imageExpand.setRotate(0);
    }

    private void loadImageCreation() {
        this.imageDown.setImage(imd);
        this.imageUp.setImage(imu);
        this.imageTrash.setImage(deilete);
    }

    private void initializeHandler_Listener() {
        //Attached an event handler on the image up, fire the method move step when the image up is clicked.
        imageUp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.moveStep(StepLineTableStepController.this, true, numberOfScript);
            event.consume();
        });

        //Attached an event handler on the image up, fire the method move step when the image up is clicked.
        imageDown.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.moveStep(StepLineTableStepController.this, false, numberOfScript);
            event.consume();
        });

        //Attached an event handler on the image expand, fire the method expand when the arrow is clicked.
//        imageExpand.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                expandChildren();
//                event.consume();
//            }
//
//        });
        //Attached an event handler on the image trash, fire the method deletet when the arrow is clicked.
        this.imageTrash.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.deleteSelectedStep(StepLineTableStepController.this);
            event.consume();
        });

        /*
         Attached an action listener to the anchor paneAction,
         enable the textarea action when it's clicked into and the step is currently selected, also disable the textarea verif.
         */
        this.anchorPaneAction.setOnMouseClicked((MouseEvent t) -> {
            if (isSelected) {
                clicked = true;
                Platform.runLater(() -> {
                    muteActionStep(true);
                    muteVerifStep(false);
                }
                );
            }
        });

        /*
         Attached an action listener to the anchor paneVerif,
         enable the textarea verif when it's clicked into and the step is currently selected, also disable the textarea action.
         */
        this.anchorPaneVerif.setOnMouseClicked((MouseEvent t) -> {
            System.out.println("IS SELECTED ? = " + isSelected);
            if (isSelected) {
                clicked = true;
                Platform.runLater(() -> {
                    muteActionStep(false);
                    muteVerifStep(true);
                }
                );
            }
        });

        this.listRequirement.setContextMenu(contextRequirementList());
    }

    private ContextMenu contextRequirementList() {
        ContextMenu listRek = new ContextMenu();
        MenuItem manageRek = new MenuItem("Manage requirements..");

        manageRek.setOnAction((ActionEvent event) -> {
            controllerViewGlobal.addRequirement(this);
        });

        listRek.getItems().add(manageRek);
        return listRek;
    }

    /**
     *
     * @param requirementsSelected
     */
    public void setAction(ObservableList<Requirement> requirementsSelected) {
        requirementsLinked.clear();
        requirementsLinked.setAll(requirementsSelected);
        requirementLinked.clear();
        requirementLinked.addAll(requirementsSelected);

        listRequirement.setItems(FXCollections.observableArrayList(requirementsSelected));
        controllerViewGlobal.updateRequirementTestCase();
    }

    /**
     *
     * @return
     */
    public ObservableList<Requirement> getRequirements() {
        return this.requirementsLinked;
    }

    void hideArrow() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.imageExpand.setVisible(false);
    }

    /**
     *
     * @return
     */
    public boolean getIsExpand() {
        return this.isExpand;
    }

    /**
     *
     * @return
     */
    public boolean getIsSelected() {
        return this.isSelected;
    }

    /**
     *
     * @return
     */
    public boolean getIsClicked() {
        return this.clicked;
    }

    /**
     *
     * @param clicked
     */
    public void setIsClicker(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     *
     * @return
     */
    public TableStepScriptCreationController getControllerViewGlobal() {
        return this.controllerViewGlobal;
    }
}
