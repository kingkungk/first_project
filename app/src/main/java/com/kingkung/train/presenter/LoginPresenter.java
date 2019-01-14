package com.kingkung.train.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kingkung.train.LoginActivity;
import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.response.Result;
import com.kingkung.train.contract.LoginContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private TrainApi api;

    @Inject
    public LoginPresenter(TrainApi api) {
        this.api = api;
    }

    @Override
    public void captcha() {
        Map<String, String> fields = new HashMap<>();
        fields.put("login_site", "E");
        fields.put("module", "login");
        fields.put("rand", "sjrand");
        Disposable disposable = api.captcha(fields)
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody body) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeStream(body.byteStream());
                        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * LoginActivity.SCALE,
                                bitmap.getHeight() * LoginActivity.SCALE, true);
                        return bitmap;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Bitmap>() {

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mView.captchaSuccess(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void captchaCheck(List<Integer> codes) {
        Map<String, String> fields = new HashMap<>();
        fields.put("login_site", "E");
        fields.put("rand", "sjrand");
        String codeStr = codes.toString();
        fields.put("answer", codeStr.substring(1, codeStr.length() - 1));
        Disposable disposable = api.captchaCheck(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Result>() {

                    @Override
                    public void onNext(Result s) {
                        int code = Integer.valueOf(s.getResult_code());
                        if (code == 4) {
                            mView.captchaCheckSuccess();
                        } else if (code == 5 || code == 7 || code == 8) { //5.验证码错误；7.验证码过期；8.验证码为空
                            mView.captchaCheckFaild();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void login(String userName, String password) {
        Map<String, String> fields = new HashMap<>();
        fields.put("username", userName);
        fields.put("password", password);
        fields.put("appid", "otn");
        Disposable disposable = api.login(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Result>() {

                    @Override
                    public void onNext(Result result) {
                        int code = Integer.parseInt(result.getResult_code());
                        if (code == 0) {
                            mView.loginSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscription(disposable);
    }
}