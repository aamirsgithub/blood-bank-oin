package com.example.bloodbankmerafinal;

public class Donor {
    private String bloodType;
    private String donorInfo;
    private String location;
    private String timeLimit;
    private String address;

    public Donor(String bloodType, String donorInfo, String location, String timeLimit, String address) {
        this.bloodType = bloodType;
        this.donorInfo = donorInfo;
        this.location = location;
        this.timeLimit = timeLimit;
        this.address = address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getDonorInfo() {
        return donorInfo;
    }

    public String getLocation() {
        return location;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public String getAddress() {
        return address;
    }
}
