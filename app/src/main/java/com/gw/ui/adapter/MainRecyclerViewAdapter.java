package com.gw.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.gw.R;
import com.gw.entity.GankEntity;
import com.gw.util.ActivityUtil;
import com.gw.util.DateUtils;
import com.gw.ui.widget.RatioImageView;
import com.gw.util.LoadImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gongwen on 2016/1/25.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<GankEntity> mList;
    private Context mContext;

    public MainRecyclerViewAdapter(Context mContext, List<GankEntity> list) {
        this.mContext = mContext;
        this.mList = list != null ? list : new ArrayList<GankEntity>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meizhi, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GankEntity entity = mList.get(position);
        int limit = 48;
        String text = entity.getDesc().length() > limit ? entity.getDesc().substring(0, limit) +
                "..." : entity.getDesc();
        holder.entity = entity;
        if (!TextUtils.isEmpty(text)) {
            holder.tvDes.setVisibility(View.VISIBLE);
            holder.tvDes.setText(text);
        } else {
            holder.tvDes.setVisibility(View.GONE);
        }
        holder.tvTime.setText(DateUtils.toDate(entity.getPublishedAt()));
        LoadImageUtil.show(mContext, entity.getUrl(), holder.ivMeizhi, new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {
                if (!holder.card.isShown()) {
                    holder.card.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.iv_meizhi)
        RatioImageView ivMeizhi;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_des)
        TextView tvDes;

        View card;
        GankEntity entity;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
            ivMeizhi.setOnClickListener(this);
            tvDes.setOnClickListener(this);
            ivMeizhi.setOriginalSize(50, 50);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_meizhi:
                    ActivityUtil.goPictureActivity(mContext, entity.getUrl(), DateUtils.toDate(entity.getPublishedAt()));
                    break;
                case R.id.tv_des:
                    ActivityUtil.goWebActivity(mContext, entity.getVideoUrl(), entity.getDesc());
                    break;
            }
        }
    }
}
