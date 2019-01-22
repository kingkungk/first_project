package com.kingkung.train.contract;

import android.content.Context;

import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface TrainContract {
    interface View extends BaseContract.View {
        void realBack();

        void uamtkSuccess(String newapptk);

        void uamtkFaild();

        void uamauthClientSuccess(String username);

        void queryTrainSuccess(List<TrainDetails> details);

        void checkUserSuccess();

        void submitOrderSuccess(TrainDetails detail);

        void initDcSuccess(TrainDetails detail);

        void getPassengerSuccess(TrainDetails detail);

        void checkOrderInfoSuccess(TrainDetails detail);

        void getQueueCountSuccess(TrainDetails detail);

        void confirmSingleForQueueSuccess(TrainDetails detail);

        void queryOrderWaitTimeSuccess(TrainDetails detail);

        void resultOrderForQueueSuccess();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void clickBack();

        void interval(long initialDelay, long period, Runnable r);

        void timer(long delay, Runnable r);

        void uamtk();

        void uamauthClient(String newapptk);

        void queryTrain(String date, String from, String to);

        void sendEmail(List<String> sendEmails, String title, String content);

        void checkUser();

        void submitOrder(TrainDetails detail);

        void initDc(TrainDetails detail);

        void getPassenger(TrainDetails detail);

        void checkOrderInfo(TrainDetails detail);

        void getQueueCount(TrainDetails detail);

        void confirmSingleForQueue(TrainDetails detail);

        void queryOrderWaitTime(TrainDetails detail);

        void resultOrderForQueue(TrainDetails detail);

        void writeFailedLog(Context context, String failedMsg);
    }
}
