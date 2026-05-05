package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.pcbuilderapplication.DatabaseManager;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import org.example.pcbuilderapplication.models.UserService;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label feedbackLabel;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText();

        DatabaseManager db = new DatabaseManager();
        if(db.userExists(user, pass)) {
            UserService.getInstance().setLoggedInUserId(db.getUserId(user, pass));
            SceneManager.getInstance().navigateTo(SceneType.HOME);
        }else{
            feedbackLabel.setText("Invalid username or password");
            feedbackLabel.setStyle("-fx-text-fill: red");

        }
    }

    @FXML
    private void handleGoToSignup() {
        SceneManager.getInstance().navigateTo(SceneType.SIGNUP);
    }
}