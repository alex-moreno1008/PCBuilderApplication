package org.example.pcbuilderapplication;

import javafx.stage.Stage;

public class SceneManager {
    private static SceneManager instance;
    private final Stage stage;

    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    public static void init(Stage stage) {
        if (instance == null) {
            instance = new SceneManager(stage);
        }
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager not initialized");
        }
        return instance;
    }

    public void navigateTo(SceneType type) {
        stage.setScene(SceneFactory.create(type));
        stage.show();
    }
}