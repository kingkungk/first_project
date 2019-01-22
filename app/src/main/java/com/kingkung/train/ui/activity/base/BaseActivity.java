package com.kingkung.train.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kingkung.train.app.TrainApplication;
import com.kingkung.train.component.ActivityComponent;
import com.kingkung.train.component.DaggerActivityComponent;
import com.kingkung.train.contract.base.BaseContract;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BaseContract.Presenter> extends AppCompatActivity implements BaseContract.View {

    @Inject
    protected P presenter;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        inject();
        attach();
        create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detach();
    }

    protected void attach() {
        unbinder = ButterKnife.bind(this);
        presenter.attachView(this);
    }

    protected void detach() {
        unbinder.unbind();
        presenter.detachView();
    }

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(TrainApplication.getApplication().getAppComponent())
                .build();
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed(String failedMsg) {
        showMsg(failedMsg);
    }

    @Override
    public void complete() {

    }

    protected abstract void inject();

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void create();
}
