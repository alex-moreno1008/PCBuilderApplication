package org.example.pcbuilderapplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageTest {

    @BeforeAll
    static void initJfx() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already started
        }
    }

    @Test
    void homeFxmlLoadsSuccessfully() throws Exception {
        URL fxmlUrl = getClass().getResource("/org/example/pcbuilderapplication/home.fxml");
        assertNotNull(fxmlUrl, "home.fxml should exist in resources");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        assertNotNull(root, "FXML should load into a root node");
        assertTrue(root instanceof StackPane, "Root should be a StackPane");
    }

    @Test
    void homePageContainsExpectedLabelsAndButtons() throws Exception {
        URL fxmlUrl = getClass().getResource("/org/example/pcbuilderapplication/home.fxml");
        assertNotNull(fxmlUrl, "home.fxml should exist in resources");

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Label homeLabel = findLabel(root, "Home Page!");
        Label subLabel = findLabel(root, "Select your option below...");
        Label statusLabel = findLabel(root, "System Ready");

        Button buildButton = findButton(root, "BUILD A PC");
        Button savedButton = findButton(root, "SAVED BUILDS");
        Button logoutButton = findButton(root, "LOGOUT");

        assertNotNull(homeLabel, "Home Page label should exist");
        assertNotNull(subLabel, "Subtitle label should exist");
        assertNotNull(statusLabel, "System Ready label should exist");

        assertNotNull(buildButton, "BUILD A PC button should exist");
        assertNotNull(savedButton, "SAVED BUILDS button should exist");
        assertNotNull(logoutButton, "LOGOUT button should exist");
    }

    private Label findLabel(Parent root, String text) {
        for (var node : root.lookupAll(".label")) {
            if (node instanceof Label label && text.equals(label.getText())) {
                return label;
            }
        }
        return null;
    }

    private Button findButton(Parent root, String text) {
        for (var node : root.lookupAll(".button")) {
            if (node instanceof Button button && text.equals(button.getText())) {
                return button;
            }
        }
        return null;
    }
}