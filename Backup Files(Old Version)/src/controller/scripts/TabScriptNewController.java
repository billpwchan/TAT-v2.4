/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.scripts;

import DB.Parameters;
import DB.Script;
import DB.ScriptHasParameters;
import DBcontroller.sessionFactorySingleton;
import configuration.settings;
import controller.tablestep.ViewScriptController;
import controller.tabtestcase.TabTestCaseNewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import main.Main;
import org.apache.log4j.Logger;
import org.controlsfx.dialog.CommandLinksDialog;
import org.controlsfx.dialog.CommandLinksDialog.CommandLinksButtonType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author T0155041
 */
public class TabScriptNewController implements Initializable {

    @FXML
    private AnchorPane anchorPanelNewScript;
    @FXML
    private GridPane gridPaneScriptNew;
    @FXML
    private Button buttonValid;
    @FXML
    private GridPane gridPaneTableParam;
    @FXML
    private Text labelParamTitle;
    @FXML
    private Button buttonAddParam;
    @FXML
    private GridPane gridPaneLabelScriptNew;
    @FXML
    private Label labelScriptName;
    @FXML
    private Label labelScriptEditionDate;
    @FXML
    private Label labelScriptCreationDate;
    @FXML
    private Label labelScriptVersion;
    @FXML
    private Label labelScriptObjectives;
    @FXML
    private TextField jtextfieldScriptName;
    @FXML
    private TextField jtextfieldScriptNew;
    @FXML
    private TextField jtextfieldScriptCreationDate;
    @FXML
    private TextField jtextfieldTypeScriptEditionDate;
    @FXML
    private TextArea jtextareaObjectivesScript;
    @FXML
    private ScrollPane scrollPanePreview;
    @FXML
    private Label labelScriptEditionDate1;
    @FXML
    private CheckBox stimuliCheckBox;
    @FXML
    private AnchorPane anchorHeader;
    @FXML
    private Text labelPreview;
    @FXML
    private TextField jtextfieldJarName;

    private static TabScriptsMainViewController mainController;

    private ViewScriptController controllerViewScript;

    private TableParamCreationController controllerTableParam;

    private Path scriptPath = null;
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.constructTableParams();
        this.initButtons();
        this.labelPreview.setVisible(false);
        this.scrollPanePreview.setVisible(false);
        //this.loadPreviewScript();
        //this.loadPreview();

        this.labelScriptName.setTextFill(Color.RED);

