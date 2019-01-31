package com.kingkung.train.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkung.train.ConfigActivity;
import com.kingkung.train.R;
import com.kingkung.train.bean.City;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitySelectAdapter2 extends CharacterDividedAdapter {

    private Activity activity;

    private HotCityItem hotCityItem;

    public static final int TYPE_CITY = 1;
    public static final int TYPE_HOT_CITY = 2;

    public CitySelectAdapter2(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected int getItemViewTypeForCharacterItem(int position) {
        if (hotCityItem == null) {
            return TYPE_CITY;
        } else {
            return position == 1 ? TYPE_HOT_CITY : TYPE_CITY;
        }
    }

    @Override
    public RecyclerView.ViewHolder createViewHolderForCharacterItem(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CITY) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_city_select, parent, false));
        } else if (viewType == TYPE_HOT_CITY) {
            return new HotViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hot_city, parent, false));
        }
        return null;
    }

    @Override
    protected void bindViewHolderForCharacterItem(RecyclerView.ViewHolder viewHolder, CharacterItem item) {
        if (viewHolder instanceof ViewHolder) {
            City city = (City) item;
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvCityName.setText(city.name);

            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra(ConfigActivity.STATION_KEY, city);
                activity.setResult(ConfigActivity.SELECT_STATION_RESULT_CODE, data);
                activity.finish();
            });
        } else if (viewHolder instanceof HotViewHolder) {
            HotCityItem hotCityItem = (HotCityItem) item;
            HotViewHolder hotViewHolder = (HotViewHolder) viewHolder;
            hotViewHolder.hotCityAdapter.loadItems(hotCityItem.hotCities);
        }
    }

    public void setHotCities(List<City> cities) {
        hotCityItem = new HotCityItem(cities);
        addGroup(hotCityItem);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_city_name)
        TextView tvCityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        HotCityAdapter hotCityAdapter;

        public HotViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            GridLayoutManager manager = new GridLayoutManager(itemView.getContext(), 3);
            recyclerView.setLayoutManager(manager);
            hotCityAdapter = new HotCityAdapter(activity);
            recyclerView.setAdapter(hotCityAdapter);
        }
    }

    class HotCityItem extends CharacterItem {

        List<City> hotCities;

        HotCityItem(List<City> hotCities) {
            this.hotCities = hotCities;
        }

        @Override
        public String getCharacter() {
            return "热门城市";
        }

        @Override
        public char getSortCharacter() {
            return '2';
        }
    }
}
