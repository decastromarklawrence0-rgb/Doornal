package com.mycompany.doornal;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String displayName;
    private String profilePicture;
    private String joinDate;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.displayName = username;
        this.profilePicture = "";
        this.joinDate = LocalDate.now().toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}