package com.daffodil.cardiocare.Models;

import java.io.Serializable;

public class LoggedpatientDetail implements Serializable {

    String token ;
    String userId ;
    String email;
    String phoneNumber ;
    String userName ;

    public LoggedpatientDetail(String token, String userId, String email, String phoneNumber, String userName) {
        this.token = token;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "LoggedpatientDetail{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
