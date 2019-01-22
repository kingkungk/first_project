package com.kingkung.train.contract.base;

public interface BaseContract {
    interface View {
        void showMsg(String msg);

        void failed(String failedMsg);

        void complete();
    }

    interface Presenter<T extends View> {
        void attachView(T view);

        void detachView();
    }
}
