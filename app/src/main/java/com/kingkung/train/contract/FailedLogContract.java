package com.kingkung.train.contract;

import android.content.Context;

import com.kingkung.train.contract.base.BaseContract;

public interface FailedLogContract {
    interface View extends BaseContract.View {
        void getFailedMsgSucceed(String failedMsg);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void getFailedMsg(Context context);
    }
}
