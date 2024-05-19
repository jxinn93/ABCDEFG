package com.example.hackingthefuture;

public class PassData {
    private static final PassData instance = new PassData();
    private String username;
    private String email;
    private String password;
    private String role;
    private double xCoordinate;
    private double yCoordinate;
    private String code;
    private int resend;
    private PassData(){}

    public static PassData getInstance(){
        return instance;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public int getResend(){
        return resend;
    }

    public void setResend(int resend){
        this.resend = resend;
    }

    public void clearAllValues() {
        username = null;
        email = null;
        password = null;
        role = null;
        code = null;
        resend = 0;
    }
}
