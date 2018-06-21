/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.scriptParameters;

import DB.Macro;
import DB.ParamScriptMacro;
import DB.Parameters;
import DB.Script;
import controller.macroActions.ScriptLineTableMacroController;
import controller.macroActions.TableActionCreationController;
import controller.macroActions.ViewScriptMacroController;
import controller.scripts.TableParamCreationController;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class ScriptLineParameterController implements Initializable {

    @FXML
    private AnchorPane anchroPaneScript;
    @FXML
    private GridPane gridPaneScript;
    @FXML
    private ListView<?> listRequirements;
    @FXML
    private Label labelID;
    @FXML
    private ImageView imageViewTrash;
    @FXML
    private ImageView imageUp;
    @FXML
    private ImageView imageDown;
    @FXML
    private ComboBox comboBoxParam;
    @FXML
    private ChoiceBox choiceBoxType;
    @FXML
    private TextArea description;
    @FXML
    private GridPane gridPaneParam;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldName2;
    @FXML
    private TextField textFieldType;

    private ObservableList<Parameters> paramToDisplay = FXCollections.observableArrayList();

    private ObservableList<String> observableListType = FXCollections.observableArrayList();

    private TableParamCreationController controllerViewGlobal;

    private int personalID;

    private final static Image delete = new Image("images/trash.png");

    private final static Image imd = new Image("images/imageDown.png");

    private final static Image imu = new Image("images/imageUp.png");

    private ArrayList<Parameters> paramsArray = new ArrayList<>();

    private Parameters paramSelected;

    private String type;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.textFieldName.setVisible(false);
        this.description.setEditable(false);
        this.description.setDisable(true);
        initComboBox();
    }

    /**
     * Initialize the reference of the object tableStepScript in the object
     * controllerViewGlobal.
     *
     * @param controllerGLobal
     */
    public void initControllerTable(TableParamCreationController controllerGLobal) {
        controllerViewGlobal = controllerGLobal;
    }

    /**
     * Put the ID label of the Action based on the ranked number in the
     * controller list.
     *
     * @param scriptID
     */
    public void constructInformation(int scriptID) {
        this.labelID.setText(String.valueOf(scriptID));
        this.personalID = scriptID;
    }

    /**
     *
     * @return
     */
    public int getIDParam() {
        return this.personalID;
    }

    /**
     * return the reference of this anchorpane
     *
     * @return anchorpane
     */
    public AnchorPane getAnchorPane() {
        return this.anchroPaneScript;
    }

    /**
     * Set the ID of this script with the id given in parameters.
     *
     * @param id id of this test step.
     */
    public void setID(int id) {
        constructInformation(id);
    }

    /**
     *
     * @param params
     */
    public void setParamsCreation(ArrayList<Parameters> params) {

        //this.loadViewAction();
        this.imageDown.setImage(imd);
        this.imageUp.setImage(imu);
        this.imageViewTrash.setImage(delete);

        imageUp.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.goUp(ScriptLineParameterController.this);
            event.consume();
        });

        imageDown.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.goDown(ScriptLineParameterController.this);
            event.consume();
        });

        this.imageViewTrash.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            controllerViewGlobal.deleteSelectedAction(ScriptLineParameterController.this);
            event.consume();
        });

        this.loadParams(params);
        //defineCursor();
    }

    /**
     * Load the corresponding script in the combobox depending on whether its a
     * script stimuli or script check.
     *
     * @param script
     */
    public void loadParams(ArrayList<Parameters> params) {

        this.paramsArray = new ArrayList<>(params);
        paramToDisplay = FXCollections.observableArrayList(paramsArray);
        // paramToDisplay.setAll(params);
        comboBoxParam.setItems(paramToDisplay);
        comboBoxParam.setPrefWidth(Control.USE_COMPUTED_SIZE);
        choiceBoxType.setItems(observableListType);
        //gridPaneParam.getColumnConstraints().set(0, new ColumnConstraints(comboBoxParam.getPrefWidth()));
        comboBoxParam.valueProperty().addListener(new ChangeListener<Parameters>() {
            @Override
            public void changed(ObservableValue ov, Parameters t, Parameters t1) {
                //System.out.println("SIZE = " + comboBoxParam.getPrefWidth());
                paramSelected = t1;
                if (paramSelected.getName().equals("Add new parameter")) {
                    description.setEditable(true);
                    description.setDisable(false);
                    textFieldName.setVisible(true);
                    choiceBoxType.setDisable(false);
                    observableListType.clear();
                    observableListType.addAll("string", "integer", "buffer");
                    choiceBoxType.valueProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            type = newValue;
                            //controllerViewGlobal.getControllerFather().UpdateScriptPreview();

                        }
                    });
                    description.setText("");
                    description.setDisable(false);
                    //description.setOpacity(0.95);
                } else {
                    textFieldName.setVisible(false);
                    observableListType.clear();
                    observableListType.add(paramSelected.getParameterType());
                    choiceBoxType.setDisable(true);
                    choiceBoxType.getSelectionModel().select(observableListType.get(0));
                    choiceBoxType.setOpacity(0.95);
                    description.setText(paramSelected.getDescription());
                    description.setDisable(true);
                    description.setOpacity(0.95);
                }
            }
        });

