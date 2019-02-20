package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingkung.train.R;
import com.kingkung.train.bean.TrainDay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateSelectAdapter extends DateDividedAdapter {

    @Override
    protected RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_select, parent, false));
    }

    @Override
    protected void bindViewHolderForTimedItem(RecyclerView.ViewHolder viewHolder, TimedItem item) {
        ViewHolder holder = (ViewHolder) viewHolder;
        TrainDay day = (TrainDay) item;
        if ((day.getDatType() & TrainDay.DAY_TODAY) != 0) {
            holder.tvDate.setText("今天");
        } else {
            holder.tvDate.setText(day.getDayOfMonth() > 0 ? String.valueOf(day.getDayOfMonth()) : "");
        }
        initTvDateColor(day, holder.tvDate);
        if (day.isSelect()) {
            holder.tvDate.setTextColor(0xFFFFFFFF);
            holder.rlContent.setBackgroundResource(R.drawable.date_select);
        } else {
            holder.rlContent.setBackgroundColor(0xFFFFFFFF);
        }
        holder.rlContent.setOnClickListener(v -> {
            if ((day.getDatType() & TrainDay.DAY_PAST) != 0) {
                return;
            }
            if (day.isSelect()) {
                initTvDateColor(day, holder.tvDate);
                day.setSelect(false);
                holder.rlContent.setBackgroundColor(0xFFFFFFFF);
            } else {
                holder.tvDate.setTextColor(0xFFFFFFFF);
                day.setSelect(true);
                holder.rlContent.setBackgroundResource(R.drawable.date_select);
            }
        });
    }

    private void initTvDateColor(TrainDay day, TextView tvDate) {
        if ((day.getDatType() & TrainDay.DAY_WEEKEND) != 0) {
            if ((day.getDatType() & TrainDay.DAY_PAST) != 0) {
                tvDate.setTextColor(0x66FF0000);
            } else {
                tvDate.setTextColor(0xFFFF0000);
            }
        } else if ((day.getDatType() & TrainDay.DAY_PAST) != 0) {
            tvDate.setTextColor(0x66999999);
        } else if ((day.getDatType() & TrainDay.DAY_TODAY) != 0) {
            tvDate.setTextColor(0xFF5F76F7);
        } else if ((day.getDatType() & TrainDay.DAY_NORM) != 0) {
            tvDate.setTextColor(0xFF999999);
        }
    }

    @Override
    protected int getTimedItemViewResId() {
        return R.layout.date_date_view;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_content)
        RelativeLayout rlContent;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
