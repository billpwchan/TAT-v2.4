 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.Macro;
import DB.ParamScriptMacro;
import DB.Parameters;
import DB.Script;
import DB.ScriptHasBeenConfigured;
import DB.ScriptHasParameters;
import DB.TestCase;
import DB.TestStep;
import DB.TestStepHasScript;
import controller.tablestep.ScriptLineTableStepController;
import controller.tablestep.StepLineTableStepController;
import model.*;
import controller.tablestep.ViewScriptController;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import model.Classe;
import model.TextFieldWithFormat;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import java.nio.file.Paths;
import java.util.Set;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import main.Main;
import model.Properties;
import model.Properties.Type;
import model.StateClasse;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import static main.Main.HMIs;

/**
 * FXML Controller class
 *
 * @author Martinez Thibault
 */
public class PopUpWizardScriptController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private GridPane mainGrid;
    @FXML
    private SplitPane splitPan;
    @FXML
    private TableView<Parameters> tableParameters;
    @FXML
    private TableColumn<Parameters, String> columnParam;
    @FXML
    private TableColumn<Parameters, String> columnType;
    @FXML
    private TableColumn<Parameters, Parameters> columnValue;
    @FXML
    private TableColumn<Parameters, String> columnDescription;
    @FXML
    private Label descriptionScript;
    @FXML
    private ChoiceBox<String> choiceBoxScript;
    @FXML
    private StackPane stackPan;
    @FXML
    private StackPane stackPaneVariable;
    @FXML
    private Button buttonValid;
    @FXML
    private Button buttonSetParameters;
    @FXML
    private RadioButton constantRadioButton;
    @FXML
    private RadioButton variableRadioButton;
    @FXML
    private ChoiceBox<String> choiceBoxVariable;
    @FXML
    private Label labelValueConstant;
    @FXML
    private TextField textFiledValueConstant;
    @FXML
    private Label labelSourceVariable;
    @FXML
    private Label labelParamName;
    @FXML
    private Label labelParamType;
    @FXML
    private ComboBox<String> comboBoxDefinitionValue;
    @FXML
    private GridPane gridPaneValue;
    @FXML
    private Label textAreaDescriptionParameters;
    @FXML
    private ScrollPane scrollParams;
    @FXML
    private AnchorPane anchorParams;

    private static ViewScriptController controller;

    private final ObservableList<Parameters> observableListParam = FXCollections.observableArrayList();

    private ArrayList<ScriptHasBeenConfigured> observableListScriptMartinth = new ArrayList<>();

    private static final ObservableList<String> BufferList = FXCollections.observableArrayList();

    private final ObservableList<TextFieldWithFormat> textFieldList = FXCollections.observableArrayList();

    private final static String Separator = ("" + ((char) 007));

    private final ComboBox<Classe> comboBoxClasse = new ComboBox();

    private ComboBox<HMI> comboBoxHMI = new ComboBox();

    private final ComboBox<Properties> combobxProperty = new ComboBox();

    private final ComboBox<StateClasse> comboboxState = new ComboBox();

    TextFieldWithFormat sheetNameTextField = new TextFieldWithFormat("[A-Za-z]{1,20}", true);

    TextFieldWithFormat sheetTextField = new TextFieldWithFormat("[0-9]{1,10}", true);

    private Classe classChose;

    private String Property;

    private String HMI;

    private String Classe;

    private final ComboBox<HMI> comboboxHMI = new ComboBox();

    private HMI HMIChose;

    private String scriptChose;

    private ScriptHasBeenConfigured paramToLink;

    private ObservableList<String> paramToDisplay = FXCollections.observableArrayList();

    private ObservableList<ScriptHasBeenConfigured> scriptHasBeenConfListtoLink = FXCollections.observableArrayList();

    private ObservableList<ParamScriptMacro> parametersListtoLink = FXCollections.observableArrayList();

    private String paramStringToLink;

    private boolean isMacro = false;

    private ArrayList<String> currentParam;

    private ArrayList<String> currentParamScMacro;

    private TestStepHasScript toLinkTestStepHasScript;

    private int toLinkScriptHasBeenConfiguredIndex;

    private String currScriptChosen;

    /**
     * Initializes the controller class. Class of the configuration popup for
     * scripts.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*
         Definition of the columns in the tableview parameters.
         */
        columnParam.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("parameterType"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnValue.setVisible(false);

        /*
         Set the constraints policy on the table parameters.
         */
        this.tableParameters.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /*
         Linked the tableview to its corresponding observable list.
         */
        this.tableParameters.setItems(this.observableListParam);

        /*
         Add a listener on the comboBox value (comboBox to select where the values are from) in order to redraw the right panel of this popup.
         */
        comboBoxDefinitionValue.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                comboBoxClasse.getSelectionModel().clearSelection();
                comboBoxClasse.setItems(null);
                displayPanel(newValue);
            }
        });

        /*
         Add a listener on the table, to know which row is seleted in order to redraw the right side.
         */
        this.tableParameters.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection)
                -> {
                    if (this.tableParameters.getSelectionModel().getSelectedIndex() >= 0) {
                        comboBoxDefinitionValue.getSelectionModel().clearSelection();
                        //System.out.println("Construct parameters");
                        constructConfigurationParameters();
                    }
                });

        /*
         Action fire on button set parameters pressed.
         */
        buttonSetParameters.setOnAction((ActionEvent e) -> {
            this.setParameters();
        }
        );

        /*
         Action fire on button close.
         */
        buttonValid.setOnAction((ActionEvent e) -> {
            controller.closePopUp();
        });

    }

    /**
     * Init the controller of viewScript by giving the reference of the father
     * view to this popup.
     *
     * @param aThis controller viewScriptController
     */
    public void init(ViewScriptController aThis) {
        controller = aThis;
    }

    /**
     * This method is called by the class @see viewScript and refere the
     * parameters is they have been already configured or not, construct the
     * left part of the popup.
     *
     * @param script Script selected in the combobox of view script
     * @param hashScriptHasBeenConfigured
     * @param selectedParam number of the selected parameter in the viewScript.
     */
    public void constructInformation(Script script, HashSet<ScriptHasBeenConfigured> hashScriptHasBeenConfigured, int selectedParam) {
        TestCase testCase = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getTestCase();
        if (testCase != null) {
            if (BufferList.isEmpty()) {
                Iterator<TestStep> itSteps = testCase.getTestSteps().iterator();
                while (itSteps.hasNext()) {
                    TestStep testStep = itSteps.next();
                    Iterator<TestStepHasScript> itTSHS = testStep.getTestStepHasScripts().iterator();
                    while (itTSHS.hasNext()) {
                        TestStepHasScript tshs = itTSHS.next();
                        Iterator<ScriptHasBeenConfigured> itSHBC = tshs.getScriptHasBeenConfigureds().iterator();
                        while (itSHBC.hasNext()) {
                            ScriptHasBeenConfigured shbc = itSHBC.next();
                            if (shbc.getValuePath().equals("Buffer list")) {
                                String buffer = shbc.getValue().replace("@&Buffer_", "").trim();
                                addStringInBuffer(buffer);
                            }
                        }
                    }
                }
            }
        }
        this.descriptionScript.setText(script.getDesciption());
        this.choiceBoxScript.setItems(FXCollections.observableArrayList(script.getName()));
        this.choiceBoxScript.getSelectionModel().selectFirst();

        this.observableListScriptMartinth = new ArrayList<>(hashScriptHasBeenConfigured);
        Collections.sort(this.observableListScriptMartinth, (ScriptHasBeenConfigured o1, ScriptHasBeenConfigured o2) -> Integer.compare(o1.getParamOrder(), o2.getParamOrder()));
        //Update the table paramters by giving a new set of params t the linked observablelist.
        Iterator<ScriptHasParameters> itScriptHasParameters = script.getScriptHasParameterses().iterator();
        while (itScriptHasParameters.hasNext()) {
            ScriptHasParameters scriptHasParameters = itScriptHasParameters.next();
            this.observableListParam.add(scriptHasParameters.getParameters());

        }
        //Select the line corresponding of the clicked parameters on the view Case creation.
        this.tableParameters.getSelectionModel().select(selectedParam);
    }

    /**
     * Return the observablelist with the configured parameters.
     *
     * @return observableListScriptMartinth list with the configured parameters.
     */
    public ArrayList<ScriptHasBeenConfigured> getConfigScript() {
        return this.observableListScriptMartinth;
    }

    /**
     * Method called when the button setParameters is clicked. Take the
     * information set in the textField and check the integrity of the data, if
     * the test passed these information are set in the observable list
     * observableListScriptMartinth
     */
    private void setParameters() {
        int i = 0;
        String value = Separator;
        //Boolean to check if the values put in textfield match the regex.
        boolean isFormat = true;
        //Take the paramsFOrScript in the observablelist corresponding to the slected row in the tableview parameters.
        ScriptHasBeenConfigured toBeSet = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex());
        //Get the string selected in the comboBoxDefinitionValue
        String pathToVariable = comboBoxDefinitionValue.getSelectionModel().getSelectedItem();
        //Check is the format of each textField is good or not.
        while (i < textFieldList.size() && isFormat) {
            if (!textFieldList.get(i).isFormat()) {
                isFormat = false;
            }
            value = value + textFieldList.get(i).getText() + Separator;
            i++;
        }
        //If the format is not good a popUp alert is shown.
        if (isFormat == false) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR CONFIGURATION");
            alert.setHeaderText("The parameters types are wrong, please check what you have done");
            alert.setContentText("The type of each parameter is available on the label name of your parameter (i.e : row position)");
            alert.showAndWait();
        } else {
            //If the format is good, then save the parameters in the observable list.
            toBeSet.setValuePath(pathToVariable);
            switch (pathToVariable) {
                case "Buffer list":
                    value = value.replace(Separator, "");
                    toBeSet.setValue(Separator + "@&Buffer_" + value + Separator);
                    break;
                case "Excel file":
                    if (textFieldList.get(0).getId().equals("SheetName")) {
                        toBeSet.setValue(Separator + "@&Name_" + value);
                    } else {
                        toBeSet.setValue(Separator + "@&Number_" + value);
                    }
                    break;
                case "Property":
                    toBeSet.setValue(Property);
                    break;
                case "HMI":
                    toBeSet.setValue(Separator + HMI + Separator);
                    break;
                case "Classes":
                    toBeSet.setValue(Separator + Classe + Separator);
                    break;
                case "From Other Script":
                    //This needs to be done: deleteReferenceToParam
                    if (toBeSet.getRefScriptHasBeenConfigured() != null) {
                        deleteReferenceToParam(toBeSet);
                    }
                    //int index = this.paramToDisplay.indexOf(this.paramStringToLink);
                    int index = 0;
                    if (isMacro) {
                        //ParamScriptMacro paramScMacro = this.parametersListtoLink.get(index);
                        index = this.currentParamScMacro.indexOf(this.paramStringToLink);
                    } else {
                        index = this.currentParam.indexOf(this.paramStringToLink);
                    }
                    System.out.println("Index: " + index);
                    int count = 0;
                    Iterator<ScriptHasBeenConfigured> itSHBC = this.toLinkTestStepHasScript.getScriptHasBeenConfigureds().iterator();
                    while (itSHBC.hasNext()) {
                        ScriptHasBeenConfigured currSHBC = itSHBC.next();
                        if (currSHBC.getParamOrder() == index) {
                            this.paramToLink = currSHBC;
                        }
                        count++;
                    }
                    toBeSet.setRefScriptHasBeenConfigured(this.paramToLink);
                    this.paramToLink.addScriptHasBeenConfiguredToMe(toBeSet);
                    toBeSet.setValuePath(Separator + scriptChose);
                    //if (!this.paramToLink.getValue().equals(null)) {
                    toBeSet.setValue(this.paramToLink.getValue());
                    //                } else {
//                    System.out.println("ENTERED: LINKED TO PARAMETER");
//                    toBeSet.setValue("Linked to the Parameter " + scriptChose);
//                }
                    System.out.println("paramtoLink.value: " + this.paramToLink.getValue());
                    //System.out.println("PARAM = " + toBeSet.getParamScriptMacro().getValue());
                    //byte configured = 0;
                    //toBeSet.setToDisplay(configured);
                    break;
                default:
                    //System.out.println("Value is : " + value);
                    toBeSet.setValue(value);
                    break;
            }
            byte configured = 1;
            toBeSet.setIsConfigured(configured);
            controller.referParameters(this.observableListScriptMartinth);
            //controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().stream().forEach((ScriptHasBeenConfigured collectionControllerScript) -> {
//            for (controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep()) {
//                ArrayList<ScriptHasBeenConfigured> params = new ArrayList<>(collectionControllerScript.getCollectionScript().getHashParamScriptMacro());
//                collectionControllerScript.getScriptControllerAction().referParameters(params);
//            });

            ObservableList<StepLineTableStepController> sLTSCList = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep();
            Iterator<StepLineTableStepController> itSLTSC = sLTSCList.iterator();
            while (itSLTSC.hasNext()) {
                StepLineTableStepController stepLine = itSLTSC.next();
                ObservableList<ScriptLineTableStepController> scriptLineList = stepLine.getCollectionScript();
                Iterator<ScriptLineTableStepController> itScriptLine = scriptLineList.iterator();
                while (itScriptLine.hasNext()) {
                    ScriptLineTableStepController scriptLine = itScriptLine.next();
                    if (scriptLine.getScriptAction() != null) {
                        ArrayList<ScriptHasBeenConfigured> scriptActionList = new ArrayList(scriptLine.getScriptAction().getScriptHasBeenConfigureds());
                        Collections.sort(scriptActionList, (ScriptHasBeenConfigured o1, ScriptHasBeenConfigured o2) -> Integer.compare(o1.getParamOrder(), o2.getParamOrder()));
                        scriptLine.getScriptActionController().referParameters(scriptActionList);
                    }
                    if (scriptLine.getScriptVerif() != null) {
                        ArrayList<ScriptHasBeenConfigured> scriptVerifList = new ArrayList(scriptLine.getScriptVerif().getScriptHasBeenConfigureds());
                        Collections.sort(scriptVerifList, (ScriptHasBeenConfigured o1, ScriptHasBeenConfigured o2) -> Integer.compare(o1.getParamOrder(), o2.getParamOrder()));
                        scriptLine.getScriptVerifController().referParameters(scriptVerifList);
                    }

                }

//            Set toSet = toBeSet.getScriptHasBeenConfiguredToMe();
//            System.out.println("toSet: " + toSet);
//            Iterator<ScriptHasBeenConfigured> toUpdateSHBC = toSet.iterator();
//            while (toUpdateSHBC.hasNext()) {
//                System.out.println("toUpdateList.added: ");
//                ScriptHasBeenConfigured toConf = toUpdateSHBC.next();
//                System.out.println("toConf.getTestStepHasScript: " + toConf.getTestStepHasScript().toString());
//                TestStep currTestStep = toConf.getTestStepHasScript().getTestStep();
//                System.out.println("toConf.getTestStepHasScript.testStep: " + currTestStep);
//                System.out.println("script: " + toConf.getTestStepHasScript().getScript());
//                //currTestStep.setStepOrder((byte)1);
//                Byte step = currTestStep.getStepOrder();
//                System.out.println("TestCase: " + currTestStep.getTestCase());
//
//                System.out.println("TEST STEP ID: " + currTestStep.getIdtestStep() + ", human stim: " + currTestStep.getHumanStimuli() + ", human check: " + currTestStep.getHumanCheck());
//                int scriptCount = toConf.getTestStepHasScript().getExecutionOrder();
//                System.out.println("step: " + step + " scriptCount: " + scriptCount);
//                //boolean doesTestStepHasScHaveToConf = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get(step).getTestStep().getTestStepHasScripts().contains(toConf.getTestStepHasScript());
//                //System.out.println("doesTestStepHasScHaveToConf: " + doesTestStepHasScHaveToConf);
//                //int scriptL = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get(step)
//                ArrayList<ScriptHasBeenConfigured> toUpdateListAction, toUpdateListVerif;
//                ScriptLineTableStepController sLController = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get((int) step).getCollectionScript().get(scriptCount);
//                toUpdateListAction = new ArrayList(controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get((int) step).getCollectionScript().get(scriptCount).getScriptActionController().getTestStepHasScript().getScriptHasBeenConfigureds());
//                toUpdateListVerif = new ArrayList(controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get((int) step).getCollectionScript().get(scriptCount).getScriptVerifController().getTestStepHasScript().getScriptHasBeenConfigureds());
//                Collections.sort(toUpdateListVerif, (ScriptHasBeenConfigured o1, ScriptHasBeenConfigured o2) -> Integer.compare(o1.getParamOrder(), o2.getParamOrder()));
//                Collections.sort(toUpdateListAction, (ScriptHasBeenConfigured o1, ScriptHasBeenConfigured o2) -> Integer.compare(o1.getParamOrder(), o2.getParamOrder()));
//
//                controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get((int) step).getCollectionScript().get(scriptCount).getScriptActionController().referParameters(toUpdateListAction);
//                controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get((int) step).getCollectionScript().get(scriptCount).getScriptVerifController().referParameters(toUpdateListVerif);
//
//            }
                //controller.referParameters(toUpdateList);
            }

        }
    }

    /**
     * Construct the right panel of the popup and load parameters if any are
     * available in the observable list.
     */
    private void constructConfigurationParameters() {
        Parameters paramFromLine = this.tableParameters.getSelectionModel().getSelectedItem();
        //System.out.println("PARAM FROM LINE = "+paramFromLine);
        labelParamName.setText(paramFromLine.getName());
        labelParamType.setText(paramFromLine.getParameterType());
        textAreaDescriptionParameters.setText(paramFromLine.getDescription());
        ObservableList<String> options = getFXCollectionFromParamtersType(paramFromLine.getParameterType());
        comboBoxDefinitionValue.getItems().setAll(options);
        //System.out.println("Taille de mon array list = "+observableListScriptMartinth.size());
        System.out.println("OBSERVABLE LIST SIZE = " + this.observableListScriptMartinth.size());

        String comboValue = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath();

        if (!comboValue.isEmpty() && (comboValue.charAt(0) == Separator.charAt(0))) {
            comboBoxDefinitionValue.getSelectionModel().select("From Other Script");

        } else if (!comboValue.isEmpty()) {
            //System.out.println("Je ne suis pas empty");
            comboBoxDefinitionValue.getSelectionModel().select(comboValue);
            //displayPanel(comboValue);
        } else if (paramFromLine.getParameterType().equals("buffer")) {
            comboBoxDefinitionValue.getSelectionModel().select("Buffer list");
            //displayPanel("Buffer list");
        } else {
            comboBoxDefinitionValue.getSelectionModel().select("");
        }
        // System.out.println("j ai fini de consctruct configuration");
    }

    /**
     * Define each set of possible input for the parameter type.
     *
     * @param parameterType parameter type
     * @return listOfOptions an observable list containing the different list of
     * options for each parameter type.
     */
    private ObservableList<String> getFXCollectionFromParamtersType(String parameterType) {

        ObservableList<String> listOfOptions = FXCollections.observableArrayList();

        if (null != parameterType) {
            switch (parameterType) {
                case "integer":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Excel file", "Buffer list", "From Other Script");
                    break;
                case "ip":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Excel file", "From Other Script");//, "Instantiate from Excel file");
                    break;
                case "string":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Excel file", "Buffer list", "Classes", "HMI", "Property", "From Other Script");//("Constant", "Absolute path", "Variable path", "Excel file", "Instantiate from Excel file","Buffer");
                    break;
                case "buffer":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Excel file", "Buffer list", "From Other Script");//("Constant", "Absolute path", "Variable path", "Excel file", "Instantiate from Excel file","Buffer");
                    break;
                case "color":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Excel file", "Buffer list", "Property", "From Other Script");
                    break;
                case "image":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Excel file", "Buffer list", "Property", "From Other Script");
                    break;
                case "":

                    break;
                default:
                    listOfOptions = FXCollections.observableArrayList("Error on parameters name, not found in databse, contact the Administrator");
                    break;
            }
        }
        return listOfOptions;
    }

    /**
     * Construct the new panel depending on which type is selected in the
     * combobox and if there is values or not in the observablelist.
     *
     * @param newValue
     */
    private void displayPanel(String newValue) {

        //System.out.println("I am call !");
        textFieldList.clear();
        clearDisplayPanel();
        if (null != newValue) {
            buttonSetParameters.setDisable(true);
            switch (newValue) {
                case "Constant":

                    Label constantLabel = new Label("Constant");
                    constantLabel.setTooltip(new Tooltip("Type : String"));
                    changeColorLabel(constantLabel, true);
                    buttonSetParameters.setDisable(true);
                    TextFieldWithFormat constantTextField = new TextFieldWithFormat("[a-zA-Z_0-9]", false);
                    constantTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        buttonSetParameters.setDisable(new2Value.trim().isEmpty());
                        changeColorLabel(constantLabel, new2Value.trim().isEmpty());
                    });
                    if ("Constant".equals(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        constantTextField.setText(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().replace("" + Separator, ""));
                    }
                    this.gridPaneValue.add(constantLabel, 1, 1);
                    this.gridPaneValue.add(constantTextField.getTextField(), 2, 1);
                    textFieldList.add(constantTextField);
                    break;
                case "Excel file":
                    RadioButton sheetNumber = new RadioButton("Sheet number");
                    ToggleGroup group = new ToggleGroup();
                    sheetNumber.setTextFill(Color.RED);
                    RadioButton sheetName = new RadioButton("Sheet name");
                    sheetName.setToggleGroup(group);
                    sheetNumber.setToggleGroup(group);
                    sheetName.setTextFill(Color.RED);
                    sheetName.requestFocus();
                    sheetName.setSelected(true);
                    sheetNumber.setTextFill(Color.BLACK);
                    TextFieldWithFormat sheetNameTextField = new TextFieldWithFormat("[A-Za-z_0-9]{1,20}", true, "SheetName");
                    TextFieldWithFormat sheetTextField = new TextFieldWithFormat("[0-9]{1,10}", true, "SheetNumber");
                    sheetTextField.getTextField().setDisable(true);
                    sheetTextField.getTextField().setText("");
                    sheetNameTextField.getTextField().setDisable(false);
                    textFieldList.add(sheetNameTextField);
                    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                        @Override
                        public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                            if (group.getSelectedToggle() != null && group.getSelectedToggle() == sheetNumber) {
                                sheetName.setTextFill(Color.BLACK);
                                sheetNumber.setTextFill(Color.RED);
                                sheetNameTextField.getTextField().setDisable(true);
                                sheetNameTextField.getTextField().setText("");
//                                sheetName.setTextFill(Color.RED);
                                sheetTextField.getTextField().setDisable(false);
//                                sheetTextField.getTextField().setText("");
                                textFieldList.add(0, sheetTextField);
                                textFieldList.remove(sheetNameTextField);
                                buttonSetParameters.setDisable(true);
                            } else if (group.getSelectedToggle() != null && group.getSelectedToggle() == sheetName) {
                                sheetNumber.setTextFill(Color.BLACK);
                                sheetName.setTextFill(Color.RED);
                                sheetTextField.getTextField().setDisable(true);
                                sheetTextField.getTextField().setText("");
                                sheetNameTextField.getTextField().setDisable(false);
                                textFieldList.remove(sheetTextField);
                                textFieldList.add(0, sheetNameTextField);
                            }
                            buttonSetParameters.setDisable(true);
                        }
                    });
                    //Label sheetValue = new Label("Sheet number ");
                    //sheetValue.setTooltip(new Tooltip("Type : Integer"));
                    Label positionX = new Label("Column position ");
                    positionX.setTooltip(new Tooltip("Type : Integer"));
                    Label positionY = new Label("Row position ");
                    positionY.setTooltip(new Tooltip("Type : Integer"));
