package com.gw.dagger2.components;

import com.gw.MainApplication;
import com.gw.dagger2.models.ApiServicesModule;
import com.gw.presenter.BasePresenter;
import com.gw.presenter.MainPresenter;
import com.gw.ui.base.BaseActivity;
import com.gw.ui.view.IBaseView;
import com.gw.ui.view.IMainView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gongwen on 2016/4/24.
 */
@Singleton
@Component(modules = {ApiServicesModule.class})
public interface ApiServiceComponent {
    void inject(MainApplication mainApplication);
}
