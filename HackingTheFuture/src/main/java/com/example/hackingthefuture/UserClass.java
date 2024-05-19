package com.example.hackingthefuture;

public class UserClass {
    private static String username;
    private static String email;
    private static String password;
    private static String role;
    private static Double coordinateX;
    private static Double coordinateY;
    private static int points;

    public UserClass(){}

    public static String getUsername() {
        return username;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRole() {
        return role;
    }

    public static Double getCoordinateX() {
        return coordinateX;
    }
    public static Double getCoordinateY() { return coordinateY; }

    public static int getPoints() {
        return points;
    }

    public static void setUsername(String username) {
        UserClass.username = username;
    }

    public static void setEmail(String email) {
        UserClass.email = email;
    }

    public static void setPassword(String password) {
        UserClass.password = password;
    }

    public static void setRole(String role) {
        UserClass.role = role;
    }

    public static void setCoordinateX(Double coordinateX) {
        UserClass.coordinateX = coordinateX;
    }
    public static void setCoordinateY(Double coordinateY) {
        UserClass.coordinateY = coordinateY;
    }

    public static void setPoints(int points) {
        UserClass.points = points;
    }
}

