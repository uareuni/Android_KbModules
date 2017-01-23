package com.example.kbpark.kbretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.kbpark.kbretrofit.Retrofit.GithubServiceSample;
import com.example.kbpark.kbretrofit.Retrofit.Repo;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         *  < retrofit2 >
         */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubServiceSample service = retrofit.create(GithubServiceSample.class);
        Call<List<Repo>> repos = service.listRepos("uareuni");

        ////////////
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> test = response.body(); // 이렇게 받아오면 됩니다.
                Log.d("git", "호출 성공!");

                Iterator<Repo> itr = test.iterator();
                while(itr.hasNext())
                {
                    // Log로 찍어보기
                    Log.d("git", "name : " + itr.next().getName());
                }

            }
            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.d("git", "호출 실패ㅠㅠ");
            }
        });






    }







}
