package com.kingkung.train.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kingkung.train.ConfigActivity;
import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.contract.CitySelectContract;
import com.kingkung.train.presenter.CitySelectPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.CitySelectAdapter;
import com.kingkung.train.ui.adapter.CitySelectAdapter2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CitySelectActivity extends BaseActivity<CitySelectPresenter> implements CitySelectContract.View {

    @BindView(R.id.et_select_city)
    EditText etSelectCity;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ll_character_index)
    LinearLayout llCharacterIndex;

    private char firstChar = 'A';

    private CitySelectAdapter2 citySelectAdapter;

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

        Disposable disposabl = RxTextView.textChanges(etSelectCity)
                .skipInitialValue()
                .debounce(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
//                        citySelectAdapter.filter(charSequence.toString());
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        citySelectAdapter = new CitySelectAdapter2(this);
        recyclerView.setAdapter(citySelectAdapter);

        presenter.index();

        initCharacterIndex();
    }

    private void initCharacterIndex() {
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < 26; i++) {
            String character = String.valueOf((char) (firstChar + i));
            TextView tvCharacter = (TextView) inflater.inflate(R.layout.item_character_index, llCharacterIndex, false);
            tvCharacter.setText(character);
            llCharacterIndex.addView(tvCharacter);
            tvCharacter.setOnClickListener(v -> {
                int position = citySelectAdapter.getPosition(character);
                recyclerView.scrollToPosition(position);
            });
        }
    }

    @Override
    public void indexSucceed(String[] cityUrl) {
        presenter.cityCode(cityUrl[0]);
        presenter.hotCityCode(cityUrl[1]);
    }

    @Override
    public void cityCodeSucceed(List<City> cities) {
        citySelectAdapter.addItems(cities);
    }

    @Override
    public void hotCityCodeSucceed(List<City> cities) {
        citySelectAdapter.setHotCities(cities);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
