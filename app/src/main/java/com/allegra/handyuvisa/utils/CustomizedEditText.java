package com.allegra.handyuvisa.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.allegra.handyuvisa.R;


/**
 * Created by sfarfan on 14/06/16.
 */
public class CustomizedEditText extends EditText {


    //global variables
    private Context context;
    private AttributeSet attrs;
    private TypedArray typedArray;


    /**
     * Public constructor adding the new font attribute
     **/
    public CustomizedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attrs = attrs;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomizedEditText);

        if (isInEditMode())
            return;

        this.setTypeface(getFont());
    }

    /*
    * private getter for get the requested font
    * */

    private Typeface getFont() {

        AssetManager manager = context.getAssets();

        String fontName = typedArray.getString(R.styleable.CustomizedEditText_fontEdit);
        typedArray.recycle();

        return Typeface.createFromAsset(manager, fontName);
    }
}
