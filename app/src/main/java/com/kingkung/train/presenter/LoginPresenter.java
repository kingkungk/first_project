package com.kingkung.train.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kingkung.train.LoginActivity;
import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.Result;
import com.kingkung.train.bean.UamtkResult;
import com.kingkung.train.bean.UserNameResult;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.bean.response.ResultObserver;
import com.kingkung.train.contract.LoginContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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
                .subscribeWith(new DataObserver<Bitmap>(mView) {

                    @Override
                    public void success(Bitmap bitmap) {
                        mView.captchaSuccess(bitmap);
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
                .subscribeWith(new DataObserver<Result>(mView) {

                    @Override
                    public void success(Result result) {
                        int code = Integer.valueOf(result.getResult_code());
                        if (code == 4) {
                            mView.captchaCheckSuccess();
                        } else { //5.验证码错误；7.验证码过期；8.验证码为空
                            mView.captchaCheckFailed(result.getResult_message());
                        }
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
                .subscribeWith(new DataObserver<Result>(mView) {
                    @Override
                    public void success(Result result) {
                        int code = Integer.parseInt(result.getResult_code());
                        if (code == 0) {
                            mView.loginSuccess();
                        } else {
                            mView.loginFailed(result.getResult_message());
                        }
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void uamtk() {
        Disposable disposable = api.uamtk("otn")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResultObserver<UamtkResult>(mView) {
                    @Override
                    public void succeed(UamtkResult uamtkResult) {
                        int code = Integer.valueOf(uamtkResult.getResult_code());
                        if (code == 0) {
                            mView.uamtkSuccess(uamtkResult.getNewapptk());
                        } else {  //1.登录验证没通过；7. ；4.用户已在他处登录；3.用户已注销
                            mView.uamtkFailed(uamtkResult.getResult_message());
                        }
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void uamauthClient(String newapptk) {
        Map<String, String> fields = new HashMap<>();
        fields.put("tk", newapptk);
        Disposable disposable = api.uamauthClient(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResultObserver<UserNameResult>(mView) {
                    @Override
                    public void succeed(UserNameResult userNameResult) {
                        int code = Integer.parseInt(userNameResult.getResult_code());
                        if (code == 0) {
                            mView.uamauthClientSuccess(userNameResult.getUsername());
                        }
                    }
                });
        addSubscription(disposable);
    }
}
