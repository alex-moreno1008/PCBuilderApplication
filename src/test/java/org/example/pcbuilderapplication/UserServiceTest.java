package org.example.pcbuilderapplication;
import org.example.pcbuilderapplication.models.UserService;
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
  void testRegister_ValidInput_ReturnsSuccess() {
    UserService.SignupResult result = userService.register("john_doe", "securePass1");
    assertEquals(UserService.SignupResult.SUCCESS, result,
        "Valid input should return SUCCESS");
  }


  @Test
  void testRegister_BlankUsername_ReturnsInvalidUsername() {
    UserService.SignupResult result = userService.register("  ", "securePass1");
    assertEquals(UserService.SignupResult.INVALID_USER, result,
        "Blank username should return INVALID_USERNAME");
  }

  @Test
  void testRegister_NullUsername_ReturnsInvalidUsername() {
    UserService.SignupResult result = userService.register(null, "securePass1");
    assertEquals(UserService.SignupResult.INVALID_USER, result,
        "Null username should return INVALID_USERNAME");
  }



  @Test
  void testRegister_ShortPassword_ReturnsWeakPassword() {
    UserService.SignupResult result = userService.register("john_doe", "abc");
    assertEquals(UserService.SignupResult.WEAK_PASS, result,
        "Password under 6 chars should return WEAK_PASSWORD");
  }

  @Test
  void testRegister_NullPassword_ReturnsWeakPassword() {
    UserService.SignupResult result = userService.register("john_doe", null);
    assertEquals(UserService.SignupResult.WEAK_PASS, result,
        "Null password should return WEAK_PASSWORD");
  }

  @Test
  void testRegister_ExactlyMinPassword_ReturnsSuccess() {
    UserService.SignupResult result = userService.register("john_doe", "abc123");
    assertEquals(UserService.SignupResult.SUCCESS, result,
        "Password of exactly 6 chars should be accepted");
  }



  @Test
  void testAccountExists_NewUser_ReturnsFalse() {

    assertFalse(userService.accountExists("brand_new_user", "password123"),
        "New user should not exist in DB");
  }

  @Test
  void testAccountExists_AfterRegister_ReturnsTrue() {

    userService.register("existing_user", "password123");
    assertTrue(userService.accountExists("existing_user", "password123"),
        "User should exist after registration");
  }
}


