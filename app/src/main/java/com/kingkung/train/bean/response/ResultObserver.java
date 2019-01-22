package com.kingkung.train.bean.response;

import com.kingkung.train.bean.Result;
import com.kingkung.train.contract.base.BaseContract;

public abstract class ResultObserver<R extends Result> extends ErrorObserver<R> {

    public ResultObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void onNext(R r) {
        succeed(r);
    }

    abstract public void succeed(R r);
}
