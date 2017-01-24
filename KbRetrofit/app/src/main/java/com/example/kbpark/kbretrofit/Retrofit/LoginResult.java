package com.example.kbpark.kbretrofit.Retrofit;

/**
 * Created by KBPark on 2017. 1. 24..
 */

public class LoginResult
{
    private int resCode;
    private String message;
    private String key;
    private String name;


    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getResCode()
    {
        return resCode;
    }
    public String getMessage()
    {
        return message;
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }

}
