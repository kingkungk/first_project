package com.kingkung.train.presenter;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.response.MessageListObserver;
import com.kingkung.train.contract.ConfigContract;
import com.kingkung.train.presenter.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConfigPresenter extends BasePresenter<ConfigContract.View> implements ConfigContract.Presenter {
    private TrainApi api;

    @Inject
    public ConfigPresenter(TrainApi api) {
        this.api = api;
    }

    @Override
    public void getPassenger() {
        Disposable disposable = api.queryPassenger(1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<PassengerInfo.PassengerData>(mView) {
                    @Override
                    public void success(PassengerInfo.PassengerData passengerData) {
                        if (passengerData.flag) {
                            mView.getPassengerSucceed(passengerData.datas);
                        } else {
                            failed("获取旅客信息失败");
                        }
                    }

                    @Override
                    public void failed(String failedMsg) {
                        super.failed(failedMsg);
                        mView.getPassengerFailed();
                    }
                });
        addSubscription(disposable);
    }
}
