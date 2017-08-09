package com.takemeout.android.user.requests;

/**
 * Created by helgah on 29/03/2017.
 */

public class LoginRequest {
    private String userName;
    private String passwordHash;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
