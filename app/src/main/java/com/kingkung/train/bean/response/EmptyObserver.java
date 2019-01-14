package com.kingkung.train.bean.response;

import io.reactivex.observers.DisposableObserver;

public class EmptyObserver extends DisposableObserver<Object> {
    @Override
    public void onNext(Object o) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
