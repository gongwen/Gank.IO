package com.gw.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gw.R;
import com.gw.api.Constants;
import com.gw.api.GankCategory;
import com.gw.api.helper.ApiHelper;
import com.gw.entity.GankEntity;
import com.gw.presenter.MainPresenter;
import com.gw.ui.adapter.MainRecyclerViewAdapter;
import com.gw.ui.base.SwipeRefreshBaseActivity;
import com.gw.ui.view.IMainView;
import com.gw.util.ActivityUtil;
import com.gw.util.DbUtil;
import com.orhanobut.logger.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class MainActivity extends SwipeRefreshBaseActivity<MainPresenter> implements IMainView, NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.title)
    TextView toolbarTitle;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private MainRecyclerViewAdapter adapter;
    private final List<GankEntity> dataList = new ArrayList<>();
    private boolean mIsFirstTimeTouchBottom = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText("福利");
        setupRecyclerView(dataList);
        //Darawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this, this);
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
                loadFailed(isClean,e);
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

    private void loadFailed(boolean isClean,Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            List<GankEntity> list=mPresenter.getDataFromDb();
            Logger.i("Data from db:--->"+list.toString());
            if(list.size()>0){
                loadSuccess(isClean,list);
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
        final StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MainRecyclerViewAdapter(this, gankEntities);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPositions(
                                new int[2])[1] >=
                                adapter.getItemCount() -
                                        Constants.PRELOAD_SIZE;
                if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
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
    public boolean onNavigationItemSelected(MenuItem item) {
        GankCategory type;
        switch (item.getItemId()) {
            case R.id.nav_android:
                type = GankCategory.Android;
                break;
            case R.id.nav_ios:
                type = GankCategory.iOS;
                break;
            case R.id.nav_web:
                type = GankCategory.前端;
                break;
            case R.id.nav_rest:
                type = GankCategory.休息视频;
                break;
            case R.id.nav_expand:
                type = GankCategory.拓展资源;
                break;
            case R.id.nav_recommend:
                type = GankCategory.瞎推荐;
                break;
            default:
                type = GankCategory.福利;
                break;
        }
        ActivityUtil.goGankActivity(this, type);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

