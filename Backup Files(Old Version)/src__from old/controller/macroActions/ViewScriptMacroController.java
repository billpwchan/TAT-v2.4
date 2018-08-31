/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macroActions;

import DB.Macro;
import DB.ParamScriptMacro;
import DB.Parameters;
import DB.Script;
import DB.ScriptHasParameters;
import DBcontroller.ScriptDB;
import controller.popup.PopUpWizardActionController;
import controller.popup.PopUpWizardScriptController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import main.Main;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class ViewScriptMacroController implements Initializable {

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

    private final ObservableList<String> nameCollection = FXCollections.observableArrayList();

    private Script currentScript = new Script();

    private final GridPane gridPaneDisplayResults = new GridPane();

    private int numberOfParam = 0;

    private Stage popUpStage;

    private Macro scriptMacro = null;

    private final ObservableList<ParamScriptMacro> observableListParams = FXCollections.observableArrayList();

    private final static String Separator = ("" + ((char) 007));

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

    public ScriptLineTableMacroController getControllerScriptFather() {
        return this.controllerScriptFather;
    }

    public Script getCurrentScript() {
        return this.currentScript;
    }

    public ObservableList<ParamScriptMacro> getHashParamScriptMacro() {
        return this.observableListParams;
    }

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
        controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            scriptMacro = new Macro();
            currentScript = scriptsArray.get(newValue.intValue());
            scriptMacro.setScriptByScriptIdScript1(currentScript);
            updateGridPaneCreation(currentScript);
            controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
        });
    }

    /**
     * Return the result of the node in the gridpane depending on the column and
     * row given in parameters.
     *
     * @param row row needed
     * @param column column needed
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
     * Update the gridpane of the view depending on the script selected (view
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
     * anchor pane of the father view.
     *
     * @return
     */
    int getNumberOfParam() {
        return this.numberOfParam;
    }

    /**
     * Display the view for script configuration when a parameters is clicked.
     *
     * @param script the script to configured
     * @param selectedParam the parameters selected
     */
    private void displayWizard(Script script, int selectedParam) {

        AnchorPane popUpWizard = new AnchorPane();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/popup/popUpWizardScript.fxml"));
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
     * observable list, update the view with the corresponding values.
     *
     * @param observableListParamScripts observable list containing the
     * parameters.
     */
    public void referParameters(ArrayList<ParamScriptMacro> observableListParamScripts) {
        //Iterator<ScriptHasBeenConfigured> itParametersConfigured = observableListScriptMartinth.iterator();
        for (int i = 0; i < observableListParamScripts.size(); i++) {
            //ScriptHasBeenConfigured scriptHBC = itParametersConfigured.next();
            Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
            toSet.setStyle(null);
            if (!"".equals(observableListParamScripts.get(i).getValuePath())) {
                //Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
                if (null != observableListParamScripts.get(i).getValuePath()) {
                    toSet.setTextFill(Color.BLACK);
                    switch (observableListParamScripts.get(i).getValuePath()) {
                        case "Constant":
                            //System.out.println("");
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
                            //System.out.println("From Other Script");
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
            if ("".equals(observableListParamScripts.get(i).getValuePath())) {
                //System.out.println("LA VALUE POUR PARAM " + observableListParamScripts.get(i).getScriptHasParameters().getParameters().getName() + " est nulle");
                toSet.setText("Configure " + observableListParamScripts.get(i).getScriptHasParameters().getParameters().getName());
            }
        }
    }

    /**
     * Action launched when the popup configuration is closed ( closed stage and
     * update image).
     */
    public void closePopUp() {
        this.popUpStage.close();
        controllerScriptFather.controllerViewGlobal().getControllerFather().getControllerPreview().updateGridPaneCreation(controllerScriptFather.controllerViewGlobal());
    }

    public void updateScriptViewDisplay(Macro script) {
        constructGridPaneView(script.getScriptByScriptIdScript1());
        this.choiceBoxss.setVisible(false);
        this.labelNameOfScript.setVisible(true);
        this.labelNameOfScript.setText(script.getScriptByScriptIdScript1().getName());
        referParameters(new ArrayList(script.getParamScriptMacros()));
    }
}
