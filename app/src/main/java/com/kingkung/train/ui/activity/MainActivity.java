package com.kingkung.train.ui.activity;

import android.view.KeyEvent;

import com.kingkung.train.R;
import com.kingkung.train.contract.MainContract;
import com.kingkung.train.presenter.MainPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void create() {
    }

    @Override
    public void realBack() {
        onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            presenter.clickBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
