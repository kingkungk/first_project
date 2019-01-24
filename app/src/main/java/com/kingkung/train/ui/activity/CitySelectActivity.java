package com.kingkung.train.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.contract.CitySelectContract;
import com.kingkung.train.presenter.CitySelectPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.CitySelectAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CitySelectActivity extends BaseActivity<CitySelectPresenter> implements CitySelectContract.View {

    @BindView(R.id.et_select_city)
    EditText etSelectCity;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CitySelectAdapter citySelectAdapter;

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
        Disposable disposabl = RxTextView.textChanges(etSelectCity)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        citySelectAdapter.filter(charSequence.toString());
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        citySelectAdapter = new CitySelectAdapter();
        recyclerView.setAdapter(citySelectAdapter);

        presenter.index();
    }

    @Override
    public void indexSucceed(String cityUrl) {
        presenter.cityCode(cityUrl);
    }

    @Override
    public void cityCodeSucceed(List<City> cities) {
        citySelectAdapter.addAll(cities);
    }
}
