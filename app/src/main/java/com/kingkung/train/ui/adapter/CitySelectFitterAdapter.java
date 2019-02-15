package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.ui.activity.CitySelectActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitySelectFitterAdapter extends RecyclerView.Adapter<CitySelectFitterAdapter.ViewHolder> {

    private List<City> cities;

    private CitySelectActivity activity;

    public CitySelectFitterAdapter(CitySelectActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city_select,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        City city = cities.get(i);
        viewHolder.tvCityName.setText(city.name);

        viewHolder.itemView.setOnClickListener(v -> activity.selectCity(city));
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.size();
    }

    public void addAll(List<City> cityList) {
        if (cities == null) {
            cities = new ArrayList<>();
        } else {
            cities.clear();
        }
        cities.addAll(cityList);
        notifyDataSetChanged();
    }

    public void clear() {
        cities.clear();
        cities = null;
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
