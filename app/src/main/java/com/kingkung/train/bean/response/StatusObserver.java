package com.kingkung.train.bean.response;

import com.kingkung.train.bean.StatusResult;
import com.kingkung.train.contract.base.BaseContract;

import io.reactivex.observers.DisposableObserver;

public abstract class StatusObserver<D> extends DisposableObserver<StatusResult<D>> {
    private BaseContract.View view;

    public StatusObserver(BaseContract.View view) {
        this.view = view;
    }


    @Override
    public void onNext(StatusResult<D> result) {
        boolean status = Boolean.parseBoolean(result.getStatus());
        if (status) {
            success(result.getData());
        }
    }

    public abstract void success(D d);

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
