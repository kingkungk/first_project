package com.kingkung.train;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.contract.TrainContract;
import com.kingkung.train.presenter.TrainPresenter;
import com.kingkung.train.ui.activity.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

public class TrainActivity extends BaseActivity<TrainPresenter> implements TrainContract.View {
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    ListView listView;

    private ArrayAdapter<String> messageAdapter;

    private int count = 1;

    private SimpleDateFormat timeQuantumDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat timerDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

    private TextToSpeech textToSpeech;

    //刷新间隔
    private int refreshQueryInterval = 3000;
    //刷新登录会话
    private int refreshLoginInterval = 1000 * 60 * 5;
    //出发站
    private String fromStation;
    //到达站
    private String toStation;
    // 车次，如果为空就不过滤
    private List<String> trainNo;
    //乘车日期
    private List<String> trainDate;
    //乘车人姓名
    private List<String> passengerNames;
    //出发时间段
    private List<Long> startTimeQuantum = new ArrayList<>(2);
    //座位类别,如果为空就不过滤
    private List<SeatType> seatType = Arrays.asList(SeatType.HARD_SLEEP, SeatType.HARD_SEAT, SeatType.SECOND_CLASS);
    //定时抢票时间
    private long timerTime;
    //接受通知的邮箱
    private List<String> sendEmails;

    private boolean isStartQuery = true;

    public enum SeatType {
        HARD_SLEEP("硬卧", "3"),
        HARD_SEAT("硬座", "1"),
        SECOND_CLASS("二等座", "O"),
        FIRST_CLASS("一等座", "M");

        public String name;

        public String seatType;

        SeatType(String name, String seatType) {
            this.name = name;
            this.seatType = seatType;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_train;
    }

    @Override
    protected void create() {
        setSupportActionBar(toolbar);

        initIntent(getIntent());

        messageAdapter = new ArrayAdapter<>(this, R.layout.item_message);
        listView.setAdapter(messageAdapter);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    textToSpeech.speak("语音初始化成功", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        presenter.interval(0, refreshLoginInterval, new Runnable() {
            @Override
            public void run() {
                presenter.uamtk();
            }
        });
    }

    public void initIntent(Intent intent) {
        fromStation = intent.getStringExtra("from_station");
        toStation = intent.getStringExtra("to_station");
        passengerNames = intent.getStringArrayListExtra("passenger_name");
        trainNo = intent.getStringArrayListExtra("train_no");
        trainDate = intent.getStringArrayListExtra("train_date");
        refreshQueryInterval = Integer.parseInt(intent.getStringExtra("refresh_interval"));
        try {
            List<String> startTimeQuantumStr = intent.getStringArrayListExtra("start_time_quantum");
            startTimeQuantum.add(timeQuantumDateFormat.parse(startTimeQuantumStr.get(0)).getTime());
            startTimeQuantum.add(timeQuantumDateFormat.parse(startTimeQuantumStr.get(1)).getTime());

            String timerTimeStr = intent.getStringExtra("timer_date");
            timerTime = timerDateFormat.parse(timerTimeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sendEmails = intent.getStringArrayListExtra("send_email");
    }

    @Override
    public void uamtkSuccess(String newapptk) {
        presenter.uamauthClient(newapptk);
    }

    @Override
    public void uamtkFaild() {
        isStartQuery = true;
        presenter.detachView();
        startActivityForResult(new Intent(this, LoginActivity.class), 100);
    }

    @Override
    public void uamauthClientSuccess(String username) {
        setTitle(username);
        if (!isStartQuery) {
            return;
        }
        isStartQuery = false;
        long timer;
        long curTime = System.currentTimeMillis();
        if (timerTime <= curTime) {
            timer = 0;
        } else {
            timer = timerTime - curTime;
        }
        presenter.interval(timer, refreshQueryInterval, new Runnable() {
            @Override
            public void run() {
                for (String date : trainDate) {
                    presenter.queryTrain(date, fromStation, toStation);
                }
            }
        });
    }

    @Override
    public void queryTrainSuccess(List<TrainDetails> details) {
        Iterator<TrainDetails> it = details.iterator();
        while (it.hasNext()) {
            TrainDetails trainDetail = it.next();
            if (filterSeatType(trainDetail)) {
                it.remove();
                continue;
            }
            if (filterTrainNo(trainDetail)) {
                it.remove();
                continue;
            }
            if (filterLeaveTime(trainDetail)) {
                it.remove();
                continue;
            }
        }
        messageAdapter.add("第" + count + "次");
        count++;
        List<TrainDetails> result = new ArrayList<>(details);
        if (result.size() == 0) {
            messageAdapter.add("没有查询到可买的票");
            messageAdapter.add("");
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.smoothScrollByOffset(listView.getCount());
                }
            });
            return;
        }
        textToSpeech.speak("查询到可买的票，将会帮你自动提交订单", TextToSpeech.QUEUE_FLUSH, null);
        presenter.sendEmail(sendEmails, "标题1", "查询到可买的票");
        for (TrainDetails detail : result) {
            messageAdapter.add("车次:" + detail.trainNo + "\t\t可买票类型:" +
                    detail.seatTypes.toString() + "\t\t张数:" + detail.count);
            presenter.submitOrder(detail);
            break;
        }
        messageAdapter.add("");
    }

