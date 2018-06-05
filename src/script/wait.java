/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.application.Platform;
import javafx.scene.Node;

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class wait {

    ArrayList<String> arrayToSearchIn;
    int timeToWait;

    public String run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws IOException, InterruptedException {
        //System.out.println("IN WAIT  dans le TAT !!!");

        this.timeToWait = (int) Double.parseDouble(parameters.get(1).getValue());
        this.timeToWait = this.timeToWait * 1000;
        Platform.runLater(() -> {
       
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Please wait " + timeToWait/1000 + " seconds");
                Node OKButton = alert.getDialogPane().lookupButton(ButtonType.OK);
                OKButton.setVisible(false);
                //alert.getButtonTypes().get(alert.getButtonTypes().indexOf(ButtonType.OK));
                alert.show();

                
                alert.close();
  
        });
        Thread.sleep(this.timeToWait);
        return null;
    }

    public void close() {

    }

    public ArrayList<Parameters> parameters(int i) {
        ArrayList<Parameters> params = new ArrayList<>();
        Parameters paramTimer = new Parameters();
        paramTimer.setName("Timer2");
        paramTimer.setParameterType("int");
        paramTimer.setDescription("Ceci est la description");
        params.add(paramTimer);
        return params;
    }
}
