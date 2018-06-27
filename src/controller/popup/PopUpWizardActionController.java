/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.ParamScriptMacro;
import DB.Parameters;
import DB.Script;
import DB.ScriptHasParameters;
import controller.macroActions.PreviewMacro;
import controller.macroActions.ScriptLineTableMacroController;
import controller.macroActions.ViewScriptMacroController;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import model.Classe;
import model.HMI;
import model.Properties;
import model.StateClasse;
import model.TextFieldWithFormat;

/**
 * FXML Controller class
 *
 * @author Morin Thomas
 */
public class PopUpWizardActionController implements Initializable {

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

    private static ViewScriptMacroController controller;

    private static PreviewMacro previewController;

    private final ObservableList<Parameters> observableListParam = FXCollections.observableArrayList();

    private ArrayList<ParamScriptMacro> observableListParams = new ArrayList<>();

    private static final ObservableList<String> BufferList = FXCollections.observableArrayList();

    private final ObservableList<TextFieldWithFormat> textFieldList = FXCollections.observableArrayList();

    private final static String Separator = ("" + ((char) 007));

    ParamScriptMacro paramToLink = new ParamScriptMacro();

    String scriptChose;

    String paramChose;

    private final ComboBox<Classe> comboBoxClasse = new ComboBox();

    private final ComboBox<Properties> combobxProperty = new ComboBox();

    private final ComboBox<StateClasse> comboboxState = new ComboBox();

    private final ComboBox<HMI> comboboxHMI = new ComboBox();

    private Classe classChose;

    private String Property;

    private HMI HMIChose;

    /**
     * Initializes the controller class. Class of the configuration popup for
     * scripts.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println("NOUVEAU CONTROLLER");
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
         Linked the tableview to it's corresponding observable list.
         */
        this.tableParameters.setItems(this.observableListParam);

