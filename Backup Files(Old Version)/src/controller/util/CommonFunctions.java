/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author billpwchan
 */
public class CommonFunctions {

    public static boolean displayWarningIncorrectInputFormat(String fieldName, Integer maxLength, boolean identifier) {
        if (!identifier) {
            return false;
        }
        CommonFunctions.displayAlert(AlertType.WARNING, "Warning. ", "Incorect Input Format in Field \"" + fieldName + "\": ", fieldName + " exceeds maximum characters allowed (" + maxLength.toString() + " characters). Please use another value, or only part of your input will be recorded.");
        return true;
    }

    public static void displayAlert(AlertType alertType, String title, String headerTexxt, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerTexxt);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
