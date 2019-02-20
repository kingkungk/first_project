package com.kingkung.train.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingkung.train.ConfigActivity;
import com.kingkung.train.R;
import com.kingkung.train.bean.TrainDay;
import com.kingkung.train.contract.DateSelectContract;
import com.kingkung.train.presenter.DateSelectPresenter;
import com.kingkung.train.presenter.TrainPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.DateDividedAdapter;
import com.kingkung.train.ui.adapter.DateSelectAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DateSelectActivity extends BaseActivity<DateSelectPresenter> implements DateSelectContract.View {

    public final static String CHECKED_TRAIN_DATE_KEY = "checked_train_date_key";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private DateSelectAdapter dateSelectAdapter;

    private int showMonthCount = 5;

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_date_select;
    }

    @Override
    protected void create() {
        GridLayoutManager manager = new GridLayoutManager(this, 7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (dateSelectAdapter.getItemViewType(i) == DateSelectAdapter.TYPE_DATE) {
                    return 7;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(manager);
        dateSelectAdapter = new DateSelectAdapter();
        recyclerView.setAdapter(dateSelectAdapter);

        List<String> trainDates = getIntent().getStringArrayListExtra(CHECKED_TRAIN_DATE_KEY);

        presenter.initDate(showMonthCount, trainDates);
    }

    @Override
    public void initDateSucceed(List<TrainDay> trainDays) {
        dateSelectAdapter.loadItems(trainDays);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_confirm)
    public void selectTrainDate() {
        ArrayList<String> trainDates = new ArrayList<>();
        List<DateDividedAdapter.TimedItem> items = dateSelectAdapter.getAllItem();
        for (DateDividedAdapter.TimedItem item : items) {
            TrainDay day = (TrainDay) item;
            if (day.isSelect()) {
                trainDates.add(TrainPresenter.trainDateFormat.format(new Date(day.getTimestamp())));
            }
        }
        Intent data = new Intent();
        data.putStringArrayListExtra(ConfigActivity.SELECT_TRAIN_DATE_KEY, trainDates);
        setResult(ConfigActivity.SELECT_TRAIN_DATE_RESULT_CODE, data);
        finish();
    }
}
