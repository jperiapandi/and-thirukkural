package com.jpp.and_thirukkural;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jpp.and_thirukkural.utils.FontCache;

/**
 * Created by jperiapandi on 12-09-2016.
 */
public class TamilTextView extends TextView {
    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public TamilTextView(Context context) {
        super(context);
        applyCustomFontFace(context, null);
    }

    public TamilTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFontFace(context, attrs);
    }

    public TamilTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFontFace(context, attrs);
    }

    private void applyCustomFontFace(Context context, AttributeSet attrs){
        int textStyle;

        if(attrs == null){
            textStyle = Typeface.NORMAL;
        }
        else
        {
            textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
        }

        Typeface typeface;

        switch (textStyle){
            case Typeface.BOLD:
            case Typeface.BOLD_ITALIC:
                typeface = FontCache.getTypeface("fonts/NotoSansTamil-Bold.ttf", context);
                break;
            default:
                typeface = FontCache.getTypeface("fonts/NotoSansTamil-Regular.ttf", context);
        }

        setTypeface(typeface);
    }
}
