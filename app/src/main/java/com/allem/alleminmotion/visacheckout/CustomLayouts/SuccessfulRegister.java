package com.allem.alleminmotion.visacheckout.CustomLayouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.allem.alleminmotion.visacheckout.R;
import com.allem.alleminmotion.visacheckout.utils.CustomizedTextView;

/**
 * Created by sergio on 18/07/16.
 */
public class SuccessfulRegister extends LinearLayout {

    public SuccessfulRegister(Context context) {//, AttributeSet attrs
        super(context);//, attrs)

        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        CustomizedTextView tv1 = new CustomizedTextView(context);
        tv1.setText("HELLO");
        addView(tv1);
        tv1.setTextColor(getResources().getColor(R.color.magenta));



        CustomizedTextView tv2 = new CustomizedTextView(context);
        tv2.setText("WORLD");
        tv2.setTextColor(getResources().getColor(R.color.magenta));
        addView(tv2);
    }
}
