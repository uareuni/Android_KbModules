package com.example.kbpark.kbretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kbpark.kbretrofit.Retrofit.GithubServiceSample;
import com.example.kbpark.kbretrofit.Retrofit.Login;
import com.example.kbpark.kbretrofit.Retrofit.LoginResult;
import com.example.kbpark.kbretrofit.Retrofit.Repo;
import com.example.kbpark.kbretrofit.Retrofit.User;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Server로부터 읽어오기 : Github repos json으로 test
 * Server로 보내고 return 받기 : http://www.jsontest.com/#echo에 있는 'Echo JSON'으로 test 했음!
 */


public class MainActivity extends AppCompatActivity {

    TextView toServer;
    TextView fromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toServer = (TextView) findViewById(R.id.toServer);
        fromServer = (TextView) findViewById(R.id.fromServer);

    }

    public void onSendClicked(View v)
    {
        /**
         *  server로 json object 보내고, 다시 return받기
         */

        User user = new User("아이디와", "비밀번호"); // 임의의 user obj

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://echo.jsontest.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Login login = retrofit.create(Login.class);
        final Call<LoginResult> res = login.login(user.getId(), user.getPw());
        //Log.d("git", "현재 res값 : " + res.request());

        res.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                LoginResult loginResult = response.body();
                toServer.append(loginResult.getKey());

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {

            }
        });

    }

    public void onReceiveClicked(View v)
    {
        /**
         *  server로부터 읽어오기
         */

        // 1.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2.
        GithubServiceSample service = retrofit.create(GithubServiceSample.class);
        Call<List<Repo>> repos = service.listRepos("uareuni");

        // 3.
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> test = response.body(); // 이렇게 받아오면 됩니다.
                Log.d("git", "호출 성공!");

                Iterator<Repo> itr = test.iterator();
                while(itr.hasNext())
                {
                    fromServer.append(itr.next().getName() + "\n");

                    // Log로 찍어보기
                    //Log.d("git", "name : " + itr.next().getName());
                }
            }
            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.d("git", "호출 실패ㅠㅠ");
            }
        });
    }




}
