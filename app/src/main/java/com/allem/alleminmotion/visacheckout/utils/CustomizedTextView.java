package com.allem.alleminmotion.visacheckout.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.allem.alleminmotion.visacheckout.R;

/**
 * Created by RafaelRodriguez on 03/06/16.
 */
public class CustomizedTextView extends TextView {

    /**
     * Customized TextView for make easy set a font type from xml and reduce the code management
     **/

    //global variables
    private Context context;
    private AttributeSet attrs;
    private TypedArray typedArray;


    /**
     * Public constructor adding the new font attribute
     **/

    public CustomizedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attrs = attrs;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomizedTextView);

        if (isInEditMode())
            return;

        this.setTypeface(getFont());
    }

    /*
    * private getter for get the requested font
    * */

    private Typeface getFont() {

        AssetManager manager = context.getAssets();

        String fontName = typedArray.getString(R.styleable.CustomizedTextView_font);
        typedArray.recycle();

        return Typeface.createFromAsset(manager, fontName);
    }


    //customize the font size
    /*private void fontSize() {

        float size = context.getResources().getDisplayMetrics().scaledDensity;
        float requestedSize = typedArray.getInt(R.styleable.CustomizedTextView_fontSize,0);
        float xSize;

        xSize = (requestedSize * size) / 100;

        this.setTextScaleX(xSize);
        Log.e("CustomizedTextView", "screen density is "+size+", requested size is "+requestedSize+", return size is "+xSize);

    }*/


}
