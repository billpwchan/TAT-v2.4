/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.popup;

import DB.Requirement;
import controller.tablestep.StepLineTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import java.net.URL;
import java.util.ResourceBundle;
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

/**
 * FXML Controller class
 *
 * @author T0155040
 */
public class PopUPRequirementSelectionController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPanePopUpRequirement;
    @FXML
    private TableView<Requirement> tableViewRequirementPopUpAddRea;
    @FXML
    private Button buttonValidationReq;

    public static TableStepScriptCreationController controllerNewStep;

    private StepLineTableStepController stepLineController;

    private final ObservableList<Requirement> observableListRequirement = FXCollections.observableArrayList();

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
        this.anchorPane.getStylesheets().add("/view/CustomeStyle.css");
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
//        System.out.println(view.lookupAll(".button"));
//        //System.out.println("this.anchorPane.lookupAll("#button").size()");
        //this.view.set;
//        tableViewRequirementPopUpAddRea.setPlaceholder(new Label(""));
//        tableViewRequirementPopUpAddRea.setTableMenuButtonVisible(true);
//        tableViewRequirementPopUpAddRea.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tableViewRequirementPopUpAddRea.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        requirementsSelected = tableViewRequirementPopUpAddRea.getSelectionModel().getSelectedItems();
        buttonValidationReq.setOnAction((ActionEvent event) -> {
            stepLineController.setAction(this.view.getTargetItems());
            controllerNewStep.closePopUp();
        });

//        tableViewRequirementPopUpAddRea.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Requirement> observable, Requirement oldValue, Requirement newValue) -> {
//            buttonValidationReq.setDisable(false);
//        });
//        this.tableViewRequirementPopUpAddRea.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() == 2 && tableViewRequirementPopUpAddRea.getSelectionModel().getSelectedItem() != null) {
//                    requirementsSelected = tableViewRequirementPopUpAddRea.getSelectionModel().getSelectedItems();
//                    stepLineController.setAction(requirementsSelected);
//                    controllerNewStep.closePopUp();
//                }
//            }
//        });
//        ContextMenu menu = new ContextMenu();
//        MenuItem viewCase = new MenuItem("View");
//        menu.getItems().add(viewCase);
//        tableViewCasePopUpAddCase.setContextMenu(menu);
//
//        viewCase.setOnAction((ActionEvent event) -> {
//            if (tableViewCasePopUpAddCase.getSelectionModel().getSelectedItem() != null && casesSelected.size() == 1) {
//                controllerNewCampaign.viewTestCase(casesSelected.get(0));
//            }
//        });
    }

    public void init(TableStepScriptCreationController controllerNewCampaign, StepLineTableStepController stepLine) {
        stepLineController = stepLine;
        controllerNewStep = controllerNewCampaign;
    }

    public void setTable(ObservableList<Requirement> casesInDB,ObservableList<Requirement> requirementInStep) {
        casesInDB.removeAll(requirementInStep);
        view.getSourceItems().addAll(casesInDB);
        view.getTargetItems().addAll(requirementInStep);
        this.tableViewRequirementPopUpAddRea.setVisible(false);
        //tableViewRequirementPopUpAddRea.setItems(casesInDB);
        //initColumn campaignColumnInit = new initColumn();
        //campaignColumnInit.initColumnRequirement(tableViewRequirementPopUpAddRea);
    }

}