//                    Label rang = new Label("Range ");
//                    rang.setTooltip(new Tooltip("Type : Integer"));

                    //changeColorLabel(sheetValue.getText(), true);
                    changeColorLabel(positionX, true);
                    changeColorLabel(positionY, true);
                    //changeColorLabel(rang, true);

                    buttonSetParameters.setDisable(true);

                    TextFieldWithFormat postXTextField = new TextFieldWithFormat("[0-9]{1,10}", true);
                    TextFieldWithFormat posYTextField = new TextFieldWithFormat("[0-9]{1,10}", true);
                    //TextFieldWithFormat rangTextField = new TextFieldWithFormat("[0-9]{1,10}", true);

                    sheetTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        if (sheetNumber.isSelected()) {
                            changeColorRadioButton(sheetNumber, new2Value.trim().isEmpty());
                            if (!sheetTextField.getText().isEmpty()
                                    && !postXTextField.getText().isEmpty()
                                    && !posYTextField.getText().isEmpty()) {
                                buttonSetParameters.setDisable(false);
                            } else {
                                buttonSetParameters.setDisable(true);
                            }
                        }
                    });

                    sheetNameTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        if (sheetName.isSelected()) {
                            changeColorRadioButton(sheetName, new2Value.trim().isEmpty());
                            if (!sheetNameTextField.getText().isEmpty()
                                    && !postXTextField.getText().isEmpty()
                                    && !posYTextField.getText().isEmpty()) {
                                buttonSetParameters.setDisable(false);
                            } else {
                                buttonSetParameters.setDisable(true);
                            }
                        }
                    });

                    postXTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        if (sheetNumber.isSelected()) {
                            changeColorRadioButton(sheetNumber, new2Value.trim().isEmpty());
                            if (!sheetTextField.getText().isEmpty()
                                    && !postXTextField.getText().isEmpty()
                                    && !posYTextField.getText().isEmpty()) {
                                buttonSetParameters.setDisable(false);
                            } else {
                                buttonSetParameters.setDisable(true);
                            }
                        } else {
                            if (sheetName.isSelected()) {
                                changeColorRadioButton(sheetName, new2Value.trim().isEmpty());
                                if (!sheetNameTextField.getText().isEmpty()
                                        && !postXTextField.getText().isEmpty()
                                        && !posYTextField.getText().isEmpty()) {
                                    buttonSetParameters.setDisable(false);
                                } else {
                                    buttonSetParameters.setDisable(true);
                                }
                            }
                        }
