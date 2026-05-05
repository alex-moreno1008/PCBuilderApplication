package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import org.example.pcbuilderapplication.models.BuildSelection;
import org.example.pcbuilderapplication.models.UserService;
import java.util.List;
import java.util.Map;

import javafx.scene.control.ListView;
import org.example.pcbuilderapplication.DatabaseManager;

public class SavedBuildController {
    @FXML private ListView<String> savedBuildsList;

    private Map<Integer, String> buildsMap;

    @FXML
    public void initialize() {
        loadBuilds();
        savedBuildsList.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE, BACK_SPACE -> deleteSelected();
            }
        });
    }

    private void loadBuilds() {
        savedBuildsList.getItems().clear();
        int userId = UserService.getInstance().getLoggedInUserId();
        DatabaseManager db = new DatabaseManager();
        buildsMap = db.getSavedBuildSummaries(userId);

        if (buildsMap.isEmpty()) {
            savedBuildsList.getItems().add("No saved builds yet.");
        } else {
            savedBuildsList.getItems().addAll(buildsMap.values());
        }
    }

    @FXML
    private void deleteSelected() {
        int index = savedBuildsList.getSelectionModel().getSelectedIndex();
        if (index < 0 || buildsMap.isEmpty()) return;

        int buildId = (int) buildsMap.keySet().toArray()[index];

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this build?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                new DatabaseManager().deleteBuild(buildId);
                loadBuilds();
            }
        });
    }

    @FXML
    public void viewSelected() {
        int index = savedBuildsList.getSelectionModel().getSelectedIndex();
        if (index < 0 || buildsMap.isEmpty()) return;

        int buildId = (int) buildsMap.keySet().toArray()[index];
        BuildSelection build = new DatabaseManager().getBuildById(buildId);

        if (build != null) {
            BuildSelection.cpu = build.cpu;
            BuildSelection.motherboard = build.motherboard;
            BuildSelection.gpu = build.gpu;
            BuildSelection.ram = build.ram;
            BuildSelection.storage = build.storage;
            SceneManager.getInstance().navigateTo(SceneType.SUMMARY);
        }
    }

    @FXML
    private void goHome() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }
}