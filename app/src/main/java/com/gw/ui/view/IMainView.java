package com.gw.ui.view;

import com.gw.entity.GankEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by gongwen on 2016/4/24.
 */
public interface IMainView extends IBaseView {
    void loadDataComplete(Observable<List<GankEntity>> observable, boolean clean);
}
