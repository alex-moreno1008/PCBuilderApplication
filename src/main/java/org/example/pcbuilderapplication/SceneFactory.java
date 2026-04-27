package org.example.pcbuilderapplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public class SceneFactory {

    public static Scene create(SceneType type) {
        return switch (type) {
            case LOGIN -> loadScene("/org/example/pcbuilderapplication/login.fxml");
            case HOME -> loadScene("/org/example/pcbuilderapplication/home.fxml");
            case CATALOG -> loadScene("/org/example/pcbuilderapplication/catalog.fxml");
            case SUMMARY -> loadScene("/org/example/pcbuilderapplication/summary.fxml");
            case SAVED_BUILDS -> loadScene("/org/example/pcbuilderapplication/saved-builds.fxml");
            case SIGNUP -> loadScene("/org/example/pcbuilderapplication/signup.fxml");
        };
    }

    private static Scene loadScene(String path) {
        URL url = SceneFactory.class.getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("FXML not found: " + path);
        }

        try {
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            return new Scene(root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + path, e);
        }
    }
}