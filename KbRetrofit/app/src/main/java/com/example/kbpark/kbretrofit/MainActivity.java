package com.example.kbpark.kbretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kbpark.kbretrofit.Retrofit.GetResult;
import com.example.kbpark.kbretrofit.Retrofit.PostResult;
import com.example.kbpark.kbretrofit.Retrofit.ServiceApi;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.kbpark.kbretrofit.Retrofit.ServiceApi.GET_BASE_URL;
import static com.example.kbpark.kbretrofit.Retrofit.ServiceApi.POST_BASE_URL;

/**
 *
 * Retrofit 통신에서 고려해야 할 점은 다음과 같다.
 * 1. GET or POST
 * 2. 동기 or 비동기
 *      : retrofit은 동기/비동기 방식 모두를 지원한다. retrofit2 부터는 Service interface에서 둘다 Call을 return받아야 하기에 차이가 없어졌고,
 *      구현할때 쓰는 method가 달라졌다. ( 동기 : execute() / 비동기 : enqueue() )
 *
 *
 * + 또한 retrofit은 대부분의 경우 'Gson'이라는 라이브러리를 함께 쓰게 되는데,
 *   Gson은 받아온 JSON객체를 android 객체로 바꿔주는 역할을 하는 library다.
 *
 */

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
        ServiceApi service = retrofit.create(ServiceApi.class);
        final Call<List<GetResult>> repos = service.listRepos("uareuni");

        // 3. Server로부터 Json받아옴 (비동기)
        repos.enqueue(new Callback<List<GetResult>>() {
            @Override
            public void onResponse(Call<List<GetResult>> call, Response<List<GetResult>> response) {
                List<GetResult> test = response.body(); // 이렇게 받아오면 됩니다.
                Log.d("git", "호출 성공!");

                Iterator<GetResult> itr = test.iterator();
                while(itr.hasNext())
                {
                    get_text.append(itr.next().getName() + "\n");
                }
            }
            @Override
            public void onFailure(Call<List<GetResult>> call, Throwable t) {
                Log.d("git", "호출 실패ㅠㅠ");
            }
        });

    /*
        // 3. Server로부터 Json받아옴 (동기)

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<GetResult> getResult = repos.execute().body();

                    } catch (IOException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }).start();
    */

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
        ServiceApi postService = retrofit.create(ServiceApi.class);
        final Call<PostResult> res = postService.login("uareuni", "1234");
        //Log.d("git", "현재 res값 : " + res.request());

        // 3. Server로부터 Json값 받아오기 (비동기)
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

    /*
        // 3. Server로부터 Json값 받아오기 (동기)

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    PostResult postResult = res.execute().body(); // 여기서 network 작업이 일어나므로 작업 thread에서 실행해야만 한다!
                    String Key = postResult.getKey();
                } catch (IOException ie)
                {
                    ie.printStackTrace();
                }
            }
        }).start();
    */

    }

}
