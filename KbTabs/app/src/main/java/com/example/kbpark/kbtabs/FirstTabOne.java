package com.example.kbpark.kbtabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.kbpark.kbtabs.MainActivity.firstTabCurListener;


public class FirstTabOne extends Fragment implements View.OnClickListener, MainActivity.onKeyBackPressedListener
{
    FragmentManager manager;

    public FirstTabOne() {}

    public static FirstTabOne newInstance()
    {
        return new FirstTabOne();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.first_one, container, false);

        Button btn = (Button) rootView.findViewById(R.id.btn_first_one);
        btn.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        /**
         * add : 기존 fragment를 가만히 놔둔 상태로, 새로운 fragment를 붙인다.
         * replace : 기존 fragment를 detach하고, 새로운 fragment를 붙인다.
         *
         * 붙이는 대상에 주의하자! ViewPager에 직접 붙이면 error난다!
         */

        manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.layout_first_one, new FirstTabTwo())
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
        firstTabCurListener = this;
    }

    // 참고로, 지금 내 경우는 FirstTabOne이랑 SecondTabOne에 있는 layout에다가 fragment들을 붙이고 있는 상황이라
    // 해당 두 Fragment들에서만 setUserVisibleHint가 먹는것 같다.


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        if(isVisibleToUser)
        {
            // MainActivity.setCurTab(Cons.TAB1); // 사실 얘는 이제 무쓸모
            // Log.d("TEST", "cur tab : " + MainActivity.getCurTab());

            // listener set
            if(getContext()!=null)
            {
                ((MainActivity) getContext()).setOnKeyBackPressedListener(firstTabCurListener);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

}