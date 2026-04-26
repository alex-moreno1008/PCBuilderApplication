package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }
}