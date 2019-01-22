package com.kingkung.train.ui.activity;

import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kingkung.train.R;
import com.kingkung.train.contract.FailedLogContract;
import com.kingkung.train.presenter.FailedLogPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;

import butterknife.BindView;

public class FailedLogActivity extends BaseActivity<FailedLogPresenter> implements FailedLogContract.View {

    @BindView(R.id.listView)
    ListView listView;

    private ArrayAdapter<String> messageAdapter;

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_failed_log;
    }

    @Override
    protected void create() {
        messageAdapter = new ArrayAdapter<>(this, R.layout.item_message);
        listView.setAdapter(messageAdapter);

        presenter.getFailedMsg(this);
    }

    @Override
    public void getFailedMsgSucceed(String failedMsg) {
        messageAdapter.add(Html.fromHtml(failedMsg).toString());
        messageAdapter.add("");
    }
}
