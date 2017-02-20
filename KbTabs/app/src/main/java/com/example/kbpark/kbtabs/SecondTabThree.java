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
 * Created by KBPark on 2017. 2. 20..
 */

public class SecondTabThree extends Fragment implements View.OnClickListener, MainActivity.onKeyBackPressedListener
{
    FragmentManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.second_three, container, false);

        Button btn = (Button) rootView.findViewById(R.id.btn_second_three);
        btn.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View view)
    {
        // do something
    }

    @Override
    public void onBackKey()
    {
            manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.layout_second_one, new SecondTabTwo())
                    .commit();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
        MainActivity.secondTabCurListener = this;
    }

}
