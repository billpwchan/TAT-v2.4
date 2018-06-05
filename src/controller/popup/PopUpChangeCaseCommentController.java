/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.CaseExecutions;
import controller.tabtestexecution.TabViewResultsController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class PopUpChangeCaseCommentController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPanePopUpCase;
    @FXML
    private Button buttonValidComment;
    @FXML
    private Button buttonCancelComment;
    @FXML
    private TextArea textAreaComment;

    private TabViewResultsController mainController;

    private CaseExecutions caseExecu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonValidComment.setOnAction((ActionEvent event) -> {
            this.validNewComment();
        });

        buttonCancelComment.setOnAction((ActionEvent event) -> {
            mainController.closePopUp();
        });

    }

    public void init(TabViewResultsController mainController) {
        this.mainController = mainController;
    }

    public void setComment(CaseExecutions caseExecu) {
        this.caseExecu = caseExecu;
        if (!caseExecu.getCaseExecutionResultObj().getComment().contains(caseExecu.getCaseExecutionResultObj().getSimpleStringCommentProperty())) {
            this.textAreaComment.setText(caseExecu.getCaseExecutionResultObj().getSimpleStringCommentProperty() + caseExecu.getCaseExecutionResultObj().getComment());
        } else {
            this.textAreaComment.setText(caseExecu.getCaseExecutionResultObj().getComment());
        }
    }

    private void validNewComment() {
        mainController.validCommentChange(caseExecu, this.textAreaComment.getText());
    }

}
