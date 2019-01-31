package com.kingkung.train;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kingkung.train.contract.LoginContract;
import com.kingkung.train.presenter.LoginPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.iv_code)
    ImageView ivCode;

    public final static int SCALE = 3;
    public List<Integer> codes = new ArrayList<>();

    int[][][] coordinates = new int[][][]{{{40, 40}, {40, 110}}, {{110, 40}, {110, 110}}, {{180, 40}, {180, 110}}, {{250, 40}, {250, 110}}};

    private String userName;
    private String password;

    public final static String TAG_KEY = "tag_key";
    private String tag;

    private SharedPreferences sp;

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void create() {
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        tag = getIntent().getStringExtra(TAG_KEY);

        etUsername.setText(sp.getString("username", ""));
        etPassword.setText(sp.getString("password", ""));

        presenter.captcha();
    }

    @OnClick(R.id.btn_go_logiin)
    public void login() {
        userName = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            showMsg("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showMsg("密码不能为空");
            return;
        }
        if (codes.isEmpty()) {
            showMsg("请填写验证码");
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userName);
        editor.putString("password", password);
        editor.commit();

        presenter.captchaCheck(codes);
    }

    @OnTouch(R.id.iv_code)
    public boolean clickCode(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int xPonit = 0;
            int yPonit = 0;
            int width = v.getWidth();
            int height = v.getHeight();
            float x = event.getX();
            float y = event.getY();
            float widthWeight = width / 4f;
            float heightWeight = height / 2f;
            if (x < widthWeight) {
                xPonit = 0;
            } else if (x < widthWeight * 2) {
                xPonit = 1;
            } else if (x < widthWeight * 3) {
                xPonit = 2;
            } else if (x < widthWeight * 4) {
                xPonit = 3;
            }
            if (y < heightWeight) {
                yPonit = 0;
            } else if (y < heightWeight * 2) {
                yPonit = 1;
            }
            int[] coord = coordinates[xPonit][yPonit];
            codes.add(coord[0]);
            codes.add(coord[1]);
        }
        return true;
    }

    @Override
    public void captchaSuccess(Bitmap bitmap) {
        codes.clear();
        ivCode.setImageBitmap(bitmap);
    }

    @Override
    public void captchaCheckSuccess() {
        presenter.login(userName, password);
    }

    @Override
    public void captchaCheckFaild() {
        presenter.captcha();
    }

    @Override
    public void loginSuccess() {
        if (tag.equals(ConfigActivity.TAG)) {
            presenter.uamtk();
        } else {
            finish();
        }
    }

    @Override
    public void uamtkSuccess(String newapptk) {
        presenter.uamauthClient(newapptk);
    }

    @Override
    public void uamtkFaild() {

    }

    @Override
    public void uamauthClientSuccess(String username) {
        setResult(ConfigActivity.GO_LOGIN_RESULT_CODE);
        finish();
    }
}

