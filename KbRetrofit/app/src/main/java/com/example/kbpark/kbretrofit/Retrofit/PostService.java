package com.example.kbpark.kbretrofit.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by KBPark on 2017. 1. 24..
 */

public interface PostService
{
    String POST_BASE_URL = "http://echo.jsontest.com";
    String POST_ADDITIONAL_URL = "/key/value/one/two";


    @FormUrlEncoded
    @POST(POST_ADDITIONAL_URL)
    Call<PostResult> login(@Field("user_id") String id,  // 얘네는 server에서 받을 data들?
                           @Field("user_pw") String pw); // 얘네는 server에서 받을 data들?
}
