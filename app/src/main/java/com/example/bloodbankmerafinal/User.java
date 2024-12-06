package com.example.bloodbankmerafinal;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name, bloodGroup, phone, email, address, city, unitsNeeded;

    // Constructor
    public User(String name, String bloodGroup, String phone, String email, String address, String city, String unitsNeeded) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.unitsNeeded = unitsNeeded;
    }

    // Getters
    public String getName() { return name; }
    public String getBloodGroup() { return bloodGroup; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getUnitsNeeded() { return unitsNeeded; }

    // Parcelable implementation
    protected User(Parcel in) {
        name = in.readString();
        bloodGroup = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        city = in.readString();
        unitsNeeded = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(bloodGroup);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(unitsNeeded);
    }
}
