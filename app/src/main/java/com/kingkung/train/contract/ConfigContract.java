package com.kingkung.train.contract;

import android.content.Context;

import com.kingkung.train.bean.Config;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface ConfigContract {
    interface View extends BaseContract.View {
        void getPassengerSucceed(List<PassengerInfo> passengers);

        void getPassengerFailed();

        void uploadConfigSucceed();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void getPassenger();

        void uploadConfig(String userName, String password, Config config);
    }
}
