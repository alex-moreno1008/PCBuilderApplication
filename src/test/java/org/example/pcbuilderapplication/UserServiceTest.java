package org.example.pcbuilderapplication;
import org.example.pcbuilderapplication.models.User;
import org.example.pcbuilderapplication.models.UserService;
import org.example.pcbuilderapplication.models.UserService.SignupResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = UserService.getInstance();
  }

  @Test
  void validInput_ReturnSuccess(){
    String uniqueID = "test_user_" + System.currentTimeMillis();
    UserService.SignupResult result = userService.register(uniqueID, "securePass1");
    assertEquals(UserService.SignupResult.SUCCESS, result, "Valid input should be returned: SUCCESS");
  }

  @Test
  void blankUser_ReturnsInvalid(){
    UserService.SignupResult result = userService.register(" ", "securePass1");
    assertEquals(SignupResult.INVALID_USER, result, "Blank username should be returned: INVALID_USER");
  }

  @Test
  void nullUser_ReturnsInvalid(){
    UserService.SignupResult result = userService.register(null, "securePass1");
    assertEquals(UserService.SignupResult.INVALID_USER, result, "Null username should be returned: INVALID_USER");
  }

  @Test
  void shortPassword_ReturnsWeak(){
    String uniqueID = "test_user_" + System.currentTimeMillis();
    UserService.SignupResult result = userService.register(uniqueID, "abc");
    assertEquals(UserService.SignupResult.WEAK_PASS, result, "Password under 6 characters should return: WEAK_PASS");
  }

  @Test
  void exactlyMinPass_ReturnsTrue(){
    String uniqueID = "test_user_" + System.currentTimeMillis();
    UserService.SignupResult result = userService.register(uniqueID, "abc123");
    assertEquals(UserService.SignupResult.SUCCESS, result, "Password of exactly 6 characters should be accepted");
  }

  @Test
  void nullPass_Weak(){
    String uniqueID = "test_user_" + System.currentTimeMillis();
    UserService.SignupResult result = userService.register(uniqueID, null);
    assertEquals(UserService.SignupResult.WEAK_PASS, result, "Null password should be returned: WEAK_PASS");
  }

  @Test
  void accountExists_NewUser_False(){
    assertFalse(userService.accountExists("brand_new_user", "password123") ,"New user should not exist in DB");
  }

  @Test
  void accountExists_AfterRegister_ReturnsTrue() {
    String uniqueUser = "test_user_" + System.currentTimeMillis();
    userService.register(uniqueUser, "password123");
    assertTrue(userService.accountExists(uniqueUser, "password123"),
        "User should exist after registration");
  }
}


