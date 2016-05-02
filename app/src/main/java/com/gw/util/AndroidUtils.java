package com.gw.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;


public class AndroidUtils {

    public static void copyToClipBoard(Context context, String text, String success) {
        ClipData clipData = ClipData.newPlainText("meizhi_copy", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        ToastUtil.showShortToast(context,success);
    }
}
