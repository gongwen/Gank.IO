package com.gw.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gw.presenter.BasePresenter;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by gongwen on 2016/4/24.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;

    //等待 使用Dagger2 初始化
    protected abstract void initPresenter();

    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    protected CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mCompositeSubscription = new CompositeSubscription();
        //初始化Presenter
        initPresenter();
        checkPresenterIsNull();
    }

    protected void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //一旦调用了 CompositeSubscription.unsubscribe()，这个CompositeSubscription对象就不可用了,
        // 如果还想使用CompositeSubscription，就必须在创建一个新的对象了。
        mCompositeSubscription.unsubscribe();
    }

    protected abstract int getLayoutId();
}
