package com.example.kbpark.kbretrofit.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by KBPark on 2017. 1. 23..
 */

public interface GetService
{
    String GET_BASE_URL = "https://api.github.com";
    String GET_ADDITIONAL_URL = "/users/{user}/repos";

    @GET(GET_ADDITIONAL_URL)
    Call<List<GetResult>> listRepos(@Path("user") String user);
}
