package com.gw.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gw.MainApplication;

/**
 * Created by gongwen on 2016/5/1.
 */
public class NetUtil {
    /**
     * 检查当前网络是否可用
     */
    public static boolean isNetworkAvailable() {
        Context context = MainApplication.getApplication().getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }

        return false;
    }
}
