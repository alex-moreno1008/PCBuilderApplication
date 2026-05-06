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

    @Test
    void login_withInvalidCredentials_userShouldNotExist() {
        DatabaseManager db = new DatabaseManager();
        boolean exists = db.userExists("fake_user_xyz", "wrongpassword");
        assertFalse(exists, "Non-existent user should not be found in DB");
    }
}