//                        if (!sheetTextField.getText().isEmpty()
//                                && !postXTextField.getText().isEmpty()
//                                && !posYTextField.getText().isEmpty()) {
//                            buttonSetParameters.setDisable(false);
//                        } else {
//                            buttonSetParameters.setDisable(true);
//                        }
                        changeColorLabel(positionX, new2Value.trim().isEmpty());
                    });
                    posYTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        if (sheetNumber.isSelected()) {
                            changeColorRadioButton(sheetNumber, new2Value.trim().isEmpty());
                            if (!sheetTextField.getText().isEmpty()
                                    && !postXTextField.getText().isEmpty()
                                    && !posYTextField.getText().isEmpty()) {
                                buttonSetParameters.setDisable(false);
                            } else {
                                buttonSetParameters.setDisable(true);
                            }
                        } else {
                            if (sheetName.isSelected()) {
                                changeColorRadioButton(sheetName, new2Value.trim().isEmpty());
                                if (!sheetNameTextField.getText().isEmpty()
                                        && !postXTextField.getText().isEmpty()
                                        && !posYTextField.getText().isEmpty()) {
                                    buttonSetParameters.setDisable(false);
                                } else {
                                    buttonSetParameters.setDisable(true);
                                }
                            }
                        }
                        changeColorLabel(positionY, new2Value.trim().isEmpty());
                    });
