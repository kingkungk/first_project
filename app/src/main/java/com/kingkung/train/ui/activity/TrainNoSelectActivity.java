package com.kingkung.train.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.kingkung.train.ConfigActivity;
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

import static com.kingkung.train.ConfigActivity.SELECT_TRAIN_NO_KEY;

public class TrainNoSelectActivity extends BaseActivity<TrainNoSelectPresenter> implements TrainNoSelectContract.View {

    public final static String CHECKED_TRAIN_NO_KEY = "checked_train_no_key";
    public final static String TRAIN_DATE_KEY = "train_date_key";
    public final static String FROM_STATION_KEY = "from_station_key";
    public final static String TO_STATION_KEY = "to_station_key";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<TrainDetails> checkDetails;

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
        checkDetails = getIntent().getParcelableArrayListExtra(CHECKED_TRAIN_NO_KEY);

        Intent intent = getIntent();
        String trainDate = intent.getStringExtra(TRAIN_DATE_KEY);
        City fromStation = intent.getParcelableExtra(FROM_STATION_KEY);
        City toStation = intent.getParcelableExtra(TO_STATION_KEY);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trainNoSelectAdapter = new TrainNoSelectAdapter(fromStation.name, toStation.name);
        recyclerView.setAdapter(trainNoSelectAdapter);

        presenter.queryTrain(trainDate, fromStation.code, toStation.code);
    }

    @Override
    public void queryTrainSuccess(List<TrainDetails> details) {
        if (checkDetails != null && !checkDetails.isEmpty()) {
            for (TrainDetails detail : details) {
                if (checkDetails.contains(detail)) {
                    detail.isCheck = true;
                }
            }
        }
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

    @OnClick(R.id.btn_confirm)
    public void selectTraiinNo() {
        List<TrainDetails> details = trainNoSelectAdapter.getShowItem();
        if (details.isEmpty()) {
            showMsg("请选择车次");
            return;
        }
        ArrayList<TrainDetails> resultDetails = new ArrayList<>();
        for (TrainDetails detail : details) {
            if (detail.isCheck) {
                resultDetails.add(detail);
            }
        }
        if (resultDetails.isEmpty()) {
            showMsg("请选择车次");
            return;
        }
        Intent data = new Intent();
        data.putParcelableArrayListExtra(ConfigActivity.SELECT_TRAIN_NO_KEY, resultDetails);
        setResult(ConfigActivity.SELECT_TRAIN_NO_RESULT_CODE, data);
        finish();
    }
}
