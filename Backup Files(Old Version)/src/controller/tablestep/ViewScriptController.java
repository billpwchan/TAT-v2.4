/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tablestep;

import DB.*;
import DBcontroller.MacroDB;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
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
 * @author Martinez Thibault
 */
public class ViewScriptController implements Initializable {

    private final static String Separator = ("" + ((char) 007));
    private final ObservableList<String> nameCollection = FXCollections.observableArrayList();
    private final GridPane gridPaneDisplayResults = new GridPane();
    private final MacroDB macroHandler = new MacroDB();
    @FXML
    private AnchorPane anchorScript;
    @FXML
    private ChoiceBox<String> choiceBoxss;
    @FXML
    private GridPane gridPaneScript;
    @FXML
    private Circle okVerif;
    @FXML
    private Circle okAction;
    @FXML
    private ListView displayparam;
    @FXML
    private Label labelScriptName;
    @FXML
    private Label labelNameOfScript;
    private Stage popUpStage;
    private ScriptLineTableStepController controllerScriptFather;
    private ArrayList<Script> scriptsArray;
    private HashSet<ScriptHasBeenConfigured> observableListScript = new HashSet<>(0);
    private String scriptName;
    private int numberOfParam = 0;
    private boolean isCOnfigured = true;
    private Boolean canBeConfigured = true;
    private Script currentScript;// = new Script();
    private TestStepHasScript teststephasscript = null;

