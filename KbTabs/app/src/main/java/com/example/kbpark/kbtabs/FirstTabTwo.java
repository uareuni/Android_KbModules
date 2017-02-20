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

public class FirstTabTwo extends Fragment implements View.OnClickListener ,MainActivity.onKeyBackPressedListener
{
    FragmentManager manager;

    public FirstTabTwo() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.first_two, container, false);

        Button btn = (Button) rootView.findViewById(R.id.btn_first_two);
        btn.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.layout_first_one, new FirstTabThree())
                .commit();
    }

    @Override
    public void onBackKey()
    {
        manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.layout_first_one, new FirstTabOne())
                .commit();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
        MainActivity.firstTabCurListener = this;
    }
}
