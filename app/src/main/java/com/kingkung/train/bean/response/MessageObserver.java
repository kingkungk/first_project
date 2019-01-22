package com.kingkung.train.bean.response;

import android.text.TextUtils;

import com.kingkung.train.contract.base.BaseContract;

public abstract class MessageObserver<D> extends StatusObserver<D, String> {

    public MessageObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void statusFailed(String messages) {
        if (!TextUtils.isEmpty(messages)) {
            failed(messages);
        }
    }
}
