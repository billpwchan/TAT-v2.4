package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DB.User;
import DB.Iterations;
import DBcontroller.IterationDB;
import DBcontroller.UserDB;
import DBcontroller.sessionFactorySingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.SessionFactory;
import controller.TATFrameController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import model.Classe;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import model.HMI;
import model.Position;
import model.Properties;
import model.Properties.Type;
import model.StateClasse;
import model.equipment;
import model.WriteReport;
import org.controlsfx.dialog.LoginDialog;

/**
 *
 * @author tmartinez
 */
public class Main extends Application {
    public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public static Scene scene;
    public static Stage primaryStage;

    public TATFrameController tatFrameController;

    public static String pathForFunctional;

    public static boolean isSet = false;

    public static User currentUser = null;

    public boolean tryAgain = true;

    public static ObservableList<Classe> classFound = FXCollections.observableArrayList();

    public static ArrayList<HMI> HMIs = new ArrayList<>();

    ;//=  

    @Override
    public void start(Stage primaryStage) {
        boolean askUser = false;
        if (!askUser) {
            User tom = new User();
            tom.setName("");
            tom.setEmail("");
            tom.setRight("Admin");
            currentUser = tom;
        }
        //System.out.println(System.getProperty("java.version"));
        UserDB userHandler = new UserDB();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        LoginDialog dlg = new LoginDialog(null, null);
        while (currentUser == null && tryAgain == true && askUser == true) {
            dlg.showAndWait().ifPresent(result -> {

                System.out.println("Result is " + result.getKey());
                System.out.println("RESULT IS " + result.getValue());
                currentUser = userHandler.getUser(result.getKey(), result.getValue());
            });
            System.out.println("CURRENT USER = " + currentUser);
            if (currentUser == null) {
                Alert dlg2 = new Alert(AlertType.CONFIRMATION, "");
                dlg2.setTitle("Wrong username or password");
                dlg2.getDialogPane().setContentText("Your username of password is wrong, do you want to try again or quit");
                dlg2.showAndWait().ifPresent(result -> {
                    if (result.getText().equals("Cancel")) {
                        tryAgain = false;
                    }
                });
                //System.out.println("CURRENT USER = " + currentUser);
            }
        }

        if (currentUser != null) {
            System.out.println("AVANT");

            System.out.println("After");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("/view/TATFrame.fxml").openStream());
                tatFrameController = (TATFrameController) fxmlLoader.getController();
                scene = new Scene(root);
                primaryStage.setScene(scene);
                //getMainString(scene);
                primaryStage.show();
                primaryStage.setTitle("Test Automation Tool");
                primaryStage.getIcons().add(new Image("/images/images.png"));

                Main.primaryStage = primaryStage;
                tatFrameController.setPrimaryStage(Main.primaryStage);
                
//              Iterations it = new Iterations();
//              Iterations it2 = new Iterations();
//              IterationDB itDB = new IterationDB();
//              //it = itDB.getIterationFromBaselineID("1166B Demo");
//              it2 = itDB.getIterationFromID(278);
//              System.out.println(it2.getIditerations());
//              System.out.println(it2.getBaselineId());
//              System.out.println(it2.getIterationResult());
//              WriteReport newReport = new WriteReport();
//              newReport.createReport(it2);
                

                
                
                

                primaryStage.setOnCloseRequest((WindowEvent event) -> {
                    SessionFactory factory = sessionFactorySingleton.getInstance();
        
                    factory.close();
                    //System.exit(0);
                    //executor.shutdown();
                });
              //SessionFactory fac = sessionFactorySingleton.getInstance();

              
              
              //fac.close();
              
                
                
                
                
//            Main.primaryStage.setX(primaryScreenBounds.getMinX());
//            Main.primaryStage.setY(primaryScreenBounds.getMinY());
//            Main.primaryStage.setWidth(primaryScreenBounds.getWidth());
//            Main.primaryStage.setHeight(primaryScreenBounds.getHeight());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //set Stage boundaries to visible bounds of the main screen
        //displayFunctionalPath();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
        
        //
        Iterations iterationT = new Iterations();
    }

    public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public void getMainString(Scene scene) {

        Image image = new Image("/images/valid.png");  //pass in the image path
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_ANY, KeyCombination.CONTROL_ANY), (Runnable) () -> {
            //condition here of you that want you want to achive.
            scene.setCursor(new ImageCursor(image));
            isSet = true;
        });
    }

}
