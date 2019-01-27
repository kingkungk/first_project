package com.kingkung.train;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kingkung.train.bean.City;
import com.kingkung.train.bean.Config;
import com.kingkung.train.bean.Passenger;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.contract.ConfigContract;
import com.kingkung.train.presenter.ConfigPresenter;
import com.kingkung.train.ui.activity.CitySelectActivity;
import com.kingkung.train.ui.activity.TrainNoSelectActivity;
import com.kingkung.train.ui.activity.base.BaseActivity;
import com.kingkung.train.ui.adapter.PassengerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public final static String CONFIG_FILE_NAME = "config";

    @BindView(R.id.tv_from_station)
    TextView tvFromStation;
    @BindView(R.id.tv_to_station)
    TextView tvToStation;
    @BindView(R.id.tv_passenger_name)
    TextView tvPassengerName;
    @BindView(R.id.tv_train_no)
    TextView tvTrainNo;
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
            return;
        }

        City fromCity = config.getFromCity();
        if (fromCity != null) {
            tvFromStation.setText(fromCity.name);
        }
        City toCity = config.getToCity();
        if (toCity != null) {
            tvToStation.setText(toCity.name);
        }

        List<Passenger> passenger = config.getPassengers();
        if (passenger != null && !passenger.isEmpty()) {
            String passengerStr = passenger.toString();
            tvPassengerName.setText(passengerStr.substring(1, passengerStr.length() - 1));
        }

        List<TrainDetails> details = config.getTrainDetails();
        if (details != null && !details.isEmpty()) {
            String trainNoStr = details.toString();
            tvTrainNo.setText(trainNoStr.substring(1, trainNoStr.length() - 1));
        }

        List<String> emails = config.getEmails();
        if (emails != null && !emails.isEmpty()) {
            String emailStr = emails.toString();
            tvEmail.setText(emailStr.substring(1, emailStr.length() - 1));
        }
    }

    @OnClick(R.id.btn_start)
    public void start() {
        writeConfigJson();
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
        } else if (resultCode == SELECT_TRAIN_NO_RESULT_CODE) {
            List<TrainDetails> details = data.getParcelableArrayListExtra(SELECT_TRAIN_NO_KEY);
            config.setTrainDetails(details);
            String trainNosStr = details.toString();
            tvTrainNo.setText(trainNosStr.substring(1, trainNosStr.length() - 1));
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
        List<TrainDetails> details = config.getTrainDetails();
        if (details != null && !details.isEmpty()) {
            intent.putParcelableArrayListExtra(TrainNoSelectActivity.CHECKED_TRAIN_NO_KEY, (ArrayList<? extends Parcelable>) details);
        }
        intent.putExtra(TrainNoSelectActivity.TRAIN_DATE_KEY, "2019-01-31");
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
            List<String> emails = new ArrayList<>();
            for (Passenger passenger : passengers) {
                if (passenger.isCheck) {
                    checkPassengers.add(passenger);
                    emails.add(passenger.email);
                }
            }
            if (checkPassengers.isEmpty()) {
                showMsg("请选择联系人");
                return;
            }
            String checkPassengerStr = checkPassengers.toString();
            tvPassengerName.setText(checkPassengerStr.substring(1, checkPassengerStr.length() - 1));
            config.setPassengers(checkPassengers);

            String emailStr = emails.toString();
            tvEmail.setText(emailStr.substring(1, emailStr.length() - 1));
            config.setEmails(emails);

            dialog.cancel();
        }

        @OnClick(R.id.btn_cancel)
        public void cancel() {
            dialog.cancel();
        }
    }
}
