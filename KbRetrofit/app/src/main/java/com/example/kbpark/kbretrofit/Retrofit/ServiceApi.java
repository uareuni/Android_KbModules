package com.example.kbpark.kbretrofit.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by KBPark on 2017. 1. 23..
 */

public interface ServiceApi
{
    String GET_BASE_URL = "https://api.github.com";
    String GET_ADDITIONAL_URL = "/users/{user}/repos";

    String POST_BASE_URL = "http://echo.jsontest.com";
    String POST_ADDITIONAL_URL = "/key/value/one/two";

    /**
     * GET 방식 request는 다음과 같은 annotation set들이 주로 사용됩니다.
     * 아래 request는 이런식으로 전송 될겁니다.
     * -> https://api.github.com/users/{user}/repos?user=user_id
     */
    @GET(GET_ADDITIONAL_URL)
    Call<List<GetResult>> listRepos(@Path("user") String user_id);

    /**
     * POST 방식 request는 다음과 같은 annotation set들이 주로 사용됩니다.
     */
    @FormUrlEncoded
    @POST(POST_ADDITIONAL_URL)
    Call<PostResult> login(@Field("user_id") String id,  // 얘네는 server에서 받을 data들. 'request.body.user_id' 이런식으로 받아쓰면 된다.
                           @Field("user_pw") String pw); // 얘네는 server에서 받을 data들.
}
