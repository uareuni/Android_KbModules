package com.example.kbpark.kbtabs;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by KBPark on 2017. 2. 18..
 */

public class CustomViewPager extends ViewPager
{
    private boolean enabled;

    public CustomViewPager(Context context)
    {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (enabled)
        {
            return super.onInterceptTouchEvent(ev);
        } else
        {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (enabled)
        {
            return super.onTouchEvent(ev);
        } else
        {
            return MotionEventCompat.getActionMasked(ev) != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
        }
    }

    public void setPagingEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
