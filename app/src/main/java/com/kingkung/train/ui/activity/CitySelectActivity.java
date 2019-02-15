package com.kingkung.train.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingkung.train.ConfigActivity;
import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.contract.CitySelectContract;
import com.kingkung.train.presenter.CitySelectPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.CitySelectAdapter;
import com.kingkung.train.ui.adapter.CitySelectFitterAdapter;
import com.kingkung.train.utils.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;

public class CitySelectActivity extends BaseActivity<CitySelectPresenter> implements CitySelectContract.View {

    @BindView(R.id.et_select_city)
    EditText etSelectCity;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.rl_city)
    RelativeLayout rlCity;
    @BindView(R.id.rl_select_city)
    RelativeLayout rlSelectCity;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ll_character_index)
    LinearLayout llCharacterIndex;

    @BindView(R.id.recyclerView_fitter)
    RecyclerView recyclerViewFitter;

    private CitySelectAdapter citySelectAdapter;

    private List<String> characters;

    private CitySelectFitterAdapter citySelectFitterAdapter;

    private List<City> oftenAndLocationCities;

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected void create() {
        int requestType = getIntent().getIntExtra(ConfigActivity.REQUEST_TYPE_KEY, 0);
        if (requestType == ConfigActivity.FROM_STATION_REQUEST_CODE) {
            tvTitle.setText("出发城市");
        } else if (requestType == ConfigActivity.TO_STATION_REQUEST_CODE) {
            tvTitle.setText("到达城市");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        citySelectAdapter = new CitySelectAdapter(this);
        recyclerView.addItemDecoration(new StickyHeaderDecoration());
        recyclerView.setAdapter(citySelectAdapter);

        recyclerViewFitter.setLayoutManager(new LinearLayoutManager(this));
        citySelectFitterAdapter = new CitySelectFitterAdapter(this);
        recyclerViewFitter.setAdapter(citySelectFitterAdapter);

        presenter.readerOftenAndLocation(this);

        presenter.citySelectListener(citySelectAdapter, etSelectCity);

        presenter.index();
    }

    @Override
    public void indexSucceed(String[] cityUrl) {
        presenter.cityCode(cityUrl[0]);
        presenter.hotCityCode(cityUrl[1]);
    }

    @Override
    public void cityCodeSucceed(List<City> cities) {
        citySelectAdapter.addItems(cities);

        LayoutInflater inflater = getLayoutInflater();
        characters = citySelectAdapter.getCharacter();
        for (String character : characters) {
            TextView tvCharacter = (TextView) inflater.inflate(R.layout.item_character_index, llCharacterIndex, false);
            tvCharacter.setText(character);
            llCharacterIndex.addView(tvCharacter);
        }
    }

    @OnTouch(R.id.ll_character_index)
    public boolean changeCharacter(View view, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            int height = view.getHeight();
            float y = event.getY();
            int characterIndex = (int) (y / (height / ((ViewGroup) view).getChildCount()));
            if (characterIndex >= characters.size()) {
                characterIndex = characters.size() - 1;
            } else if (characterIndex < 0) {
                characterIndex = 0;
            }
            int position = citySelectAdapter.getPosition(characters.get(characterIndex));
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
            if (position != firstVisibleItemPosition) {
                manager.scrollToPositionWithOffset(position, 0);
            }
        }
        return true;
    }

    @OnFocusChange(R.id.et_select_city)
    public void selectCityFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            rlCity.setVisibility(View.GONE);
            rlSelectCity.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hotCityCodeSucceed(List<City> cities) {
        citySelectAdapter.setHotCities(cities);
    }

    @Override
    public void citySelectCallBack(List<City> cities) {
        if (cities == null) {
            citySelectFitterAdapter.clear();
            rlCity.setVisibility(View.VISIBLE);
            rlSelectCity.setVisibility(View.GONE);
        } else {
            citySelectFitterAdapter.addAll(cities);
            if (rlCity.getVisibility() == View.VISIBLE
                    || rlSelectCity.getVisibility() == View.GONE) {
                rlCity.setVisibility(View.GONE);
                rlSelectCity.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void readerOftenAndLocationSucceed(List<City> cities) {
        oftenAndLocationCities = cities;
        citySelectAdapter.setOftenAndLocationCities(cities);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    public void selectCity(City city) {
        if (oftenAndLocationCities == null) {
            oftenAndLocationCities = new ArrayList<>();
        }
        int index = oftenAndLocationCities.indexOf(city);
        if (index > 0) {
            oftenAndLocationCities.remove(city);
            oftenAndLocationCities.add(0, city);
        } else if (index == -1) {
            oftenAndLocationCities.add(0, city);
        }
        if (index != 0) {
            presenter.writeOften(this, oftenAndLocationCities);
        }

        Intent data = new Intent();
        data.putExtra(ConfigActivity.STATION_KEY, city);
        setResult(ConfigActivity.SELECT_STATION_RESULT_CODE, data);
        finish();
    }
}
