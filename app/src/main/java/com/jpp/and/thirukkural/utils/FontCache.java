package com.jpp.and.thirukkural.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by jperiapandi on 12-09-2016.
 */
public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontFileName, Context context){
        Typeface typeface = fontCache.get(fontFileName);

        if(typeface == null){
            try{
                typeface = Typeface.createFromAsset(context.getAssets(), fontFileName);
            }
            catch (Exception e){
                return null;
            }

            fontCache.put(fontFileName, typeface);
        }

        return typeface;
    }
}
