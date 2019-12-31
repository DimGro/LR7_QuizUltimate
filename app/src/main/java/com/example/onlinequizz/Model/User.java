package com.example.onlinequizz.Model;

public class User {
    private String userName;
    private String password;
    private String emaile;

    public User() {
    }

    public User(String userName, String password, String emale) {
        this.userName = userName;
        this.password = password;
        this.emaile = emale;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmaile() {
        return emaile;
    }

    public void setEmaile(String emale) {
        this.emaile = emaile;
    }
}
