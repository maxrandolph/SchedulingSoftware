/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author max.randolph
 */
public class LoginController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    @FXML
    private void handleBtnLoginAction(ActionEvent event) throws SQLException, IOException {
        boolean validCreds = false;
        try {
            // Inits and opens the connection
            Connection connection = SchedulingSoftware.conManager.open();

            PreparedStatement statement = connection.prepareStatement("select userId from user where userName=? and password=?");
            statement.setString(1, txtUsername.getText());
            statement.setString(2, txtPassword.getText());
            System.out.println(statement);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                validCreds = true;
                //SchedulingSoftware.currentUserId = 
            }
            if (validCreds) {
                System.out.println("Logged in!");
                SchedulingSoftware.ChangeScene("MainView.fxml", btnLogin);
                SchedulingSoftware.currentUserName = txtUsername.getText();
            } else {
                System.out.println("Failed login.");
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception thrown: " + ex);
        }
    }

    @FXML
    private void HandleBtnExitAction(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
