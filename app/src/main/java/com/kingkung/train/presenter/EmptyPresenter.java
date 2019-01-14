package com.kingkung.train.presenter;

import com.kingkung.train.contract.EmptyContract;
import com.kingkung.train.presenter.base.BasePresenter;

import javax.inject.Inject;

public class EmptyPresenter extends BasePresenter<EmptyContract.View> implements EmptyContract.Presenter {

    @Inject
    public EmptyPresenter() {

    }
}
