package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kingkung.train.R;
import com.kingkung.train.bean.TrainDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class TrainNoSelectAdapter extends RecyclerView.Adapter<TrainNoSelectAdapter.ViewHolder> {

    private List<TrainDetails> trainDetailList;

    private List<TrainDetails> showTrainDetailList;

    public TrainNoSelectAdapter() {
        trainDetailList = new ArrayList<>();
        showTrainDetailList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrainNoSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_train_no_select, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainNoSelectAdapter.ViewHolder viewHolder, int i) {
        TrainDetails detail = showTrainDetailList.get(i);
        viewHolder.tvFromTime.setText(detail.leaveTime);
        viewHolder.tvFromStation.setText(detail.fromStation);
        viewHolder.tvToTime.setText(detail.arriveTime);
        viewHolder.tvToStation.setText(detail.toStation);
        viewHolder.tvTrainNo.setText(detail.trainNo);
        viewHolder.tvTime.setText(detail.totalConsume);

        viewHolder.cbTrainNo.setTag(detail);
        viewHolder.cbTrainNo.setChecked(detail.isCheck);
    }

    @Override
    public int getItemCount() {
        return showTrainDetailList.size();
    }

    public void addAll(List<TrainDetails> details) {
        trainDetailList.clear();
        trainDetailList.addAll(details);
        showTrainDetailList.clear();
        showTrainDetailList.addAll(details);
        notifyDataSetChanged();
    }

    public void addAllShow(List<TrainDetails> details) {
        showTrainDetailList.clear();
        showTrainDetailList.addAll(details);
        notifyDataSetChanged();
    }

    public List<TrainDetails> getAllItem() {
        return trainDetailList;
    }

    public List<TrainDetails> getShowItem() {
        return showTrainDetailList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_from_time)
        TextView tvFromTime;
        @BindView(R.id.tv_from_station)
        TextView tvFromStation;
        @BindView(R.id.tv_to_time)
        TextView tvToTime;
        @BindView(R.id.tv_to_station)
        TextView tvToStation;
        @BindView(R.id.tv_train_no)
        TextView tvTrainNo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.cb_train_no)
        CheckBox cbTrainNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnCheckedChanged(R.id.cb_train_no)
        public void checkPassenger(CompoundButton buttonView, boolean isChecked) {
            TrainDetails detail = (TrainDetails) buttonView.getTag();
            detail.isCheck = isChecked;
        }
    }
}
