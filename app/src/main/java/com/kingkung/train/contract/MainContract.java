package com.kingkung.train.contract;

import com.kingkung.train.contract.base.BaseContract;

public interface MainContract {
    interface View extends BaseContract.View {
        void realBack();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void clickBack();
    }
}