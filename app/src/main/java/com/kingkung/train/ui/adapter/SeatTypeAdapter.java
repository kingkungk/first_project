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
import com.kingkung.train.TrainActivity;
import com.kingkung.train.bean.SeatType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class SeatTypeAdapter extends RecyclerView.Adapter<SeatTypeAdapter.ViewHolder> {

    private List<SeatType> seatTypes;
    private List<SeatType> selectSeatTypes;

    public SeatTypeAdapter(List<SeatType> seatTypes) {
        this.seatTypes = seatTypes;
        selectSeatTypes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_seat_type, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SeatType seatType = seatTypes.get(i);
        viewHolder.tvSeatTypeName.setText(seatType.name);

        viewHolder.cbSeatType.setTag(seatType);
        viewHolder.cbSeatType.setChecked(selectSeatTypes.contains(seatType));
    }

    @Override
    public int getItemCount() {
        return seatTypes.size();
    }

    public List<SeatType> getSelectSeatTypes() {
        return selectSeatTypes;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_seat_type_name)
        TextView tvSeatTypeName;
        @BindView(R.id.cb_seat_type)
        CheckBox cbSeatType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnCheckedChanged(R.id.cb_seat_type)
        public void selectSeatType(CompoundButton buttonView, boolean isChecked) {
            SeatType seatType = (SeatType) buttonView.getTag();
            if (isChecked) {
                selectSeatTypes.add(seatType);
            } else {
                selectSeatTypes.remove(seatType);
            }
        }
    }
}
