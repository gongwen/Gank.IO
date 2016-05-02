package com.gw.presenter;

import android.app.Activity;
import android.content.Context;

import com.gw.api.Constants;
import com.gw.api.GankCategory;
import com.gw.api.helper.ApiHelper;
import com.gw.entity.GankEntity;
import com.gw.ui.view.IMainView;
import com.gw.util.DbUtil;
import com.gw.util.DateUtils;
import com.gw.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by gongwen on 2016/4/24.
 */
public class MainPresenter extends BasePresenter<IMainView> {
    private int mPage = 1;

    public MainPresenter(Context context, IMainView view) {
        super(context, view);
    }


    public void loadData() {
        mPage++;
        loadData(false);//下拉重新加载第一页数据
    }

    public void loadData(final boolean isClean) {
        if (isClean) {
            mPage = 1;
        }
        Observable<List<GankEntity>> observable = Observable.zip(
                mApiService.getOneTypeData(GankCategory.福利, Constants.PAGE_SIZE, mPage).compose(ApiHelper.<List<GankEntity>>applySchedulers()),
                mApiService.getOneTypeData(GankCategory.休息视频, Constants.PAGE_SIZE, mPage).compose(ApiHelper.<List<GankEntity>>applySchedulers()),
                new Func2<List<GankEntity>, List<GankEntity>, List<GankEntity>>() {
                    @Override
                    public List<GankEntity> call(List<GankEntity> meiZhiData, List<GankEntity> androidData) {
                        return createMZhiDataAndAndroidDes(meiZhiData, androidData);
                    }
                }).map(new Func1<List<GankEntity>, List<GankEntity>>() {
            @Override
            public List<GankEntity> call(List<GankEntity> gankEntities) {
                for (GankEntity item : gankEntities) {
                    if (!DbUtil.findById(item.get_id())) {
                        item.save();
                    }
                }
                return gankEntities;
            }
        })
                .flatMap(new Func1<List<GankEntity>, Observable<GankEntity>>() {
                    @Override
                    public Observable<GankEntity> call(List<GankEntity> entities) {
                        return Observable.from(entities);
                    }
                })
                .toSortedList(new Func2<GankEntity, GankEntity, Integer>() {
                    @Override
                    public Integer call(GankEntity entity, GankEntity entity2) {
                        return entity2.getPublishedAt().compareTo(entity.getPublishedAt());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        mView.loadDataComplete(observable, isClean);

    }

    private List<GankEntity> createMZhiDataAndAndroidDes(final List<GankEntity> meiZhiData, final List<GankEntity> androidData) {
        Observable.from(meiZhiData)
                .forEach(new Action1<GankEntity>() {
                             @Override
                             public void call(GankEntity entity) {
                                 for (GankEntity item : androidData) {
                                     if (DateUtils.isTheSameDay(entity.getPublishedAt(), item.getPublishedAt())) {
                                         entity.setVideoUrl(item.getUrl());
                                         entity.setDesc(item.getDesc());
                                         break;
                                     } else {
                                         entity.setDesc("");
                                     }
                                 }

                             }
                         }

                );
        return meiZhiData;
    }

    public List<GankEntity> getDataFromDb() {
        return DbUtil.findByType(GankCategory.福利, String.valueOf((mPage - 1) * Constants.PAGE_SIZE)
                + ","
                + String.valueOf(mPage * Constants.PAGE_SIZE));
    }
}
