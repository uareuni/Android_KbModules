package com.example.kbpark.kbtabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by KBPark on 2017. 2. 18..
 */

public class SecondTabTwo extends Fragment implements View.OnClickListener, MainActivity.onKeyBackPressedListener
{
    FragmentManager manager;

    public SecondTabTwo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.second_two, container, false);

        Button btn = (Button)rootView.findViewById(R.id.btn_second_two);
        btn.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.layout_second_one, new SecondTabThree())
                .commit();
    }

    @Override
    public void onBackKey()
    {
        //if(MainActivity.getCurTab().equals(TAB2))
        //{
            manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.layout_second_one, new SecondTabOne())
                    .commit();
        //}
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
        MainActivity.secondTabCurListener = this;
    }
}
