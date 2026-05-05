package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import org.example.pcbuilderapplication.models.UserService;
import java.util.List;
import javafx.scene.control.ListView;
import org.example.pcbuilderapplication.DatabaseManager;

public class SavedBuildController {
    @FXML private ListView<String> savedBuildsList;

    @FXML
    public void initialize() {
        int userId = UserService.getInstance().getLoggedInUserId();
        DatabaseManager db = new DatabaseManager();
        List<String> builds = db.getSavedBuildSummaries(userId);

        if (builds.isEmpty()) {
            savedBuildsList.getItems().add("No saved builds yet.");
        } else {
            savedBuildsList.getItems().addAll(builds);
        }
    }

    @FXML
    private void goHome() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }
}