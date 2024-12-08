package com.example.bloodbankmerafinal;

public class Request {
    private String donorId;
    private String bloodType;
    private String details;
    private String timeLimit;

    // Default constructor required for Firebase

    public Request(String bloodType, String details, String timeLimit) {
        this.bloodType = bloodType;
        this.details = details;
        this.timeLimit = timeLimit;
    }


    // Constructor for manually creating objects
    public Request(String donorId, String bloodType, String details, String timeLimit) {
        this.donorId = donorId;
        this.bloodType = bloodType;
        this.details = details;
        this.timeLimit = timeLimit;
    }

    // Getters
    public String getDonorId() {
        return donorId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getDetails() {
        return details;
    }

    public String getTimeLimit() {
        return timeLimit;
    }
}
