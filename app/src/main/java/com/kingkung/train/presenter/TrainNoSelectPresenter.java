package com.kingkung.train.presenter;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.MessageReslut;
import com.kingkung.train.bean.TrainData;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.contract.TrainNoSelectContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TrainNoSelectPresenter extends BasePresenter<TrainNoSelectContract.View> implements TrainNoSelectContract.Presenter {

    private TrainApi api;

    @Inject
    public TrainNoSelectPresenter(TrainApi api) {
        this.api = api;
    }

    @Override
    public void queryTrain(String date, String fromCode, String toCode) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("leftTicketDTO.train_date", date);
        fields.put("leftTicketDTO.from_station", fromCode);
        fields.put("leftTicketDTO.to_station", toCode);
        fields.put("purpose_codes", "ADULT");
        Disposable disposable = api.queryTrain(fields)
                .map(new Function<MessageReslut<TrainData>, List<TrainDetails>>() {
                    @Override
                    public List<TrainDetails> apply(MessageReslut<TrainData> result) throws Exception {
                        List<TrainDetails> detailsList = new ArrayList<>();
                        if (!result.isStatus()) {
                            return detailsList;
                        }
                        List<String> queryResults = result.getData().getResult();
                        for (String queryResult : queryResults) {
                            String[] info = queryResult.split("\\|");
                            TrainDetails details = new TrainDetails();
                            details.trainNo = info[TrainDetails.INDEX_TRAIN_NO];
                            details.startStationCode = info[TrainDetails.INDEX_TRAIN_START_STATION_CODE];
                            details.endStationCode = info[TrainDetails.INDEX_TRAIN_END_STATION_CODE];
                            details.fromStationCode = info[TrainDetails.INDEX_TRAIN_FROM_STATION_CODE];
                            details.toStationCode = info[TrainDetails.INDEX_TRAIN_TO_STATION_CODE];
                            details.leaveTime = info[TrainDetails.INDEX_TRAIN_LEAVE_TIME];
                            details.arriveTime = info[TrainDetails.INDEX_TRAIN_ARRIVE_TIME];
                            details.totalConsume = info[TrainDetails.INDEX_TRAIN_TOTAL_CONSUME];
                            details.businessSeat = info[TrainDetails.INDEX_TRAIN_BUSINESS_SEAT];
                            details.firstClassSeat = info[TrainDetails.INDEX_TRAIN_FIRST_CLASS_SEAT];
                            details.secondClassSeat = info[TrainDetails.INDEX_TRAIN_SECOND_CLASS_SEAT];
                            details.advancedSoftSleep = info[TrainDetails.INDEX_TRAIN_ADVANCED_SOFT_SLEEP];
                            details.softSleep = info[TrainDetails.INDEX_TRAIN_SOFT_SLEEP];
                            details.moveSleep = info[TrainDetails.INDEX_TRAIN_MOVE_SLEEP];
                            details.hardSleep = info[TrainDetails.INDEX_TRAIN_HARD_SLEEP];
                            details.softSeat = info[TrainDetails.INDEX_TRAIN_SOFT_SEAT];
                            details.hardSeat = info[TrainDetails.INDEX_TRAIN_HARD_SEAT];
                            details.noSeat = info[TrainDetails.INDEX_TRAIN_NO_SEAT];
                            details.other = info[TrainDetails.INDEX_TRAIN_OTHER];
                            details.mark = info[TrainDetails.INDEX_TRAIN_MARK];
                            details.startStation = details.startStationCode;
                            details.endStation = details.endStationCode;
                            details.fromStation = details.fromStationCode;
                            details.toStation = details.toStationCode;
                            details.secretStr = info[TrainDetails.INDEX_SECRET_STR];
                            details.startDate = info[TrainDetails.INDEX_START_DATE];
                            details.mapSeatType();
                            detailsList.add(details);
                        }
                        return detailsList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<List<TrainDetails>>(mView) {
                    @Override
                    public void success(List<TrainDetails> details) {
                        mView.queryTrainSuccess(details);
                    }
                });
        addSubscription(disposable);
    }
}
