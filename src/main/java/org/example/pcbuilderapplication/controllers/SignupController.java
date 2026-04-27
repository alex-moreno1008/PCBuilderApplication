package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import org.example.pcbuilderapplication.models.UserService;
import org.example.pcbuilderapplication.models.UserService.SignupResult;


public class SignupController {

  @FXML
  private TextField userField;

  @FXML
  private PasswordField passField;

  @FXML
  private PasswordField confirmPassField;

  @FXML
  private Label feedbackLabel;

  private final UserService userServ = UserService.getInstance();

  @FXML
  private void handleSignUp() {
    String user = userField.getText().trim();
    String pass = passField.getText();
    String confirm = confirmPassField.getText();

    if (!pass.equals(confirm)) {
      setFeedback("Passwords do not match.", true);
      return;
    }

    if (userServ.accountExists(user, pass)) {
      setFeedback("Account already exists. Please log in.", true);
      return;
    }

    SignupResult result = userServ.register(user, pass);

    switch (result) {
      case SUCCESS -> SceneManager.getInstance().navigateTo(SceneType.HOME);
      case USERNAME_TAKEN -> setFeedback("That username is already takem.", true);
      case INVALID_USER -> setFeedback("Please enter a valid username.", true);
      case WEAK_PASS -> setFeedback("Password must be at least 6 characters.", true);
      case DATABASE_ERROR -> setFeedback("Something went wrong. Please retry.", true);

    }
  }

  @FXML
  private void handleGoToLogin(){
    SceneManager.getInstance().navigateTo(SceneType.LOGIN);
  }

  private void setFeedback(String message, boolean isError) {
    feedbackLabel.setText(message);
    feedbackLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #2ecc71;");

  }


}
