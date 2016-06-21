package com.allem.alleminmotion.visacheckout.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.allem.alleminmotion.visacheckout.R;

/**
 * Created by jsandoval on 20/06/16.
 */
public class MyButton extends Button {

    private final Context context;

    public MyButton(Context context) {
        super(context);
        this.context = context;
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
    }

    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_bold)));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
        }
    }
}
