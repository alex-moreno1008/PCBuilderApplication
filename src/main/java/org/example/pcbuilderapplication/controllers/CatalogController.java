package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.example.pcbuilderapplication.DatabaseManager;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;

public class CatalogController {

    @FXML private ComboBox<String> cpuBox;
    @FXML private ComboBox<String> gpuBox;
    @FXML private ComboBox<String> ramBox;
    @FXML private ComboBox<String> storageBox;
    @FXML private ComboBox<String> motherboardBox;

    @FXML
    private void goToSummary() {
        SceneManager.getInstance().navigateTo(SceneType.SUMMARY);
    }

    @FXML
    private void goHome() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }

    @FXML
    public void initialize() {
        DatabaseManager db = new DatabaseManager();
        cpuBox.getItems().addAll(db.getPartsByCategory("CPU"));
        gpuBox.getItems().addAll(db.getPartsByCategory("GPU"));
        ramBox.getItems().addAll(db.getPartsByCategory("RAM"));
        storageBox.getItems().addAll(db.getPartsByCategory("Storage"));
        motherboardBox.getItems().addAll(db.getPartsByCategory("Motherboard"));
    }
}