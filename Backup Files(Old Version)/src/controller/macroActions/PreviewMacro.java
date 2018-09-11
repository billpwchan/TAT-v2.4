/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macroActions;

import DB.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Iterator;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class PreviewMacro {

    private final static String Separator = ("" + ((char) 7));
    private final GridPane gridPaneDisplayResults = new GridPane();
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

    /**
     * Initializes the controller class.
     *
     * @param scrollPane
     */
//    @Override
    public void initialize(ScrollPane scrollPane) {
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(33);
        col2.setPercentWidth(33);
        col3.setPercentWidth(34);
        gridPaneDisplayResults.getColumnConstraints().addAll(col1, col2, col3);
        scrollPane.setContent(gridPaneDisplayResults);
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
     * Update the gridpane of the view depending on the script selected (view
     * View).
     *
     * @param controllerActions
     */
    public void updateGridPaneCreation(TableActionCreationController controllerActions) {
        constructGridPaneView(controllerActions);
    }

    /**
     * Construct default view. "Click on Equipment"
     *
     * @param controllerActions
     */
    private void constructGridPaneView(TableActionCreationController controllerActions) {
        gridPaneDisplayResults.getChildren().clear();
        controllerActions.getCollectionControllerScript().stream().forEach((collectionControllerScript) -> {
            for (int j = 1; j < collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro().size(); j++) {
                if (collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro().get(j).getToDisplay() == 1) {
                    ScriptHasParameters scriptHasParameters = collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro().get(j).getScriptHasParameters();
                    Parameters param = scriptHasParameters.getParameters();
                    Label scriptPurpose = new Label(collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro().get(0).getValue() + "  ");
                    gridPaneDisplayResults.addColumn(0, scriptPurpose);
                    Label labelToSet = new Label(param.getName());
                    labelToSet.setTooltip(new Tooltip(param.getName()));
                    gridPaneDisplayResults.addColumn(1, labelToSet);
                    Hyperlink toSet = new Hyperlink("");
                    toSet.setTooltip(new Tooltip(""));
                    gridPaneDisplayResults.addColumn(2, toSet);
                    referParameters(collectionControllerScript.getScriptControllerAction().getHashParamScriptMacro().get(j), toSet);
                }
            }
        });
    }

    /**
     * Take the parameters from the popup script and associate them in the
     * observable list, update the view with the corresponding values.
     *
     * @param paramScriptMacros observable list containing the parameters.
     * @param toSet             the hyperlink to change
     */
    private void referParameters(ParamScriptMacro paramScriptMacros, Hyperlink toSet) {
        toSet.setStyle(null);

        if (!"".equals(paramScriptMacros.getValuePath()) && !" ".equals(paramScriptMacros.getValuePath())) {
            if (null != paramScriptMacros.getValuePath()) {
                switch (paramScriptMacros.getValuePath()) {
                    case "Constant":
                        toSet.setText(paramScriptMacros.getValue().replace(Separator, ""));
                        toSet.setTooltip(new Tooltip(paramScriptMacros.getValue().replace(Separator, "")));
                        break;
                    case "Excel file":
                        toSet.setText(paramScriptMacros.getValuePath());
                        String[] parts = paramScriptMacros.getValue().split(Separator);
                        toSet.setTooltip(new Tooltip("Sheet number: " + parts[1] + " Position X: " + parts[2] + " Postion Y: " + parts[3]));//+ " Rang: " + parts[4]));
                        break;
                    case "Buffer list":
                        String test = paramScriptMacros.getValue().replace(Separator, "");
                        test = test.replace("@&Buffer_", "");
                        toSet.setText(test);
                        toSet.setTooltip(new Tooltip(test));
                        break;
                    case "From Other Script":
                        toSet.setText(paramScriptMacros.getParamScriptMacro().getValue());
                        break;
                }
            }
        } else {
            toSet.setText("Configure " + paramScriptMacros.getScriptHasParameters().getParameters().getName());
        }
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
    public Node getNodeByRowColumnIndexView(final int row, final int column, GridPane gridPane) {
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
     * @param script
     */
    public void updateGridPaneCreationView(Script script) {
        constructGridPaneViewView(script);
    }

    /**
     * @param script
     */
    private void constructGridPaneViewView(Script script) {
        gridPaneDisplayResults.getChildren().clear();
        int i = 0;
        String purpose = "";
        Iterator<Macro> itMacro = script.getMacrosForScriptIdScript().iterator();
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
                    ScriptHasParameters scriptHasParameters = param.getScriptHasParameters();
                    Parameters param2 = scriptHasParameters.getParameters();
                    Label purposeLabel = new Label(purpose + "  ");
                    gridPaneDisplayResults.addColumn(0, purposeLabel);
                    Label labelToSet = new Label(param2.getName());//controllerActions.collectionControllerScript.get(i).getScriptControllerAction().getHashParamScriptMacro().get(0).getValue() + " " + param.getName());
                    //labelToSet.setTooltip(new Tooltip(purpose + " " + param2.getName()));
                    gridPaneDisplayResults.addColumn(1, labelToSet);
                    Hyperlink toSet = new Hyperlink("");
                    toSet.setTooltip(new Tooltip(""));
                    gridPaneDisplayResults.addColumn(2, toSet);
                    referParameters(param, toSet);
                }
                i++;
            }
        }
    }
}