//
//                    rangTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
//                        if (!sheetTextField.getText().isEmpty()
//                                && !postXTextField.getText().isEmpty()
//                                && !posYTextField.getText().isEmpty()
//                                && !rangTextField.getText().isEmpty()) {
//                            buttonSetParameters.setDisable(false);
//                        } else {
//                            buttonSetParameters.setDisable(true);
//                        }
//                        changeColorLabel(rang, new2Value.trim().isEmpty());
//                    });

                    if ("Excel file".equals(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String[] parts = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().split("" + Separator);
                            System.out.println("parts1= " + parts[1]);
                            if (parts[1].contains("@&Name_")) {
                                System.out.println("NAME");
                                sheetNameTextField.setText(parts[2]);
                            } else {
                                sheetTextField.setText(parts[2]);
                            }
                            postXTextField.setText(parts[3]);
                            posYTextField.setText(parts[4]);
                            //rangTextField.setText(parts[4]);
                        }

                    }
                    this.gridPaneValue.add(sheetName, 1, 1);
                    this.gridPaneValue.add(sheetNameTextField.getTextField(), 2, 1);
                    this.gridPaneValue.add(sheetNumber, 1, 2);
                    this.gridPaneValue.add(sheetTextField.getTextField(), 2, 2);
                    this.gridPaneValue.add(positionX, 1, 3);
                    this.gridPaneValue.add(postXTextField.getTextField(), 2, 3);
                    this.gridPaneValue.add(positionY, 1, 4);
                    this.gridPaneValue.add(posYTextField.getTextField(), 2, 4);
                    //this.gridPaneValue.add(rang, 1, 4);
