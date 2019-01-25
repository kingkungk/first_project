package com.kingkung.train;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingkung.train.bean.City;
import com.kingkung.train.bean.Config;
import com.kingkung.train.bean.Passenger;
import com.kingkung.train.contract.ConfigContract;
import com.kingkung.train.contract.EmptyContract;
import com.kingkung.train.presenter.ConfigPresenter;
import com.kingkung.train.presenter.EmptyPresenter;
import com.kingkung.train.ui.activity.CitySelectActivity;
import com.kingkung.train.ui.activity.TrainNoSelectActivity;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.PassengerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends BaseActivity<ConfigPresenter> implements ConfigContract.View {

    public final static String TAG = "ConfigActivity";

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
    @BindView(R.id.et_start_time_quantum)
    EditText etStartTimeQuantum;
    @BindView(R.id.et_refresh_interval)
    EditText etRefreshInterval;
    @BindView(R.id.et_timer_date)
    EditText etTimerDate;
    @BindView(R.id.et_email)
    EditText etEmail;

    public final static int FROM_STATION_REQUEST_CODE = 1;
    public final static int TO_STATION_REQUEST_CODE = 2;
    public final static int SELECT_STATION_RESULT_CODE = 3;
    public final static String STATION_KEY = "station_key";

    public final static String REQUEST_TYPE_KEY = "station_type_key";

    public final static int GO_LOGIN_REQUEST_CODE = 10;
    public final static int GO_LOGIN_RESULT_CODE = 11;

    public final static int SELECT_TRAIN_NO_REQUEST_CODE = 20;
    public final static int SELECT_TRAIN_NO_RESULT_CODE = 21;

    @BindView(R.id.tv_from_station)
    TextView tvFromStation;
    @BindView(R.id.tv_to_station)
    TextView tvToStation;
    @BindView(R.id.tv_passenger_name)
    TextView tvPassengerName;

    private Config config = new Config();

    private SharedPreferences configPreferences;

    private boolean isTrainActivity;

    private boolean isQueryPassenger = false;

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
        etStartTimeQuantum.setText(configPreferences.getString("start_time_quantum", resources.getString(R.string.start_time_quantum)));
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
        editor.putString("start_time_quantum", etStartTimeQuantum.getText().toString());
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
        intent.putStringArrayListExtra("start_time_quantum",
                new ArrayList<>(Arrays.asList(etStartTimeQuantum.getText().toString().split("-"))));
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

    @OnClick({R.id.tv_from_station, R.id.tv_to_station})
    public void selectStation(View view) {
        Intent intent = new Intent(this, CitySelectActivity.class);
        switch (view.getId()) {
            case R.id.tv_from_station:
                intent.putExtra(REQUEST_TYPE_KEY, FROM_STATION_REQUEST_CODE);
                startActivityForResult(intent, FROM_STATION_REQUEST_CODE);
                break;
            case R.id.tv_to_station:
                intent.putExtra(REQUEST_TYPE_KEY, TO_STATION_REQUEST_CODE);
                startActivityForResult(intent, TO_STATION_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SELECT_STATION_RESULT_CODE) {
            City city = data.getParcelableExtra(STATION_KEY);
            if (requestCode == FROM_STATION_REQUEST_CODE) {
                tvFromStation.setText(city.name);
                config.setFromCity(city);
            } else if (requestCode == TO_STATION_REQUEST_CODE) {
                tvToStation.setText(city.name);
                config.setToCity(city);
            }
        } else if (resultCode == GO_LOGIN_RESULT_CODE) {
            presenter.getPassenger();
        }
    }

    @OnClick(R.id.tv_passenger_name)
    public void selectPassengerName() {
        if (!isQueryPassenger) {
            isQueryPassenger = true;
            presenter.getPassenger();
        }
    }

    @OnClick(R.id.tv_train_no)
    public void selectTranNo() {
        City fromCity = config.getFromCity();
        if (fromCity == null) {
            showMsg("请选择出发站");
            return;
        }
        City toCity = config.getToCity();
        if (toCity == null) {
            showMsg("请选择到达站");
            return;
        }
        Intent intent = new Intent(this, TrainNoSelectActivity.class);
        intent.putExtra(TrainNoSelectActivity.TRAIN_DATE_KEY, "2019-01-26");
        intent.putExtra(TrainNoSelectActivity.FROM_STATION_KEY, config.getFromCity());
        intent.putExtra(TrainNoSelectActivity.TO_STATION_KEY, config.getToCity());
        startActivityForResult(intent, SELECT_TRAIN_NO_REQUEST_CODE);
    }

    @Override
    public void getPassengerSucceed(List<Passenger> passengers) {
        BottomSheetDialog passengerDialog = new BottomSheetDialog(this);
        View bottomView = getLayoutInflater().inflate(R.layout.passenger_select_dailog, null);
        PassengerHolder passengerHolder = new PassengerHolder();
        ButterKnife.bind(passengerHolder, bottomView);
        passengerHolder.init(passengerDialog, passengers);
        passengerDialog.setContentView(bottomView);
        passengerDialog.show();
        isQueryPassenger = false;
    }

    @Override
    public void getPassengerFailed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.TAG_KEY, TAG);
        startActivityForResult(intent, GO_LOGIN_REQUEST_CODE);
        isQueryPassenger = false;
    }

    class PassengerHolder {

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        Dialog dialog;
        List<Passenger> passengers;

        public void init(Dialog dialog, List<Passenger> passengers) {
            this.dialog = dialog;
            this.passengers = passengers;
            LinearLayoutManager manager = new LinearLayoutManager(dialog.getContext());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(new PassengerAdapter(passengers));

            List<Passenger> checkPassengers = config.getPassengers();
            if (checkPassengers == null || checkPassengers.isEmpty()) {
                return;
            }
            for (Passenger checkPassenger : checkPassengers) {
                for (Passenger passenger : passengers) {
                    if (checkPassenger.equals(passenger)) {
                        passenger.isCheck = true;
                    }
                }
            }
        }

        @OnClick(R.id.btn_confirm)
        public void confirm() {
            List<Passenger> checkPassengers = new ArrayList<>();
            for (Passenger passenger : passengers) {
                if (passenger.isCheck) {
                    checkPassengers.add(passenger);
                }
            }
            if (checkPassengers.isEmpty()) {
                showMsg("请选择联系人");
                return;
            }
            String checkPassengerStr = checkPassengers.toString();
            tvPassengerName.setText(checkPassengerStr.substring(1, checkPassengerStr.length() - 1));
            config.setPassengers(checkPassengers);
            dialog.cancel();
        }

        @OnClick(R.id.btn_cancel)
        public void cancel() {
            dialog.cancel();
        }
    }
}
