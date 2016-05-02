package com.gw.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gw.R;
import com.gw.entity.GankEntity;
import com.gw.util.ActivityUtil;
import com.gw.util.StringStyleUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gongwen on 2016/1/25.
 */
public class GankRecyclerViewAdapter extends AnimRecyclerViewAdapter<GankRecyclerViewAdapter.ViewHolder> {

    private List<GankEntity> mList;
    private Context mContext;

    public GankRecyclerViewAdapter(Context mContext, List<GankEntity> list) {
        this.mContext = mContext;
        this.mList = list != null ? list : new ArrayList<GankEntity>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gank, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GankEntity entity = mList.get(position);

        holder.category.setText(entity.getType());
        SpannableStringBuilder builder = new SpannableStringBuilder(entity.getDesc())
                .append(StringStyleUtils.format(holder.gankTv.getContext(),
                        " (via. " + entity.getWho() + ")", R.style.ViaTextAppearance));
        CharSequence gankText = builder.subSequence(0, builder.length());
        holder.gankTv.setText(gankText);
        showItemAnim(holder.gankTv, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_category)
        TextView category;
        @Bind(R.id.tv_title)
        TextView gankTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
                   }

        @OnClick(R.id.ll_gank_parent)
        public void onGankActivity(View v) {
            GankEntity entity = mList.get(getLayoutPosition());
            ActivityUtil.goWebActivity(v.getContext(), entity.getUrl(), entity.getDesc());
        }
    }
}
