package com.kingkung.train.bean.response;

import com.google.gson.JsonParseException;
import com.kingkung.train.contract.base.BaseContract;

import io.reactivex.observers.DisposableObserver;

public abstract class ErrorObserver<D> extends DisposableObserver<D> {

    protected BaseContract.View view;

    public ErrorObserver(BaseContract.View view) {
        this.view = view;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof JsonParseException) {
            failed("数据解析失败");
        } else {
            failed("请求失败");
        }
    }

    public void failed(String failedMsg) {
        view.showMsg(failedMsg);
    }

    @Override
    public void onComplete() {
        view.complete();
    }
}