//        paramsArray.stream().forEach((scriptsArray1) -> {
//            nameCollection.add(scriptsArray1.getName());
//        });
//        choiceBoxss.setItems(nameCollection);
//        choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            scriptMacro = new Macro();
//            currentScript = scriptsArray.get(newValue.intValue());
//            scriptMacro.setScriptByScriptIdScript1(currentScript);
//            updateGridPaneCreation(currentScript);
//            controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
//        });
        this.comboBoxParam.getSelectionModel().select(0);
    }

    /**
     *
     * @return
     */
    public Parameters constructParam() {
        Parameters param = new Parameters();
        if (paramSelected.getName().equals("Add new parameter")) {
            //description.setOpacity(0.95);
            param.setName(textFieldName.getText());
            param.setDescription(description.getText());
            param.setParameterType(type);
        } else {
            param = paramSelected;
        }
        return param;
    }

    /**
     *
     * @param param
     */
    public void setParam(Parameters param) {
        this.textFieldName2.setVisible(true);
        this.textFieldType.setVisible(true);
        this.comboBoxParam.setVisible(false);
        this.choiceBoxType.setVisible(false);
        this.textFieldName2.setText(param.getName());
        this.textFieldType.setText(param.getParameterType());
        this.textFieldType.setId("displayStyle");
        this.textFieldName2.setId("displayStyle");
        this.anchroPaneScript.getStylesheets().add("/view/testexecution/cssLibraryTestCase.css");
        description.setEditable(false);
        description.setText(param.getDescription());
        comboBoxParam.setOpacity(0.95);
        description.setOpacity(0.95);
    }

    /**
     *
     * @param param
     */
    public void setPreFilledParams(Parameters param) {
        int found = -1;
        for (int i = 0; i < this.comboBoxParam.getItems().size(); i++) {
            if (paramToDisplay.get(i).getName().equals(param.getName())) {
                found = i;
                break;
            }
        }
        if (found != -1) {
            this.comboBoxParam.getSelectionModel().select(found);
        } else {

            //System.out.println("JE RAJOUTE UN PARAM= " + this.paramToDisplay.size());
            this.paramToDisplay.add(param);
            this.comboBoxParam.getSelectionModel().select(param);
        }
    }

    /**
     *
     */
    public void initComboBox() {
        comboBoxParam.setCellFactory(new Callback<ListView<Parameters>, ListCell<Parameters>>() {
            @Override
            public ListCell<Parameters> call(ListView<Parameters> p) {
                ListCell cell = new ListCell<Parameters>() {
                    @Override
                    protected void updateItem(Parameters item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });

        comboBoxParam.setButtonCell(new ListCell<Parameters>() {
            @Override
            protected void updateItem(Parameters item, boolean bln) {
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
     */
    public void setScriptPurpose() {
        for (int i = 0; i < this.comboBoxParam.getItems().size(); i++) {
            if (paramToDisplay.get(i).getName().equals("Script purpose")) {
                this.comboBoxParam.getSelectionModel().select(i);
                break;
            }
        }
        this.comboBoxParam.setDisable(true);
        comboBoxParam.setOpacity(0.95);
        this.imageDown.setVisible(false);
        this.imageUp.setVisible(false);
        this.imageViewTrash.setVisible(false);
    }

    private void constructScript() {
        Script script = new Script();
    }

}
