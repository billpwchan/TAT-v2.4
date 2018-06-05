/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import engine.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class popUpStimuli implements InterfaceScript {

    String humanStimuli;

    /**
     * Constructor of popUpStimuli.
     */
    public void popUpStimuli() {
    }

    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws IOException, InterruptedException, ExecutionException {
        //System.out.println("JE RENTRE DANS LA POPUP STIMULI");
        Optional<Result> result;
        this.humanStimuli = parameters.get(1).getValue().trim();
        if (this.humanStimuli.contains("@&Buffer_")) {
            this.humanStimuli = (String) hashMap.get(this.humanStimuli);
        }
        FutureTask<Optional<Result>> futureTask = new FutureTask(
                new showDialog(this.humanStimuli)
        );
        Platform.runLater(futureTask);
        result = futureTask.get();

        return null;
    }


    @Override
    public ArrayList<Parameters> parameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Script scriptInfos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class showDialog implements Callable<Optional<Result>> {

        ButtonType selectedButton = null;
        private String message;

        public showDialog(String message) {
            this.message = message;
        }

        @Override
        public Optional<Result> call() throws Exception {
            // Create the custom dialog.
            Dialog<Result> dialog = new Dialog<>();
            // Create the custom dialog.
            dialog.setTitle("Human Stimuli");

            // Set the button types.
            ButtonType OKButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType NOKButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(OKButtonType, NOKButtonType);

            Node NOKButton = dialog.getDialogPane().lookupButton(NOKButtonType);
            NOKButton.setDisable(true);
            NOKButton.setVisible(false);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new ImageView("/images/action.png"), 0, 0, 3, 3);
            Label action = new Label("Action to perform :");
            action.setFont(Font.font("Verdana", 20));
            action.setUnderline(true);
            grid.add(action, 6, 0);
            Label message = new Label(this.message);
            message.setFont(Font.font("Verdana", 15));
            grid.add(message, 6, 1);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                Result result = new Result();
                switch (dialogButton.getText()) {
                    case "OK":
                        result.setResult("OK");
                        break;
                    default:
                        break;
                }
                selectedButton = dialogButton;
                return result;
            });

            dialog.setResizable(true);
            dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    if (selectedButton == null) {
                        event.consume();
                    }
                }
            });
            //System.out.println("ATTENTION SHOW AND WAIT");

            Optional<Result> result = dialog.showAndWait();
            return result;

        }
    }

    public void close() {

    }
}
