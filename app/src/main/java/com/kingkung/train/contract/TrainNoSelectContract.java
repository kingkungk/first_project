package com.kingkung.train.contract;

import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface TrainNoSelectContract {
    interface View extends BaseContract.View {
        void queryTrainSuccess(List<TrainDetails> details);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void queryTrain(String date, String from, String to);
    }
}
