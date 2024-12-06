package com.example.bloodbankmerafinal;

public class UserSession {
    private static UserSession instance;
    private String userId;

    // Private constructor to prevent instantiation
    private UserSession() {}

    // Get the instance of UserSession
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Get userId
    public String getUserId() {
        return userId;
    }

    // Set userId
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