    @Override
    public void checkUserSuccess() {
    }

    @Override
    public void submitOrderSuccess(TrainDetails detail) {
        presenter.initDc(detail);
    }

    @Override
    public void initDcSuccess(TrainDetails detail) {
        presenter.getPassenger(detail);
    }

    @Override
    public void getPassengerSuccess(TrainDetails detail) {
        List<String> copyPassengerNames = new ArrayList<>(passengerNames);
        if (copyPassengerNames.size() == 0) {
            presenter.checkOrderInfo(detail);
            return;
        }
        List<PassengerInfo> passengerInfos = detail.passengerInfos;
        Iterator<PassengerInfo> it = passengerInfos.iterator();
        while (it.hasNext()) {
            PassengerInfo info = it.next();
            if (!copyPassengerNames.contains(info.passenger_name)) {
                it.remove();
            }
        }
        presenter.checkOrderInfo(detail);
    }

    @Override
    public void checkOrderInfoSuccess(TrainDetails detail) {
        presenter.getQueueCount(detail);
    }

    @Override
    public void getQueueCountSuccess(TrainDetails detail) {
        presenter.confirmSingleForQueue(detail);
    }

    @Override
    public void confirmSingleForQueueSuccess(TrainDetails detail) {
        textToSpeech.speak("订单排队中，请等待，或者去12306上查看排队订单", TextToSpeech.QUEUE_FLUSH, null);
        presenter.sendEmail(sendEmails, "标题2", "订单排队中");
        presenter.queryOrderWaitTime(detail);
    }

    @Override
    public void queryOrderWaitTimeSuccess(TrainDetails detail) {
        presenter.resultOrderForQueue(detail);
    }

    @Override
    public void resultOrderForQueueSuccess() {
        textToSpeech.speak("订单提交成功，请去12306上立即支付", TextToSpeech.QUEUE_FLUSH, null);
        presenter.sendEmail(sendEmails, "标题3", "订单提交成功");
        presenter.detachView();
    }

    public boolean filterTrainNo(TrainDetails detail) {
        if (trainNo.size() == 0) {
            return false;
        }
        List<String> copyTrainNo = new ArrayList<>(trainNo);
        int position = 0;
        for (; position < copyTrainNo.size(); position++) {
            String type = copyTrainNo.get(position);
            if (detail.trainNo.startsWith(type)) {
                break;
            }
        }
        if (position == copyTrainNo.size()) {
            return true;
        }
        return false;
    }

    public boolean filterLeaveTime(TrainDetails detail) {
        try {
            long leaveTime = timeQuantumDateFormat.parse(detail.leaveTime).getTime();
            if (leaveTime < startTimeQuantum.get(0) || leaveTime > startTimeQuantum.get(1)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean filterSeatType(TrainDetails detail) {
        if (seatType.size() == 0) {
            return false;
        }
        List<SeatType> copySeatType = new ArrayList<>(seatType);
        for (int i = 0; i < copySeatType.size(); i++) {
            SeatType type = copySeatType.get(i);
            if (type == SeatType.HARD_SLEEP && !TextUtils.isEmpty(detail.hardSleep)
                    && !"无".equals(detail.hardSleep) && !"*".equals(detail.hardSleep)) {
                detail.seatTypes.add(SeatType.HARD_SLEEP);
                detail.count = detail.hardSleep;
            }
            if (type == SeatType.HARD_SEAT && !TextUtils.isEmpty(detail.hardSeat)
                    && !"无".equals(detail.hardSeat) && !"*".equals(detail.hardSeat)) {
                detail.seatTypes.add(SeatType.HARD_SEAT);
                detail.count = detail.hardSeat;
            }
            if (type == SeatType.SECOND_CLASS && !TextUtils.isEmpty(detail.secondClassSeat)
                    && !"无".equals(detail.secondClassSeat) && !"*".equals(detail.secondClassSeat)) {
                detail.seatTypes.add(SeatType.SECOND_CLASS);
                detail.count = detail.secondClassSeat;
            }
            if (type == SeatType.FIRST_CLASS && !TextUtils.isEmpty(detail.firstClassSeat)
                    && !"无".equals(detail.firstClassSeat) && !"*".equals(detail.firstClassSeat)) {
                detail.seatTypes.add(SeatType.FIRST_CLASS);
                detail.count = detail.firstClassSeat;
            }
        }
        if (detail.seatTypes.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_config) {
            isStartQuery = true;
            presenter.detachView();
            Intent intent = new Intent(this, ConfigActivity.class);
            intent.putExtra("isTrainActivity", true);
            startActivityForResult(intent, 100);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 200) {
                count = 1;
                messageAdapter.clear();
                initIntent(data);
            }
            presenter.attachView(this);
            presenter.interval(0, refreshLoginInterval, new Runnable() {
                @Override
                public void run() {
                    presenter.uamtk();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }
}
