package com.allem.alleminmotion.visacheckout.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.allem.alleminmotion.visacheckout.R;

/**
 * Created by jsandoval on 20/06/16.
 */
public class MyEditText extends EditText {
    private final Context context;
    private final Drawable imgCloseButton = getResources().getDrawable(R.drawable.deletedata);

    public MyEditText(Context context) {
        super(context);
        this.context = context;
        setPadding(5, 5, 10, 5);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setPadding(5, 5, 10, 5);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setPadding(5, 5, 10, 5);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
        init();
    }

    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_bold)));
        }else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.fontName_regular)));
        }
    }

    void init() {
        //setPadding(10, 5, 10, 25);
        imgCloseButton.setBounds(0, 0, imgCloseButton.getIntrinsicWidth(), imgCloseButton.getIntrinsicHeight());
        handleClearButton();
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                MyEditText et = MyEditText.this;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight()
                        - imgCloseButton.getIntrinsicWidth()) {
                    et.setText("");
                    MyEditText.this.handleClearButton();
                }
                return false;
            }
        });

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {

                MyEditText.this.handleClearButton();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });
    }

    void handleClearButton() {
        if (this.getText().toString().equals("")) {
            // add the clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0],
                    this.getCompoundDrawables()[1], null,
                    this.getCompoundDrawables()[3]);
        } else {
            // remove clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0],
                    this.getCompoundDrawables()[1], imgCloseButton,
                    this.getCompoundDrawables()[3]);

        }

        if(this.isFocusable()==false){
            this.setCompoundDrawables(this.getCompoundDrawables()[0],
                    this.getCompoundDrawables()[1], null,
                    this.getCompoundDrawables()[3]);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (this.getText().toString().equals("")) {
            // add the clear button
            this.setCompoundDrawables(this.getCompoundDrawables()[0],
                    this.getCompoundDrawables()[1], null,
                    this.getCompoundDrawables()[3]);
        } else {
            if (focused) {
                // remove clear button
                this.setCompoundDrawables(this.getCompoundDrawables()[0],
                        this.getCompoundDrawables()[1], imgCloseButton,
                        this.getCompoundDrawables()[3]);
            } else {
                // add the clear button
                this.setCompoundDrawables(this.getCompoundDrawables()[0],
                        this.getCompoundDrawables()[1], null,
                        this.getCompoundDrawables()[3]);
            }

        }

    }
}
