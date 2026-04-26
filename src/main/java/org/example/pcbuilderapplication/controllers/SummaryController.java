package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;

public class SummaryController {

    @FXML
    private void goToCatalog() {
        SceneManager.getInstance().navigateTo(SceneType.CATALOG);
    }

    @FXML
    private void goHome() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }
}