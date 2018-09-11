/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DB.TestCampaign;
import DB.TestCase;
import DBcontroller.sessionFactorySingleton;
import configuration.settings;
import controller.macro.TabMacroMainViewController;
import controller.popup.PopUpConfigurationController;
import controller.requirements.TabRequirementMainViewController;
import controller.scripts.TabScriptsMainViewController;
import controller.tabtestcampaign.TabTestCampaignMainViewController;
import controller.tabtestcase.TabTestCaseMainViewController;
import controller.tabtestexecution.TabTestCampaignExecutionMainViewController;
import controller.util.CommonFunctions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import model.*;
import model.Properties;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.Main.HMIs;

/**
 * FXML Controller class
 *
 * @author tmartinez
 */
public class TATFrameController implements Initializable {

    private int globalSize = 40;
    private boolean DisplayTabLabel = false;
    private String oldCss = "customTabReq";
    @FXML
    private TabPane tabPaneHMI;
    @FXML
    private Label labelUserName;
    @FXML
    private Label labelUserRight;
    @FXML
    private Label labelRight;
    @FXML
    private Label labelName;
    @FXML
    private Label labelTime;
    @FXML
    private MenuItem itemSettings;
    @FXML
    private MenuItem itemHMIs;
    @FXML
    private MenuItem Load;
    private Tab tabViewCase;
    private Tab tabViewCampaign;
    private Tab tabViewExecution;
    private Tab tabRequirement;
    private Tab tabMacro;
    private Tab tabScripts;
    private TabTestCaseMainViewController mainControllerCase;
    private TabTestCampaignMainViewController mainControllerCampaign;
    private TabTestCampaignExecutionMainViewController mainControllerCampaignExecution;
    private TabRequirementMainViewController mainControllerRequirement;
    private TabMacroMainViewController mainControllerMacro;
    private TabScriptsMainViewController mainControllerScript;
    private Stage primaryStage;
    private Stage popUpSettingsStage;
    private settings setting = new settings();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabPaneHMI.getStylesheets().add("view/CustomeStyle.css");
        setting.readSettings();

        try {
            if (DisplayTabLabel) {
                this.tabRequirement = new Tab("Requirement");
            } else {
                this.tabRequirement = new Tab();
            }
            this.tabRequirement.setId("customTabReq");
            this.tabRequirement.getStyleClass().add("customTabReq");
            FXMLLoader fxmlLoaderRequirement = new FXMLLoader();
            AnchorPane RequirementPane = fxmlLoaderRequirement.load(getClass().getResource("/view/requirements/TabRequirementMainView.fxml").openStream());
            this.tabRequirement.setContent(RequirementPane);
            mainControllerRequirement = fxmlLoaderRequirement.getController();
            mainControllerRequirement.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        try {
            if (DisplayTabLabel) {
                this.tabScripts = new Tab("Script");
            } else {
                this.tabScripts = new Tab();
            }
            this.tabScripts.setId("customTabScript");
            FXMLLoader fxmlLoaderScript = new FXMLLoader();
            AnchorPane scriptPane = fxmlLoaderScript.load(getClass().getResource("/view/scriptmanagement/TabScriptsMainView.fxml").openStream());
            this.tabScripts.setContent(scriptPane);
            mainControllerScript = fxmlLoaderScript.getController();
            mainControllerScript.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        try {
            if (DisplayTabLabel) {
                this.tabMacro = new Tab("Macro");
            } else {
                this.tabMacro = new Tab();
            }
            this.tabMacro.setId("customTabMacro");
            FXMLLoader fxmlLoaderMacro = new FXMLLoader();
            AnchorPane macroPane = fxmlLoaderMacro.load(getClass().getResource("/view/macro/TabMacroMainView.fxml").openStream());
            this.tabMacro.setContent(macroPane);
            mainControllerMacro = fxmlLoaderMacro.getController();
            mainControllerMacro.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        try {
            if (DisplayTabLabel) {
                this.tabViewCase = new Tab("Test Case");
            } else {
                this.tabViewCase = new Tab();
            }
            this.tabViewCase.setId("customTabCase");
            FXMLLoader fxmlLoaderTestCase = new FXMLLoader();
            AnchorPane TestCasePane = fxmlLoaderTestCase.load(getClass().getResource("/view/testcase/TabTestCaseMainView.fxml").openStream());
            this.tabViewCase.setContent(TestCasePane);
            mainControllerCase = fxmlLoaderTestCase.getController();
            mainControllerCase.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        try {
            if (DisplayTabLabel) {
                this.tabViewCampaign = new Tab("Test Campaign");
            } else {
                this.tabViewCampaign = new Tab();
            }
            this.tabViewCampaign.setId("customTabCampaign");
            FXMLLoader fxmlLoader2 = new FXMLLoader();
            AnchorPane TestCampaignPane = fxmlLoader2.load(getClass().getResource("/view/testcampaign/TabTestCampaignMainView.fxml").openStream());
            this.tabViewCampaign.setContent(TestCampaignPane);
            mainControllerCampaign = fxmlLoader2.getController();
            mainControllerCampaign.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        try {
            if (DisplayTabLabel) {
                this.tabViewExecution = new Tab("Test execution");
            } else {
                this.tabViewExecution = new Tab();
            }
            this.tabViewExecution.setId("customTabExec");
            FXMLLoader fxmlLoaderExecution = new FXMLLoader();
            AnchorPane TestCampaignExecutionPane = fxmlLoaderExecution.load(getClass().getResource("/view/testexecution/TabTestCampaignExecutionMainView.fxml").openStream());
            this.tabViewExecution.setContent(TestCampaignExecutionPane);
            mainControllerCampaignExecution = fxmlLoaderExecution.getController();
            mainControllerCampaignExecution.init(this);
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }

        this.tabImgAndStyleInit();

        this.tabPaneHMI.getTabs().addAll(this.tabRequirement, this.tabScripts, this.tabMacro, this.tabViewCase, this.tabViewCampaign, this.tabViewExecution);
        this.tabPaneHMI.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    oldValue.getStyleClass().remove(oldCss);
                    newValue.getStyleClass().add(newValue.getId());
                    oldCss = newValue.getId();
                    oldValue.getStyleClass().add("customTabNotSelected");
                }
        );

        labelName.setText(Main.currentUser.getName());
        labelName.setStyle("-fx-text-fill: slateblue;");
        labelRight.setText(Main.currentUser.getRight());
        labelRight.setStyle("-fx-text-fill: slateblue;");
        labelTime.setStyle("-fx-text-fill: slateblue;");

        final DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            final Calendar cal = Calendar.getInstance();
            labelTime.setText(format.format(cal.getTime()));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        itemSettings.setOnAction((ActionEvent t) -> displaySettings());

        //MenuItem loadHMIs = new MenuItem("Load HMIs");
        itemHMIs.setOnAction((ActionEvent t) -> displayFunctionalPath());

        Load.setOnAction((ActionEvent t) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose your database");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                sessionFactorySingleton.newInstance(file.getAbsolutePath());
                try {
                    mainControllerRequirement.updateRepository();
                    mainControllerScript.updateRepository();
                    mainControllerMacro.updateRepository();
                    mainControllerCase.updateLibrary();
                    mainControllerCampaign.updateRepository();
                    mainControllerCampaignExecution.updateBaselineTree();
                } catch (ParseException ex) {
                    CommonFunctions.debugLog.error("", ex);
                }

            }
        });
    }

