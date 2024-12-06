// MyApp.java
package com.example.bloodbankmerafinal;

import android.app.Application;

public class MyApp extends Application {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
