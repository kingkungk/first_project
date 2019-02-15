package com.kingkung.train.bean.response;

import com.kingkung.train.bean.StatusResult;
import com.kingkung.train.bean.SubmitStatusData;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public abstract class SubmitSatusObserver<D extends SubmitStatusData> extends MessageListObserver<D> {

    public SubmitSatusObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void onNext(StatusResult<D, List<String>> result) {
        if (result.isStatus()) {
            D data = result.getData();
            if (data.isSubmitStatus()) {
                success(data);
            } else {
                failed(data.getErrMsg());
            }
        } else {
            statusFailed(result.getMessages());
        }
    }
}
