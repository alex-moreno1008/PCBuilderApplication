package org.example.pcbuilderapplication.controllers;

import javafx.fxml.FXML;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import javafx.scene.control.Label;
import org.example.pcbuilderapplication.DatabaseManager;
import org.example.pcbuilderapplication.models.BuildSelection;

public class SummaryController {

    @FXML private Label cpuLabel;
    @FXML private Label motherboardLabel;
    @FXML private Label gpuLabel;
    @FXML private Label ramLabel;
    @FXML private Label storageLabel;
    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        DatabaseManager db = new DatabaseManager();

        double cpuPrice = db.getPartPrice(BuildSelection.cpu);
        double motherboardPrice = db.getPartPrice(BuildSelection.motherboard);
        double gpuPrice = db.getPartPrice(BuildSelection.gpu);
        double ramPrice = db.getPartPrice(BuildSelection.ram);
        double storagePrice = db.getPartPrice(BuildSelection.storage);

        cpuLabel.setText(format("CPU", BuildSelection.cpu, cpuPrice));
        motherboardLabel.setText(format("Motherboard", BuildSelection.motherboard, motherboardPrice));
        gpuLabel.setText(format("GPU", BuildSelection.gpu, gpuPrice));
        ramLabel.setText(format("RAM", BuildSelection.ram, ramPrice));
        storageLabel.setText(format("Storage", BuildSelection.storage, storagePrice));

        double total = cpuPrice + motherboardPrice + gpuPrice + ramPrice + storagePrice;
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private String format(String label, String name, double price) {
        if (name == null) {
            return label + ": Not selected";
        }

        return String.format("%s: %s - $%.2f", label, name, price);
    }

    @FXML
    private void goToCatalog() {
        SceneManager.getInstance().navigateTo(SceneType.CATALOG);
    }

    @FXML
    private void goHome() {
        SceneManager.getInstance().navigateTo(SceneType.HOME);
    }
}