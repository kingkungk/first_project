package com.kingkung.train.presenter;

import com.kingkung.train.bean.TrainDay;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.contract.DateSelectContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DateSelectPresenter extends BasePresenter<DateSelectContract.View> implements DateSelectContract.Presenter {

    @Inject
    public DateSelectPresenter() {

    }

    @Override
    public void initDate(int showMonthCount, List<String> trainDates) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<List<TrainDay>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TrainDay>> emitter) throws ParseException {
                SimpleDateFormat format = TrainPresenter.trainDateFormat;
                List<Long> trainMill = new ArrayList<>();
                if (trainDates != null) {
                    for (String trainDate : trainDates) {
                        trainMill.add(format.parse(trainDate).getTime());
                    }
                }

                List<TrainDay> trainDays = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(format.parse(format.format(new Date())));
                int curYear = calendar.get(Calendar.YEAR);
                int curMonth = calendar.get(Calendar.MONTH);
                int curDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                while (true) {
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    if (year != curYear) {
                        if ((showMonthCount - (12 - curMonth)) == month) {
                            break;
                        }
                    } else if ((month - curMonth) == showMonthCount) {
                        break;
                    }
                    if (dayOfMonth == 1) {
                        for (int i = 1; i < dayOfWeek; i++) {
                            TrainDay day = new TrainDay();
                            day.setTimestamp(calendar.getTimeInMillis());
                            day.setDayOfMonth(0);
                            day.setDatType(TrainDay.DAY_NORM);
                            trainDays.add(day);
                        }
                    }
                    TrainDay day = new TrainDay();
                    if (year == curYear && month == curMonth) {
                        if (dayOfMonth < curDayOfMonth) {
                            day.setDatType(TrainDay.DAY_PAST);
                        } else if (dayOfMonth == curDayOfMonth) {
                            day.setDatType(TrainDay.DAY_TODAY);
                            if (trainMill.isEmpty()) {
                                day.setSelect(true);
                            }
                        } else {
                            day.setDatType(TrainDay.DAY_NORM);
                        }
                    } else {
                        day.setDatType(TrainDay.DAY_NORM);
                    }
                    if (dayOfWeek == 1 || dayOfWeek == 7) {
                        day.setDatType(day.getDatType() | TrainDay.DAY_WEEKEND);
                    }
                    long mill = calendar.getTimeInMillis();
                    if (trainMill.contains(mill)) {
                        day.setSelect(true);
                    }
                    day.setTimestamp(mill);
                    day.setDayOfMonth(dayOfMonth);
                    trainDays.add(day);
                    calendar.add(Calendar.DATE, 1);
                }
                emitter.onNext(trainDays);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<List<TrainDay>>(mView) {
                    @Override
                    public void success(List<TrainDay> trainDays) {
                        mView.initDateSucceed(trainDays);
                    }
                });
        addSubscription(disposable);
    }
}
