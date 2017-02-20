package com.example.kbpark.kbtabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.kbpark.kbtabs.Cons.TAB2;
import static com.example.kbpark.kbtabs.MainActivity.secondTabCurListener;

/**
 * Created by KBPark on 2017. 2. 18..
 */

public class SecondTabOne extends Fragment implements View.OnClickListener, MainActivity.onKeyBackPressedListener
{
    FragmentManager manager;

    public SecondTabOne() {}

    public static SecondTabOne newInstance() {
        SecondTabOne fragment = new SecondTabOne();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.second_one, container, false);

        Button btn = (Button) rootView.findViewById(R.id.btn_second_one);
        btn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.layout_second_one, new SecondTabTwo())
                .commit();
    }

    @Override
    public void onBackKey()
    {
        // back key 다시 원상복구 시켜놓기!
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
        secondTabCurListener = this;
    }



    // 참고로, 지금 내 경우는 FirstTabOne이랑 SecondTabOne에 있는 layout에다가 fragment들을 붙이고 있는 상황이라
    // 해당 두 Fragment들에서만 setUserVisibleHint가 먹는것 같다.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        if(isVisibleToUser)
        {
            MainActivity.setCurTab(TAB2);
            // Log.d("TEST", "cur tab : " + MainActivity.getCurTab());

            // listener 가져오기
            if(getContext()!=null)
            {
                ((MainActivity) getContext()).setOnKeyBackPressedListener(secondTabCurListener);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}
