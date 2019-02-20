package com.kingkung.train;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kingkung.train.bean.City;
import com.kingkung.train.bean.Config;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.contract.ConfigContract;
import com.kingkung.train.presenter.ConfigPresenter;
import com.kingkung.train.ui.activity.CitySelectActivity;
import com.kingkung.train.ui.activity.DateSelectActivity;
import com.kingkung.train.ui.activity.TrainNoSelectActivity;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.EmailAdapter;
import com.kingkung.train.ui.adapter.PassengerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends BaseActivity<ConfigPresenter> implements ConfigContract.View {

    public final static String TAG = "ConfigActivity";

    public final static int FROM_STATION_REQUEST_CODE = 1;
    public final static int TO_STATION_REQUEST_CODE = 2;
    public final static int SELECT_STATION_RESULT_CODE = 3;
    public final static String STATION_KEY = "station_key";

    public final static String REQUEST_TYPE_KEY = "station_type_key";

    public final static int GO_LOGIN_REQUEST_CODE = 10;
    public final static int GO_LOGIN_RESULT_CODE = 11;

    public final static int SELECT_TRAIN_NO_REQUEST_CODE = 20;
    public final static int SELECT_TRAIN_NO_RESULT_CODE = 21;
    public final static String SELECT_TRAIN_NO_KEY = "select_train_no_key";

    public final static int SELECT_TRAIN_DATE_REQUEST_CODE = 30;
    public final static int SELECT_TRAIN_DATE_RESULT_CODE = 31;
    public final static String SELECT_TRAIN_DATE_KEY = "select_train_date_key";

    public final static String CONFIG_FILE_NAME = "config";

    @BindView(R.id.tv_from_station)
    TextView tvFromStation;
    @BindView(R.id.tv_to_station)
    TextView tvToStation;
    @BindView(R.id.tv_passenger_name)
    TextView tvPassengerName;
    @BindView(R.id.tv_train_date)
    TextView tvTrainDate;
    @BindView(R.id.tv_train_no)
    TextView tvTrainNo;
    @BindView(R.id.tv_refresh_interval)
    TextView tvRefreshInterval;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    private Config config;

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
        initConfigInfo();
    }

    private void initConfigInfo() {
        readConfigJson();
        if (config == null) {
            config = new Config();
        }

        City fromCity = config.getFromCity();
        if (fromCity != null) {
            tvFromStation.setText(fromCity.name);
        }
        City toCity = config.getToCity();
        if (toCity != null) {
            tvToStation.setText(toCity.name);
        }

        List<PassengerInfo> passenger = config.getPassengers();
        if (passenger != null && !passenger.isEmpty()) {
            String passengerStr = passenger.toString();
            tvPassengerName.setText(passengerStr.substring(1, passengerStr.length() - 1));
        }

        List<String> trainDates = config.getTrainDates();
        if (trainDates != null && !trainDates.isEmpty()) {
            String trainDateStr = trainDates.toString();
            tvTrainDate.setText(trainDateStr.substring(1, trainDateStr.length() - 1));
        }

        List<TrainDetails> details = config.getTrainDetails();
        if (details != null && !details.isEmpty()) {
            String trainNoStr = details.toString();
            tvTrainNo.setText(trainNoStr.substring(1, trainNoStr.length() - 1));
        }

        int refreshInterval = config.getRefreshInterval();
        tvRefreshInterval.setText(String.valueOf(refreshInterval));

        List<String> emails = config.getEmails();
        if (emails != null && !emails.isEmpty()) {
            String emailStr = emails.toString();
            tvEmail.setText(emailStr.substring(1, emailStr.length() - 1));
        }
    }

    @OnClick(R.id.btn_start)
    public void start() {
        writeConfigJson();
        Intent intent = new Intent(this, TrainActivity.class);
        intent.putExtra("config", config);
        startActivity(intent);
        finish();
    }

    public void writeConfigJson() {
        try {
            FileWriter fileWriter = new FileWriter(new File(getFilesDir(), CONFIG_FILE_NAME));
            new Gson().toJson(config, Config.class, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readConfigJson() {
        try {
            FileReader fileReader = new FileReader(new File(getFilesDir(), CONFIG_FILE_NAME));
            config = new Gson().fromJson(fileReader, Config.class);
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        } else if (resultCode == SELECT_TRAIN_NO_RESULT_CODE) {
            List<TrainDetails> details = data.getParcelableArrayListExtra(SELECT_TRAIN_NO_KEY);
            config.setTrainDetails(details);
            String trainNosStr = details.toString();
            tvTrainNo.setText(trainNosStr.substring(1, trainNosStr.length() - 1));
        } else if (resultCode == SELECT_TRAIN_DATE_RESULT_CODE) {
            List<String> trainDate = data.getStringArrayListExtra(SELECT_TRAIN_DATE_KEY);
            config.setTrainDates(trainDate);
            String trainDateStr = trainDate.toString();
            tvTrainDate.setText(trainDateStr.substring(1, trainDateStr.length() - 1));
        }
    }

    @OnClick(R.id.tv_passenger_name)
    public void selectPassengerName() {
        if (!isQueryPassenger) {
            isQueryPassenger = true;
            presenter.getPassenger();
        }
    }

    @OnClick(R.id.tv_train_date)
    public void selectTrainDate() {
        Intent intent = new Intent(this, DateSelectActivity.class);
        List<String> trainDates = config.getTrainDates();
        if (trainDates != null && !trainDates.isEmpty()) {
            intent.putStringArrayListExtra(DateSelectActivity.CHECKED_TRAIN_DATE_KEY, (ArrayList<String>) trainDates);
        }
        startActivityForResult(intent, SELECT_TRAIN_DATE_REQUEST_CODE);
    }

    @OnClick(R.id.tv_email)
    public void selectEmail() {
        EmailHolder emailHolder = new EmailHolder();
        emailHolder.show(this);
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
        List<String> trainDates = config.getTrainDates();
        if (trainDates == null || trainDates.isEmpty()) {
            showMsg("请选在出发日期");
            return;
        }
        Intent intent = new Intent(this, TrainNoSelectActivity.class);
        List<TrainDetails> details = config.getTrainDetails();
        if (details != null && !details.isEmpty()) {
            intent.putParcelableArrayListExtra(TrainNoSelectActivity.CHECKED_TRAIN_NO_KEY, (ArrayList<? extends Parcelable>) details);
        }
        intent.putExtra(TrainNoSelectActivity.TRAIN_DATE_KEY, trainDates.get(0));
        intent.putExtra(TrainNoSelectActivity.FROM_STATION_KEY, config.getFromCity());
        intent.putExtra(TrainNoSelectActivity.TO_STATION_KEY, config.getToCity());
        startActivityForResult(intent, SELECT_TRAIN_NO_REQUEST_CODE);
    }

    @Override
    public void getPassengerSucceed(List<PassengerInfo> passengers) {
        PassengerHolder passengerHolder = new PassengerHolder();
        passengerHolder.show(this, passengers);
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
        List<PassengerInfo> passengers;

        public void show(Context context, List<PassengerInfo> passengers) {
            this.passengers = passengers;
            List<PassengerInfo> checkPassengers = config.getPassengers();
            if (checkPassengers != null && !checkPassengers.isEmpty()) {
                for (PassengerInfo checkPassenger : checkPassengers) {
                    int index = passengers.indexOf(checkPassenger);
                    if (index != -1) {
                        passengers.get(index).isCheck = true;
                    }
                }
            }

            dialog = new BottomSheetDialog(context);
            View bottomView = getLayoutInflater().inflate(R.layout.passenger_select_dailog, null);
            ButterKnife.bind(this, bottomView);

            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(new PassengerAdapter(passengers));

            dialog.setContentView(bottomView);
            dialog.show();
        }

        @OnClick(R.id.btn_confirm)
        public void confirm() {
            List<PassengerInfo> checkPassengers = new ArrayList<>();
            List<String> checkEmails = new ArrayList<>();
            for (PassengerInfo passenger : passengers) {
                if (passenger.isCheck) {
                    checkPassengers.add(passenger);
                    if (!TextUtils.isEmpty(passenger.email)) {
                        checkEmails.add(passenger.email);
                    }
                }
            }
            if (checkPassengers.isEmpty()) {
                showMsg("请选择联系人");
                return;
            }
            String checkPassengerStr = checkPassengers.toString();
            tvPassengerName.setText(checkPassengerStr.substring(1, checkPassengerStr.length() - 1));
            config.setPassengers(checkPassengers);

            String checkEmailStr = checkEmails.toString();
            tvEmail.setText(checkEmailStr.substring(1, checkEmailStr.length() - 1));
            config.setEmails(checkEmails);

            dialog.cancel();
        }

        @OnClick(R.id.btn_cancel)
        public void cancel() {
            dialog.cancel();
        }
    }

    class EmailHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        Dialog dialog;
        Map<String, Boolean> emailMap = new HashMap<>();

        public void show(Context context) {
            List<String> emails = config.getEmails();
            if (emails == null || emails.isEmpty()) {
                showMsg("请先选择购票人姓名");
                return;
            }
            List<PassengerInfo> passengers = config.getPassengers();
            for (PassengerInfo passenger : passengers) {
                if (!TextUtils.isEmpty(passenger.email)) {
                    emailMap.put(passenger.email, false);
                }
            }
            for (String email : emails) {
                emailMap.put(email, true);
            }

            dialog = new BottomSheetDialog(context);
            View bottomView = getLayoutInflater().inflate(R.layout.email_select_dailog, null);
            ButterKnife.bind(this, bottomView);

            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(new EmailAdapter(emailMap));

            dialog.setContentView(bottomView);
            dialog.show();
        }

        @OnClick(R.id.btn_confirm)
        public void confirm() {
            List<String> resultEmail = new ArrayList<>();
            Set<Map.Entry<String, Boolean>> set = emailMap.entrySet();
            Iterator<Map.Entry<String, Boolean>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Boolean> entry = it.next();
                String email = entry.getKey();
                Boolean isCheck = entry.getValue();
                if (isCheck) {
                    resultEmail.add(email);
                }
            }

            if (resultEmail.isEmpty()) {
                showMsg("请选择通知邮箱");
                return;
            }

            String resultEmailStr = resultEmail.toString();
            tvEmail.setText(resultEmailStr.substring(1, resultEmailStr.length() - 1));
            config.setEmails(resultEmail);

            dialog.cancel();
        }

        @OnClick(R.id.btn_cancel)
        public void cancel() {
            dialog.cancel();
        }
    }
}
