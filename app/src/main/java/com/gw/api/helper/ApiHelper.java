package com.gw.api.helper;

import com.gw.MainApplication;
import com.gw.api.ApiException;
import com.gw.entity.base.Response;
import com.gw.util.ToastUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gongwen on 2016/4/25.
 */
public class ApiHelper {

    /**
     * 网络请求统一处理处
     */
    public static <T> Observable.Transformer<Response<T>, T> applySchedulers() {
        return (Observable.Transformer<Response<T>, T>) transformer;
    }

    /**
     * 创建观察者
     */
    public static <T> Subscriber newSubscriber(final CompositeSubscription mCompositeSubscription, final Subscriber<T> subscriber) {
        return new Subscriber<T>() {
            @Override
            public void onNext(T results) {
                if (!mCompositeSubscription.isUnsubscribed()) {
                    subscriber.onNext(results);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!mCompositeSubscription.isUnsubscribed()) {
                    /*if (e instanceof ApiException) {
                        ToastUtil.showShortToast(MainApplication.getApplication().getApplicationContext(), "请求接口数据失败");
                    } else {
                        ToastUtil.showShortToast(MainApplication.getApplication().getApplicationContext(), e.getMessage());
                    }*/
                    subscriber.onError(e);
                }
            }

            @Override
            public void onCompleted() {
                if (!mCompositeSubscription.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        };
    }

    private static final Observable.Transformer transformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1() {
                        @Override
                        public Object call(Object response) {
                            return flatResponse((Response<Object>) response);
                        }
                    })
                    ;
        }
    };

    private static <T> Observable<T> flatResponse(final Response<T> response) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    if (response.isSuccess()) {
                        subscriber.onNext(response.getResults());
                    } else {
                        subscriber.onError(new ApiException("服务器返回数据错误"));
                    }
                    subscriber.onCompleted();
                }
            }
        });
    }


}
