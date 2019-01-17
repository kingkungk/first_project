package com.kingkung.train;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.EditText;

import com.kingkung.train.contract.EmptyContract;
import com.kingkung.train.presenter.EmptyPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfigActivity extends BaseActivity<EmptyPresenter> implements EmptyContract.View {

    @BindView(R.id.et_from_Station)
    EditText etFromStation;
    @BindView(R.id.et_to_Station)
    EditText etToStation;
    @BindView(R.id.et_passenger)
    EditText etPassenger;
    @BindView(R.id.et_train_no)
    EditText etTrainNo;
    @BindView(R.id.et_train_date)
    EditText etTrainDate;
    @BindView(R.id.et_refresh_interval)
    EditText etRefreshInterval;
    @BindView(R.id.et_timer_date)
    EditText etTimerDate;
    @BindView(R.id.et_email)
    EditText etEmail;

    private SharedPreferences configPreferences;

    private boolean isTrainActivity;

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_config;
    }

    @Override
    protected void create() {
        isTrainActivity = getIntent().getBooleanExtra("isTrainActivity", false);

        getFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment, new ConfigFragment())
                .commit();

        configPreferences = getSharedPreferences("configData", Context.MODE_PRIVATE);
        initViewData();
        etTimerDate.setText(TrainActivity.timerDateFormat.format(new Date()));
    }

    private void initViewData() {
        Resources resources = getResources();
        etFromStation.setText(configPreferences.getString("from_station", resources.getString(R.string.from_Station)));
        etToStation.setText(configPreferences.getString("to_station", resources.getString(R.string.to_Station)));
        etPassenger.setText(configPreferences.getString("passenger_name", resources.getString(R.string.passenger)));
        etTrainNo.setText(configPreferences.getString("train_no", resources.getString(R.string.train_no)));
        etTrainDate.setText(configPreferences.getString("train_date", resources.getString(R.string.train_date)));
        etRefreshInterval.setText(configPreferences.getString("refresh_interval", resources.getString(R.string.refresh_interval)));
        etEmail.setText(configPreferences.getString("send_email", resources.getString(R.string.email)));
    }

    @OnClick(R.id.btn_start)
    public void start() {
        SharedPreferences.Editor editor = configPreferences.edit();
        editor.putString("from_station", etFromStation.getText().toString());
        editor.putString("to_station", etToStation.getText().toString());
        editor.putString("passenger_name", etPassenger.getText().toString());
        editor.putString("train_no", etTrainNo.getText().toString());
        editor.putString("train_date", etTrainDate.getText().toString());
        editor.putString("refresh_interval", etRefreshInterval.getText().toString());
        editor.putString("send_email", etEmail.getText().toString());
        editor.apply();

        Intent intent = new Intent(this, TrainActivity.class);
        intent.putExtra("from_station", etFromStation.getText().toString());
        intent.putExtra("to_station", etToStation.getText().toString());
        intent.putStringArrayListExtra("passenger_name",
                new ArrayList<>(Arrays.asList(etPassenger.getText().toString().split(","))));
        intent.putStringArrayListExtra("train_no",
                new ArrayList<>(Arrays.asList(etTrainNo.getText().toString().split(","))));
        intent.putStringArrayListExtra("train_date",
                new ArrayList<>(Arrays.asList(etTrainDate.getText().toString().split(","))));
        intent.putExtra("refresh_interval", etRefreshInterval.getText().toString());
        intent.putExtra("timer_date", etTimerDate.getText().toString());
        intent.putStringArrayListExtra("send_email",
                new ArrayList<>(Arrays.asList(etEmail.getText().toString().split(","))));
        if (isTrainActivity) {
            setResult(200, intent);
        } else {
            startActivity(intent);
        }
        finish();
    }

    public static class ConfigFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_config);
        }
    }
}
