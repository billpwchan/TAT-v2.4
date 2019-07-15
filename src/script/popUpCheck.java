/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.ParametersExecution;
import controller.util.CommonFunctions;
import engine.Result;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Method to display text in the command line.
 *
 * @author Thomas Morin
 * @version 1.0
 */
public class popUpCheck {

    String humanCheck;
    ButtonType buttonOK = new ButtonType("OK");
    ButtonType buttonNOK = new ButtonType("NOK");

    /**
     * Constructor of popUpStimuli.
     */
    public void popUpStimuli() {
    }

    /**
     * @param parameters
     * @param hashMap
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws IOException, InterruptedException, ExecutionException {
        CommonFunctions.debugLog.error("JE RENTRE DANS POPUP");
        this.humanCheck = parameters.get(1).getValue().trim();
        if (this.humanCheck.contains("@&Buffer_")) {
            this.humanCheck = (String) hashMap.get(this.humanCheck);
        }
        ButtonType buttonOK = new ButtonType("OK");
        ButtonType buttonNOK = new ButtonType("NOK");
        ButtonType buttonOKWC = new ButtonType("OKWC");
        Optional<Result> result;
        Result finalResult = new Result();
        FutureTask<Optional<Result>> futureTask = new FutureTask(
                new showDialogBox(this.humanCheck)
        );
        final TextField comment = new TextField();
        Platform.runLater(futureTask);
        result = futureTask.get();
        finalResult.setComment(result.get().getComment());
        finalResult.setResult(result.get().getResult());
        //CommonFunctions.debugLog.error("RESULTAT DE LA DIALOG= " + result.get().getResult());
//        result = futureTask.get();
//        if (result.get()==buttonOK) {
//            finalResult.setResult("OK");
//        } else if (result.get() == buttonNOK) {
//            finalResult.setResult("NOK");
//        }
        return finalResult;
    }

    /**
     *
     */
    public void close() {

    }

    class showDialogBox implements Callable<Optional<Result>> {

        String message;
        ButtonType selectedButton = null;

        public showDialogBox(String message) {
            this.message = message;
        }

        @Override
        public Optional<Result> call() throws Exception {
            final TextField comment = new TextField();
            // Create the custom dialog.
            Dialog<Result> dialog = new Dialog<>();
            // Create the custom dialog.
            dialog.setTitle("Human Check");
            //dialog.setHeaderText("Verification : " + message);

// Set the icon (must be included in the project).
//            dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));
// Set the button types.
            //ButtonType notTestedButtonType = new ButtonType("not tested", ButtonData.OK_DONE);
            ButtonType OKButtonType = new ButtonType("OK", ButtonData.OK_DONE);
            ButtonType OKWCButtonType = new ButtonType("OKWC", ButtonData.OK_DONE);
            ButtonType NOKButtonType = new ButtonType("NOK", ButtonData.OK_DONE);
            ButtonType notTestabledButtonType = new ButtonType("Not testable", ButtonData.OK_DONE);
            ButtonType OSButtonType = new ButtonType("Out of scope", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(OKButtonType, NOKButtonType, OKWCButtonType, notTestabledButtonType, OSButtonType);

// Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            Label action = new Label("Verification :");
            action.setFont(Font.font("Verdana", 18));
            action.setUnderline(true);
            grid.add(action, 5, 0);
            Label message = new Label(this.message);
            message.setFont(Font.font("Verdana", 13));
            grid.add(message, 5, 2);
            comment.setPromptText("Comment if needed");
            Label commentLabel = new Label("Comment");
            commentLabel.setFont(Font.font("Verdana", 13));
            grid.add(commentLabel, 0, 7);
            grid.add(comment, 1, 7, 50, 1);

//// Enable/Disable login button depending on whether a username was entered.
            Node OKButton = dialog.getDialogPane().lookupButton(OKWCButtonType);
            OKButton.setStyle("-fx-background-color: #00FF00;");
            Node notTestableButton = dialog.getDialogPane().lookupButton(notTestabledButtonType);
            notTestableButton.setDisable(true);
            notTestableButton.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%);");
            //notTestableButton.setStyle("-fx-backround-color: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%)");
            Node OKWCButton = dialog.getDialogPane().lookupButton(OKWCButtonType);
            OKWCButton.setDisable(true);
            OKWCButton.setStyle("-fx-background-color: #FFC000;");
            Node NOKButton = dialog.getDialogPane().lookupButton(NOKButtonType);
            NOKButton.setDisable(true);
            NOKButton.setStyle("-fx-background-color: #FF0000;");
            Node OSButton = dialog.getDialogPane().lookupButton(OSButtonType);
            OSButton.setDisable(true);
            OSButton.setStyle("-fx-background-color: #8DB4E2;");
//
//// Do some validation (using the Java 8 lambda syntax).
            comment.textProperty().addListener((observable, oldValue, newValue) -> {
                notTestableButton.setDisable(newValue.trim().isEmpty());
                OKWCButton.setDisable(newValue.trim().isEmpty());
                NOKButton.setDisable(newValue.trim().isEmpty());
                OSButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                Result result = new Result();
                switch (dialogButton.getText()) {
                    case "OK":
                        result.setComment(comment.getText());
                        result.setResult("OK");
                        break;
                    case "OKWC":
                        result.setComment(comment.getText());
                        result.setResult("OKWC");
                        break;
                    case "NOK":
                        result.setComment(comment.getText());
                        result.setResult("NOK");
                        break;
                    case "Not testable":
                        result.setComment(comment.getText());
                        result.setResult("Not testable");
                        break;
                    case "Out of scope":
                        result.setComment(comment.getText());
                        result.setResult("OS");
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

            Optional<Result> result = dialog.showAndWait();
            return result;

        }

    }
}
