package com.example.kbpark.kbretrofit.Retrofit;

/**
 * Created by KBPark on 2017. 1. 24..
 */

public class User
{
    String id;
    String pw;

    public User(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
