package com.kingkung.train.bean.response;

import com.kingkung.train.contract.base.BaseContract;

public abstract class DataObserver<D> extends ErrorObserver<D> {

    public DataObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void onNext(D d) {
        success(d);
    }

    public abstract void success(D d);
}
