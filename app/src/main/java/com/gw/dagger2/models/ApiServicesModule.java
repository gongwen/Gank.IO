package com.gw.dagger2.models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gw.MainApplication;
import com.gw.api.ApiService;
import com.gw.api.Constants;
import com.gw.api.HttpLoggingInterceptor;
import com.gw.api.ResponseInterceptor;
import com.gw.util.LoadImageUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gongwen on 2016/4/24.
 */
@Module
public class ApiServicesModule {
    final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").serializeNulls().create();
    private Context mContext;

    public ApiServicesModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        OkHttpClient client = getOkHttpClient();
        //LoadImageUtil.initPicasso(context, client);
        LoadImageUtil.initGlideCache(mContext);
        Retrofit retrofit = new Retrofit.Builder()
                //baseUrl must end in /
                .baseUrl(Constants.ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.i(message);
            }
        });
        ResponseInterceptor mResponseInterceptor = new ResponseInterceptor();
        File cacheFile = MainApplication.getApplication().getCacheDir();
        // config log
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(cacheFile, 1024 * 1024 * 100))
                .addInterceptor(mHttpLoggingInterceptor)
                .addInterceptor(mResponseInterceptor)
                .build();

    }
}
