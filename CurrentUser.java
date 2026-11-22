package com.mycompany.doornal;

public class CurrentUser {

    public static User user;
    public static String username;

    public static boolean isLoggedIn() {
        return user != null;
    }

    public static void login(User loggedUser) {
        user = loggedUser;
        username = loggedUser.getUsername();
    }

    public static void logout() {
        user = null;
        username = null;
    }

    public static String getDisplayName() {
        if (user == null) {
            return "Guest";
        }
        return user.getDisplayName();
    }

    public static String getJoinDate() {
        if (user == null) {
            return "";
        }
        return user.getJoinDate();
    }

    public static String getProfilePicture() {
        if (user == null) {
            return "";
        }
        return user.getProfilePicture();
    }
}