    /**
     * @param testCase
     */
    public void callViewToTestCase(TestCase testCase) {
        this.tabPaneHMI.getSelectionModel().select(this.tabViewCase);
        this.mainControllerCase.displayViewTab(testCase);
    }

    /**
     * @param testCampaign
     */
    public void callViewToTestCampaign(TestCampaign testCampaign) {
        this.tabPaneHMI.getSelectionModel().select(this.tabViewCampaign);
        this.mainControllerCampaign.displayViewTab(testCampaign);
    }

//    public void manageBaselineButton(int size){
//        this.mainControllerCampaignExecution.buttonBaselineManagement(size);
//    }

    /**
     * @return
     */
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    /**
     * @param primaryStage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Display the view for script configuration when a parameters is clicked.
     *
     */
    private void displaySettings() {

        AnchorPane popUpSettings = new AnchorPane();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/popup/popUpConfiguration.fxml"));
            PopUpConfigurationController controller = new PopUpConfigurationController();
            fxmlLoader.setController(controller);
            popUpSettings = fxmlLoader.load();

            controller.init(this);
            controller.constructInformation();
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
        popUpSettingsStage = new Stage();
        popUpSettingsStage.setTitle("TAT Settings");
        popUpSettingsStage.initModality(Modality.WINDOW_MODAL);
//        popUpStage.initOwner(main.getMainController().getPrimaryStage());
        popUpSettingsStage.setScene(new Scene(popUpSettings));

        popUpSettingsStage.setOnCloseRequest(event -> {
            setting.readSettings();
            popUpSettingsStage.close();

        });
        popUpSettingsStage.showAndWait();

    }

    /**
     *
     */
    public void setOnValidParams() {
        setting.saveSettings();
        popUpSettingsStage.close();
    }

    /**
     *
     */
    public void focusOnSettings() {
        popUpSettingsStage.requestFocus();
    }