        /*
         Add a listener on the comboBox value (comboBox to select where the values are from) in order to redraw the right panel of this popup.
         */
        comboBoxDefinitionValue.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            displayPanel(newValue);
        });

        /*
         Add a listener on the table, to know which row is seleted in order to redraw the right side.
         */
        this.tableParameters.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection)
                -> {
                    if (this.tableParameters.getSelectionModel().getSelectedIndex() >= 0) {
                        comboBoxDefinitionValue.getSelectionModel().clearSelection();
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
    public void init(ViewScriptMacroController aThis) {
        controller = aThis;
    }

    /**
     * This method is called by the class @see viewScript and refere the
     * parameters is they have been already configured or not, construct the
     * left part of the popup.
     *
     * @param script Script selected in the combobox of view script
     * @param paramScriptMacro
     * @param selectedParam number of the selected parameter in the viewScript.
     */
    public void constructInformation(Script script, ObservableList<ParamScriptMacro> paramScriptMacro, int selectedParam) {

        this.descriptionScript.setText(script.getDesciption());
        this.choiceBoxScript.setItems(FXCollections.observableArrayList(script.getName()));     //Allow only to show one Script name in the Display Wizzard.
        this.choiceBoxScript.getSelectionModel().selectFirst();

        this.observableListParams = new ArrayList<>(paramScriptMacro);
        Collections.sort(this.observableListParams, (ParamScriptMacro o1, ParamScriptMacro o2) -> Integer.compare(o1.getScriptHasParameters().getParamOrder(), o2.getScriptHasParameters().getParamOrder()));
        Iterator<ScriptHasParameters> itScriptHasParameters = script.getScriptHasParameterses().iterator();
        while (itScriptHasParameters.hasNext()) {
            ScriptHasParameters scriptHasParameters = itScriptHasParameters.next();
            this.observableListParam.add(scriptHasParameters.getParameters());
        }
        this.tableParameters.getSelectionModel().select(selectedParam);

    }

    /**
     * Return the observablelist with the configured parameters.
     *
     * @return observableListParams list with the configured parameters.
     */
    public ArrayList<ParamScriptMacro> getConfigScript() {
        return this.observableListParams;
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
        ParamScriptMacro toBeSet = this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex());
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
                    toBeSet.setParamScriptMacro(null);
                    value = value.replace(Separator, "");
                    toBeSet.setValue(Separator + "@&Buffer_" + value + Separator);
                    break;
                case "From Other Script":
                    if (toBeSet.getParamScriptMacros() != null) {
                        deleteReferenceToParam(toBeSet);
                    }
                    toBeSet.setParamScriptMacro(paramToLink);
                    toBeSet.setValuePath(scriptChose);
                    toBeSet.setValue(paramToLink.getScriptHasParameters().getParameters().getName());
                    //System.out.println("PARAM = " + toBeSet.getParamScriptMacro().getValue());
                    byte configured = 0;
                    toBeSet.setToDisplay(configured);
                    break;
                case "Property":
                    toBeSet.setValue(Property);
                    break;
                default:
                    toBeSet.setParamScriptMacro(null);
                    toBeSet.setValue(value);
                    break;
            }
            controller.getControllerScriptFather().controllerViewGlobal().getCollectionControllerScript().stream().forEach((ScriptLineTableMacroController collectionControllerScript) -> {
                ArrayList<ParamScriptMacro> params = new ArrayList<>(collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro());
                collectionControllerScript.getScriptControllerAction().referParameters(params);

                System.out.println("entered reconfigure of sc");
            });
            toBeSet.setToDisplay((byte) 0);
        }

    }

    /**
     * Construct the right panel of the popup and load parameters if any are
     * available in the observable list.
     */
    private void constructConfigurationParameters() {

        Parameters paramFromLine = this.tableParameters.getSelectionModel().getSelectedItem();
        labelParamName.setText(paramFromLine.getName());
        labelParamType.setText(paramFromLine.getParameterType());
        textAreaDescriptionParameters.setText(paramFromLine.getDescription());
        ObservableList<String> options = getFXCollectionFromParamtersType(paramFromLine.getParameterType());
        comboBoxDefinitionValue.getItems().setAll(options);
        //System.out.println("Taille de mon array list = "+observableListScriptMartinth.size());
        String comboValue = this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath();

        if (!comboValue.isEmpty() && this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getParamScriptMacro() == null) {
            //System.out.println("Je ne suis pas empty");
            comboBoxDefinitionValue.getSelectionModel().select(comboValue);
            //displayPanel(comboValue);
        } else if (paramFromLine.getParameterType().equals("buffer")) {
            comboBoxDefinitionValue.getSelectionModel().select("Buffer list");
        } else if (this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getParamScriptMacro() != null) {
            comboBoxDefinitionValue.getSelectionModel().select("From Other Script");
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
                    listOfOptions = FXCollections.observableArrayList("Constant", "Buffer list", "From Other Script");
                    break;
                case "ip":
                    listOfOptions = FXCollections.observableArrayList("Constant", "From Other Script");//, "Instantiate from Excel file");
                    break;
                case "string":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Buffer list", "From Other Script", "Property");//("Constant", "Absolute path", "Variable path", "Excel file", "Instantiate from Excel file","Buffer");
                    break;
                case "buffer":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Buffer list", "From Other Script");//("Constant", "Absolute path", "Variable path", "Excel file", "Instantiate from Excel file","Buffer");
                    break;
                case "color":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Buffer list", "Property");
                    break;
                case "image":
                    listOfOptions = FXCollections.observableArrayList("Constant", "Buffer list", "Property");
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
                    if ("Constant".equals(this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        constantTextField.setText(this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().replace("" + Separator, ""));
                    }
                    this.gridPaneValue.add(constantLabel, 1, 1);
                    this.gridPaneValue.add(constantTextField.getTextField(), 2, 1);
                    textFieldList.add(constantTextField);
                    break;
                case "Excel file":
                    Label sheetValue = new Label("Sheet number ");
                    sheetValue.setTooltip(new Tooltip("Type : Integer"));
                    Label positionX = new Label("Column position ");
                    positionX.setTooltip(new Tooltip("Type : Integer"));
                    Label positionY = new Label("Row position ");
                    positionY.setTooltip(new Tooltip("Type : Integer"));
//                    Label rang = new Label("Range ");
//                    rang.setTooltip(new Tooltip("Type : Integer"));

                    changeColorLabel(sheetValue, true);
                    changeColorLabel(positionX, true);
                    changeColorLabel(positionY, true);
                    //changeColorLabel(rang, true);

                    buttonSetParameters.setDisable(true);

                    TextFieldWithFormat sheetTextField = new TextFieldWithFormat("[0-9]{1,10}", true);
                    TextFieldWithFormat postXTextField = new TextFieldWithFormat("[0-9]{1,10}", true);
                    TextFieldWithFormat posYTextField = new TextFieldWithFormat("[0-9]{1,10}", true);
                    //TextFieldWithFormat rangTextField = new TextFieldWithFormat("[0-9]{1,10}", true);

                    sheetTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        changeColorLabel(sheetValue, new2Value.trim().isEmpty());
                        if (!sheetTextField.getText().isEmpty()
                                && !postXTextField.getText().isEmpty()
                                && !posYTextField.getText().isEmpty()) {
                            buttonSetParameters.setDisable(false);
                        } else {
                            buttonSetParameters.setDisable(true);
                        }
                    });
                    postXTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        if (!sheetTextField.getText().isEmpty()
                                && !postXTextField.getText().isEmpty()
                                && !posYTextField.getText().isEmpty()) {
                            buttonSetParameters.setDisable(false);
                        } else {
                            buttonSetParameters.setDisable(true);
                        }
                        changeColorLabel(positionX, new2Value.trim().isEmpty());
                    });
                    posYTextField.getTextField().textProperty().addListener((observable, oldValue, new2Value) -> {
                        if (!sheetTextField.getText().isEmpty()
                                && !postXTextField.getText().isEmpty()
                                && !posYTextField.getText().isEmpty()) {
                            buttonSetParameters.setDisable(false);
                        } else {
                            buttonSetParameters.setDisable(true);
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

                    if ("Excel file".equals(this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String[] parts = this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().split("" + Separator);
                            sheetTextField.setText(parts[1]);
                            postXTextField.setText(parts[2]);
                            posYTextField.setText(parts[3]);
                            //rangTextField.setText(parts[4]);
                        }

                    }
                    this.gridPaneValue.add(sheetValue, 1, 1);
                    this.gridPaneValue.add(sheetTextField.getTextField(), 2, 1);
                    this.gridPaneValue.add(positionX, 1, 2);
                    this.gridPaneValue.add(postXTextField.getTextField(), 2, 2);
                    this.gridPaneValue.add(positionY, 1, 3);
                    this.gridPaneValue.add(posYTextField.getTextField(), 2, 3);
                    textFieldList.add(sheetTextField);
                    textFieldList.add(postXTextField);
                    textFieldList.add(posYTextField);

                    break;
                case "Buffer list":
                    Label bufferSelection = new Label("Select buffer");
                    System.out.println("Selected Buffer List");
                    final ComboBox comboBox = new ComboBox();
                    comboBox.setEditable(true);
                    comboBox.setItems(BufferList);
                    comboBox.valueProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue ov, String t, String t1) {
                            boolean foundInComboBox = false;
                            int i = 0;
                            while (!foundInComboBox && i < BufferList.size()) {
                                if ((t1).equals(BufferList.get(i))) {
                                    foundInComboBox = true;
                                }
                                System.out.println("inside buffer_changed");
                                i++;
                            }
                            if (!foundInComboBox && !t1.isEmpty()) {
                                System.out.println("New value is :" + t1);
                                BufferList.add(t1);
                            }
                            buttonSetParameters.setDisable(false);
                        }
                    });
                    TextFieldWithFormat comboFormat = new TextFieldWithFormat("", false);
                    comboFormat.setTextField(comboBox.getEditor());
                    textFieldList.add(comboFormat);
                    this.gridPaneValue.add(bufferSelection, 1, 1);
                    this.gridPaneValue.add(comboBox, 2, 1);
                    if ("Buffer list".equals(this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        if (!this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String buffer = this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().replace("" + Separator, "");
                            buffer = buffer.replace("@&Buffer_", "");
                            comboBox.getSelectionModel().select(buffer);
                        }

                    }
                    break;
                case "From Other Script":
                    ChoiceBox<String> choiceBoxss = new ChoiceBox<>();
                    ComboBox comboBoxParam = new ComboBox();

                    comboBoxParam.setCellFactory(new Callback<ListView<ParamScriptMacro>, ListCell<ParamScriptMacro>>() {
                        @Override
                        public ListCell<ParamScriptMacro> call(ListView<ParamScriptMacro> p) {
                            ListCell cell = new ListCell<ParamScriptMacro>() {
                                @Override
                                protected void updateItem(ParamScriptMacro item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setText("");
                                    } else {
                                        setText(item.getScriptHasParameters().getParameters().getName());
                                    }
                                }
                            };
                            return cell;
                        }
                    });

                    comboBoxParam.setButtonCell(new ListCell<ParamScriptMacro>() {
                        @Override
                        protected void updateItem(ParamScriptMacro item, boolean bln) {
                            super.updateItem(item, bln);
                            if (bln) {
                                setText("");
                            } else {
                                setText(item.getScriptHasParameters().getParameters().getName());
                            }

                        }
                    });
                    Label ScriptSelection = new Label("Select the script");
                    this.gridPaneValue.add(ScriptSelection, 1, 1);
                    this.gridPaneValue.add(choiceBoxss, 2, 1);
                    Label paramSelection = new Label("Select the parameter");
                    this.gridPaneValue.add(paramSelection, 1, 2);
                    this.gridPaneValue.add(comboBoxParam, 2, 2);

                    ObservableList<String> scriptsCollection = FXCollections.observableArrayList();
                    for (int i = 0; i < controller.getControllerScriptFather().getIDScript() - 1; i++) {
                        //System.out.println("HERE CHOICE");
                        String stringToAdd = i + 1 + " (" + controller.getControllerScriptFather().controllerViewGlobal().getCollectionControllerScript().get(i).getScriptControllerAction().getCurrentScript().getName() + ")";
                        scriptsCollection.add(stringToAdd);
                    }

                    choiceBoxss.setItems(scriptsCollection);
                    choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue1) -> {
                        System.out.println("SCRIPT CHOOOSE");
                        scriptChose = scriptsCollection.get(newValue1.intValue());
                        int id = Integer.parseInt(scriptChose.split(" ")[0]);
                        ViewScriptMacroController scriptSelected = controller.getControllerScriptFather().controllerViewGlobal().getCollectionControllerScript().get(id - 1).getScriptControllerAction();
                        ObservableList<ParamScriptMacro> paramToDisplay = FXCollections.observableArrayList();
                        for (int i = 0; i < scriptSelected.getHashParamScriptMacro().size(); i++) {
                            if (scriptSelected.getHashParamScriptMacro().get(i).getParamScriptMacro() == null) {
                                paramToDisplay.add(scriptSelected.getHashParamScriptMacro().get(i));
                            }
                        }
                        comboBoxParam.setItems(paramToDisplay);
                        comboBoxParam.valueProperty().addListener(new ChangeListener<ParamScriptMacro>() {
                            @Override
                            public void changed(ObservableValue ov, ParamScriptMacro t, ParamScriptMacro t1) {
                                //System.out.println("PARAM CHOOSE");
                                paramToLink = t1;
                                buttonSetParameters.setDisable(false);
                            }
                        });
//                            ObservableList<String> paramsCollection = FXCollections.observableArrayList();
//
//                            Iterator<ParamScriptMacro> itParamMacro = scriptSelected.getHashParamScriptMacro().iterator();
//                            while (itParamMacro.hasNext()) {
//                                ParamScriptMacro paramMacro = itParamMacro.next();
//                                paramsCollection.add(paramMacro.getScriptHasParameters().getParameters().getName());
//
//                            }
//                            choiceBoxParam.setItems(paramsCollection);
//                            choiceBoxParam.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//                                @Override
//                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                                    paramToLink = scriptSelected.getHashParamScriptMacro().get(newValue.intValue());
//                                    buttonSetParameters.setDisable(false);
//                                }
//                            });
                    });
                     {
                    }
                    if (this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getParamScriptMacro() != null) {
                        String script = this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath();
                        //System.out.println("SCRIPT = " + script);
                        choiceBoxss.getSelectionModel().select(script);
                        comboBoxParam.getSelectionModel().select(this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getParamScriptMacro());
                    }

                    //System.out.println("NOMBRE DE SCRIPT = " + controller.getControllerScriptFather().controllerViewGlobal().collectionControllerScript.size());
                    //System.out.println("ID SELECTED SCRIPT = " + controller.getControllerScriptFather().getIDScript());
                    break;
                case "Property":
                    Rectangle preview = new Rectangle(10, 10);
                    Hyperlink linkImg = new Hyperlink();
                    Label labelPreview = new Label("");
                    Label classeName2 = new Label("Classe name : ");
                    classeName2.setTooltip(new Tooltip("Type : Classe"));
                    Label classeState = new Label("State : ");
                    Label property = new Label("Property :");
                    Label HMI = new Label("HMI :");
                    comboboxHMI.setItems(FXCollections.observableArrayList(Main.HMIs));
                    this.setCellFactoryComboboxHMI(); //                    /comboBoxClasse.setItems(Main.classFound);
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
//                            System.out.println("State= " + classChose.getState());
                            if (HMIChose != null && !HMIChose.getClasses().isEmpty()) {
                                comboBoxClasse.setItems(FXCollections.observableArrayList(HMIChose.getClasses()));
                            }

                        }
                    });

                    comboBoxClasse.valueProperty().addListener(new ChangeListener<Classe>() {
                        @Override
                        public void changed(ObservableValue ov, Classe c, Classe c1) {
                            System.out.println("CHANGE LISTENER");
                            comboboxState.getSelectionModel().clearSelection();
                            comboboxState.setItems(null);
                            combobxProperty.getSelectionModel().clearSelection();
                            comboboxState.setItems(null);
                            classChose = c1;
//                            System.out.println("State= " + classChose.getState());
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
                                if (Properties.Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Properties.Type.color) {
                                    preview.setFill(Color.web(c1.getValue()));
                                    gridPaneValue.add(preview, 2, 5);
                                    GridPane.setHalignment(preview, HPos.CENTER);
                                    GridPane.setValignment(preview, VPos.CENTER);
                                } else if (Properties.Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Properties.Type.image) {
                                    linkImg.setText(c1.getValue());
                                    linkImg.setOnAction((ActionEvent e) -> {
                                        //if (canBeConfigured) {
                                        displayPreview(c1.getValue());
                                        //}
                                    });
                                    gridPaneValue.add(linkImg, 2, 5);
                                } else if (Properties.Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Properties.Type.integer || Properties.Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()) == Properties.Type.string) {
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
                                    //System.out.println("TYPE = " + c1.getProperties().get(i).getType().toString());
                                    if (c2.getProperties().get(i).getType().equals(Properties.Type.valueOf(tableParameters.getSelectionModel().getSelectedItem().getParameterType()))) {
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
                    if ("Property".equals(this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValuePath())) {
                        //System.out.println("VALUE - " + this.observableListScriptMartinth.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue());
                        if (!this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().isEmpty()) {
                            String[] parts = this.observableListParams.get(this.tableParameters.getSelectionModel().getSelectedIndex()).getValue().split("" + Separator);
                            System.out.println("SELECT COMBOBIX");
                            autoSelectComboBoxValue(comboboxHMI, parts[0].trim(), (cmbProp, val) -> cmbProp.getName().equals(val));
                            autoSelectComboBoxValue(comboBoxClasse, parts[1].trim(), (cmbProp, val) -> cmbProp.getClasseName().equals(val));
                            autoSelectComboBoxValue(comboboxState, parts[2].trim(), (cmbProp, val) -> cmbProp.getStateName().equals(val));
                            autoSelectComboBoxValue(combobxProperty, parts[3].trim(), (cmbProp, val) -> cmbProp.getName().equals(val));
                        }
                    }
                    break;

                case "Instantiate from Excel file":

                    break;
                case "Absolute path":

                    break;
                case "Variable path":

                    break;
                default:

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

    /**
     *
     * @param param
     */
    public void deleteReferenceToParam(ParamScriptMacro param) {
        for (ScriptLineTableMacroController collectionControllerScript : controller.getControllerScriptFather().controllerViewGlobal().getCollectionControllerScript()) {
            ArrayList<ParamScriptMacro> paramsScriptMacro = new ArrayList<>(collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro());
            paramsScriptMacro.stream().filter((ParamScriptMacro paramsScriptMacro1) -> (paramsScriptMacro1.getParamScriptMacro() == param)).map((paramsScriptMacro1) -> {
                paramsScriptMacro1.setParamScriptMacro(null);
                return paramsScriptMacro1;
            }).map((paramsScriptMacro1) -> {
                paramsScriptMacro1.setValue("");
                return paramsScriptMacro1;
            }).map((paramsScriptMacro1) -> {
                paramsScriptMacro1.setValuePath("");
                return paramsScriptMacro1;
            }).forEach((paramsScriptMacro1) -> {
                paramsScriptMacro1.setToDisplay((byte) 1);
            });
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
        for (T t : comboBox.getItems()) {
            if (f.compare(t, value)) {
                comboBox.getSelectionModel().select(t);
            }
        }
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
}
