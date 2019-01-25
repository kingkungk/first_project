package com.kingkung.train.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.kingkung.train.R;
import com.kingkung.train.bean.City;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.contract.TrainNoSelectContract;
import com.kingkung.train.presenter.TrainNoSelectPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.TrainNoSelectAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class TrainNoSelectActivity extends BaseActivity<TrainNoSelectPresenter> implements TrainNoSelectContract.View {

    public final static String TRAIN_DATE_KEY = "train_date_key";
    public final static String FROM_STATION_KEY = "from_station_key";
    public final static String TO_STATION_KEY = "to_station_key";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private TrainNoSelectAdapter trainNoSelectAdapter;

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_train_no_select;
    }

    @Override
    protected void create() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trainNoSelectAdapter = new TrainNoSelectAdapter();
        recyclerView.setAdapter(trainNoSelectAdapter);

        Intent intent = getIntent();
        String trainDate = intent.getStringExtra(TRAIN_DATE_KEY);
        City fromStation = intent.getParcelableExtra(FROM_STATION_KEY);
        City toStation = intent.getParcelableExtra(TO_STATION_KEY);
        presenter.queryTrain(trainDate, fromStation.code, toStation.code);
    }

    @Override
    public void queryTrainSuccess(List<TrainDetails> details) {
        trainNoSelectAdapter.addAll(details);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnCheckedChanged({R.id.cb_g, R.id.cb_d, R.id.cb_k, R.id.cb_z, R.id.cb_t})
    public void filterTrainNo(CompoundButton buttonView, boolean isChecked) {
        List<TrainDetails> details = new ArrayList<>(trainNoSelectAdapter.getAllItem());
        Iterator<TrainDetails> it = details.iterator();
        while (it.hasNext()) {
            TrainDetails detail = it.next();
            if (!detail.trainNo.startsWith((String) buttonView.getTag())) {
                it.remove();
            }
        }
        List<TrainDetails> showDetails = new ArrayList<>(trainNoSelectAdapter.getShowItem());
        if (isChecked) {
            showDetails.addAll(details);
        } else {
            showDetails.removeAll(details);
        }
        Collections.sort(showDetails);
        trainNoSelectAdapter.addAllShow(showDetails);
    }
}
