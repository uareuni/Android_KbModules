package com.example.kbpark.kbretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kbpark.kbretrofit.Retrofit.GetResult;
import com.example.kbpark.kbretrofit.Retrofit.GetService;
import com.example.kbpark.kbretrofit.Retrofit.PostResult;
import com.example.kbpark.kbretrofit.Retrofit.PostService;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.kbpark.kbretrofit.Retrofit.GetService.GET_BASE_URL;
import static com.example.kbpark.kbretrofit.Retrofit.PostService.POST_BASE_URL;


public class MainActivity extends AppCompatActivity {

    TextView post_text;
    TextView get_text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        post_text = (TextView) findViewById(R.id.post_text);
        get_text = (TextView) findViewById(R.id.get_text);

    }

    public void onGetClicked(View v)
    {
        /**
         *  Get 방식 통신
         *  test URL : https://api.github.com/users/{user}/repos
         */

        // 1.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GET_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2.
        GetService service = retrofit.create(GetService.class);
        Call<List<GetResult>> repos = service.listRepos("uareuni");

        // 3.
        repos.enqueue(new Callback<List<GetResult>>() {
            @Override
            public void onResponse(Call<List<GetResult>> call, Response<List<GetResult>> response) {
                List<GetResult> test = response.body(); // 이렇게 받아오면 됩니다.
                Log.d("git", "호출 성공!");

                Iterator<GetResult> itr = test.iterator();
                while(itr.hasNext())
                {
                    get_text.append(itr.next().getName() + "\n");

                    // Log로 찍어보기
                    //Log.d("git", "name : " + itr.next().getName());
                }
            }
            @Override
            public void onFailure(Call<List<GetResult>> call, Throwable t) {
                Log.d("git", "호출 실패ㅠㅠ");
            }
        });
    }


    public void onPostClicked(View v)
    {
        /**
         *  Post 방식 통신
         *  test URL : http://echo.jsontest.com/key/value/one/two
         */

        // 1.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2.
        PostService postService = retrofit.create(PostService.class);
        final Call<PostResult> res = postService.login("uareuni", "1234");
        //Log.d("git", "현재 res값 : " + res.request());

        // 3.
        res.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {

                PostResult postResult = response.body();
                post_text.append(postResult.getKey());
                post_text.append(postResult.getOne());

            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {

            }
        });
    }


}
