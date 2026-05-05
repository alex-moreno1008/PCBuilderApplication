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





}


