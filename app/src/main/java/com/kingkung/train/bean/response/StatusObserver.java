package com.kingkung.train.bean.response;

import com.kingkung.train.bean.StatusResult;
import com.kingkung.train.contract.base.BaseContract;

public abstract class StatusObserver<D, M> extends ErrorObserver<StatusResult<D, M>> {

    public StatusObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void onNext(StatusResult<D, M> result) {
        if (result.isStatus()) {
            success(result.getData());
        } else {
            statusFailed(result.getMessages());
        }
    }

    public abstract void statusFailed(M messages);

    public abstract void success(D d);
}
