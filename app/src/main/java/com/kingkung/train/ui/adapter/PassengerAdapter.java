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
import com.kingkung.train.bean.Passenger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ViewHolder> {

    private List<Passenger> passengers;

    public PassengerAdapter(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_passenger, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Passenger passenger = passengers.get(i);
        viewHolder.tvPassengerName.setText(passenger.passenger_name);
        viewHolder.cbPassenger.setTag(passenger);
        viewHolder.cbPassenger.setChecked(passenger.isCheck);
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_passenger_name)
        TextView tvPassengerName;
        @BindView(R.id.cb_passenger)
        CheckBox cbPassenger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnCheckedChanged(R.id.cb_passenger)
        public void checkPassenger(CompoundButton buttonView, boolean isChecked) {
            Passenger passenger = (Passenger) cbPassenger.getTag();
            passenger.isCheck = isChecked;
        }
    }
}
