package com.oneMap.module.common.widget.materailintroview.utils;

import android.content.res.Resources;

/**
 *
 */
public class Utils {

    public static int pxToDp(int px){
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
