package com.gw.ui.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.gw.R;
import com.gw.presenter.PicturePresenter;
import com.gw.ui.base.ToolbarActivity;
import com.gw.ui.view.IPictureView;
import com.gw.util.LoadImageUtil;
import com.gw.util.ToastUtil;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gongwen on 2016/4/25.
 */
public class PictureActivity extends ToolbarActivity<PicturePresenter> implements IPictureView {
    private String mImageUrl, mImageTitle;
    private PhotoViewAttacher mPhotoViewAttacher;

    @Bind(R.id.picture)
    ImageView ivPicture;
    @Bind(R.id.title)
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        mImageUrl = getIntent().getStringExtra("ImageUrl");
        mImageTitle = getIntent().getStringExtra("ImageTitle");
    }

    private void initView() {
        LoadImageUtil.show(this, mImageUrl, new GlideDrawableImageViewTarget(ivPicture) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                if (mPhotoViewAttacher != null) {
                    mPhotoViewAttacher.update();
                } else {
                    setupPhotoAttacher();
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                ToastUtil.showShortToast(PictureActivity.this, "加载失败，请检查网络");
            }
        });
        setAppBarAlpha(0.7f);
        toolbarTitle.setText(mImageTitle);
    }

    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(ivPicture);
        mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                hideOrShowToolbar();
            }
        });
        mPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(PictureActivity.this)
                        .setMessage("保存到手机吗？")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.saveImageToGallery(mImageUrl, mImageTitle);
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mPresenter.saveImageToGallery(mImageUrl, mImageTitle);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhotoViewAttacher != null) {
            mPhotoViewAttacher.cleanup();
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PicturePresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    public boolean canBack() {
        return true;
    }

}
