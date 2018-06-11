/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.macro;

import java.net.URL;
import java.text.DateFormat;
import java.util.ResourceBundle;

import com.sun.istack.internal.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabMacroEditController implements Initializable {
    @FXML
    private AnchorPane anchorPanelEditMacro;
    @FXML
    private GridPane gridPaneMacroEdit;
    @FXML
    private Button buttonValid;
    @FXML
    private GridPane gridPaneTableAction;
    @FXML
    private Text labelActionTitle;
    @FXML
    private Button buttonAddAction;
    @FXML
    private GridPane gridPaneLabelCaseEdit;
    @FXML
    private Label labelMacroNameEdit;
    @FXML
    private Label labelMacroEditionDate;
    @FXML
    private Label labelMacroCreationDate;
    @FXML
    private Label labelMacroVersion;
    @FXML
    private Label labelMacroObjectives;
    @FXML
    private TextField jtextfieldMacroNameEdit;
    @FXML
    private TextField jtextfieldMacroEdit;
    @FXML
    private TextField jtextfieldMacroCreationDateEdit;
    @FXML
    private TextField jtextfieldTypeMacroEditionDateEdit;
    @FXML
    private TextArea jtextareaObjectivesMacroEdit;
    @FXML
    private ScrollPane scrollPanePreview;
    @FXML
    private Label labelIsStimuli;
    @FXML
    private CheckBox stimuliCheckBoxEdit;
    @FXML
    private AnchorPane anchorHeader;
    @FXML
    private Text labelPreview;

    private static TabMacroMainViewController mainController;

    private TabActionCreationController controllerTableAction;

    private final PreviewMacro controllerPreviewMacro = new PreviewMacro();

    private final boolean canBeValidate = false;

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    

    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initButtons();

    }    

    public void initButtons() {
        this.buttonValid.setDisable(true);
        this.buttonAddAction.setOnAction((ActionEvent e) -> {
            controllerTableAction.addAction();
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            try{
                this.createMacro();
            } catch (ParseException ex){
                Logger.getLogger(TabMacroNewController.class.getName().log(Level.SEVERE, null, ex));
            }
        });

    }
    
}
