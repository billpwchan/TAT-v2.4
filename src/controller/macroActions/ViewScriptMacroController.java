/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macroActions;

import DB.*;
import DBcontroller.ScriptDB;
import controller.popup.PopUpWizardActionController;
import controller.popup.PopUpWizardScriptController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class ViewScriptMacroController implements Initializable {

    private final static String Separator = ("" + ((char) 007));
    private final ObservableList<String> nameCollection = FXCollections.observableArrayList();
    private final GridPane gridPaneDisplayResults = new GridPane();
    private final ObservableList<ParamScriptMacro> observableListParams = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorScript;
    @FXML
    private GridPane gridPaneScript;
    @FXML
    private ChoiceBox<String> choiceBoxss;
    @FXML
    private Label labelScriptName;
    @FXML
    private Label labelNameOfScript;
    private ScriptLineTableMacroController controllerScriptFather;
    private ArrayList<Script> scriptsArray;
    private Script currentScript = new Script();
    private int numberOfParam = 0;
    private Stage popUpStage;
    private Macro scriptMacro = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        ColumnConstraints col3 = new ColumnConstraints();
        col2.setPercentWidth(34);
        gridPaneDisplayResults.getColumnConstraints().addAll(col1, col2, col3);
        gridPaneScript.add(gridPaneDisplayResults, 1, 2, 3, 1);
    }

    /**
     * Initialize the instance of the controller step father
     *
     * @param aThis
     */
    void init(ScriptLineTableMacroController aThis) {
        controllerScriptFather = aThis;
    }

    /**
     * @return
     */
    public ScriptLineTableMacroController getControllerScriptFather() {
        return this.controllerScriptFather;
    }

    /**
     * @return
     */
    public Script getCurrentScript() {
        return this.currentScript;
    }

    /**
     * @return
     */
    public ObservableList<ParamScriptMacro> getHashParamScriptMacro() {
        return this.observableListParams;
    }

    /**
     * @return
     */
    public Macro getScriptMacro() {
        return this.scriptMacro;
    }

    /**
     * Load the corresponding script in the combobox depending on whether its a
     * script stimuli or script check.
     *
     * @param script
     */
    public void loadScripts(HashSet<Script> script) {
        this.scriptsArray = new ArrayList<>(script);
        Collections.sort(scriptsArray, (Script result1, Script result2) -> result1.getName().compareToIgnoreCase(result2.getName()));
        scriptsArray.stream().forEach((scriptsArray1) -> {
            nameCollection.add(scriptsArray1.getName());
        });
        choiceBoxss.setItems(nameCollection);
        choiceBoxss.getSelectionModel().selectFirst();
        scriptMacro = new Macro();
        currentScript = scriptsArray.get(0);
        scriptMacro.setScriptByScriptIdScript1(currentScript);
        updateGridPaneCreation(currentScript);

        if (controllerScriptFather.controllerViewGlobal().getControllerFather() != null) {
            controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        } else {
            controllerScriptFather.controllerViewGlobal().getControllerFatherEdit().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        }
        //Select a particular script should update all parameters involed.
        choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            scriptMacro = new Macro();
            currentScript = scriptsArray.get(newValue.intValue());
            scriptMacro.setScriptByScriptIdScript1(currentScript);
            updateGridPaneCreation(currentScript);
            if (controllerScriptFather.controllerViewGlobal().getControllerFather() != null) {
                controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
            } else {
                controllerScriptFather.controllerViewGlobal().getControllerFatherEdit().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
            }
        });
    }

    /**
     * Load the corresponding script in the combobox depending on whether its a
     * script stimuli or script check.
     *
     * @param script
     */
    public void loadScriptsEdit(HashSet<Script> script) {
        this.scriptsArray = new ArrayList<>(script);
        Collections.sort(scriptsArray, (Script result1, Script result2) -> result1.getName().compareToIgnoreCase(result2.getName()));
        scriptsArray.stream().forEach((scriptsArray1) -> {
            nameCollection.add(scriptsArray1.getName());
        });
        choiceBoxss.setItems(nameCollection);       //nameCollection contains all pre-defined scripts (not Macro)
        choiceBoxss.getSelectionModel().selectFirst();
        scriptMacro = new Macro();
        currentScript = scriptsArray.get(0);
        scriptMacro.setScriptByScriptIdScript1(currentScript);
        updateGridPaneEdit(currentScript);

//        For the edit part, I don't think need to updateGridPaneCreation at first...?
        if (controllerScriptFather.controllerViewGlobal().getControllerFather() != null) {
            controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        } else {
            controllerScriptFather.controllerViewGlobal().getControllerFatherEdit().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        }
        //Select a particular script should update all parameters involed.
        choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            scriptMacro = new Macro();
            currentScript = scriptsArray.get(newValue.intValue());
            scriptMacro.setScriptByScriptIdScript1(currentScript);
            updateGridPaneEdit(currentScript);
//            updateGridPaneCreation(currentScript);
            if (controllerScriptFather.controllerViewGlobal().getControllerFather() != null) {
                controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
            } else {
                controllerScriptFather.controllerViewGlobal().getControllerFatherEdit().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
            }
        });
    }

    /**
     * Return the result of the node in the gridpane depending on the column and
     * row given in parameters.
     *
     * @param row      row needed
     * @param column   column needed
     * @param gridPane gridpane needed
     * @return
     */
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    /**
     * Update the gridpane of the assets.view depending on the script selected (assets.view
     * View).
     *
     * @param currentSelectedScript
     */
    private void updateGridPaneModification(Script currentSelectedScript) {
        constructGridPaneView(currentSelectedScript);
        Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScriptHasParameterses().iterator();
        int i = 0;
        while (itScriptParameters.hasNext()) {
            ScriptHasParameters scriptHP = itScriptParameters.next();
            Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
            toSet.setTextFill(Color.RED);
            toSet.setText("Configure " + scriptHP.getParameters().getName());
            final int selectedRow = i;
            toSet.setOnAction((ActionEvent e) -> {
                //if (canBeConfigured) {
                displayWizard(currentScript, selectedRow);
                //}
            });
            i++;
        }

    }

    /**
     * @param currentSelectedScript
     */
    public void updateGridPaneEdit(Script currentSelectedScript) {  //This is for update script's parameter. 
        Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScriptHasParameterses().iterator();
        while (itScriptParameters.hasNext()) {
            ScriptHasParameters scriptHasParameters = itScriptParameters.next();
            Iterator<ParamScriptMacro> itParamScriptMacro = scriptHasParameters.getParamScriptMacros().iterator();
            while (itParamScriptMacro.hasNext()) {
                this.observableListParams.add(0, itParamScriptMacro.next());
            }
        }
    }

    /**
     * @param currentSelectedMacro
     */
    public void updateGridPaneEditNew(Macro currentSelectedMacro) {  //To update observableListParams object (For display wizzard)
        constructGridPaneView(currentSelectedMacro.getScriptByScriptIdScript1());
        this.observableListParams.clear();
        if (this.scriptMacro == null) {
            scriptMacro = new Macro();
        }
        ArrayList<ParamScriptMacro> paramScriptMacro = new ArrayList(currentSelectedMacro.getParamScriptMacros());
        paramScriptMacro.stream().map((PSM) -> {
            this.observableListParams.add(PSM);
            return PSM;
        }).forEach((PSM) -> {
            scriptMacro.addParamScriptMacro(PSM);
        });
    }

    /**
     * This will show the popup configuration window for modification
     *
     * @param currentSelectedScript
     */
    public void updateGridPaneCreation(Script currentSelectedScript) {
        byte order = 0;
        updateGridPaneModification(currentSelectedScript);
        this.observableListParams.clear();
        Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScriptHasParameterses().iterator();
        while (itScriptParameters.hasNext()) {
            ScriptHasParameters scriptHasParameters = itScriptParameters.next();
            ParamScriptMacro paramScriptsMacro = new ParamScriptMacro();
            paramScriptsMacro.setScriptHasParameters(scriptHasParameters);
            paramScriptsMacro.setToDisplay((byte) 1);
            paramScriptsMacro.setParamOrder(order);
            scriptMacro.addParamScriptMacro(paramScriptsMacro);
            this.observableListParams.add(paramScriptsMacro);
            order++;
        }
    }

    /**
     * @param currentSelectedScript
     */
    public void constructGridPaneView(Script currentSelectedScript) {
        gridPaneDisplayResults.getChildren().clear();
        ScriptDB scriptHandler = new ScriptDB();
        scriptHandler.getAllFromScript(currentSelectedScript);
        Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScriptHasParameterses().iterator();
        while (itScriptParameters.hasNext()) {
            ScriptHasParameters scriptHasParameters = itScriptParameters.next();
            Parameters param = scriptHasParameters.getParameters();
            Label labelToSet = new Label(param.getName());
            labelToSet.setTooltip(new Tooltip(param.getName()));
            gridPaneDisplayResults.addColumn(0, labelToSet);
            Hyperlink toSet = new Hyperlink("");
            toSet.setTooltip(new Tooltip(""));
            gridPaneDisplayResults.addColumn(1, toSet);
        }
        this.numberOfParam = currentSelectedScript.getScriptHasParameterses().size();
        controllerScriptFather.updateSizeGrid();
    }

    /**
     * return the number of parameters for this script (use to redefine the
     * anchor pane of the father assets.view.
     *
     * @return
     */
    public int getNumberOfParam() {
        return this.numberOfParam;
    }

    /**
     * Display the assets.view for script configuration when a parameters is clicked.
     *
     * @param script        the script to configured
     * @param selectedParam the parameters selected
     */
    private void displayWizard(Script script, int selectedParam) {

        AnchorPane popUpWizard = new AnchorPane();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/assets/view/popup/popUpWizardScript.fxml"));
            PopUpWizardActionController controller = new PopUpWizardActionController();
            fxmlLoader.setController(controller);
            popUpWizard = fxmlLoader.load();
            controller.init(this);
            controller.constructInformation(script, observableListParams, selectedParam);
        } catch (IOException ex) {
            Logger.getLogger(PopUpWizardScriptController.class
                    .getName()).error("", ex);
        }
        popUpStage = new Stage();
        popUpStage.setTitle("Script configuration");
        popUpStage.initOwner(Main.primaryStage);
        popUpStage.initModality(Modality.WINDOW_MODAL);