        this.jtextfieldScriptName.textProperty().addListener((observable, oldValue, newValue) -> {
            this.buttonValid.setDisable(newValue.trim().isEmpty());
            //this.UpdateScriptPreview();
            if (newValue.trim().isEmpty()) {
                this.labelScriptName.setTextFill(Color.RED);
            } else {
                this.labelScriptName.setTextFill(Color.BLACK);
            }
        });

    }

    /**
     *
     * @param mainController
     */
    public void init(TabScriptsMainViewController mainController) {
        TabScriptNewController.mainController = mainController;
        controllerTableParam.initScriptNew(this);

        Alert dlg = new Alert(AlertType.INFORMATION);
        dlg.setTitle("Select script ?");
        dlg.getDialogPane().setContentText("Do you want to select your .Jar and have information prefilled or fill information by yourself ?");
        ButtonType select = new ButtonType("Select jar");
        ButtonType Nselect = new ButtonType("don't select jar");
        dlg.getButtonTypes().remove(ButtonType.OK);
        dlg.getButtonTypes().addAll(select, Nselect);
        dlg.initOwner(Main.primaryStage);
        dlg.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> result = dlg.showAndWait();
        if (result.get() == select) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Script");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Jar", "*.jar"));
            File file = fileChooser.showOpenDialog(Main.primaryStage);
            if (file != null) {
                scriptPath = file.toPath();
                this.scriptCopy();
            } else {

            }
        } else {

        }
    }

    private void constructTableParams() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            this.gridPaneTableParam.add((AnchorPane) fxmlLoader.load(getClass().getResource("/view/scriptmanagement/tableScriptCreation.fxml").openStream()), 0, 1, 1, 5);// this.anchorPaneStepTable.getChildren().setAll((AnchorPane) fxmlLoader.load(getClass().getResource("/view/stepcreation/tableStepScriptCreation.fxml").openStream())) ;

        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class
                    .getName()).error("", ex);
        }
        controllerTableParam = fxmlLoader.getController();
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/scriptmanagement/headerParameters.fxml").openStream());
            this.gridPaneTableParam.add(paneTest, 0, 0, 1, 1);

        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class
                    .getName()).error("", ex);
        }

    }

    /**
     *
     */
    public void initButtons() {

        this.buttonValid.setDisable(true);

        this.buttonAddParam.setOnAction((ActionEvent e) -> {
            controllerTableParam.addParam();
        });

        this.buttonValid.setOnAction((ActionEvent e) -> {
            try {
                this.validScriptCreation();
            } catch (ParseException ex) {
                Logger.getLogger(TabScriptNewController.class.getName()).error("", ex);
            }
        });

    }

    /**
     *
     * @throws ParseException
     */
    public void createScript() throws ParseException {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Script script = constructScript();
        session.save(script);
        for (int i = 0; i < controllerTableParam.getCollectionControllerParam().size(); i++) {
            ScriptHasParameters schp = new ScriptHasParameters();
            schp.setScript(script);
            schp.setParamOrder((byte) i);
            schp.setParameters(controllerTableParam.getCollectionControllerParam().get(i).constructParam());
            session.save(schp);
        }
        session.beginTransaction().commit();
        session.close();
    }

    /**
     *
     * @return @throws ParseException
     */
    public Script constructScript() throws ParseException {
        Script newScript = new Script();
        newScript.setName(jtextfieldScriptName.getText());
        newScript.setCreationDate(df.format(new Date()));
        newScript.setDesciption(jtextareaObjectivesScript.getText());
        newScript.setIsStimuli((byte) (stimuliCheckBox.isSelected() ? 1 : 0));
        newScript.setCallName(this.jtextfieldJarName.getText());
        newScript.setIsMacro((byte) 0);
        newScript.setScriptVersion(1);
        return newScript;
    }

    /**
     *
     */
    public void initNewScript() {
        this.loadCSS();
        this.jtextfieldScriptCreationDate.setText(String.valueOf(df.format(new Date())));
        this.jtextfieldScriptCreationDate.setId("displayStyle");
        this.jtextfieldTypeScriptEditionDate.setId("displayStyle");
        this.jtextfieldScriptNew.setText("1");
        this.jtextfieldScriptNew.setId("displayStyle");

    }

    /**
     * Add css sheet to the anchorPane of the view
     */
    private void loadCSS() {
        this.anchorPanelNewScript.getStylesheets().add("/view/testexecution/cssLibraryTestCase.css");
    }

    private int verifyScriptExist() {
        int result = -1;
        try {
            Path dest = Paths.get(settings.scriptsPaht + "\\" + scriptPath.getFileName());
            File destFile = new File(settings.scriptsPaht + "\\" + scriptPath.getFileName());
            if (!destFile.exists()) {
                Files.copy(scriptPath, dest);
                result = 2;
            } else {
                List<CommandLinksButtonType> links = Arrays
                        .asList(new CommandLinksButtonType(
                                        "   Replace the existing jar ",
                                        "   The Script jar already available will be deleted", false),
                                new CommandLinksButtonType(
                                        "   Don't copy",
                                        "   Both scripts jar are let at their original location.", false));

                CommandLinksDialog dlg = new CommandLinksDialog(links);
                dlg.setTitle("Warning");
                String optionalMasthead = "A Jar script with the same name already exists";
                dlg.getDialogPane().setContentText("What do you want to do ?");
                dlg.getDialogPane().setHeaderText(optionalMasthead);

                dlg.showAndWait();
                if (dlg.getResult() == links.get(0).getButtonType()) {
                    result = 0;
                } else if (dlg.getResult() == links.get(1).getButtonType()) {
                    result = 1;
                }
                //System.out.println(dlg.getResult() == links.get(0).getButtonType());

            }
        } catch (IOException ex) {
            Logger.getLogger(TabScriptNewController.class
                    .getName()).error("", ex);
        }
        return result;
    }

    private ArrayList<Parameters> getParams() {
        ArrayList<Parameters> params = new ArrayList<>();
        try {
            File root = new File(settings.scriptsPaht + "\\" + scriptPath.getFileName());
            Class<?> cls = null;
            if (root.exists()) {
                try {
                    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                    cls = Class.forName(scriptPath.getFileName().toString().replace(".jar", ""), true, classLoader);

                } catch (ClassNotFoundException | MalformedURLException ex) {
                    Logger.getLogger(TabScriptNewController.class
                            .getName()).error("", ex);
                }
            }

            final Constructor<?> constructor = cls.getConstructor();
            final Object o = constructor.newInstance();
            java.lang.reflect.Method method;
            method = o.getClass().getDeclaredMethod("parameters");
            params = (ArrayList<Parameters>) method.invoke(o);

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(TabScriptNewController.class
                    .getName()).error("", ex);
        }
        return params;
    }

    private Script getScriptInfos() {
        Script script = new Script();
        try {
            File root = new File(settings.scriptsPaht + "\\" + scriptPath.getFileName());
            Class<?> cls = null;
            if (root.exists()) {
                try {
                    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                    cls = Class.forName(scriptPath.getFileName().toString().replace(".jar", ""), true, classLoader);

                } catch (ClassNotFoundException | MalformedURLException ex) {
                    Logger.getLogger(TabScriptNewController.class
                            .getName()).error("", ex);
                }
            }

            final Constructor<?> constructor = cls.getConstructor();
            final Object o = constructor.newInstance();
            java.lang.reflect.Method method;
            method = o.getClass().getDeclaredMethod("scriptInfos");
            script = (Script) method.invoke(o);
            script.setCallName(scriptPath.getFileName().toString());

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(TabScriptNewController.class
                    .getName()).error("", ex);
        }
        return script;
    }

    private void validScriptCreation() throws ParseException {
        this.createScript();
        mainController.closeTab();
        mainController.updateRepository();
        mainController.focusLibrary();
    }

    /**
     *
     * @param param
     */
    public void prefillParam(ArrayList<Parameters> param) {
        controllerTableParam.PrefilParams(param);
    }

    /**
     *
     */
    public void scriptCopy() {
        ArrayList<Parameters> params = new ArrayList<>();
        try {
            int scriptExist = verifyScriptExist();
            switch (scriptExist) {
                case -1:
                    break;
                case 0:
                    File toDelete = new File(settings.scriptsPaht + "\\" + scriptPath.getFileName());
                    toDelete.delete();
                    Path dest = Paths.get(settings.scriptsPaht + "\\" + scriptPath.getFileName());
                    Thread.sleep(500);
                    try {
                        Files.copy(scriptPath, dest);

                    } catch (IOException ex) {
                        Logger.getLogger(TabScriptNewController.class
                                .getName()).error("", ex);
                    }
                    scriptInfo(this.getScriptInfos());
                    params = getParams();
                    this.prefillParam(params);
                    break;
                case 1:
                    break;
                case 2:
                    scriptInfo(this.getScriptInfos());
                    params = getParams();
                    this.prefillParam(params);
                    break;

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TabScriptNewController.class
                    .getName()).error("", ex);
        }
    }

    private void scriptInfo(Script script) {
        this.jtextfieldScriptName.setText(script.getName());
        this.jtextareaObjectivesScript.setText(script.getDesciption());
        this.stimuliCheckBox.setSelected(script.getIsStimuli() != 0);
        this.jtextfieldJarName.setText(script.getCallName().replace(".jar", ""));
    }

    private void loadPreview() {
        //System.out.println("HERE header load");
        //        System.out.println("SCROLLPANE = " + scrollPanePreview);
        //        System.out.println("CONTROLLER MACRO = " + controllerPreviewMacro);
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        try {
            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/stepcreation/viewScript.fxml").openStream());
            this.scrollPanePreview.setContent(paneTest);

            controllerViewScript = (ViewScriptController) fxmlLoader2.getController();

            //this.gridPaneLabelScriptNew.add(paneTest, 5, 0, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
        }

        try {
            //this.controllerPreviewMacro.initialize(scrollPanePreview);
        } catch (Exception e) {
            //System.out.println("exception = " + e);
        }
    }

