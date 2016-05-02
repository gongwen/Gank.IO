package com.gw.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gw.R;
import com.gw.api.GankCategory;
import com.gw.api.helper.ApiHelper;
import com.gw.entity.GankEntity;
import com.gw.presenter.GankPresenter;
import com.gw.ui.adapter.GankRecyclerViewAdapter;
import com.gw.ui.base.SwipeRefreshBaseActivity;
import com.gw.ui.view.IGankView;
import com.orhanobut.logger.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class GankActivity extends SwipeRefreshBaseActivity<GankPresenter> implements IGankView {
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.title)
    TextView toolbarTitle;
    private GankRecyclerViewAdapter adapter;
    private final List<GankEntity> dataList = new ArrayList<>();
    private boolean mIsFirstTimeTouchBottom = true;
    private GankCategory type;

    @Override
    protected int getLayoutId() {
        return R.layout.swipe_refresh_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        toolbarTitle.setText(type.toString());
        mPresenter.setType(type);
        setupRecyclerView(dataList);

    }

    private void getIntentData() {
        type = (GankCategory) getIntent().getSerializableExtra("Type");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new GankPresenter(this, this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRequestDataRefresh(true);
            }
        }, 358);
        mPresenter.loadData(true);
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        mPresenter.loadData(true);

    }

    @Override
    public void loadDataComplete(Observable<List<GankEntity>> observable, final boolean isClean) {
        Subscription subscription = observable.subscribe(ApiHelper.newSubscriber(mCompositeSubscription, new Subscriber<List<GankEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loadFailed(isClean, e);

            }

            @Override
            public void onNext(List<GankEntity> results) {
                loadSuccess(isClean, results);

            }
        }));
        mCompositeSubscription.add(subscription);
    }

    private void loadSuccess(boolean clean, final List<GankEntity> entities) {
        if (clean) {
            dataList.clear();
        }
        dataList.addAll(entities);
        adapter.notifyDataSetChanged();
        setRequestDataRefresh(false);
    }

    private void loadFailed(boolean isClean, Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            List<GankEntity> list = mPresenter.getDataFromDb(type);
            Logger.i("Data from db:--->" + list.toString());
            if (list.size() > 0) {
                loadSuccess(isClean, list);
                return;
            }
        }
        Logger.i("Action1<Throwable>--->" + throwable.toString());
        throwable.printStackTrace();
        setRequestDataRefresh(false);
        Snackbar.make(coordinatorLayout, "加载失败请重试",
                Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestDataRefresh(true);
                requestDataRefresh();
            }
        }).show();
    }

    private void setupRecyclerView(List<GankEntity> gankEntities) {
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new GankRecyclerViewAdapter(this, gankEntities);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom = (adapter.getItemCount() - layoutManager.findLastCompletelyVisibleItemPosition()) <= 6;

                if (!mSwipeRefreshLayout.isRefreshing() && isBottom && dy > 0) {
                    if (!mIsFirstTimeTouchBottom) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mPresenter.loadData();
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

    @Override
    public boolean canBack() {
        return true;
    }
}

