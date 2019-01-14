package com.kingkung.train.contract.base;

public interface BaseContract {
    interface View {
        void showMsg(String msg);

        void complete();

        void gotoSignIn();
    }

    interface Presenter<T extends View> {
        void attachView(T view);

        void detachView();
    }
}
