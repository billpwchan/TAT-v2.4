/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.requirements;

import DB.Requirement;
import DB.TestCase;
import DBcontroller.sessionFactorySingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import main.Main;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author T0155040
 */
public class TabRequirementCreationController implements Initializable {

    @FXML
    private AnchorPane anchorPanelNewTestCampaign;
    @FXML
    private GridPane gridPaneAddCampaign;
    @FXML
    private GridPane gridPaneButtonAddCampaign;
    @FXML
    private Button buttonValidRequirement;
    @FXML
    private GridPane gridPaneFieldsAddCampaign;
    @FXML
    private Label labelReferenceAddCampaign;
    @FXML
    private TextField jtextfieldIDRequirement;
    @FXML
    private Label labelVersionAddCampaign;
    @FXML
    private TextField jtextfieldRequirementVersion;
    @FXML
    private Label labelCreationDateAddCampaign;
    @FXML
    private TextField jtextfieldRequirementTitle;
    @FXML
    private Label labelEditionDateAddCampaign;
    @FXML
    private TextField jtextfieldRquirementWriter;
    @FXML
    private Label labelCommentsAddCampaign;
    @FXML
    private TextArea textareaComments;
    @FXML
    private Text textTestCaseAddCampaign1;
    @FXML
    private Text textTestCaseAddCampaign;
    @FXML
    private TableView<TestCase> TableViewTestCasesAdded;
    @FXML
    private Button buttonAddCase;
    @FXML
    private Button buttonDeleteCase;
    @FXML
    private Button buttonUp;
    @FXML
    private Button buttonDown;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private ChoiceBox choiceBoxIADT;

    private TabRequirementMainViewController main;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceBoxIADT.getItems().addAll("Inspection", "Analysis", "Demonstration", "Test");
        this.jtextfieldRquirementWriter.setText(Main.currentUser.getName());
        buttonValidRequirement.setOnAction((ActionEvent event) -> {
            createRequirement();
            main.updateRepository();
            main.closeTab();
        });

    }

    void init(TabRequirementMainViewController aThis) {
        main = aThis;
    }

    private void createRequirement() {
        Requirement toSave = new Requirement(this.jtextfieldIDRequirement.getText(), this.jtextfieldRequirementTitle.getText(), this.jtextfieldRquirementWriter.getText(), this.htmlEditor.getHtmlText(), (short) 0, this.textareaComments.getText(), null, (String) choiceBoxIADT.getSelectionModel().getSelectedItem());
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.save(toSave);
        session.beginTransaction().commit();
        session.close();
    }

}
