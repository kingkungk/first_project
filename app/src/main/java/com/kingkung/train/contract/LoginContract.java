package com.kingkung.train.contract;

import android.graphics.Bitmap;

import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface LoginContract {
    interface View extends BaseContract.View {
        void captchaSuccess(Bitmap bitmap);

        void captchaCheckSuccess();

        void captchaCheckFaild();

        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void captcha();

        void captchaCheck(List<Integer> codes);

        void login(String userName, String password);
    }
}
