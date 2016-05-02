package com.gw;

import android.app.Application;

import com.gw.api.ApiService;
import com.gw.dagger2.components.ApiServiceComponent;
import com.gw.dagger2.components.DaggerApiServiceComponent;
import com.gw.dagger2.models.ApiServicesModule;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.orm.SugarContext;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import javax.inject.Inject;

/**
 * Created by gongwen on 2016/4/24.
 */
public class MainApplication extends Application {

    private static MainApplication mApplication;

    @Inject
    ApiService mApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("Gank").hideThreadInfo().setMethodCount(1).setLogLevel(LogLevel.FULL);
        CrashReport.initCrashReport(getApplicationContext());
        LeakCanary.install(this);
        SugarContext.init(this);

        mApplication = this;
        initApi();
    }

    public static MainApplication getApplication() {
        return mApplication;
    }

    public ApiService getApiService() {
        checkApiService();
        return mApiService;
    }

    private void initApi() {
        ApiServiceComponent mApiServiceComponent = DaggerApiServiceComponent.builder().apiServicesModule(new ApiServicesModule(getApplicationContext())).build();
        mApiServiceComponent.inject(this);
    }

    private void checkApiService() {
        if (mApiService == null) {
            throw new NullPointerException("mApiService must not be null!");
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
