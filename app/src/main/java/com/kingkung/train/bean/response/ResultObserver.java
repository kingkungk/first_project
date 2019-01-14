package com.kingkung.train.bean.response;

import com.kingkung.train.contract.base.BaseContract;

import io.reactivex.observers.DisposableObserver;

public abstract class ResultObserver<R extends Result> extends DisposableObserver<R> {

    private BaseContract.View view;

    public ResultObserver(BaseContract.View view) {
        this.view = view;
    }

    @Override
    public void onNext(R r) {
        succeed(r);
    }

    @Override
    public void onError(Throwable e) {
        failed("请求失败");
    }

    @Override
    public void onComplete() {
        view.complete();
    }

    abstract public void succeed(R r);

    public void failed(String msg) {
        view.showMsg(msg);
    }
}
