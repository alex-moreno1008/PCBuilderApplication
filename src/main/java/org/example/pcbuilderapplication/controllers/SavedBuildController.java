package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;

public class SavedBuildController {

    @FXML
    private void goHome() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }
}