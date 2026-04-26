package org.example.pcbuilderapplication;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        stage.setTitle("PC Builder Application");
        SceneManager.getInstance().navigateTo(SceneType.LOGIN);
    }
}