package com.kingkung.train.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.ui.activity.CitySelectActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitySelectAdapter extends CharacterDividedAdapter {

    private CitySelectActivity activity;

    public static final int TYPE_CITY = 1;
    public static final int TYPE_HOT_CITY = 2;

    public CitySelectAdapter(CitySelectActivity activity) {
        this.activity = activity;
    }

    @Override
    protected int getItemViewTypeForCharacterItem(Pair<ItemGroup, Integer> pair) {
        char sortCharacter = pair.first.mSortCharacter;
        if (sortCharacter >= 'a' && sortCharacter <= 'z') {
            return TYPE_CITY;
        } else {
            return TYPE_HOT_CITY;
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

            holder.itemView.setOnClickListener(v -> activity.selectCity(city));
        } else if (viewHolder instanceof HotViewHolder) {
            TopCityItem topCityItem = (TopCityItem) item;
            HotViewHolder hotViewHolder = (HotViewHolder) viewHolder;
            hotViewHolder.hotCityAdapter.loadItems(topCityItem.hotCities);
        }
    }

    public void setHotCities(List<City> cities) {
        addGroup(new HotCityItem(cities));
    }

    public void setOftenAndLocationCities(List<City> cities) {
        addGroup(new OftenAndLocationItem(cities));
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

    abstract class TopCityItem extends CharacterItem {
        List<City> hotCities;

        TopCityItem(List<City> hotCities) {
            this.hotCities = hotCities;
        }
    }

    class HotCityItem extends TopCityItem {

        HotCityItem(List<City> hotCities) {
            super(hotCities);
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

    class OftenAndLocationItem extends TopCityItem {

        OftenAndLocationItem(List<City> hotCities) {
            super(hotCities);
        }

        @Override
        public String getCharacter() {
            return "当前/历史";
        }

        @Override
        public char getSortCharacter() {
            return '1';
        }
    }
}
