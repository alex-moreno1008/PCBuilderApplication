package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;

public class HomeController {

    @FXML
    private void goToCatalog() {
        SceneManager.getInstance().navigateTo(SceneType.CATALOG);
    }

    @FXML
    private void goToSaved() {
        SceneManager.getInstance().navigateTo(SceneType.SAVED_BUILDS);
    }

    @FXML
    private void goToLogin() {
        SceneManager.getInstance().navigateTo(SceneType.LOGIN);
    }
}