package org.example.pcbuilderapplication.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.pcbuilderapplication.DatabaseManager;
import org.example.pcbuilderapplication.SceneManager;
import org.example.pcbuilderapplication.SceneType;
import org.example.pcbuilderapplication.models.BuildSelection;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;



public class CatalogController {

    @FXML private ComboBox<String> cpuBox;
    @FXML private ComboBox<String> gpuBox;
    @FXML private ComboBox<String> ramBox;
    @FXML private ComboBox<String> storageBox;
    @FXML private ComboBox<String> motherboardBox;
    @FXML private Button fetchGpuBtn;
    @FXML private Label apiStatusLabel;

    @FXML
    private void goToSummary() {
        BuildSelection.cpu = cpuBox.getValue();
        BuildSelection.motherboard = motherboardBox.getValue();
        BuildSelection.gpu = gpuBox.getValue();
        BuildSelection.ram = ramBox.getValue();
        BuildSelection.storage = storageBox.getValue();

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

    @FXML
    private void fetchGpusFromApi(){
        fetchGpuBtn.setDisable(true);
        apiStatusLabel.setText("Fetching GPUS....");
        apiStatusLabel.setStyle("-fx-text-fill: #00e5ff;");

        Thread thread = new Thread(() ->{
            try{
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.recursionist.io/builder/computers?type=gpu"))
                    .GET()
                    .build();

                HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
              //  System.out.println(response.body());

                List<String> gpus = parseModelNames(response.body());

                Platform.runLater(() -> {
                    // Add only GPUs not already in the list
                    for (String gpu : gpus) {
                        if (!gpuBox.getItems().contains(gpu)) {
                            gpuBox.getItems().add(gpu);
                        }
                    }
                    apiStatusLabel.setText("Loaded " + gpus.size() + " GPUs from web!");
                    apiStatusLabel.setStyle("-fx-text-fill: #2ecc71;");
                    fetchGpuBtn.setDisable(false);
                });


    }catch(Exception e){
                Platform.runLater(() -> {
                    apiStatusLabel.setText("Failed to fetch. Check connection.");
                    apiStatusLabel.setStyle("-fx-text-fill: #e74c3c;");
                    fetchGpuBtn.setDisable(false);
                });
                System.err.println("API fetch failed: " + e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    private List<String> parseModelNames(String json) {
        List<String> names = new ArrayList<>();

        String key = "\"Model\":";
        int index = 0;

        while ((index = json.indexOf(key, index)) != -1) {
            int firstQuote = json.indexOf("\"", index + key.length());
            int secondQuote = json.indexOf("\"", firstQuote + 1);

            if (firstQuote != -1 && secondQuote != -1) {
                names.add(json.substring(firstQuote + 1, secondQuote));
            }

            index = secondQuote + 1;
        }

        return names.size() > 20 ? names.subList(0, 20) : names;
    }




}

