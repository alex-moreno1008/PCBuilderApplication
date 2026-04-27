package org.example.pcbuilderapplication.models;

import org.example.pcbuilderapplication.DatabaseManager;


public class UserService {
  private static UserService instance;
  private final DatabaseManager db;

  private UserService(){
    db = new DatabaseManager();
  }

  public static UserService getInstance(){
    if(instance == null){
      instance = new UserService();
    }
    return instance;
  }

  public SignupResult register(String user, String pass){
    if(user == null || user.isBlank()) return SignupResult.INVALID_USER;
    if (pass == null || pass.length() < 6) return SignupResult.WEAK_PASS;

    if(db.userExists(user, pass)) return SignupResult.USERNAME_TAKEN;

    db.insertUser(user, pass);
    return SignupResult.SUCCESS;
  }

  public boolean accountExists(String user, String pass){
    return db.userExists(user, pass);
  }

  public enum SignupResult{
    SUCCESS,
    USERNAME_TAKEN,
    INVALID_USER,
    WEAK_PASS,
    DATABASE_ERROR
  }

}
