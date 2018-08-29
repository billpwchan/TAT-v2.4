/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.tabtestcase;

import DB.Requirement;
import DB.TestCase;
import DBcontroller.TestCaseDB;
import controller.tablestep.HeaderTableStepController;
import controller.tablestep.TableStepScriptCreationController;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TabTestCaseViewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelViewTestCase;
    @FXML
    private GridPane gridPaneCaseView;
    @FXML
    private GridPane gridPaneLabelCaseView;
    @FXML
    private Label labelCaseIDView;
    @FXML
    private Label labelTypeTestCaseView;
    @FXML
    private Label labelProjectCaseView;
    @FXML
    private Label labelCaseVersionView;
    @FXML
    private Label labelTestSheetIDCaseView;
    @FXML
    private Label labelTestSheetVersionCaseView;
    @FXML
    private Label labelCaseTitleView;
    @FXML
    private Label labelWriterCaseView;
    @FXML
    private Label labelWritingStatusCaseView;
    @FXML
    private Label labelWriterEmailCaseView;
    @FXML
    private Label labelTestCategoryCaseView;
    @FXML
    private Label labelTestEnvironementCaseView;
    @FXML
    private TextField jtextfieldCaseIDView;
    @FXML
    private TextField jtextfieldVersionCaseView;
    @FXML
    private TextField jtextfieldCaseTitleView;
    @FXML
    private TextField jtextfieldWriterCaseView;
    @FXML
    private TextField jtextfieldTypeTestCaseView;
    @FXML
    private TextField jtextfieldCaseSourceView;
    @FXML
    private TextField jtextfieldWritingStatusCaseView;
    @FXML
    private TextField jtextfieldWriterEmailCaseView;
    @FXML
    private TextField jtextfieldProjectCaseView;
    @FXML
    private TextField jtextfieldCaseVersionView;
    @FXML
    private TextField jtextfieldTestCategoryCaseView;
    @FXML
    private TextField jtextfieldTestEnvironementCaseView;
    @FXML
    private TextArea jtextareaCaseObjectivesView;
    @FXML
    private Label labelDescriptionCaseView;
    @FXML
    private Label labelCommentsCaseView;
    @FXML
    private TextArea jtextareaInternalCommentsCaseView;
    @FXML
    private TextField jtextfieldCreationDateCaseView;
    @FXML
    private TextField jtextfieldNumberStepCaseView;
    @FXML
    private CheckBox checkBoxBlockingCaseView;
    @FXML
    private TextField jtextfieldTestMethodCaseView;
    @FXML
    private TextField jtextfieldLocationCaseView;
    @FXML
    private TextField jtextfieldInternalVersionCaseView;
    @FXML
    private TextField jtextfieldEditionDateCaseView;
    @FXML
    private AnchorPane anchorStep;
    @FXML
    private ListView listViewRequirementsCaseView;

    private final TestCaseDB testCaseHandler = new TestCaseDB();

    private static TabTestCaseMainViewController main;

    private TableStepScriptCreationController controllerTableStep;

    private HeaderTableStepController controllerHeaderTableStep;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.anchorPanelViewTestCase.getStylesheets().add("/view/stepcreation/cssScriptLine.css");
        constructTableStep();
        this.listViewRequirementsCaseView.setCellFactory(listView -> {
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
//        /**
//         * Define the the cell factory of the table test step.
//         */
//TreeTableColumn<testCaseTreeTableViewObject, String> stimuliCol
//                = new TreeTableColumn<>("Stimuli");
//        stimuliCol.setPrefWidth(150);
//        stimuliCol.setCellValueFactory(
//                (TreeTableColumn.CellDataFeatures<testCaseTreeTableViewObject, String> param)
//                -> param.getValue().getValue().stimuliProperty()
//        );
//        TreeTableColumn<testCaseTreeTableViewObject, String> checkCol
//                = new TreeTableColumn<>("check");
//        checkCol.setPrefWidth(150);
//        checkCol.setCellValueFactory(
//                (TreeTableColumn.CellDataFeatures<testCaseTreeTableViewObject, String> param)
//                -> param.getValue().getValue().checkProperty()
//        );
//        
//        
//        this.tableViewTestStep.getColumns().addAll(stimuliCol, checkCol);
//                         this.root = new TreeItem<>(new testCaseTreeTableViewObject(true));
//        this.tableViewTestStep.setRoot(this.root);
//        this.tableViewTestStep.setShowRoot(false);
//        this.tableViewTestStep.setEditable(false);
//        this.tableViewTestStep.setColumnResizePolicy(tableViewTestStep.CONSTRAINED_RESIZE_POLICY);
//
//        /**
//         * Define the row factory of the table test step.
//         */
//         this.tableViewTestStep.setRowFactory(new Callback<TreeTableView<testCaseTreeTableViewObject>, TreeTableRow<testCaseTreeTableViewObject>>() {
//
//            @Override
//            public TreeTableRow<testCaseTreeTableViewObject> call(TreeTableView<testCaseTreeTableViewObject> param) {
//                TreeTableRow<testCaseTreeTableViewObject> treeTableRow = new TreeTableRow<testCaseTreeTableViewObject>() {
//                    protected void updateItem(testCaseTreeTableViewObject item, boolean empty) {
//                        super.updateItem(item, empty);
//                        System.out.println(item);
//                        if (item == null) {
//                            setText("");
//                            setStyle("-fx-background-color: white; -fx-text-fill: white;");
//                        } else if (item.getType()) {
//                            setStyle("-fx-background-color: #CCFFCC; "
//                                    + "-fx-text-fill: black;"
//                                    + "   -fx-border-color: grey;\n"
//                                    + "    -fx-border-style: solid;"
//                                    + "-fx-border-width: 2");
//
//                        } else if (item.getType() == false) {
//                            setStyle("-fx-background-color: white;"
//                                    + " -fx-text-fill: black;"
//                                    + " -fx-border-color: grey;\n"
//                                    + " -fx-border-style: solid;"
//                                    + " -fx-indent: 25;"
//                                    + "-fx-border-width: 0.1"
//                            );
//                        } else {
//                            setStyle("-fx-background-color: blue;"
//                                    + " -fx-text-fill: black;"
//                            );
//                        }
//                    }
//                };
//                return treeTableRow;
//            }
//
//        }
//        );
    }

    /**
     * Init the main controller with the reference of the object
     * tabtestcaseMainViewController.
     *
     * @param mainController
     */
    void init(TabTestCaseMainViewController mainController) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        TabTestCaseViewController.main = mainController;

    }

    /**
     * Construct the information of this view form the information in the
     * parameter test case.
     *
     * @param testCase test case to display information of.
     */
    void constructInformation(TestCase testCase) {

        /**
         * Set all the field of the view form the information in the variable
         * test case
         */
        this.jtextfieldCaseIDView.setText(testCase.getTestCaseIdentification());
        this.jtextfieldCaseVersionView.setText(testCase.getTestCaseVersion());
        this.jtextfieldProjectCaseView.setText(testCase.getProject());
        this.jtextfieldTypeTestCaseView.setText(testCase.getTypeOfTest());
        this.jtextfieldTestCategoryCaseView.setText(testCase.getCategoryOfTest());
        this.jtextfieldLocationCaseView.setText(testCase.getLocation());
        this.jtextfieldCaseTitleView.setText(testCase.getTestCaseTitle());
        this.jtextfieldTestMethodCaseView.setText(testCase.getTestMethodIadt());
        this.jtextfieldWritingStatusCaseView.setText(testCase.getWritingStatus());
        this.jtextfieldWriterCaseView.setText(testCase.getWritter());
        this.jtextfieldWriterEmailCaseView.setText(testCase.getWritterEmail());
        this.jtextfieldTestEnvironementCaseView.setText(testCase.getEnvironment());
        this.jtextfieldCreationDateCaseView.setText(testCase.getCreationDate());
        this.jtextfieldInternalVersionCaseView.setText(String.valueOf(testCase.getCaseInternalVersion()));
        this.jtextfieldNumberStepCaseView.setText(String.valueOf(testCase.getTotalSteps()));
        this.jtextareaCaseObjectivesView.setText(testCase.getTestObjective());
        this.jtextareaInternalCommentsCaseView.setText(testCase.getInternalComments());
        this.checkBoxBlockingCaseView.setSelected(testCase.getBlocking() != 0);
        this.jtextfieldCaseSourceView.setText(testCase.getTestCaseSource());
        this.jtextfieldEditionDateCaseView.setText(String.valueOf(testCase.getEditionDate()).equals("null") ? "" : testCase.getEditionDate());

        /**
         * Query the D and get all the test step and script of this particular
         * test case.
         */
        //Need to create a view mode for the table test step;
        //displayTestStepToTreeTable(testCaseHandler.getAllFromCase(testCase.getIdtestCase()));
        //System.out.println("Get All from case");
        testCaseHandler.getAllFromCase(testCase);
        controllerTableStep.displayScriptAndStepView(testCase);
        HashSet<Requirement> requirementsCase = new HashSet<>();
        for (int i = 0; i < this.controllerTableStep.getCollectionTestStep().size(); i++) {
            requirementsCase.addAll(this.controllerTableStep.getCollectionTestStep().get(i).getRequirements());
        }
        this.listViewRequirementsCaseView.setItems(FXCollections.observableArrayList(requirementsCase));
    }
    //    /**
    //     * Display the script and step encapuslet in the arraylist.
    //     * @param displayTestStep 
    //     */
    //      private void displayTestStepToTreeTable(ArrayList<TestStepAndScripts> displayTestStep) {
    //
    //        int i = 0;
    //        while (i < displayTestStep.size()) {
    //            testCaseTreeTableViewObject testStep = new testCaseTreeTableViewObject(true);
    //            testStep.setStimuli(displayTestStep.get(i).getTestStep().getHumanStimuli());
    //            testStep.setCheck(displayTestStep.get(i).getTestStep().getHumanCheck());
    //            TreeItem step = new TreeItem<>(testStep);
    //            for (int j = 0; j < displayTestStep.get(i).getScriptHasScriptID().size(); j++) {
    //                TestStepHasScriptId currentScript = displayTestStep.get(i).getScriptHasScriptID().get(j);
    //                testCaseTreeTableViewObject testScript = new testCaseTreeTableViewObject(false);
    //                if (j == displayTestStep.get(i).getScriptHasScriptID().size() - 1) {
    //                    if(currentScript.isStimuli() == true){
    //                         testScript.setStimuli(currentScript.getName());
    //                    }else{
    //                         testScript.setCheck(currentScript.getName());
    //                    }
    //                   
    //                } else {
    //                    if (currentScript.isStimuli() == true && displayTestStep.get(i).getScriptHasScriptID().get(j + 1).isStimuli() == false) {
    //                        testScript.setStimuli(currentScript.getName());
    //                        testScript.setCheck(displayTestStep.get(i).getScriptHasScriptID().get(j + 1).getName());
    //                       j++;
    //                    } else {
    //                        testScript.setCheck(currentScript.getName());
    //                       
    //                    }
    //                }
    //
    //                TreeItem script = new TreeItem<>(testScript);
    //                step.getChildren().add(script);
    //            }
    //            i++;
    //            this.root.getChildren().add(step);
    //        }
    //
    //    }

    private void constructTableStep() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneCaseView.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream()), 0, 1, 1, 5);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerTableStep = fxmlLoader.getController();

        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/stepcreation/headerTableStep.fxml").openStream());
            this.gridPaneCaseView.add(paneTest, 0, 0, 1, 1);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        controllerHeaderTableStep = fxmlLoader2.getController();
        controllerHeaderTableStep.init(controllerTableStep);
        controllerTableStep.setControllerHeader(controllerHeaderTableStep);
    }

}
