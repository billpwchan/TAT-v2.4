/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tablestep;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class HeaderTableStepController implements Initializable {

    private static final Image ime = new Image("assets/images/arrowDown.png");
    @FXML
    private AnchorPane anchorPanInVbox;
    @FXML
    private GridPane gridPanInAnchor;
    @FXML
    private Label labelReadyAction;
    @FXML
    private Label labelReadyVerif;
    @FXML
    private ImageView imageExpand;
    private boolean expand;
    private TableStepScriptCreationController controllerTableStep;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loadImageExpand();
        this.imageExpand.setVisible(true);
        imageExpand.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            expandChildren();
            event.consume();
        });
        this.anchorPanInVbox.getStylesheets().add("/assets/view/CustomeStyle.css");
        this.anchorPanInVbox.getStyleClass().add("header-custom");
        this.anchorPanInVbox.getStyleClass().add("header-custom2");
    }

    /**
     *
     */
    public void setRestults() {
        this.labelReadyAction.setText("Results");
        this.labelReadyVerif.setText("Results");
    }

    /**
     *
     */
    public void loadImageExpand() {
        this.expand = false;
        this.imageExpand.setImage(ime);
        this.imageExpand.setRotate(-90);
    }

    /**
     * @param controllerTableStep
     */
    public void init(TableStepScriptCreationController controllerTableStep) {
        this.controllerTableStep = controllerTableStep;
    }

    /**
     *
     */
    public void expandChildren() {
        for (int i = 0; i < controllerTableStep.getCollectionTestStep().size(); i++) {
            if (controllerTableStep.getCollectionTestStep().get(i).getIsExpand() == this.expand) {
                controllerTableStep.getCollectionTestStep().get(i).expandChildren();
            }
        }
    }

    /**
     *
     */
    public void verifyExpand() {
        //System.out.println("verify expand");
        boolean same = true;
        //System.out.println("SIZE = " + controllerTableStep.getCollectionTestStep().size());
        for (int i = 0; i < controllerTableStep.getCollectionTestStep().size(); i++) {
            //System.out.println("MASTER EXPAND ? " + this.expand);
            //System.out.println("Children Expand ? " + controllerTableStep.getCollectionTestStep().get(i).getIsExpand());
            if (this.expand == controllerTableStep.getCollectionTestStep().get(i).getIsExpand()) {
                //System.out.println("SAME STATE");
                same = false;
                //break;
            } else {
                //System.out.println("NOT SAME STATE");
            }
        }
        if (same == true) {
            if (this.expand) {
                this.expand = false;
                imageExpand.setRotate(-90);
            } else {
                this.expand = true;
                imageExpand.setRotate(0);
            }
        }
    }
}
