/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcampaign;

import DB.TestCampaign;
import DB.TestCase;
import DBcontroller.TestCaseDB;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.initColumn;

/**
 * FXML Controller class
 *
 * @author Thomas M.
 */
public class TabTestCampaignViewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelViewTestCampaign;
    @FXML
    private GridPane gridPaneCampaignView;
    @FXML
    private TableView<TestCase> tableViewTestCase;
    @FXML
    private GridPane gridPaneLabelCampaignView;
    @FXML
    private Label labelReferenceCampaignView;
    @FXML
    private Label labelNameCampaignView;
    @FXML
    private Label labelSystemCampaignView;
    @FXML
    private Label labelWriterCampaignView;
    @FXML
    private Label labelVersionCampaignView;
    @FXML
    private Label labelCreationDateCampaignView;
    @FXML
    private Label labelEditionDateCampaignView;
    @FXML
    private Label labelSUTReleaseCampaignView;
    @FXML
    private Label labelNumberOfCaseCampaignView;
    @FXML
    private Label labelRegressionThreadCampaignView;
    @FXML
    private Label labelWriterMailCampaignView;
    @FXML
    private Label labelDescriptionCampaignView;
    @FXML
    private Label labelCommentsCampaignView;
    @FXML
    private TextField jtextfieldReferenceCampaignView;
    @FXML
    private TextField jtextfieldNameCampaignView;
    @FXML
    private TextField jtextfieldSystemCampaignView;
    @FXML
    private TextField jtextfieldWriterCampaignView;
    @FXML
    private TextField jtextfieldVersionCampaignView;
    @FXML
    private TextField jtextfieldCreationDateCampaignView;
    @FXML
    private TextField jtextfieldEditionDateCampaignView;
    @FXML
    private TextField jtextfieldSUTReleaseCampaignView;
    @FXML
    private TextField jtextfieldNumberCasesCampaignView;
    @FXML
    private TextField jtextfieldWriterEmailCampaignView;
    @FXML
    private TextArea jtextareaCommentsCampaignView;
    @FXML
    private CheckBox CheckboxRegressionThreadCampaignView;

    /**
     *
     */
    public TestCaseDB testCaseHandler = new TestCaseDB();

    /**
     *
     */
    public static TabTestCampaignMainViewController main;

    private final ObservableList<TestCase> observableListTestCase = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.anchorPanelViewTestCampaign.getStylesheets().add("/view/testcampaign/cssViewCampaign.css");
        tableViewTestCase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewTestCase.setTableMenuButtonVisible(true);
        initColumn campaignColumnInit = new initColumn();
        campaignColumnInit.initColumnCase(tableViewTestCase);
        tableViewTestCase.setItems(observableListTestCase);

        this.tableViewTestCase.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                main.viewTestCase(tableViewTestCase.getSelectionModel().getSelectedItem());
            }
        });

        Label label1 = new Label();
        this.tableViewTestCase.getSelectionModel().getSelectedCells().addListener((ListChangeListener.Change<? extends TablePosition> c) -> {
            label1.setText(String.valueOf(c.getList().get(0).getRow()));
            //System.out.println(label1);
        });

    }

    /**
     * initialize the reference of the class parent
     * TabTestCampaignViewController to this controller.
     *
     * @param mainController the reference to the parent class
     */
    void init(TabTestCampaignMainViewController mainController) {
        TabTestCampaignViewController.main = mainController;
    }

    /**
     * Construct all the information to display in the tab view
     *
     * @param testCampaign the campaign to display
     */
    void constructInformation(TestCampaign testCampaign) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates       this.jtextfieldNameCaseView.setText(testCase.getName());
        this.jtextfieldReferenceCampaignView.setText(String.valueOf(testCampaign.getReference()));
        this.jtextfieldSystemCampaignView.setText(testCampaign.getSystem());
        this.jtextfieldWriterCampaignView.setText(testCampaign.getWritter());
        this.jtextfieldVersionCampaignView.setText(String.valueOf(testCampaign.getCampaignVersion()));
        this.jtextfieldCreationDateCampaignView.setText(testCampaign.getCreationDate());
        this.jtextfieldEditionDateCampaignView.setText(String.valueOf(testCampaign.getEditionDate()).equals("null") ? "" : testCampaign.getEditionDate());
        this.jtextfieldSUTReleaseCampaignView.setText(testCampaign.getSoftwareSutRelease());
        this.jtextfieldNumberCasesCampaignView.setText(String.valueOf(testCampaign.getNumberTestCase()));
        this.CheckboxRegressionThreadCampaignView.setSelected(testCampaign.getRegressionThread() != 0);
        this.CheckboxRegressionThreadCampaignView.setDisable(true);
        this.jtextfieldWriterEmailCampaignView.setText(testCampaign.getWriterEmail());
        this.jtextareaCommentsCampaignView.setText(testCampaign.getComments());
        observableListTestCase.setAll(testCaseHandler.getTestCasesFromTestCampaign(testCampaign));
    }

}