    /**
     * Initializes the controller class. Class for the display of the script.
     *
     * @param url
     * @param rb
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        gridPaneDisplayResults.getColumnConstraints().addAll(col1, col2);
        gridPaneScript.add(gridPaneDisplayResults, 1, 2, 3, 1);
    }

    /**
     * Display the view for script configuration when a parameters is clicked.
     *
     * @param script        the script to configured
     * @param selectedParam the parameters selected
     */
    private void displayWizard(Script script, int selectedParam) {

        AnchorPane popUpWizard = new AnchorPane();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/popup/popUpWizardScript.fxml"));
            PopUpWizardScriptController controller = new PopUpWizardScriptController();
            fxmlLoader.setController(controller);
            popUpWizard = fxmlLoader.load();
            controller.init(this);
            controller.constructInformation(script, observableListScript, selectedParam);
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
     * Initialize the instance of the controller step father
     *
     * @param aThis
     */
    void init(ScriptLineTableStepController aThis) {
        controllerScriptFather = aThis;
    }

    /**
     * Take the parameters from the popup script and associate them in the
     * observable list, update the view with the corresponding values.
     *
     * @param observableListScriptMartinth observable list containing the
     *                                     parameters.
     */
    public void referParameters(ArrayList<ScriptHasBeenConfigured> observableListScriptMartinth) {
        this.observableListScript = new HashSet(observableListScriptMartinth);
        Iterator<ScriptHasBeenConfigured> itParametersConfigured = observableListScriptMartinth.iterator();
        int i = 0;
        while (itParametersConfigured.hasNext()) {
            ScriptHasBeenConfigured scriptHBC = itParametersConfigured.next();
            //System.out.println("ORDER = "+scriptHBC.getParamOrder());
            //scriptHBC.setParamOrder((byte) i);
            if (scriptHBC.getIsConfigured() != 0) {
                //Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
                Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
                //System.out.println("TO SET = "+toSet);
                toSet.setId("displayStyle");
                this.anchorScript.getStylesheets().add("/view/testexecution/cssLibraryTestCase.css");
                toSet.setStyle(null);
                String valuePath = scriptHBC.getValuePath();
                if (scriptHBC.getValuePath().charAt(0) == Separator.charAt(0)) {
                    valuePath = "From Other Script";
                    //scriptHBC = scriptHBC.getRefScriptHasBeenConfigured();
                }
                if (null != valuePath) {
                    switch (valuePath) {

                        case "HMI":
                            System.out.println("HMI value : " + scriptHBC.getValue());
                            toSet.setText(scriptHBC.getValue().replace(Separator, ""));
                            toSet.setTooltip(new Tooltip(scriptHBC.getValue().replace(Separator, "")));
                            break;
                        case "Classes":
                            System.out.println("Classe value : " + scriptHBC.getValue());
                            toSet.setText(scriptHBC.getValue().replace(Separator, ""));
                            toSet.setTooltip(new Tooltip(scriptHBC.getValue().replace(Separator, "")));
                            break;
                        case "Constant":
                            toSet.setText(scriptHBC.getValue().replace(Separator, ""));
                            toSet.setTooltip(new Tooltip(scriptHBC.getValue().replace(Separator, "")));
                            break;
                        case "Excel file":
                            toSet.setText(scriptHBC.getValuePath());
                            String excel = scriptHBC.getValue();
//                            excel = excel.replace("@&Name_", "");
//                            excel = excel.replace("@&Number_", "");
                            String[] parts = excel.split(Separator);
                            if (parts[1].contains("@&Name_")) {
                                toSet.setTooltip(new Tooltip("Sheet name: " + parts[2] + " Position X: " + parts[3] + " Postion Y: " + parts[4]));//+ " Rang: " + parts[4]));
                            } else {
                                toSet.setTooltip(new Tooltip("Sheet number: " + parts[2] + " Position X: " + parts[3] + " Postion Y: " + parts[4]));//+ " Rang: " + parts[4])); 
                            }
                            //toSet.setTooltip(new Tooltip("Sheet number: " + parts[2] + " Position X: " + parts[3] + " Postion Y: " + parts[4]));//+ " Rang: " + parts[4]));
                            break;
                        case "Buffer list":
                            String test = scriptHBC.getValue().replace(Separator, "");
                            test = test.replace("@&Buffer_", "");
                            toSet.setText(test);
                            toSet.setTooltip(new Tooltip(test));
                            break;
                        case "Property":
                            String[] property = scriptHBC.getValue().split(Separator);
                            toSet.setText(property[3]);
                            //System.out.println("PROPERTY 3= "+property[3]+" property 4= " + property[4]);
                            if (property[3].contains("#")) {
                                toSet.setStyle("-fx-text-fill :" + property[3]);
                            }
                            break;
                        case "From Other Script":
                            if (scriptHBC.getRefScriptHasBeenConfigured().getValue().isEmpty()) {
                                toSet.setText("Linked to Parameter " + " of Script " + scriptHBC.getValuePath());
                                toSet.setTooltip(new Tooltip("Linked to Parameter " + " of Script " + scriptHBC.getValuePath()));
                            } else {
                                toSet.setText(scriptHBC.getRefScriptHasBeenConfigured().getValue().replace(Separator, ""));
                                toSet.setTooltip(new Tooltip(scriptHBC.getRefScriptHasBeenConfigured().getValue().replace(Separator, "")));
                            }
                            break;

                    }
                } else {

                }
            }
            i++;
        }
        //teststephasscript.setScriptHasBeenConfigureds(observableListScript);
    }

    /**
     * Action launched when the popup configuration is closed ( closed stage and
     * update image).
     */
    public void closePopUp() {
        this.popUpStage.close();
        this.updateImage();
    }

    /**
     * Update the image corresponding to the view.
     */
    private void updateImage() {
        if (currentScript != null) {
            //System.out.println("name :" + currentScript.getName());
            int i = 0;
            Iterator<ScriptHasBeenConfigured> itScriptHasBeenConfigured = this.observableListScript.iterator();
            while (itScriptHasBeenConfigured.hasNext()) {
                ScriptHasBeenConfigured scriptHasBeenConfigured = itScriptHasBeenConfigured.next();
                if (scriptHasBeenConfigured.getIsConfigured() != 0) {
                    i++;
                }
            }
            if (this.observableListScript.isEmpty()) {
                isCOnfigured = true;
                if (currentScript.getIsStimuli() == 0) {
                    controllerScriptFather.updateImageConfigurationVerif(new Image("/images/ready.png"));
                } else {
                    controllerScriptFather.updateImageConfigurationAction(new Image("/images/ready.png"));
                }
            } else {
                if (i == this.observableListScript.size() && i != 0) {
                    isCOnfigured = true;
                    if (currentScript.getIsStimuli() == 0) {
                        controllerScriptFather.updateImageConfigurationVerif(new Image("/images/ready.png"));
                    } else {
                        controllerScriptFather.updateImageConfigurationAction(new Image("/images/ready.png"));
                    }
                } else {
                    isCOnfigured = false;
                    if (currentScript.getIsStimuli() == 0) {
                        controllerScriptFather.updateImageConfigurationVerif(new Image("/images/notready.png"));
                    } else {
                        controllerScriptFather.updateImageConfigurationAction(new Image("/images/notready.png"));
                    }
                }
            }

        }

    }

    /**
     * Return the script selected in the combobox. (if none selected return
     * null).
     *
     * @return
     */
    public TestStepHasScript getTestStepHasScript() {
//        if (this.teststephasscript != null) {
////            ArrayList<ScriptHasBeenConfigured> param=new ArrayList<>(this.teststephasscript.getScriptHasBeenConfigureds());
////            System.out.println("param 1 = "+param.get(0).getValuePath());
////            //System.out.println("TESTSSFSFDSD = " + this.teststephasscript.getScriptHasBeenConfigureds());
//        }
        return this.teststephasscript;
    }

    /**
     * Load the corresponding script in the combobox depending on whether its a
     * script stimuli or script check.
     *
     * @param script
     * @param currTestStep
     */
    public void loadScripts(HashSet<Script> script, TestStep currTestStep) {

        this.scriptsArray = new ArrayList<>(script);
        Collections.sort(scriptsArray, (Script result1, Script result2) -> result1.getName().compareToIgnoreCase(result2.getName()));
        scriptsArray.stream().forEach((scriptsArray1) -> {
            nameCollection.add(scriptsArray1.getName());
        });
        choiceBoxss.setItems(nameCollection);
        //System.out.println("Je load les script , taille est de : " + nameCollection.size());
        choiceBoxss.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if ("".equals(scriptsArray.get(newValue.intValue()).getName())) {
                numberOfParam = 0;
                observableListScript.clear();
                gridPaneDisplayResults.getChildren().clear();
                controllerScriptFather.updateSizeGrid();
                teststephasscript = null;
            } else {
                //teststephasscript = new TestStepHasScript();
                teststephasscript = new TestStepHasScript(currentScript, currTestStep, (byte) 0);
                currTestStep.addTestStephasScript(teststephasscript);
                currentScript = scriptsArray.get(newValue.intValue());
                teststephasscript.setScript(currentScript);
                //teststephasscript.setTestStep(currTestStep);

                //System.out.println("SCRIPT HASP.SIZE " + teststephasscript.getScript().getScriptHasParameterses().size());
                updateGridPaneCreation(teststephasscript);
            }
        });
    }

    /**
     * Modify the view for the instance view (disable the non needed options).
     */
    void executionInstance() {

        this.choiceBoxss.setVisible(false);
        this.gridPaneScript.add(new Label(this.scriptName), 2, 0);
        if (this.scriptName != null) {
            this.labelScriptName.setVisible(true);
        } else {
            this.labelScriptName.setVisible(false);
        }

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
     * @param currentSelectedScript
     */
    public void constructGridPaneView(TestStepHasScript currentSelectedScript) {
        currentScript = currentSelectedScript.getScript();
        gridPaneDisplayResults.getChildren().clear();
        if (currentSelectedScript.getScript().getIsMacro() == 1) {
            currentSelectedScript.getScript().getScriptHasParameterses().clear();
            String purpose = "";
            int i;
            this.numberOfParam = 0;
            Iterator<Macro> itMacro = currentSelectedScript.getScript().getMacrosForScriptIdScript().iterator();
            while (itMacro.hasNext()) {
                i = 0;
                Macro macro = itMacro.next();
                Iterator<ParamScriptMacro> itParamMacro = macro.getParamScriptMacros().iterator();
                while (itParamMacro.hasNext()) {
                    ParamScriptMacro param = itParamMacro.next();
                    if (i == 0) {
                        purpose = param.getValue();
                    }
                    if (param.getToDisplay() == 1) {
                        ScriptHasParameters schp = new ScriptHasParameters(param.getScriptHasParameters());
                        currentSelectedScript.getScript().getScriptHasParameterses().add(schp);
                        this.numberOfParam++;
                        ScriptHasParameters scriptHasParameters = param.getScriptHasParameters();
                        Parameters param2 = scriptHasParameters.getParameters();
                        Label labelToSet = new Label(purpose + " " + param2.getName());//controllerActions.collectionControllerScript.get(i).getScriptControllerAction().getHashParamScriptMacro().get(0).getValue() + " " + param.getName());
                        labelToSet.setTooltip(new Tooltip(param2.getName()));
                        gridPaneDisplayResults.addColumn(0, labelToSet);
                        Hyperlink toSet = new Hyperlink("");
                        toSet.setTooltip(new Tooltip(""));
                        gridPaneDisplayResults.addColumn(1, toSet);
                        controllerScriptFather.updateSizeGrid();
                    }
                    i++;
                }
            }
        } else {
            Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScript().getScriptHasParameterses().iterator();
            while (itScriptParameters.hasNext()) {
                ScriptHasParameters scriptHasParameters = itScriptParameters.next();
                Parameters param = scriptHasParameters.getParameters();
                Label labelToSet = new Label(param.getName());
                labelToSet.setTooltip(new Tooltip(param.getName()));
                gridPaneDisplayResults.addColumn(0, labelToSet);
                Hyperlink toSet = new Hyperlink("");
                toSet.setTooltip(new Tooltip(""));
                gridPaneDisplayResults.addColumn(1, toSet);
                //System.out.println("GridPane display result= "+gridPaneDisplayResults+" sc= "+scriptHasParameters.getScript().getName() );

            }
            this.numberOfParam = currentSelectedScript.getScript().getScriptHasParameterses().size();
            controllerScriptFather.updateSizeGrid();
        }
    }

    /**
     * Update the gridpane of the view depending on the script selected (view
     * View).
     *
     * @param currentSelectedScript
     */
    private void updateGridPaneModification(TestStepHasScript currentSelectedScript) {
        if (currentSelectedScript.getScript().getIsMacro() == 1) {
            int l = 0;
            Script macr = currentSelectedScript.getScript();
            macroHandler.getAllFromMacro(macr);
            constructGridPaneView(currentSelectedScript);
            Iterator<Macro> itMacro = macr.getMacrosForScriptIdScript().iterator();
            while (itMacro.hasNext()) {
                Macro macro = itMacro.next();
                Iterator<ParamScriptMacro> itParamMacro = macro.getParamScriptMacros().iterator();
                while (itParamMacro.hasNext()) {
                    ParamScriptMacro param = itParamMacro.next();
                    if (param.getToDisplay() == 1) {
                        Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(l, 1, gridPaneDisplayResults);
                        toSet.setText("Configure " + param.getScriptHasParameters().getParameters().getName());
                        toSet.setStyle("-fx-text-fill:#FF0000;");
                        final int selectedRow = l;
                        toSet.setOnAction((ActionEvent e) -> {
                            if (canBeConfigured) {
                                displayWizard(currentScript, selectedRow);
                            }
                        });
                        //toSet.setText("Configure " + controllerActions.collectionControllerScript.get(i).getScriptControllerAction().getHashParamScriptMacro().get(j).getScriptHasParameters().getParameters().getName());
                        l++;
                    }
                }
            }
        } else {
            constructGridPaneView(currentSelectedScript);
            Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScript().getScriptHasParameterses().iterator();
            int i = 0;
            while (itScriptParameters.hasNext()) {
                ScriptHasParameters scriptHP = itScriptParameters.next();
                Hyperlink toSet = (Hyperlink) getNodeByRowColumnIndex(i, 1, gridPaneDisplayResults);
                toSet.setText("Configure " + scriptHP.getParameters().getName());
                toSet.setStyle("-fx-text-fill:#FF0000;");
                final int selectedRow = i;
                toSet.setOnAction((ActionEvent e) -> {
                    if (canBeConfigured) {
                        displayWizard(currentScript, selectedRow);
                    }
                });
                i++;
            }
        }
        updateImage();
    }

    /**
     * @param currentSelectedScript
     */
    public void updateGridPaneCreation(TestStepHasScript currentSelectedScript) {
        updateGridPaneModification(currentSelectedScript);
        this.observableListScript.clear();
//        if (currentSelectedScript.getScript().getIsMacro() == 1) {
//            byte order = 0;
//            Iterator<Macro> itMacro = currentSelectedScript.getScript().getMacrosForScriptIdScript().iterator();
//            while (itMacro.hasNext()) {
//                Macro macro = itMacro.next();
//                Iterator<ParamScriptMacro> itParamMacro = macro.getParamScriptMacros().iterator();
//                while (itParamMacro.hasNext()) {
//                    ParamScriptMacro param = itParamMacro.next();
//                    if (param.getToDisplay() == 1) {
//                        ScriptHasParameters scriptHasParameters = itScriptParameters.next();
//                        ScriptHasBeenConfigured test = new ScriptHasBeenConfigured();
//                        test.setParamOrder((byte) j);
//                        test.setParameters(scriptHasParameters.getParameters());
//                        teststephasscript.addScripthasBeenConfigured(test);
//                        this.observableListScript.add(test);
//                        order++;
//                    }
//                }
//            }
//        } else {
        Iterator<ScriptHasParameters> itScriptParameters = currentSelectedScript.getScript().getScriptHasParameterses().iterator();
        int j = 0;
        teststephasscript.getScriptHasBeenConfigureds().clear();
        this.observableListScript.clear();
        while (itScriptParameters.hasNext()) {
            ScriptHasParameters scriptHasParameters = itScriptParameters.next();
            ScriptHasBeenConfigured test = new ScriptHasBeenConfigured();
            test.setParamOrder((byte) j);
            test.setParameters(scriptHasParameters.getParameters());
            teststephasscript.addScripthasBeenConfigured(test);
            //teststephasscript.setTestStep(currentSelectedScript.getTestStep());
            test.setTestStepHasScript(teststephasscript);

            this.observableListScript.add(test);
            j++;
//            }
        }
        //System.out.println("SIZE OF OBSERVALBE = " + this.observableListScript.size());
        updateImage();
//        
//        for (int j = 0; j < this.numberOfParam; j++) {
//            ScriptHasBeenConfigured test = new ScriptHasBeenConfigured();
//            test.setParamOrder((byte) j);
//            test.setParameters();
//            teststephasscript.addScripthasBeenConfigured(test);
//            this.observableListScript.add(test);
//        }
    }

    /**
     * @param currentSelectedScript
     */
    public void updateGridPaneEdit(TestStepHasScript currentSelectedScript) {
        //constructGridPaneView(currentSelectedScript);
        constructGridPaneView(currentSelectedScript);
        this.choiceBoxss.getSelectionModel().select(currentSelectedScript.getScript().getName());
        updateGridPaneModification(currentSelectedScript);
        referParameters(new ArrayList(currentSelectedScript.getScriptHasBeenConfigureds()));
        Iterator<ScriptHasBeenConfigured> iteTSHS = this.observableListScript.iterator();
        teststephasscript.getScriptHasBeenConfigureds().clear();
        int i = 0;
        while (iteTSHS.hasNext()) {
            ScriptHasBeenConfigured currentSHBC = iteTSHS.next();
            teststephasscript.addScripthasBeenConfigured(currentSHBC);
            //teststephasscript.setTestStep(currentSelectedScript.getTestStep());
        }
        //teststephasscript.setScriptHasBeenConfigureds(this.observableListScript);
        updateImage();
    }

    /**
     * @param currentSelectedScript
     */
    public void updateGridPaneBaseline(TestStepHasScript currentSelectedScript) {

//        Script macr = currentSelectedScript.getScript();
//        macroHandler.getAllFromMacro(macr);
        //constructGridPaneView(currentSelectedScript);
        currentScript = currentSelectedScript.getScript();
        this.choiceBoxss.setVisible(false);
        //System.out.println("Le non du script est : " + currentSelectedScript.getScript().getName());
        this.labelNameOfScript.setVisible(true);
        this.labelNameOfScript.setText(currentSelectedScript.getScript().getName());
        updateGridPaneModification(currentSelectedScript);
        //System.out.println("NUMBERO OF PARAM SELECTED = "+currentSelectedScript.getScriptHasBeenConfigureds().size());
        referParameters(new ArrayList(currentSelectedScript.getScriptHasBeenConfigureds()));
        updateImage();

    }

    /**
     * @param testStephasScript
     */
    public void updateScriptViewDisplay(TestStepHasScript testStephasScript) {

        // currentScript = testStephasScript
        Script macr = testStephasScript.getScript();
        macroHandler.getAllFromMacro(macr);
        constructGridPaneView(testStephasScript);
        this.choiceBoxss.setVisible(false);
        this.labelNameOfScript.setVisible(true);
        this.labelNameOfScript.setText(testStephasScript.getScript().getName());
        referParameters(new ArrayList(testStephasScript.getScriptHasBeenConfigureds()));
        updateImage();
    }

    void updateGridPaneExecution(ScriptExecutions currentTSHS) {

        this.choiceBoxss.setVisible(false);
        this.labelNameOfScript.setVisible(true);
        this.labelNameOfScript.setText(currentTSHS.getScript().getName());

        gridPaneDisplayResults.getChildren().clear();

        Iterator<ParametersExecution> itScriptParameters = currentTSHS.getParametersExecutions().iterator();
        this.numberOfParam = currentTSHS.getParametersExecutions().size();
        while (itScriptParameters.hasNext()) {
            ParametersExecution paramExecution = itScriptParameters.next();
            Label labelToSet;
            if (currentTSHS.getScript().getIsMacro() == 1) {
                if (paramExecution.getPurpose() != null && !"".equals(paramExecution.getPurpose())) {
                    labelToSet = new Label(paramExecution.getPurpose() + " " + paramExecution.getParameters().getName());
                } else {
                    labelToSet = new Label(paramExecution.getParameters().getName());
                }
            } else {
                labelToSet = new Label(paramExecution.getParameters().getName());
            }
            //Label labelToSet = new Label(paramExecution.getParameters().getName());
            //labelToSet.setTooltip(new Tooltip(param.getName()));
            gridPaneDisplayResults.addColumn(0, labelToSet);
            Hyperlink toSet = new Hyperlink(paramExecution.getValue());
            toSet.setText(toSet.getText().replace("@&Buffer_", ""));
            gridPaneDisplayResults.addColumn(1, toSet);

        }
        controllerScriptFather.updateSizeGrid();
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
     * return the boolean if the script is fully configured or not.
     *
     * @return
     */
    public boolean getConfigured() {
        return this.isCOnfigured;
    }

    /**
     * Disable the configuration when the baseline is validated, no more
     * configuration is available.
     */
    public void disableParamConfiguration() {
        this.canBeConfigured = false;
    }

    /**
     * @return
     */
    public ScriptLineTableStepController getControllerScriptFather() {
        return this.controllerScriptFather;
    }
}
