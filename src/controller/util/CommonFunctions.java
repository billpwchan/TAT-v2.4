/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import DB.Script;
import DB.TestCase;
import controller.tablestep.ScriptLineTableStepController;
import controller.tablestep.StepLineTableStepController;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * @author billpwchan
 */
public class CommonFunctions {

    public static final Logger debugLog = Logger.getLogger("debugLogger");
    public static final Logger reportLog = Logger.getLogger("reportsLogger");

    public static boolean displayWarningIncorrectInputFormat(String fieldName, Integer maxLength, boolean identifier) {
        if (!identifier) {
            return false;
        }
        CommonFunctions.displayAlert(AlertType.WARNING, "Warning. ", "Incorrect Input Format in Field \"" + fieldName + "\": ", fieldName + " exceeds maximum characters allowed (" + maxLength.toString() + " characters). Please use another value, or only part of your input will be recorded.");
        return true;
    }

    public static Optional<ButtonType> displayAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public static void AddListener(TextField fieldFilter, FilteredList<TestCase> filteredData) {
        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tCase -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (tCase.getTestCaseIdentification().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (tCase.getProject() != null && tCase.getProject().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTypeOfTest() != null && tCase.getTypeOfTest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getCategoryOfTest() != null && tCase.getCategoryOfTest().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getLocation() != null && tCase.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestCaseTitle() != null && tCase.getTestCaseTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestCaseSource() != null && tCase.getTestCaseSource().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTotalSteps() != null && tCase.getTotalSteps().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getBlocking() != null && tCase.getBlocking().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getInternalComments() != null && tCase.getInternalComments().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getWritingStatus() != null && tCase.getWritingStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getWritter() != null && tCase.getWritter().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getCreationDate() != null && tCase.getCreationDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestObjective() != null && tCase.getTestObjective().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tCase.getTestMethodIadt() != null && tCase.getTestMethodIadt().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else
                    return tCase.getEnvironment() != null && tCase.getEnvironment().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    public static void addListenerScript(FilteredList<Script> filteredData, TextField fieldFilter) {
        fieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(script -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (script.getCreationDate() != null && script.getCreationDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (script.getEditionDate() != null && script.getEditionDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (script.getDesciption() != null && script.getDesciption().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (script.getName() != null && script.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else
                    return script.getScriptVersion() != null && script.getScriptVersion().toString().toLowerCase().contains(lowerCaseFilter);
                // Does not match.
            });
        });
    }

    public static boolean validateUpdateTestCase(ObservableList<StepLineTableStepController> observableTestStep) {
        for (StepLineTableStepController current : observableTestStep) {
            for (int j = 0; j < current.getCollectionScript().size(); j++) {
                ScriptLineTableStepController currentScript = current.getCollectionScript().get(j);
                //A step should not have null step description.
                if (currentScript.getScriptAction() == null) {
                    CommonFunctions.displayAlert(AlertType.ERROR, "Invalid Script Found", "Invalid Script", "A script cannot only has expected result without step description");
                    return true;
                }
            }
        }
        return false;
    }

}
