package org.example.pcbuilderapplication;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    @Test
    void loginSceneTypeShouldExist() {
        assertNotNull(SceneType.LOGIN);
    }

    @Test
    void allSceneTypesShouldBeDefined() {
        SceneType[] types = SceneType.values();
        assertEquals(5, types.length);
    }

    @Test
    void loginSceneTypeShouldHaveCorrectName() {
        assertEquals("LOGIN", SceneType.LOGIN.name());
    }

    @Test
    void homeSceneTypeShouldExist() {
        assertNotNull(SceneType.HOME);
    }

    @Test
    void catalogSceneTypeShouldExist() {
        assertNotNull(SceneType.CATALOG);
    }
}