    private void tabImgAndStyleInit() {

        //Get Img and create imageView for each icon.
        Image imgReq = new Image(getClass().getResourceAsStream("/images/requirement2.png"), globalSize, globalSize, true, true);
        ImageView ivReq = new ImageView(imgReq);
        Image imgScript = new Image(getClass().getResourceAsStream("/images/script_1.png"), globalSize, globalSize, true, true);
        ImageView ivScript = new ImageView(imgScript);
        Image imgMacro = new Image(getClass().getResourceAsStream("/images/macro.png"), globalSize, globalSize, true, true);
        ImageView ivMacro = new ImageView(imgMacro);
        Image imgCase = new Image(getClass().getResourceAsStream("/images/case2.png"), globalSize, globalSize, true, true);
        ImageView ivCase = new ImageView(imgCase);
        Image imgCampaign = new Image(getClass().getResourceAsStream("/images/campaign2.png"), globalSize, globalSize, true, true);
        ImageView ivCampaign = new ImageView(imgCampaign);
        Image imgExec = new Image(getClass().getResourceAsStream("/images/execution2.png"), globalSize, globalSize, true, true);
        ImageView ivExec = new ImageView(imgExec);

        // Set the rigth icon to each tab.
        this.tabRequirement.setGraphic(ivReq);
        this.tabScripts.setGraphic(ivScript);
        this.tabMacro.setGraphic(ivMacro);
        this.tabViewCase.setGraphic(ivCase);
        this.tabViewCampaign.setGraphic(ivCampaign);
        this.tabViewExecution.setGraphic(ivExec);

        //Set the custom style for all tabs 
        this.tabRequirement.getStyleClass().add("customTab");
        this.tabScripts.getStyleClass().add("customTab");
        this.tabMacro.getStyleClass().add("customTab");
        this.tabViewCase.getStyleClass().add("customTab");
        this.tabViewCampaign.getStyleClass().add("customTab");
        this.tabViewExecution.getStyleClass().add("customTab");

        //Set the style not selected for all the tabs, except for requirements tab
        this.tabScripts.getStyleClass().add("customTabNotSelected");
        this.tabMacro.getStyleClass().add("customTabNotSelected");
        this.tabViewCase.getStyleClass().add("customTabNotSelected");
        this.tabViewCampaign.getStyleClass().add("customTabNotSelected");
        this.tabViewExecution.getStyleClass().add("customTabNotSelected");

    }

    private void displayFunctionalPath() {
        Optional<ButtonType> result = CommonFunctions.displayAlert(Alert.AlertType.CONFIRMATION, "HMI folder selection ", "Please, select the folder with the HMIs", "");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("This is my file");
            //Show open file dialog
            File file = directoryChooser.showDialog(null);
            if (file != null) {
                File[] directories = new File(file.getPath()).listFiles((File current, String name) -> new File(current, name).isDirectory());
                System.out.println(Arrays.toString(directories));
                for (File s : directories) {
                    System.out.println("Name folder : " + s.getName());
                    HMI hmi = new HMI(s.getName(), s.toPath());
                    hmi.setAllClasse(preparesClasses(s));
                    HMIs.add(hmi);
                }
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private ArrayList<Classe> preparesClasses(File classPath) {
        ArrayList<Classe> classes = new ArrayList<>();
        File[] directories = new File(classPath.getPath() + "\\Classes").listFiles((File current, String name) -> new File(current, name).isDirectory());
        System.out.println(Arrays.toString(directories));
        for (File s : Objects.requireNonNull(directories)) {
            System.out.println("Name folder : " + s.getName());
            Classe classe = new Classe(s.getName());
            sortEquipmentOfClasse(s, classe);
            classes.add(classe);
        }
        return classes;
    }

    private void sortEquipmentOfClasse(File fileInClass, Classe currentClasse) {
        //ArrayList<equipment> arrayEquipment = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileInClass.getAbsolutePath() + "\\Equipment.txt"))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null && !sCurrentLine.isEmpty()) {
                equipment eqp;
                String[] equipmentString = sCurrentLine.split("\\|\\|");
                eqp = new equipment(equipmentString[0]);

                if (equipmentString.length > 1) {
                    String[] positionValue = equipmentString[1].split("@&");
                    for (int i = 0; i < positionValue.length - 1; i = i + 2) {
                        String[] regionValue = positionValue[i + 1].split(",");
                        ScreenRegion sr = new DesktopScreenRegion(
                                Double.valueOf(regionValue[0]).intValue(),
                                Double.valueOf(regionValue[1]).intValue(),
                                Double.valueOf(regionValue[2]).intValue(),
                                Double.valueOf(regionValue[3]).intValue());
                        eqp.setPosition(new Position(sr, Paths.get(positionValue[i])));//getPositions().add();
                    }

                }
                currentClasse.addEquipment(eqp);//.add(eqp);
            }
            File[] files = fileInClass.listFiles();

            for (File file : Objects.requireNonNull(files)) {
                if (file.isFile()) {
                    if (!file.getName().equals("Equipment.txt")) {
                        StateClasse currentState = new StateClasse(file.getName().replace(".txt", ""));
                        currentClasse.addState(currentState);
                        try (BufferedReader br2 = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                            String sCurrentLine2;
                            while ((sCurrentLine2 = br2.readLine()) != null) {
                                if (!sCurrentLine2.isEmpty()) {
                                    String[] propertiesString = sCurrentLine2.split("@&");
                                    currentState.addProperties(new Properties(propertiesString[1], Properties.Type.valueOf(propertiesString[0]), propertiesString[2]));
                                }
                            }
                        } catch (IOException ex) {
                            CommonFunctions.debugLog.error("", ex);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            CommonFunctions.debugLog.error("", ex);
        }
    }

}
