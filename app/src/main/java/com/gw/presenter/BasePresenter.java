package com.gw.presenter;

import android.app.Activity;
import android.content.Context;

import com.gw.MainApplication;
import com.gw.api.ApiService;
import com.gw.ui.view.IBaseView;

/**
 * Created by gongwen on 2016/4/24.
 */
public class BasePresenter<GV extends IBaseView> {
    protected ApiService mApiService;
    protected Context mContext;
    protected GV mView;

    public BasePresenter(Context context, GV view) {
        mContext = context;
        mView = view;
        mApiService = MainApplication.getApplication().getApiService();
    }


}
