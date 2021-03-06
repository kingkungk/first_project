package com.kingkung.train.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkung.train.ConfigActivity;
import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.ui.activity.CitySelectActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.ViewHolder> {

    private CitySelectActivity activity;

    private List<City> cities = new ArrayList<>();

    public HotCityAdapter(CitySelectActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hot_city_select, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        City city = cities.get(position);
        viewHolder.tvCityName.setText(city.name);

        viewHolder.itemView.setOnClickListener(v -> activity.selectCity(city));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void loadItems(List<City> newCities) {
        if (!cities.isEmpty()) {
            return;
        }
        cities.addAll(newCities);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_city_name)
        TextView tvCityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