//    private void viewScriptPreview(Script script) {
//        TestStepHasScript tshs = new TestStepHasScript();
//        tshs.setScript(script);
//        controllerViewScript.constructGridPanePreview(tshs);
//    }
//    public void UpdateScriptPreview() {
//        Script script = constructScript();
//        for (int i = 0; i < controllerTableParam.getCollectionControllerParam().size(); i++) {
//            ScriptHasParameters schp = new ScriptHasParameters();
//            schp.setScript(script);
//            script.getScriptHasParameterses().add(schp);
//            schp.setParameters(controllerTableParam.getCollectionControllerParam().get(i).constructParam());
//        }
//        viewScriptPreview(script);
//    }
//    private void loadPreviewScript() {
//        //System.out.println("HERE header load");
//        //        System.out.println("SCROLLPANE = " + scrollPanePreview);
//        //        System.out.println("CONTROLLER MACRO = " + controllerPreviewMacro);
//        FXMLLoader fxmlLoader2 = new FXMLLoader();
//        try {
//            AnchorPane paneTest = (AnchorPane) fxmlLoader2.load(getClass().getResource("/view/macroActions/headerPreviewMacro.fxml").openStream());
//            AnchorPane.setTopAnchor(paneTest, 0.0);
//            AnchorPane.setRightAnchor(paneTest, 0.0);
//            AnchorPane.setLeftAnchor(paneTest, 0.0);
//            AnchorPane.setBottomAnchor(paneTest, 0.0);
//            this.anchorHeader.getChildren().add(paneTest);
//
//            //this.gridPaneLabelScriptNew.add(paneTest, 5, 0, 1, 1);
//        } catch (IOException ex) {
//            Logger.getLogger(TabTestCaseNewController.class.getName()).error("", ex);
//        }
//
//        try {
//            //this.controllerPreviewMacro.initialize(scrollPanePreview);
//        } catch (Exception e) {
//            System.out.println("exception = " + e);
//        }
//    }
}
