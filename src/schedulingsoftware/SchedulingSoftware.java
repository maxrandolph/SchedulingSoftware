/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import schedulingsoftware.Entities.User;

/**
 *
 * @author max.randolph
 */
public class SchedulingSoftware extends Application {

    public static ConnectionManager conManager;
    // JavaFX stuff
    public static Stage stage;
    public static Parent root;
    public static int currentUserId;
    public static String currentUserName;

    @Override
    public void start(Stage stage) throws Exception {

        InitConnectionInfo();
        // Database connection context
        conManager = new ConnectionManager();

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void InitConnectionInfo() throws IOException {
        Properties prop = new Properties();
        String fileName = "config.properties";
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileName);
            prop.load(inputStream);

        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + ex);
        }
    }

    @FXML
    public static void ChangeScene(String fxmlFile, Button buttonClicked) throws IOException {
        stage = (Stage) buttonClicked.getScene().getWindow();
        root = FXMLLoader.load(SchedulingSoftware.class.getResource(fxmlFile));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
