package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkung.train.R;
import com.kingkung.train.bean.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitySelectAdapter extends RecyclerView.Adapter<CitySelectAdapter.ViewHolder> {

    private List<City> cities;

    private List<City> showCities;

    public CitySelectAdapter() {
        cities = new ArrayList<>();
        showCities = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city_select,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        City city = showCities.get(i);
        viewHolder.tvCityName.setText(city.name);
    }

    @Override
    public int getItemCount() {
        return showCities.size();
    }

    public void filter(String text) {
        showCities.clear();
        if (TextUtils.isEmpty(text)) {
            showCities.addAll(cities);
            return;
        }
        for (City city : cities) {
            if (city.firstSpell.contains(text)) {
                showCities.add(city);
            } else if (city.spell.contains(text)) {
                showCities.add(city);
            } else if (city.name.contains(text)) {
                showCities.add(city);
            }
        }
        notifyDataSetChanged();
    }

    public void addAll(List<City> cities) {
        this.cities.clear();
        this.cities.addAll(cities);
        showCities.clear();
        showCities.addAll(cities);
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
