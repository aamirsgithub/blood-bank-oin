package com.example.bloodbankmerafinal;

public class UserSession {
    private static UserSession instance;
    private Integer userId;

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
    public Integer getUserId() {
        return userId;
    }

    // Set userId
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
