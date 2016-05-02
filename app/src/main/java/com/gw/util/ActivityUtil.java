package com.gw.util;

import android.content.Context;
import android.content.Intent;

import com.gw.api.GankCategory;
import com.gw.ui.activity.GankActivity;
import com.gw.ui.activity.PictureActivity;
import com.gw.ui.activity.WebActivity;

/**
 * Created by gongwen on 2016/4/25.
 */
public class ActivityUtil {
    public static void goPictureActivity(Context mContext,String imageUrl,String imageTitle){
        mContext.startActivity(new Intent(mContext, PictureActivity.class)
                .putExtra("ImageUrl",imageUrl).putExtra("ImageTitle",imageTitle));
    }

    public static void goGankActivity(Context mContext, GankCategory type){
        mContext.startActivity(new Intent(mContext, GankActivity.class)
                .putExtra("Type",type));
    }

    public static void goWebActivity(Context mContext, String url,String title){
        mContext.startActivity(new Intent(mContext, WebActivity.class)
                .putExtra("Url",url) .putExtra("Title",title));
    }
}
