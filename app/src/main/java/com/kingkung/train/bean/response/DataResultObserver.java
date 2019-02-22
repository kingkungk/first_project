package com.kingkung.train.bean.response;

import com.kingkung.train.bean.DataResult;
import com.kingkung.train.contract.base.BaseContract;

public abstract class DataResultObserver<D> extends ResultObserver<DataResult<D>> {

    public DataResultObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void succeed(DataResult<D> dDataResult) {
        if (dDataResult.getResult_code() == 0) {
            succeed(dDataResult.getResult_data());
        }
    }

    abstract public void succeed(D d);
}
