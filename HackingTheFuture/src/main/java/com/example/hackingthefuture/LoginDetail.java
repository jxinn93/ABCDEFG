package com.example.hackingthefuture;

public class LoginDetail {
    private static final LoginDetail instance = new LoginDetail();
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean check;

    private LoginDetail(){}

    public static LoginDetail getInstance(){
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

    public void setLogin(boolean check){
        this.check = check;
    }

    public boolean getLogin(){
        return check;
    }

    public void clearAllValues() {
        username = null;
        email = null;
        password = null;
        role = null;
        check = false;
    }
}
