package com.kingkung.train.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.contract.FailedLogContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;

public class FailedLogPresenter extends BasePresenter<FailedLogContract.View> implements FailedLogContract.Presenter {
    private TrainApi api;

    @Inject
    public FailedLogPresenter(TrainApi api) {
        this.api = api;
    }

    @Override
    public void getFailedMsg(final Context context) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws IOException {
                File logFile = new File(context.getCacheDir(), "log.txt");
                if (!logFile.exists()) {
                    emitter.onNext("");
                    return;
                }
                BufferedReader br = new BufferedReader(new FileReader(logFile));
                String line;
                while ((line = br.readLine()) != null) {
                    emitter.onNext(line);
                }
                br.close();
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<String>(mView) {

                    @Override
                    public void success(String s) {
                        mView.getFailedMsgSucceed(s);
                    }
                });
        addSubscription(disposable);
    }
}
