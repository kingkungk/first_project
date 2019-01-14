package com.kingkung.train.bean.response;

import io.reactivex.observers.DisposableObserver;

public abstract class DataObserver<D> extends DisposableObserver<D> {
    @Override
    public void onNext(D d) {
        success(d);
    }

    public abstract void success(D d);

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
