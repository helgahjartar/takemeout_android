package com.takemeout.android.user.requests;

/**
 * Created by helgah on 29/03/2017.
 */

public class RegisterUserRequest {
    private String userName;
    private String passwordHash;
    private String email;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