//                    int toto = TableStepScriptCreationController.getExcelRank();
//                    rangTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
//                        if (isNumeric(new2Value)) {
//                            TableStepScriptCreationController.setExcelRank(Integer.valueOf(new2Value));
//                            System.out.println("My new value is :" + new2Value);
//                        }
//                        if (!sheetTextField.getText().isEmpty()
//                                && !postXTextField.getText().isEmpty()
//                                && !posYTextField.getText().isEmpty()
//                                && !rangTextField.getText().isEmpty()) {
//                            buttonSetParameters.setDisable(false);
//                        } else {
//                            buttonSetParameters.setDisable(true);
//                        }
//                        changeColorLabel(rang, new2Value.trim().isEmpty());
//                    });
//                    rangTextField.setText(String.valueOf(toto));
                    //this.gridPaneValue.add(rangTextField.getTextField(), 2, 4);

                    textFieldList.add(postXTextField);
                    textFieldList.add(posYTextField);
                    // textFieldList.add(rangTextField);
                    System.out.println("BREAK");
                    break;
                case "Buffer list":
                    System.out.println("On m'appele buffer");
                    Label bufferSelection = new Label("Select buffer");
                    final ComboBox comboBox = new ComboBox();
                    comboBox.setEditable(true);
                    comboBox.setItems(BufferList);
                    comboBox.valueProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue ov, String t, String t1) {
                            System.out.println("ADD STRING BUFFER");
                            addStringInBuffer(t1);
                            buttonSetParameters.setDisable(false);
                        }
                    });
                    TextFieldWithFormat comboFormat = new TextFieldWithFormat("", false);
                    comboFormat.setTextField(comboBox.getEditor());
                    textFieldList.add(comboFormat);
                    this.gridPaneValue.add(bufferSelection, 1, 1);
                    this.gridPaneValue.add(comboBox, 2, 1);
                    if ("Buffer list".equals(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String buffer = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().replace("" + Separator, "");
                            buffer = buffer.replace("@&Buffer_", "");
                            comboBox.getSelectionModel().select(buffer);
                        }
                    }
                    break;
                case "HMI":
                    Label HMIName = new Label("HMI name : ");
                    HMIName.setTooltip(new Tooltip("Type : HMI"));
                    comboBoxHMI.setItems(FXCollections.observableArrayList(HMIs));
                    comboBoxHMI.setCellFactory(new Callback() {
                        @Override
                        public Object call(Object listView) {
                            ListCell<HMI> cell = new ListCell<HMI>() {
                                @Override
                                public void updateItem(HMI item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setText(null);
                                        setGraphic(null);
                                    } else {
                                        setText(item.getName());
                                        setGraphic(null);
                                    }
                                }
                            };
                            return cell;
                        }
                    });

                    comboBoxHMI.setButtonCell(new ListCell<HMI>() {
                        @Override
                        protected void updateItem(HMI item, boolean bln) {
                            super.updateItem(item, bln);
                            if (bln) {
                                setText("");
                            } else {
                                setText(item.getName());
                            }

                        }
                    });
                    TextFieldWithFormat comboHMI = new TextFieldWithFormat("", false);

                    textFieldList.add(comboHMI);
                    this.gridPaneValue.add(HMIName, 1, 1);
                    this.gridPaneValue.add(comboBoxHMI, 2, 1);
                    comboBoxHMI.valueProperty().addListener(new ChangeListener<HMI>() {
                        @Override
                        public void changed(ObservableValue ov, HMI c, HMI c1) {
                            if (c1 != null) {
                                buttonSetParameters.setDisable(false);
                                HMI = comboBoxHMI.getSelectionModel().getSelectedItem().getName();
                                //comboHMI.setTextField(new TextField(comboBoxHMI.getSelectionModel().getSelectedItem().getName()));
//                                comboHMI.setTextField(comboBoxHMI.getEditor());
//                                System.out.println("Text is : "+comboBoxHMI.getPromptText());//getEditor().getText());
                            }
                        }

                    });
                    if ("HMI".equals(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String buffer = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue();
                            //buffer = buffer.replace("@&Buffer_", "");
                            System.out.println("BUGGER = " + buffer);
                            autoSelectComboBoxValue(comboBoxHMI, buffer.trim(), (cmbProp, val) -> cmbProp.getName().equals(val));
                            //comboBoxHMI.getSelectionModel().select(index);//getSelectionModel().select(buffer);
                        }
                    }
                    break;
                //default:
                case "From Other Script":
                    ChoiceBox<String> choiceBoxss = new ChoiceBox<>();
                    ComboBox comboBoxParam = new ComboBox();
                    ComboBox comboBoxScHasBConf = new ComboBox();
                    boolean hasBeenConfigBefore = false;

