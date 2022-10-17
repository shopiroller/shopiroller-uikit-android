package com.shopiroller.util.cache;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;
import java.util.Map;

public class FontCache {

    private static Map<Integer, Typeface> sCachedFonts = new HashMap<>();

    public static Typeface getTypeface(Context context, int fontId) {
        if (!sCachedFonts.containsKey(fontId)) {
            Typeface tf = ResourcesCompat.getFont(context, fontId);
            if (tf != null) {
                sCachedFonts.put(fontId, tf);
            }
        }

        return sCachedFonts.get(fontId);
    }
}
