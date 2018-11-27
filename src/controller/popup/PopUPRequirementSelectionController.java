/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.Requirement;
import controller.tablestep.StepLineTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.ListSelectionView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155040
 */
public class PopUPRequirementSelectionController implements Initializable {

    /**
     *
     */
    public static TableStepScriptCreationController controllerNewStep;
    private final ObservableList<Requirement> observableListRequirement = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPanePopUpRequirement;
    @FXML
    private TableView<Requirement> tableViewRequirementPopUpAddRea;
    @FXML
    private Button buttonValidationReq;
    private StepLineTableStepController stepLineController;
    private ObservableList<Requirement> requirementsSelected = FXCollections.observableArrayList();

    private ListSelectionView<Requirement> view = new ListSelectionView<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.anchorPane.getStylesheets().add("/assets/view/CustomeStyle.css");
        buttonValidationReq.setDisable(false);
        this.gridPanePopUpRequirement.add(view, 0, 0, 1, 1);
        this.view.setCellFactory(listView -> {
            ListCell<Requirement> cell = new ListCell<Requirement>() {
                @Override
                public void updateItem(Requirement item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getRequirementID());
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });

        buttonValidationReq.setOnAction((ActionEvent event) -> {
            stepLineController.setAction(this.view.getTargetItems());
            controllerNewStep.closePopUp();
        });
    }

    /**
     * @param controllerNewCampaign
     * @param stepLine
     */
    public void init(TableStepScriptCreationController controllerNewCampaign, StepLineTableStepController stepLine) {
        stepLineController = stepLine;
        controllerNewStep = controllerNewCampaign;
    }

    /**
     * @param casesInDB
     * @param requirementInStep
     */
    public void setTable(ObservableList<Requirement> casesInDB, ObservableList<Requirement> requirementInStep) {
        casesInDB.removeAll(requirementInStep);
        view.getSourceItems().addAll(casesInDB);
        view.getTargetItems().addAll(requirementInStep);
        this.tableViewRequirementPopUpAddRea.setVisible(false);
    }

}
