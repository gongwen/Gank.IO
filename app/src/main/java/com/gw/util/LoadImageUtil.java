package com.gw.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;

/**
 * Created by gongwen on 2016/4/26.
 */
public class LoadImageUtil {
    /*public static void initPicasso(Context context, OkHttpClient client){
    //设置缓存
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownLoader(client))
                .build();
        Picasso.setSingletonInstance(picasso);
    }*/
    public static void initGlideCache(Context context) {
        new GlideBuilder(context)
                .setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 100));
    }
    public static void show(Context context, String imageUrl, ImageView target) {
        Glide.with(context).load(imageUrl).into(target);
    }
    public static void show(Context context, String imageUrl, GlideDrawableImageViewTarget glideDrawableImageViewTarget) {
        Glide.with(context).load(imageUrl).into(glideDrawableImageViewTarget);
    }
    public static void show(Context context, String imageUrl, ImageView target, SizeReadyCallback cb) {
        Glide.with(context).load(imageUrl).centerCrop().into(target).getSize(cb);
    }


}
