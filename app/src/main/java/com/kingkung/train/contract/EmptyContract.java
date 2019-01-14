package com.kingkung.train.contract;

import com.kingkung.train.contract.base.BaseContract;

public interface EmptyContract {
    interface View extends BaseContract.View {

    }

    interface Presenter extends BaseContract.Presenter<View> {

    }
}
