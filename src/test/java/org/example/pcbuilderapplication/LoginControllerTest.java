package org.example.pcbuilderapplication;

import org.example.pcbuilderapplication.models.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    @Test
    void login_withValidCredentials_setsLoggedInUserId() {
        UserService userService = UserService.getInstance();
        String uniqueUser = "login_test_" + System.currentTimeMillis();
        userService.register(uniqueUser, "password123");

        DatabaseManager db = new DatabaseManager();
        int userId = db.getUserId(uniqueUser, "password123");
        userService.setLoggedInUserId(userId);

        assertNotEquals(-1, userService.getLoggedInUserId(),
                "User ID should be set after login");
    }

}