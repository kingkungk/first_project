package com.kingkung.train.contract;

import com.kingkung.train.bean.TrainDay;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface DateSelectContract {
    interface View extends BaseContract.View {
        void initDateSucceed(List<TrainDay> trainDays);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void initDate(int showMonthCount, List<String> trainDates);
    }
}
