package com.kingkung.train.bean.response;

import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public abstract class MessageListObserver<D> extends StatusObserver<D, List<String>> {

    public MessageListObserver(BaseContract.View view) {
        super(view);
    }

    @Override
    public void statusFailed(List<String> messages) {
        if (messages != null && !messages.isEmpty()) {
            failed(messages.get(0));
        }
    }
}
