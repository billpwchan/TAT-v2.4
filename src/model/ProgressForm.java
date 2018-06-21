/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author Thomas M.
 */
public class ProgressForm {

    private final Stage dialogStage;
    private final ProgressIndicator pin = new ProgressIndicator();

    /**
     *
     */
    public ProgressForm() {
        //System.out.println("HERE2");
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        dialogStage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
        });
        
        // PROGRESS BAR
        final Label label = new Label();
        label.setText("Please wait until the end of display preparation");
        label.setStyle("-fx-font-size:15;"
                + "-fx-font-weight:bold;");
        
        pin.setPrefSize(100,100);
        pin.setProgress(-1F);
        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label,pin);

        Scene scene = new Scene(hb);
        dialogStage.setScene(scene);
    }

    /**
     *
     * @param task
     */
    public void activateProgressBar(final Task<?> task) {
        pin.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    /**
     *
     * @return
     */
    public Stage getDialogStage() {
        return dialogStage;
    }
    
    /**
     *
     * @return
     */
    public ProgressIndicator getProgressBar(){
        return this.pin;
    }
}