//        popUpStage.initOwner(main.getMainController().getPrimaryStage());
        popUpStage.setScene(new Scene(popUpWizard));

//        popUpStage.setX(main.getMainController().getPrimaryStage().getX() + main.getMainController().getPrimaryStage().getWidth() / 2 - popUpStage.getWidth() / 2);
//        popUpStage.setY(main.getMainController().getPrimaryStage().getY() + main.getMainController().getPrimaryStage().getHeight() / 2 - popUpStage.getHeight() / 2);
        popUpStage.showAndWait();
        popUpStage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - popUpStage.getWidth() / 2);
        popUpStage.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - popUpStage.getHeight() / 2);

    }

    /**
     * Take the parameters from the popup script and associate them in the
     * observable list, update the assets.view with the corresponding values.
     *
     * @param observableListParamScripts observable list containing the
     *                                   parameters.
     */
    public void referParameters(ArrayList<ParamScriptMacro> observableListParamScripts) {
        for (int i = 0; i < observableListParamScripts.size(); i++) {
            Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
            toSet.setStyle(null);
            if (!"".equals(observableListParamScripts.get(i).getValuePath())) {
                //Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
                if (null != observableListParamScripts.get(i).getValuePath()) {
                    toSet.setTextFill(Color.BLACK);
                    switch (observableListParamScripts.get(i).getValuePath()) {     //ToolTip is for hover message.
                        case "Constant":
                            toSet.setText(observableListParamScripts.get(i).getValue().replace(Separator, ""));
                            toSet.setTooltip(new Tooltip(observableListParamScripts.get(i).getValue().replace(Separator, "")));
                            break;
                        case "Excel file":
                            toSet.setText(observableListParamScripts.get(i).getValuePath());
                            String[] parts = observableListParamScripts.get(i).getValue().split(Separator);
                            toSet.setTooltip(new Tooltip("Sheet number: " + parts[1] + " Position X: " + parts[2] + " Postion Y: " + parts[3]));//+ " Rang: " + parts[4]));
                            break;
                        case "Buffer list":
                            String test = observableListParamScripts.get(i).getValue().replace(Separator, "");
                            test = test.replace("@&Buffer_", "");
                            toSet.setText(test);
                            toSet.setTooltip(new Tooltip(test));
                            break;
                        case "From Other Script":
                            toSet.setText(observableListParamScripts.get(i).getParamScriptMacro().getValue());
                            break;
                        case "Property":
                            String[] property = observableListParamScripts.get(i).getValue().split(Separator);
                            toSet.setText(property[3]);
                            if (property[3].contains("#")) {
                                toSet.setStyle("-fx-text-fill :" + property[3]);
                            }
                            break;
                    }
                }

            }
            if (observableListParamScripts.get(i).getParamScriptMacro() != null) {
                switch (observableListParamScripts.get(i).getParamScriptMacro().getValuePath()) {
                    case "Constant":
                        toSet.setText(observableListParamScripts.get(i).getParamScriptMacro().getValue().replace(Separator, ""));
                        toSet.setTooltip(new Tooltip("Linked to the Parameter " + observableListParamScripts.get(i).getValue() + " of script " + observableListParamScripts.get(i).getValuePath()));
                        toSet.setStyle("-fx-text-fill: #9900ff;");
                        break;
                    case "Excel file":
                        toSet.setText(observableListParamScripts.get(i).getParamScriptMacro().getValuePath());
                        String[] parts = observableListParamScripts.get(i).getParamScriptMacro().getValue().split(Separator);
                        toSet.setTooltip(new Tooltip("Linked to the Parameter " + observableListParamScripts.get(i).getValue() + " of script " + observableListParamScripts.get(i).getValuePath()));//+ " Rang: " + parts[4]));
                        toSet.setStyle("-fx-text-fill: #9900ff;");
                        break;
                    case "Buffer list":
                        String test = observableListParamScripts.get(i).getParamScriptMacro().getValue().replace(Separator, "");
                        toSet.setStyle("-fx-text-fill: #9900ff;");
                        test = test.replace("@&Buffer_", "");
                        toSet.setText(test);
                        toSet.setTooltip(new Tooltip("Linked to the Parameter " + observableListParamScripts.get(i).getValue() + " of script " + observableListParamScripts.get(i).getValuePath()));
                        break;
                    case "":
                        String strRefer = "Linked to the Parameter " + observableListParamScripts.get(i).getValue() + " of script " + observableListParamScripts.get(i).getValuePath();
                        toSet.setStyle("-fx-text-fill: #9900ff;");
                        toSet.setText(strRefer);
                        toSet.setTooltip(new Tooltip(strRefer));
                        break;
                }
            }
            //Not yet configured parameters.
            if ("".equals(observableListParamScripts.get(i).getValuePath())) {
                toSet.setText("Configure " + observableListParamScripts.get(i).getScriptHasParameters().getParameters().getName());
                toSet.setTooltip(new Tooltip("Not yet configured yet."));
            }
            final int selectedRow = i;
            toSet.setOnAction((ActionEvent e) -> {
//                updateGridPaneEdit(currentScript);
                displayWizard(currentScript, selectedRow);
            });
        }
    }

    /**
     * Action launched when the popup configuration is closed ( closed stage and
     * update image).
     */
    public void closePopUp() {      //Problem, if two tabs are opened.
        this.popUpStage.close();
        if (controllerScriptFather.controllerViewGlobal().getControllerFather() == null) {       //Edit Case
            controllerScriptFather.controllerViewGlobal().getControllerFatherEdit().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        } else {        //New Case
            controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        }
    }

    /**
     * @param script
     */
    public void updateScriptViewDisplay(Macro script) {
//        this.currentScript = script.getScriptByScriptIdScript();
        constructGridPaneView(script.getScriptByScriptIdScript1());
        this.choiceBoxss.setVisible(false);
        this.labelNameOfScript.setVisible(true);
        this.labelNameOfScript.setText(script.getScriptByScriptIdScript1().getName());
        referParameters(new ArrayList(script.getParamScriptMacros()));
    }

    /**
     * @param script
     */
    public void updateScriptEditDisplay(Macro script) {     //if have six scripts in this macro, will run this function again 6 times
        this.currentScript = script.getScriptByScriptIdScript1();  //It it necessary to update the currentScript for the displayWizzard to reference. 
        updateGridPaneEditNew(script);
        this.choiceBoxss.setVisible(true);
        this.choiceBoxss.setDisable(false);
        this.labelNameOfScript.setVisible(true);
        this.labelNameOfScript.setText(script.getScriptByScriptIdScript1().getName());
        referParameters(new ArrayList(script.getParamScriptMacros()));
    }
}