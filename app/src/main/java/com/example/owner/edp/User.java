package com.example.owner.edp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Krishna N. Deoram on 2016-02-22.
 * Gumi is love. Gumi is life.
 */
public class User implements Parcelable {

    private int id;
    private String username;
    private int role;
    private String firstName;
    private String lastName;

    public User(String id, String username, String role, String firstName, String lastName) {
        this.id = Integer.parseInt(id);
        this.username = username;
        this.role = Integer.parseInt(role);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        role = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
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

    public int getUserId() {
        return this.id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return this.role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return (this.getUserId() + " " + this.getUsername() + this.getFirstName() + " " + this.getLastName() + " " + this.getRole());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeInt(role);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}
