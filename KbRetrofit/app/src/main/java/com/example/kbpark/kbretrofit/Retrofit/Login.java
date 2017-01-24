package com.example.kbpark.kbretrofit.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by KBPark on 2017. 1. 24..
 */

public interface Login
{
    @FormUrlEncoded
    @POST("/insert-key-here/insert-value-here/key/value")
    Call<LoginResult> login(@Field("name") String name,
                            @Field("password") String password);
}
