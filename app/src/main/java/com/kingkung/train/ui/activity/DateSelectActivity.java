package com.kingkung.train.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingkung.train.R;
import com.kingkung.train.contract.EmptyContract;
import com.kingkung.train.presenter.EmptyPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;

import butterknife.BindView;

public class DateSelectActivity extends BaseActivity<EmptyPresenter> implements EmptyContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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
                return 0;
            }
        });
        recyclerView.setLayoutManager(manager);
    }
}
