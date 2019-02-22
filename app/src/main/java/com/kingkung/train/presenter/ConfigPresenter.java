package com.kingkung.train.presenter;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.Config;
import com.kingkung.train.bean.DataResult;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.bean.response.DataResultObserver;
import com.kingkung.train.bean.response.MessageListObserver;
import com.kingkung.train.contract.ConfigContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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

    @Override
    public void uploadConfig(String userName, String password, Config config) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, Object>> emitter) {
                Map<String, Object> fields = new HashMap<>();
                fields.put("userName", userName);
                fields.put("password", password);
                fields.put("fromCode", config.getFromCity().code);
                fields.put("toCode", config.getToCity().code);
                List<String> passengerIdNos = new ArrayList<>();
                List<PassengerInfo> passengers = config.getPassengers();
                for (PassengerInfo passengerInfo : passengers) {
                    passengerIdNos.add(passengerInfo.passenger_id_no);
                }
                fields.put("passengerIdNos", passengerIdNos);
                List<String> trainNos = new ArrayList<>();
                List<TrainDetails> trainDetails = config.getTrainDetails();
                for (TrainDetails trainDetail : trainDetails) {
                    trainNos.add(trainDetail.trainNo);
                }
                fields.put("trainNos", trainNos);
                fields.put("trainDates", config.getTrainDates());
                fields.put("emails", config.getEmails());
                emitter.onNext(fields);
            }
        }).flatMap(new Function<Map<String, Object>, ObservableSource<DataResult<Boolean>>>() {
            @Override
            public ObservableSource<DataResult<Boolean>> apply(Map<String, Object> stringStringMap) throws Exception {
                return api.uploadConfig(stringStringMap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataResultObserver<Boolean>(mView) {
                    @Override
                    public void succeed(Boolean aBoolean) {
                        mView.showMsg("结果：" + aBoolean);
                    }
                });
        addSubscription(disposable);
    }
}
