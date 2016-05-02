package com.gw.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.gw.ui.view.IPictureView;
import com.gw.util.RxPicture;
import com.gw.util.ToastUtil;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by gongwen on 2016/4/25.
 */
public class PicturePresenter extends BasePresenter<IPictureView> {
    public PicturePresenter(Context context, IPictureView view) {
        super(context, view);
    }

    public void saveImageToGallery(String mImageUrl,String mImageTitle) {
        Subscription s = RxPicture.saveImageAndGetPathObservable(mContext, mImageUrl, mImageTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShortToast(mContext, e.getMessage() + "\n再试试...");
                    }

                    @Override
                    public void onNext(Object o) {
                        File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                        String msg = String.format("图片已保存至 %s 文件夹",
                                appDir.getAbsolutePath());
                        ToastUtil.showShortToast(mContext, msg);
                    }
                });
    }

}
