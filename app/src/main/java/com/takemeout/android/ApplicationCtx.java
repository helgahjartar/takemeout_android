package com.takemeout.android;

import android.app.Application;

/**
 * Created by halldorr on 4/3/17.
 */

public class ApplicationCtx extends Application {

    private String jWToken;
    private String username;

    public String getJWToken() { return jWToken; }
    public void setJWToken(String token) { this.jWToken = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
