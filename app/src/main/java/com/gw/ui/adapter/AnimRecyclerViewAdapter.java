package com.gw.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gw.R;

/**
 * 参考了 fython/ExpressHelper @{https://github.com/PaperAirplane-Dev-Team/ExpressHelper/blob/master/app%2Fsrc%2Fmain%2Fjava%2Finfo%2Fpapdt%2Fexpress%2Fhelper%2Fui%2Fcommon%2FMyRecyclerViewAdapter.java}
 */
public abstract class AnimRecyclerViewAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    private static final int DELAY = 138;
    private int mLastPosition = -1;
    private boolean firstLoad = true;

    public void showItemAnim(final View view, final int position) {
        final Context context = view.getContext();
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > mLastPosition) {
            if (firstLoad) {
                view.setAlpha(0f);
                view.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Animation animation = AnimationUtils.loadAnimation(
                                        context,
                                        R.anim.slide_in_right
                                );
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        view.setAlpha(1f);
                                        firstLoad = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        firstLoad = false;
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                animation.setFillAfter(true);
                                view.startAnimation(animation);
                            }
                        }
                        , position * DELAY);
            } else {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
                view.startAnimation(animation);
            }
            mLastPosition = position;
        }
    }
}
