package com.kingkung.train.contract;

import com.kingkung.train.bean.Passenger;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface ConfigContract {
    interface View extends BaseContract.View {
        void getPassengerSucceed(List<Passenger> passengers);

        void getPassengerFailed();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void getPassenger();
    }
}