//                    comboBoxParam.setCellFactory(new Callback<ListView<Parameters>, ListCell<Parameters>>() {
//                        @Override
//                        public ListCell<Parameters> call(ListView<Parameters> p) {
//                            ListCell cell = new ListCell<Parameters>() {
//                                @Override
//                                protected void updateItem(Parameters item, boolean empty) {
//                                    super.updateItem(item, empty);
//                                    if (empty) {
//                                        setText("");
//                                    } else {
//                                        setText(item.getName());
//                                    }
//                                }
//                            };
//                            return cell;
//                        }
//                    });
//
//                    comboBoxParam.setButtonCell(new ListCell<Parameters>() {
//                        @Override
//                        protected void updateItem(Parameters item, boolean bln) {
//                            super.updateItem(item, bln);
//                            if (bln) {
//                                setText("");
//                            } else {
//                                setText(item.getName());
//                            }
//
//                        }
//                    });
                    //Labeling the comboBoxes
                    Label ScriptSelection = new Label("Select the script");
                    this.gridPaneValue.add(ScriptSelection, 1, 1);
                    this.gridPaneValue.add(choiceBoxss, 2, 1);
                    Label paramSelection = new Label("Select the parameter");
                    this.gridPaneValue.add(paramSelection, 1, 2);
                    this.gridPaneValue.add(comboBoxParam, 2, 2);

                    //Collection of Scripts to be displayed 
                    ObservableList<String> scriptsCollection = FXCollections.observableArrayList();

                    Iterator<StepLineTableStepController> itStep = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().iterator();
                    //controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get(4).getTestStep().getTestStepHasScripts().
                    //System.out.println(itStep.next().getTestStep().getTestStepHasScripts());
                    int stepCount = 0;
                    int scriptLineCount = 0;
                    int numScriptInStep = controller.getControllerScriptFather().getControllerStepParent().getNumberOdScript();
                    int scriptLinenum = controller.getControllerScriptFather().getControllerStepParent().getCollectionScript().indexOf(controller.getControllerScriptFather());

                    //if ()
                    //Setting up what scripts to put in scriptsCollection
                    System.out.println("        iD step : " + controller.getControllerScriptFather().getControllerStepParent().getIDStep());
                    while (itStep.hasNext() && stepCount < controller.getControllerScriptFather().getControllerStepParent().getIDStep()) {
                        ObservableList<ScriptLineTableStepController> scCont = itStep.next().getCollectionScript();
                        System.out.println("size of scriptLineTableStepContr: " + scCont.size());
                        Iterator<ScriptLineTableStepController> itScript = scCont.iterator();

                        scriptLineCount = 0;
                        while (itScript.hasNext()) {
                            ScriptLineTableStepController currScriptLine = itScript.next();
                            if ((scriptLineCount < scriptLinenum) || (stepCount + 1) < controller.getControllerScriptFather().getControllerStepParent().getIDStep()) {
                                if (currScriptLine.getScriptAction() != null) { //&& !currScriptLine.getScriptAction().equals(controller.getControllerScriptFather().getScriptAction())) {
                                    System.out.println("Inside: ScriptAction");
                                    String stringToAdd = (stepCount + 1) + "." + (scriptLineCount + 1) + " (" + currScriptLine.getScriptAction().getScript().getName() + ")";
                                    System.out.println("String just added to collection: " + stringToAdd);
                                    scriptsCollection.add(stringToAdd);
                                }
                                if (currScriptLine.getScriptVerif() != null) { // && !currScriptLine.getScriptVerif().equals(controller.getControllerScriptFather().getScriptVerif())) {
                                    System.out.println("Inside: ScriptVerif");
                                    String stringToAdd = (stepCount + 1) + "." + (scriptLineCount + 1) + " (" + currScriptLine.getScriptVerif().getScript().getName() + ")";
                                    System.out.println("String just added to collection: " + stringToAdd);

                                    scriptsCollection.add(stringToAdd);
                                }
                            }

                            scriptLineCount++;
                        }

                        stepCount++;
                    }
                    //int numScriptInStep = controller.getControllerScriptFather().getControllerStepParent().getNumberOdScript();
                    try {
                        if (controller.getTestStepHasScript().getScript().getIsStimuli() == 0 && controller.getControllerScriptFather().getScriptAction() != null) {
                            scriptsCollection.add((stepCount) + "." + (scriptLinenum + 1) + " (" + controller.getControllerScriptFather().getScriptAction().getScript().getName() + ")");
                        }
                    } catch (NullPointerException ex) {
                        //nothing
                    }
                    //Set Items in Scripts Collection Choice Box
                    choiceBoxss.setItems(scriptsCollection);

                    choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue1) -> {
                        //Label paramSelection = new Label("Select the parameter");
                        System.out.println("SCRIPT CHOOOSE");

                        this.currScriptChosen = choiceBoxss.getSelectionModel().getSelectedItem();

                        scriptChose = scriptsCollection.get(newValue1.intValue());
                        double id = Double.parseDouble(scriptChose.split(" ")[0]);
                        String scriptName = scriptChose.split(" ", 2)[1];
                        scriptName = scriptName.substring(1, scriptName.length() - 1);

                        //StepNumber
                        int step = (int) Math.round(id);

                        //ScriptLine Number
                        int scriptLineNum = (int) Math.round((id - (double) step) * 10);

                        System.out.println("ScriptName: " + scriptName + ", step: " + step + ", scriptLineNum: " + scriptLineNum);

                        StepLineTableStepController stepLine = controller.getControllerScriptFather().getControllerStepParent().getControllerViewGlobal().getCollectionTestStep().get(step - 1);

                        ScriptLineTableStepController scriptLine = stepLine.getCollectionScript().get(scriptLineNum - 1);
                        TestStepHasScript currTestStepHasScript;
                        Script currScript;

                        //finding the currentScript that was chosen from ScriptsCollection
                        if (scriptLine.getScriptAction() != null && scriptLine.getScriptAction().getScript().getName().equals(scriptName)) {
                            currScript = scriptLine.getScriptAction().getScript();
                            currTestStepHasScript = scriptLine.getScriptAction();
                        } else {
                            currScript = scriptLine.getScriptVerif().getScript();
                            currTestStepHasScript = scriptLine.getScriptVerif();
                        }

                        this.toLinkTestStepHasScript = currTestStepHasScript;

                        this.paramToDisplay = FXCollections.observableArrayList();

                        ArrayList<ScriptHasBeenConfigured> currSHBC = new ArrayList(currTestStepHasScript.getScriptHasBeenConfigureds());
                        Collections.sort(currSHBC, (ScriptHasBeenConfigured o1, ScriptHasBeenConfigured o2) -> Integer.compare(o1.getParamOrder(), o2.getParamOrder()));

                        //Gettting the parameters
                        if (currScript.getIsMacro() != 0) { //is a macro
                            this.currentParamScMacro = new ArrayList<>();
                            this.isMacro = true;
                            Set<Macro> macroSet = currScript.getMacrosForScriptIdScript();
                            Iterator<Macro> itMacro = macroSet.iterator();
                            int i = 0;
                            while (itMacro.hasNext()) {
                                Set<ParamScriptMacro> paramScMacroSet = itMacro.next().getParamScriptMacros();
                                Iterator<ParamScriptMacro> itParamScMacro = paramScMacroSet.iterator();
                                String toAdd = "";
                                while (itParamScMacro.hasNext()) {
                                    ParamScriptMacro paramScMacro = itParamScMacro.next();

                                    if (paramScMacro.getParamOrder() == 0) {
                                        toAdd += (paramScMacro.getValue() + " ");
                                    }

                                    if (paramScMacro.getToDisplay() != 0) {
                                        Parameters param = paramScMacro.getScriptHasParameters().getParameters();
                                        String toAddtemp = toAdd;
                                        toAdd += param.getName();
                                        if (currSHBC.get(i).getRefScriptHasBeenConfigured() == null) {
                                            this.paramToDisplay.add(toAdd);
                                        }
                                        //this.parametersListtoLink.add(paramScMacro);
                                        this.currentParamScMacro.add(toAdd);
                                        toAdd = toAddtemp;
                                        i++;
                                    }

                                }
                            }
                            //if it's not a macro
                        } else {
                            this.currentParam = new ArrayList<>();
                            this.isMacro = false;
                            Iterator<ScriptHasParameters> itScHasParam = currScript.getScriptHasParameterses().iterator();
                            //Iterator<TestStepHasScript> itTestStepHasScript = currScript.getTestStepHasScripts().iterator();
                            //itTestStepHasScript.next().get
                            int i = 0;
                            while (itScHasParam.hasNext()) {

                                Parameters param = itScHasParam.next().getParameters();
                                if (currSHBC.get(i).getRefScriptHasBeenConfigured() == null) {
                                    this.paramToDisplay.add(param.getName());
                                }
                                this.currentParam.add(param.getName());
                                //this.scriptHasBeenConfListtoLink.add();
                                i++;
                            }

                        }
                        comboBoxParam.setItems(this.paramToDisplay);
                        comboBoxParam.valueProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue ov, String t, String t1) {
                                System.out.println("PARAM CHOOSE, paramStrigtoLink: " + t1);
                                //paramToLink = t1;
                                paramStringToLink = t1;

                                buttonSetParameters.setDisable(false);
                            }
                        });

                    });

                    //if it has already been configured, select the boxes first
                    try {
                        if (this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath().charAt(0) == Separator.charAt(0)) {
                            //if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath().isEmpty()) {
                            if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getRefScriptHasBeenConfigured().getValue().isEmpty()) {
                                String refScript = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath();
                                refScript = refScript.substring(1);

                                String refParam = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getRefScriptHasBeenConfigured().getParameters().getName();
                                choiceBoxss.getSelectionModel().select(refScript);
                                comboBoxParam.getSelectionModel().select(refParam);

                                //rangTextField.setText(parts[4]);
                            }

                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("No previous valuePath in ScriptHasBeenConfigured");
                    }

                    break;
                case "Classes":
                    Label classeName = new Label("Classe name : ");
                    classeName.setTooltip(new Tooltip("Type : Classe"));
                    comboBoxClasse.setItems(FXCollections.observableArrayList(comboBoxHMI.getSelectionModel().getSelectedItem().getClasses()));
                    comboBoxClasse.setCellFactory(new Callback() {
                        @Override
                        public Object call(Object listView) {
                            ListCell<Classe> cell = new ListCell<Classe>() {
                                @Override
                                public void updateItem(Classe item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setText(null);
                                        setGraphic(null);
                                    } else {
                                        setText(item.getClasseName());
                                        setGraphic(null);
                                    }
                                }
                            };
                            return cell;
                        }
                    });

                    comboBoxClasse.setButtonCell(new ListCell<Classe>() {
                        @Override
                        protected void updateItem(Classe item, boolean bln) {
                            super.updateItem(item, bln);
                            if (bln) {
                                setText("");
                            } else {
                                setText(item.getClasseName());
                            }

                        }
                    });
                    TextFieldWithFormat comboClass = new TextFieldWithFormat("", false);

                    textFieldList.add(comboClass);
                    this.gridPaneValue.add(classeName, 1, 1);
                    this.gridPaneValue.add(comboBoxClasse, 2, 1);
                    comboBoxClasse.valueProperty().addListener(new ChangeListener<Classe>() {
                        @Override
                        public void changed(ObservableValue ov, Classe c, Classe c1) {
                            if (c1 != null) {
                                buttonSetParameters.setDisable(false);
                                Classe = comboBoxClasse.getSelectionModel().getSelectedItem().getClasseName();
                                //comboClass.setTextField(new TextField(comboBoxClasse.getSelectionModel().getSelectedItem().getClasseName()));
                            }
                        }

                    });
                    if ("Classes".equals(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String buffer = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue();
                            //buffer = buffer.replace("@&Buffer_", "");
                            System.out.println("BUGGER = " + buffer);
                            autoSelectComboBoxValue(comboBoxClasse, buffer.trim(), (cmbProp, val) -> cmbProp.getClasseName().equals(val));
                            //comboBoxHMI.getSelectionModel().select(index);//getSelectionModel().select(buffer);
                        }
                    }
                    break;
                case "Property":
                    Rectangle preview = new Rectangle(10, 10);
                    Hyperlink linkImg = new Hyperlink();
                    Label labelPreview = new Label("");
                    Label classeName2 = new Label("Classe name : ");
                    classeName2.setTooltip(new Tooltip("Type : Classe"));
                    Label classeState = new Label("State : ");
                    Label property = new Label("Property :");
                    //comboBoxClasse.setItems(Main.classFound);
                    Label HMI = new Label("HMI :");
                    comboboxHMI.setItems(FXCollections.observableArrayList(Main.HMIs));
                    this.setCellFactoryComboboxHMI();

                    this.setCellFactoryComboboxClass();
                    this.setCellFactoryComboboxState();
                    this.setCellFactoryComboboxProperty();

                    comboboxHMI.valueProperty().addListener(new ChangeListener<HMI>() {
                        @Override
                        public void changed(ObservableValue ov, HMI c, HMI c1) {
                            comboBoxClasse.getSelectionModel().clearSelection();
                            comboBoxClasse.setItems(null);
                            comboboxState.getSelectionModel().clearSelection();
                            comboboxState.setItems(null);
                            combobxProperty.getSelectionModel().clearSelection();
                            comboboxState.setItems(null);
                            HMIChose = c1;
                            if (HMIChose != null && !HMIChose.getClasses().isEmpty()) {
                                comboBoxClasse.setItems(FXCollections.observableArrayList(HMIChose.getClasses()));
                            }
                        }
                    });

                    comboBoxClasse.valueProperty().addListener(new ChangeListener<Classe>() {
                        @Override
                        public void changed(ObservableValue ov, Classe c, Classe c1) {
                            //System.out.println("CHANGE LISTENER");
                            comboboxState.getSelectionModel().clearSelection();
                            comboboxState.setItems(null);
                            combobxProperty.getSelectionModel().clearSelection();
                            comboboxState.setItems(null);
                            classChose = c1;
                            if (classChose != null && !classChose.getState().isEmpty()) {
                                comboboxState.setItems(classChose.getState());
                            }
                        }
                    });

                    combobxProperty.valueProperty().addListener(new ChangeListener<Properties>() {
                        @Override
                        public void changed(ObservableValue ov, Properties c, Properties c1) {
                            gridPaneValue.getChildren().remove(preview);
                            gridPaneValue.getChildren().remove(labelPreview);
                            gridPaneValue.getChildren().remove(linkImg);
                            if (c1 != null) {
                                Property = comboboxHMI.getSelectionModel().getSelectedItem().getName() + Separator + comboBoxClasse.getSelectionModel().getSelectedItem().getClasseName() + Separator + comboboxState.getSelectionModel().getSelectedItem().getStateName() + Separator + c1.getName() + Separator + c1.getValue();
                                buttonSetParameters.setDisable(false);
                                if (Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Type.color) {
                                    preview.setFill(Color.web(c1.getValue()));
                                    gridPaneValue.add(preview, 2, 5);
                                    GridPane.setHalignment(preview, HPos.CENTER);
                                    GridPane.setValignment(preview, VPos.CENTER);
                                } else if (Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Type.image) {
                                    linkImg.setText(c1.getValue());
                                    linkImg.setOnAction((ActionEvent e) -> {
                                        //if (canBeConfigured) {
                                        displayPreview(c1.getValue());
                                        //}
                                    });
                                    gridPaneValue.add(linkImg, 2, 5);
                                } else if (Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Type.integer || Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Type.string) {
                                    labelPreview.setText(String.valueOf(c1.getValue()));
                                    gridPaneValue.add(labelPreview, 2, 5);
                                }
                            }
                        }
                    });
                    comboboxState.valueProperty().addListener(new ChangeListener<StateClasse>() {
                        @Override
                        public void changed(ObservableValue ov, StateClasse c, StateClasse c2) {
                            combobxProperty.getSelectionModel().clearSelection();
                            combobxProperty.setItems(null);
                            ObservableList<Properties> choiceProperties = FXCollections.observableArrayList();
                            if (c2 != null && !c2.getProperties().isEmpty()) {
                                for (int i = 0; i < c2.getProperties().size(); i++) {
                                    if (c2.getProperties().get(i).getType().equals(Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()))) {
                                        choiceProperties.add(c2.getProperties().get(i));
                                    }
                                }
                                combobxProperty.setItems(choiceProperties);
                            }
                        }
                    });

                    Label previewText = new Label("Preview : ");
                    this.gridPaneValue.add(HMI, 1, 1);
                    this.gridPaneValue.add(comboboxHMI, 2, 1);
                    GridPane.setHalignment(comboboxHMI, HPos.CENTER);
                    GridPane.setValignment(comboboxHMI, VPos.CENTER);
                    this.gridPaneValue.add(classeName2, 1, 2);
                    this.gridPaneValue.add(comboBoxClasse, 2, 2);
                    GridPane.setHalignment(comboBoxClasse, HPos.CENTER);
                    GridPane.setValignment(comboBoxClasse, VPos.CENTER);
                    this.gridPaneValue.add(classeState, 1, 3);
                    this.gridPaneValue.add(comboboxState, 2, 3);
                    GridPane.setHalignment(comboboxState, HPos.CENTER);
                    GridPane.setValignment(comboboxState, VPos.CENTER);
                    this.gridPaneValue.add(property, 1, 4);
                    this.gridPaneValue.add(combobxProperty, 2, 4);
                    GridPane.setHalignment(combobxProperty, HPos.CENTER);
                    GridPane.setValignment(combobxProperty, VPos.CENTER);
                    this.gridPaneValue.add(previewText, 1, 5);
                    if ("Property".equals(this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String[] parts = this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().split("" + Separator);
                            autoSelectComboBoxValue(comboboxHMI, parts[0].trim(), (cmbProp, val) -> cmbProp.getName().equals(val));
                            autoSelectComboBoxValue(comboBoxClasse, parts[1].trim(), (cmbProp, val) -> cmbProp.getClasseName().equals(val));
                            autoSelectComboBoxValue(comboboxState, parts[2].trim(), (cmbProp, val) -> cmbProp.getStateName().equals(val));
                            autoSelectComboBoxValue(combobxProperty, parts[3].trim(), (cmbProp, val) -> cmbProp.getName().equals(val));

                        }

                    }
                    break;
            }
        }

    }

    /**
     * Clear the content on the new panel configuration parameters.
     */
    private void clearDisplayPanel() {
        this.gridPaneValue.getChildren().clear();
    }

    /**
     * Change the color of a label depending on a boolean.
     *
     * @param label label to change the color of.
     * @param empty boolean.
     */
    private void changeColorLabel(Label label, Boolean empty) {
        if (empty) {
            label.setTextFill(Color.RED);
        } else {
            label.setTextFill(Color.BLACK);
        }
    }

    private void changeColorRadioButton(RadioButton radioButton, Boolean empty) {
        if (empty) {
            radioButton.setTextFill(Color.RED);
        } else {
            radioButton.setTextFill(Color.BLACK);
        }
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void addStringInBuffer(String buffer) {
        boolean foundInComboBox = false;
        int i = 0;
        while (!foundInComboBox && i < BufferList.size()) {
            if ((buffer).equals(BufferList.get(i))) {

                foundInComboBox = true;
            }
            i++;
        }
        if (!foundInComboBox) {
            System.out.println("BUFFER = " + buffer + " not found");
            //System.out.println("New value is :" + t1);
            BufferList.add(buffer);
        }
    }

    /**
     *
     * @param <T>
     * @param comboBox
     * @param value
     * @param f
     */
    public static <T> void autoSelectComboBoxValue(ComboBox<T> comboBox, String value, Func<T, String> f) {
        comboBox.getItems().stream().filter((t) -> (f.compare(t, value))).forEach((t) -> {
            comboBox.getSelectionModel().select(t);
        });
    }

    /**
     *
     * @param <T>
     * @param <V>
     */
    public interface Func<T, V> {

        /**
         *
         * @param t
         * @param v
         * @return
         */
        boolean compare(T t, V v);
    }

    private void setCellFactoryComboboxHMI() {
        comboboxHMI.setCellFactory(new Callback() {
            @Override
            public Object call(Object listView) {
                ListCell<HMI> cell = new ListCell<HMI>() {
                    @Override
                    public void updateItem(HMI item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });

        comboboxHMI.setButtonCell(new ListCell<HMI>() {
            @Override
            protected void updateItem(HMI item, boolean bln) {
                super.updateItem(item, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(item.getName());
                }

            }
        });
    }

    private void setCellFactoryComboboxClass() {
        comboBoxClasse.setCellFactory(new Callback() {
            @Override
            public Object call(Object listView) {
                ListCell<Classe> cell = new ListCell<Classe>() {
                    @Override
                    public void updateItem(Classe item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getClasseName());
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });

        comboBoxClasse.setButtonCell(new ListCell<Classe>() {
            @Override
            protected void updateItem(Classe item, boolean bln) {
                super.updateItem(item, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(item.getClasseName());
                }

            }
        });
    }

    private void setCellFactoryComboboxState() {
        comboboxState.setCellFactory(new Callback() {
            @Override
            public Object call(Object listView) {
                ListCell<StateClasse> cell = new ListCell<StateClasse>() {
                    @Override
                    public void updateItem(StateClasse item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getStateName());
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });

        comboboxState.setButtonCell(new ListCell<StateClasse>() {
            @Override
            protected void updateItem(StateClasse item, boolean bln) {
                super.updateItem(item, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(item.getStateName());
                }

            }
        });

    }

    private void setCellFactoryComboboxProperty() {
        combobxProperty.setCellFactory(new Callback() {
            @Override
            public Object call(Object listView) {
                ListCell<Properties> cell = new ListCell<Properties>() {
                    @Override
                    public void updateItem(Properties item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        });

        combobxProperty.setButtonCell(new ListCell<Properties>() {
            @Override
            protected void updateItem(Properties item, boolean bln) {
                super.updateItem(item, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(item.getName());
                }

            }
        });
    }

    /**
     *
     * @param path
     */
    public void displayPreview(String path) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Load Image");

        StackPane sp = new StackPane();
        Image img = new Image(Paths.get(path).toUri().toString(), 500, 500, true, true);
        ImageView imgView = new ImageView(img);
        sp.getChildren().add(imgView);

        //Adding HBox to the scene
        Scene scene = new Scene(sp);
        primaryStage.setScene(scene);
        primaryStage.initOwner(Main.primaryStage);
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.show();
    }

    /**
     *
     * @param param
     */
    public void deleteReferenceToParam(ScriptHasBeenConfigured param) {
        //for (ScriptLineTableStepController collectionControllerScript : controller.getControllerScriptFather().controllerViewGlobal().getCollectionControllerScript()) {
        for (ScriptLineTableStepController collectionControllerScript : controller.getControllerScriptFather().getControllerStepParent().getCollectionScript()) {
            Set<ScriptHasBeenConfigured> scHasBeenConfSet = new HashSet<>(collectionControllerScript.getScriptView().getTestStepHasScript().getScriptHasBeenConfigureds());
            Iterator<ScriptHasBeenConfigured> scHasBeenConfIt = scHasBeenConfSet.iterator();
            while (scHasBeenConfIt.hasNext()) {
                ScriptHasBeenConfigured scHasBeenConf = scHasBeenConfIt.next();
                if (scHasBeenConf.getRefScriptHasBeenConfigured() == param) {
                    scHasBeenConf.setRefScriptHasBeenConfigured(null);
                    scHasBeenConf.setValue("");
                    scHasBeenConf.setValuePath("");
                }

            }
        }

    }

    private int getTestStepHasScriptIndex(int scriptNum) {

        return 0;
    }

}

//        columnValue.setCellValueFactory(new Callback<CellDataFeatures<Parameters, Parameters>, ObservableValue<Parameters>>() {
//
//            @Override
//            public ObservableValue<Parameters> call(CellDataFeatures<Parameters, Parameters> param) {
//                return new ReadOnlyObjectWrapper(param.getValue());
//            }
//        });
//
//        columnValue.setCellFactory(new Callback<TableColumn<Parameters, Parameters>, TableCell<Parameters, Parameters>>() {
//            @Override
//            public TableCell<Parameters, Parameters> call(TableColumn<Parameters, Parameters> btnCol) {
//                return new TableCell<Parameters, Parameters>() {
//                    final ImageView buttonGraphic = new ImageView();
//                    final Button button = new Button();
//
//                    final int numberButton = numberofbutton;
//
//                    {
//                        button.setGraphic(buttonGraphic);
//                        //button.setMinWidth(16);
//                        button.setAlignment(Pos.CENTER);
//                        button.setMaxSize(16, 16);
//                    }
//
//                    @Override
//                    public void updateItem(final Parameters person, boolean empty) {
//                        super.updateItem(person, empty);
//                        if (person != null) {
//                            buttonGraphic.setImage(settingsImage);
//                            setGraphic(button);
//                            button.setOnAction(new EventHandler<ActionEvent>() {
//                                @Override
//                                public void handle(ActionEvent event) {
//                                    //System.out.println("Number of this button : " + numberButton);
//                                    rowSelection = numberButton - 1;
//                                    //tableParameters.getSelectionModel().focus(row);
//                                    // Row row = tableParameters.get
//                                    Platform.runLater(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            //System.out.println("Row selection :" + rowSelection);
//                                            tableParameters.requestFocus();
//                                            tableParameters.getSelectionModel().select(rowSelection);
//                                            tableParameters.getFocusModel().focus(rowSelection);
//                                           // System.out.println("Plateforme lance");
//                                        }
//                                    });
//                                    //TO be changer !!!!!!!
//                                    showStackPaneCOnfiguration();
//                                    displaySavedInformation(observableListScriptMartinth.get(rowSelection));
//                                    //actionTaken.setText("Bought " + person.getLikes().toLowerCase() + " for: " + person.getFirstName() + " " + person.getLastName());
//                                }
//
//                                private void showStackPaneCOnfiguration() {
//                                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                                    stackPan.setVisible(true);
//                                }
//
//                            });
//                            numberofbutton++;
//                        } else {
//                            setGraphic(null);
//                        }
//                        //button.setText("Buy coffee");
//
//                    }
//
//                };
//            }
//        ;
//        });
//        
//    private void displaySavedInformation(paramsForScript get) {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        System.out.println("Paramforscript :"+get);
//        if(get.getConfigured()){
//             int typeOfVariable = get.getTypeofVariable();
//        if (typeOfVariable == 1) {
//            this.constantRadioButton.setSelected(true);
//            this.variableRadioButton.setSelected(false);
//            this.textFiledValueConstant.setText(get.getValue());
//        } else if (typeOfVariable == 2) {
//            this.constantRadioButton.setSelected(false);
//            this.variableRadioButton.setSelected(true);
//            this.choiceBoxVariable.getSelectionModel().select(get.getNumberVariableChoiceBox());
//            this.textFiledValueConstant.setText(get.getValue());
//        } 
//        }else{
//           //displayConfiguration(this.constantRadioButton);
//           this.textFiledValueConstant.setText("");
//        }
//       
//   }
//    private void displayConfiguration(Toggle toogle) {
//
//        if (toogle == variableRadioButton) {
//
//            this.choiceBoxVariable.setVisible(true);
//            this.labelSourceVariable.setVisible(true);
//
//            //this.stackPaneVariable.getChildren().setAll(this.stackPaneModel.get(0));
//        } else if (toogle == constantRadioButton) {
//
//            this.choiceBoxVariable.setVisible(false);
//            this.labelSourceVariable.setVisible(false);
//
//            //this.stackPaneVariable.getChildren().setAll(this.stackPaneModel.get(1));
//        } else {
//            System.out.println("j ai un souc");
//        }
//
//    }
//        ToggleGroup group = new ToggleGroup();
//        variableRadioButton.setToggleGroup(group);
//        constantRadioButton.setToggleGroup(group);
//        constantRadioButton.setSelected(true);
//        this.observableListVariableChoicebox.add("Excel column");
//        this.observableListVariableChoicebox.add("Excel range");
//        this.choiceBoxVariable.setVisible(false);
//        this.labelSourceVariable.setVisible(false);
//        this.choiceBoxVariable.setItems(this.observableListVariableChoicebox);
//        this.stackPan.setVisible(false);
//
//        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            public void changed(ObservableValue<? extends Toggle> ov,
//                    Toggle old_toggle, Toggle new_toggle) {
//                if (group.getSelectedToggle() != null) {
//                    displayConfiguration(new_toggle);
//                }
//            }
//
//        });
