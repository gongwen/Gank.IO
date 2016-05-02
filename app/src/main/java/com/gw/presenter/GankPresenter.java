package com.gw.presenter;

import android.app.Activity;
import android.content.Context;

import com.gw.api.Constants;
import com.gw.api.GankCategory;
import com.gw.api.helper.ApiHelper;
import com.gw.entity.GankEntity;
import com.gw.ui.view.IGankView;
import com.gw.util.DbUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by gongwen on 2016/4/30.
 */
public class GankPresenter extends BasePresenter<IGankView> {
    private int mPage = 1;
    private GankCategory type;

    public void setType(GankCategory type) {
        this.type = type;
    }


    public GankPresenter(Context context, IGankView view) {
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
        Observable<List<GankEntity>> observable =
                mApiService.getOneTypeData(type, Constants.PAGE_SIZE * 2, mPage)
                        .compose(ApiHelper.<List<GankEntity>>applySchedulers())
                        .map(new Func1<List<GankEntity>, List<GankEntity>>() {
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
                        }).toSortedList(new Func2<GankEntity, GankEntity, Integer>() {
                    @Override
                    public Integer call(GankEntity entity, GankEntity entity2) {
                        return entity2.getPublishedAt().compareTo(entity.getPublishedAt());
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        ;
        mView.loadDataComplete(observable, isClean);

    }

    public List<GankEntity> getDataFromDb(GankCategory type) {
        return DbUtil.findByType(type, String.valueOf((mPage - 1) * Constants.PAGE_SIZE * 2)
                + ","
                + String.valueOf(mPage * Constants.PAGE_SIZE * 2));
    }
}
