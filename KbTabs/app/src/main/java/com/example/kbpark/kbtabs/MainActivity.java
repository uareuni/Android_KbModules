package com.example.kbpark.kbtabs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

/**
 fragment를 control하는데 back stack을 control하는게 더 힘들것 같아서
 나는 back key를 뺐아와서 수동으로 조작하는 방법을 썼는데,
 잘 생각해보면 사실 (tab이 2개인 이 project에서는) back key를 뺐아오는 listener는 하나의 tab에 대해서만 적용되도 독립적으로 동작하는 것을 알 수 있다.
 (나머지 tab은 stack을 혼자 쓰게 되는 것이니까.)

 그러나 tab이 3개, 4개 확장될 상황까지 고려하여
 두개의 tab에 back key를 뺐아오는 listener를 놔뒀다.

 + 리펙토링을 좀더 할까했는데, 나중에도 읽기 쉽게 하기위해 중복되더라도 일단은 그냥 뒀다.
 */


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        CustomViewPager pager = (CustomViewPager) findViewById(R.id.pager);
        pager.setPagingEnabled(false); // page touch sliding disabled

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    /** current tab navigator **/
    public static String CUR_TAB = "";
    public static void setCurTab(String curTab)
    {
        CUR_TAB = curTab;
    }
    public static String getCurTab() { return CUR_TAB; }


    /***** back key 받아먹기 리스너 등록 *****/
    public interface onKeyBackPressedListener{
        void onBackKey();
    }

    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public static onKeyBackPressedListener firstTabCurListener;
    public static onKeyBackPressedListener secondTabCurListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener)
    {
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed()
    {
        // 맨 처음에는 null이겠지만, 적용되는 fragment의 onAttach()(<-얘는 fragment가 view에 붙을때 호출되는 call back임) 호출 후에는 초기화가 되기 때문에 back key를 뺏아올 수 있다.
        if (mOnKeyBackPressedListener != null)
        {
            mOnKeyBackPressedListener.onBackKey();
        } else
        {
            super.onBackPressed();
        }
    }

}
