package com.gw.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by gongwen on 2016/4/24.
 */
public class ToastUtil {
    public static void showShortToast(Context mContext,@StringRes int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context mContext, CharSequence text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context mContext, CharSequence text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context mContext, @StringRes int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_LONG).show();
    }
}
