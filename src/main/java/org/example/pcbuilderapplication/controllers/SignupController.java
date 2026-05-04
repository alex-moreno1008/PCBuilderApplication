package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import org.example.pcbuilderapplication.models.UserService;
import org.example.pcbuilderapplication.models.UserService.SignupResult;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


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
      case SUCCESS -> {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
        showNotifToast("You successfully created an account!");
      }
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

  private void showNotifToast(String notif){
    Stage toastStage = new Stage();
    toastStage.initOwner(SceneManager.getInstance().getStage());
    toastStage.setResizable(false);
    toastStage.initStyle(StageStyle.TRANSPARENT);

    Label label = new Label(notif);
    label.setStyle("-fx-background-color: #00e5ff;" +
                     "-fx-text-fill: #0a0c10;" +
                   "-fx-font-family: 'Courier New';" +
                   "-fx-font-size: 12px;" +
                   "-fx-font-weight: bold;" +
                   "-fx-padding: 12 20 12 20;" +
                   "-fx-background-radius: 4;");

    StackPane r = new StackPane(label);
    r.setStyle("-fx-background-color: transparent;");

    Scene scene = new Scene(r);
    scene.setFill(Color.TRANSPARENT);
    toastStage.setScene(scene);
    toastStage.show();

    Stage owner = SceneManager.getInstance().getStage();
    toastStage.setX(owner.getX()+owner.getWidth()-toastStage.getWidth()-20);
    toastStage.setY(owner.getY()+owner.getHeight()-toastStage.getHeight()-40);

    PauseTransition delay = new PauseTransition(Duration.seconds(3));
    delay.setOnFinished(e -> toastStage.close());
    delay.play();
  }




}
