package com.kingkung.train.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kingkung.train.api.TrainApi;
import com.kingkung.train.bean.CheckOrderData;
import com.kingkung.train.bean.MessageListReslut;
import com.kingkung.train.bean.MessageReslut;
import com.kingkung.train.bean.PassengerForm;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.QueryOrderWaitTimeData;
import com.kingkung.train.bean.QueueCountData;
import com.kingkung.train.bean.ResultOrderForQueueData;
import com.kingkung.train.bean.SubmitStatusData;
import com.kingkung.train.bean.TrainData;
import com.kingkung.train.bean.TrainDetails;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.bean.response.EmptyObserver;
import com.kingkung.train.bean.response.MessageListObserver;
import com.kingkung.train.bean.response.ResultObserver;
import com.kingkung.train.bean.UamtkResult;
import com.kingkung.train.bean.UserNameResult;
import com.kingkung.train.bean.response.SubmitSatusObserver;
import com.kingkung.train.contract.TrainContract;
import com.kingkung.train.presenter.base.BasePresenter;
import com.kingkung.train.utils.City2Code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TrainPresenter extends BasePresenter<TrainContract.View> implements TrainContract.Presenter {
    private Subject<Integer> backSubject = PublishSubject.create();

    private TrainApi api;

    private SimpleDateFormat standardFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT+0800' (中国标准时间)", Locale.ENGLISH);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyyMMdd");

    @Inject
    public TrainPresenter(TrainApi api) {
        this.api = api;
        listenBackEvent();
    }

    public void listenBackEvent() {
        listenBackEvent(backSubject);
    }

    @Override
    public void clickBack() {
        backSubject.onNext(1);
    }

    public void listenBackEvent(Subject<Integer> back) {
        Disposable disposable = Observable.merge(back, back.debounce(2000, TimeUnit.MILLISECONDS)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer i) throws Exception {
                        return 0;
                    }
                }))
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        if (integer2 == 0) {
                            return 0;
                        }
                        return integer + 1;
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 0;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            mView.showMsg("再按一次退出程序");
                        } else if (integer == 2) {
                            mView.realBack();
                        }
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void interval(long initialDelay, long period, final Runnable r) {
        Disposable disposable = Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
                .subscribeWith(new DataObserver<Long>(mView) {
                    @Override
                    public void success(Long aLong) {
                        r.run();
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void timer(long delay, final Runnable r) {
        Disposable disposable = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .subscribeWith(new DataObserver<Long>(mView) {
                    @Override
                    public void success(Long aLong) {
                        r.run();
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
                        } else if (code == 1 || code == 7) {  //登录验证没通过
                            mView.uamtkFaild();
                        } else if (code == 4) {  //用户已在他处登录
                            mView.uamtkFaild();
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

    @Override
    public void queryTrain(String date, String from, String to) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("leftTicketDTO.train_date", date);
        fields.put("leftTicketDTO.from_station", City2Code.city2Code(from));
        fields.put("leftTicketDTO.to_station", City2Code.city2Code(to));
        fields.put("purpose_codes", "ADULT");
        Disposable disposable = api.queryTrain(fields)
                .map(new Function<MessageReslut<TrainData>, List<TrainDetails>>() {
                    @Override
                    public List<TrainDetails> apply(MessageReslut<TrainData> result) throws Exception {
                        List<TrainDetails> detailsList = new ArrayList<>();
                        if (!result.isStatus()) {
                            return detailsList;
                        }
                        List<String> queryResults = result.getData().getResult();
                        for (String queryResult : queryResults) {
                            String[] info = queryResult.split("\\|");
                            TrainDetails details = new TrainDetails();
                            details.trainNo = info[TrainDetails.INDEX_TRAIN_NO];
                            details.startStationCode = info[TrainDetails.INDEX_TRAIN_START_STATION_CODE];
                            details.endStationCode = info[TrainDetails.INDEX_TRAIN_END_STATION_CODE];
                            details.fromStationCode = info[TrainDetails.INDEX_TRAIN_FROM_STATION_CODE];
                            details.toStationCode = info[TrainDetails.INDEX_TRAIN_TO_STATION_CODE];
                            details.leaveTime = info[TrainDetails.INDEX_TRAIN_LEAVE_TIME];
                            details.arriveTime = info[TrainDetails.INDEX_TRAIN_ARRIVE_TIME];
                            details.totalConsume = info[TrainDetails.INDEX_TRAIN_TOTAL_CONSUME];
                            details.businessSeat = info[TrainDetails.INDEX_TRAIN_BUSINESS_SEAT];
                            details.firstClassSeat = info[TrainDetails.INDEX_TRAIN_FIRST_CLASS_SEAT];
                            details.secondClassSeat = info[TrainDetails.INDEX_TRAIN_SECOND_CLASS_SEAT];
                            details.advancedSoftSleep = info[TrainDetails.INDEX_TRAIN_ADVANCED_SOFT_SLEEP];
                            details.softSleep = info[TrainDetails.INDEX_TRAIN_SOFT_SLEEP];
                            details.moveSleep = info[TrainDetails.INDEX_TRAIN_MOVE_SLEEP];
                            details.hardSleep = info[TrainDetails.INDEX_TRAIN_HARD_SLEEP];
                            details.softSeat = info[TrainDetails.INDEX_TRAIN_SOFT_SEAT];
                            details.hardSeat = info[TrainDetails.INDEX_TRAIN_HARD_SEAT];
                            details.noSeat = info[TrainDetails.INDEX_TRAIN_NO_SEAT];
                            details.other = info[TrainDetails.INDEX_TRAIN_OTHER];
                            details.mark = info[TrainDetails.INDEX_TRAIN_MARK];
                            details.startStation = City2Code.code2City(details.startStationCode);
                            details.endStation = City2Code.code2City(details.endStationCode);
                            details.fromStation = City2Code.code2City(details.fromStationCode);
                            details.toStation = City2Code.code2City(details.toStationCode);
                            details.secretStr = info[TrainDetails.INDEX_SECRET_STR];
                            details.startDate = info[TrainDetails.INDEX_START_DATE];
                            details.mapSeatType();
                            detailsList.add(details);
                        }
                        return detailsList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<List<TrainDetails>>(mView) {
                    @Override
                    public void success(List<TrainDetails> details) {
                        mView.queryTrainSuccess(details);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void sendEmail(final List<String> sendEmails, final String title, final String content) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                try {
                    Properties properties = new Properties();
                    properties.put("mail.transport.protocol", "smtp");// 连接协议
                    properties.put("mail.smtp.host", "smtp.163.com");// 主机名
                    properties.put("mail.smtp.port", 465);// 端口号
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
                    properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
                    // 得到回话对象
                    Session session = Session.getInstance(properties);
                    // 获取邮件对象
                    Message message = new MimeMessage(session);
                    // 设置发件人邮箱地址
                    message.setFrom(new InternetAddress("18257177261@163.com"));
                    // 设置收件人邮箱地址
                    InternetAddress[] addresses = new InternetAddress[sendEmails.size()];
                    for (int i = 0; i < sendEmails.size(); i++) {
                        addresses[i] = new InternetAddress(sendEmails.get(i));
                    }
                    message.setRecipients(Message.RecipientType.TO, addresses);
                    // 设置邮件标题
                    message.setSubject(title);
                    // 设置邮件内容
                    message.setText(content);
                    // 得到邮差对象
                    Transport transport = session.getTransport();
                    // 连接自己的邮箱账户
                    transport.connect("18257177261", "chenghang123");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
                    // 发送邮件
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    emitter.onNext(true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                emitter.onNext(false);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new EmptyObserver());
        addSubscription(disposable);
    }

    @Override
    public void checkUser() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("_json_att", "");
        Disposable disposable = api.checkUser(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<Object>(mView) {
                    @Override
                    public void success(Object o) {
                        mView.checkUserSuccess();
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void submitOrder(final TrainDetails detail) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws ParseException {
                StringBuilder builder = new StringBuilder();
                builder.append("secretStr=" + detail.secretStr);
                builder.append("&");
                builder.append("train_date=" + dateFormat.format(serverDateFormat.parse(detail.startDate)));
                builder.append("&");
                builder.append("back_train_date=" + dateFormat.format(new Date()));
                builder.append("&");
                builder.append("tour_flag=" + "dc");
                builder.append("&");
                builder.append("purpose_codes=" + "ADULT");
                builder.append("&");
                builder.append("query_from_station_name=" + detail.fromStation);
                builder.append("&");
                builder.append("query_to_station_name=" + detail.toStation);
                builder.append("&");
                builder.append("undefined=" + "");
                emitter.onNext(builder.toString());
            }
        }).flatMap(new Function<String, ObservableSource<MessageListReslut<Object>>>() {
            @Override
            public ObservableSource<MessageListReslut<Object>> apply(String s) throws Exception {
                return api.submitOrder(
                        RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), s));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<Object>(mView) {
                    @Override
                    public void success(Object o) {
                        mView.submitOrderSuccess(detail);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void initDc(final TrainDetails detail) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("_json_att", "");
        Disposable disposable = api.initDc(fields)
                .map(new Function<String, Object[]>() {
                    @Override
                    public Object[] apply(String s) throws Exception {
                        int tokenIndex = s.indexOf("globalRepeatSubmitToken");
                        String tokenStr = s.substring(tokenIndex, s.indexOf(";", tokenIndex));
                        String token = tokenStr.substring(tokenStr.indexOf("'") + 1, tokenStr.lastIndexOf("'"));
                        int passengerIndex = s.indexOf("ticketInfoForPassengerForm=");
                        String passengerStr = s.substring(passengerIndex, s.indexOf(";", passengerIndex));
                        String passenger = passengerStr.substring(passengerStr.indexOf("{"));
                        PassengerForm passengerForm = new Gson().fromJson(passenger, PassengerForm.class);
                        return new Object[]{token, passengerForm};
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<Object[]>(mView) {
                    @Override
                    public void success(Object[] objects) {
                        detail.submitToken = (String) objects[0];
                        detail.passengerForm = (PassengerForm) objects[1];
                        mView.initDcSuccess(detail);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void getPassenger(final TrainDetails detail) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("_json_att", "");
        fields.put("REPEAT_SUBMIT_TOKEN", detail.submitToken);
        Disposable disposable = api.getPassenger(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<PassengerInfo.PassengerData>(mView) {
                    @Override
                    public void success(PassengerInfo.PassengerData passengerData) {
                        detail.passengerInfos = passengerData.getNormal_passengers();
                        mView.getPassengerSuccess(detail);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void checkOrderInfo(final TrainDetails detail) {
        PassengerForm passengerForm = detail.passengerForm;
        PassengerForm.OrderRequest orderRequest = passengerForm.getOrderRequestDTO();
        List<PassengerInfo> passengerInfos = detail.passengerInfos;
        StringBuilder passengerBuilder = new StringBuilder();
        for (PassengerInfo info : passengerInfos) {
            passengerBuilder.append(String.format("%s,0,%s,%s,%s,%s,%s,N", detail.seatTypes.get(0).seatType, info.passenger_type,
                    info.passenger_name, info.passenger_id_type_code, info.passenger_id_no, info.mobile_no));
            passengerBuilder.append("_");
        }
        passengerBuilder.deleteCharAt(passengerBuilder.length() - 1);
        StringBuilder oldPassengerBuilder = new StringBuilder();
        for (PassengerInfo info : passengerInfos) {
            oldPassengerBuilder.append(String.format("%s,%s,%s,1_", info.passenger_name, info.passenger_id_type_code, info.passenger_id_no));
        }
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("cancel_flag", orderRequest.getCancel_flag());
        fields.put("bed_level_order_num", orderRequest.getBed_level_order_num());
        fields.put("passengerTicketStr", passengerBuilder.toString());
        fields.put("oldPassengerStr", oldPassengerBuilder.toString());
        fields.put("tour_flag", passengerForm.getTour_flag());
        fields.put("randCode", "");
        fields.put("whatsSelect", "1");
        fields.put("_json_att", "");
        fields.put("REPEAT_SUBMIT_TOKEN", detail.submitToken);
        Disposable disposable = api.checkOrderInfo(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SubmitSatusObserver<CheckOrderData>(mView) {
                    @Override
                    public void success(CheckOrderData checkOrderData) {
                        mView.checkOrderInfoSuccess(detail);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void getQueueCount(final TrainDetails detail) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, String>> emitter) throws ParseException {
                Map<String, String> fields = new LinkedHashMap<>();
                PassengerForm passengerForm = detail.passengerForm;
                PassengerForm.LeftTicketRequest leftTicketRequest = passengerForm.getQueryLeftTicketRequestDTO();
                fields.put("train_date", standardFormat.format(serverDateFormat.parse(leftTicketRequest.getTrain_date())));
                fields.put("train_no", leftTicketRequest.getTrain_no());
                fields.put("stationTrainCode", detail.trainNo);
                fields.put("seatType", detail.seatTypes.get(0).seatType);
                fields.put("fromStationTelecode", detail.fromStationCode);
                fields.put("toStationTelecode", detail.toStationCode);
                fields.put("leftTicket", passengerForm.getLeftTicketStr());
                fields.put("purpose_codes", passengerForm.getPurpose_codes());
                fields.put("train_location", passengerForm.getTrain_location());
                fields.put("_json_att", "");
                fields.put("REPEAT_SUBMIT_TOKEN", detail.submitToken);
                emitter.onNext(fields);
            }
        }).flatMap(new Function<Map<String, String>, ObservableSource<MessageListReslut<QueueCountData>>>() {
            @Override
            public ObservableSource<MessageListReslut<QueueCountData>> apply(Map<String, String> stringStringMap) throws Exception {
                return api.getQueueCount(stringStringMap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<QueueCountData>(mView) {
                    @Override
                    public void success(QueueCountData data) {
                        mView.getQueueCountSuccess(detail);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void confirmSingleForQueue(final TrainDetails detail) {
        PassengerForm passengerForm = detail.passengerForm;
        List<PassengerInfo> passengerInfos = detail.passengerInfos;
        StringBuilder passengerBuilder = new StringBuilder();
        for (PassengerInfo info : passengerInfos) {
            passengerBuilder.append(String.format("%s,0,%s,%s,%s,%s,%s,N", detail.seatTypes.get(0).seatType, info.passenger_type,
                    info.passenger_name, info.passenger_id_type_code, info.passenger_id_no, info.mobile_no));
            passengerBuilder.append("_");
        }
        passengerBuilder.deleteCharAt(passengerBuilder.length() - 1);
        StringBuilder oldPassengerBuilder = new StringBuilder();
        for (PassengerInfo info : passengerInfos) {
            oldPassengerBuilder.append(String.format("%s,%s,%s,1_", info.passenger_name, info.passenger_id_type_code, info.passenger_id_no));
        }
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("passengerTicketStr", passengerBuilder.toString());
        fields.put("oldPassengerStr", oldPassengerBuilder.toString());
        fields.put("randCode", "");
        fields.put("purpose_codes", passengerForm.getPurpose_codes());
        fields.put("key_check_isChange", passengerForm.getKey_check_isChange());
        fields.put("leftTicketStr", passengerForm.getLeftTicketStr());
        fields.put("train_location", passengerForm.getTrain_location());
        fields.put("choose_seats", "");
        fields.put("seatDetailType", "000");
        fields.put("whatsSelect", "1");
        fields.put("roomType", "00");
        fields.put("dwAll", "N");
        fields.put("_json_att", "");
        fields.put("REPEAT_SUBMIT_TOKEN", detail.submitToken);
        Disposable disposable = api.confirmSingleForQueue(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SubmitSatusObserver<SubmitStatusData>(mView) {
                    @Override
                    public void success(SubmitStatusData data) {
                        mView.confirmSingleForQueueSuccess(detail);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void queryOrderWaitTime(final TrainDetails detail) {
        PassengerForm passengerForm = detail.passengerForm;
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("random", String.format("%10d", System.currentTimeMillis()));
        fields.put("tourFlag", passengerForm.getTour_flag());
        fields.put("_json_att", "");
        fields.put("REPEAT_SUBMIT_TOKEN", detail.submitToken);
        Disposable disposable = api.queryOrderWaitTime(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<QueryOrderWaitTimeData>(mView) {
                    @Override
                    public void success(QueryOrderWaitTimeData data) {
                        long waitTime = Long.parseLong(data.waitTime);
                        if (waitTime < 0) {
                            if (!TextUtils.isEmpty(data.orderId)) {
                                detail.orderId = data.orderId;
                                mView.queryOrderWaitTimeSuccess(detail);
                            }
                        } else {
                            timer(1000, new Runnable() {
                                @Override
                                public void run() {
                                    queryOrderWaitTime(detail);
                                }
                            });
                        }
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void resultOrderForQueue(TrainDetails detail) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("orderSequence_no", detail.orderId);
        fields.put("_json_att", "");
        fields.put("REPEAT_SUBMIT_TOKEN", detail.submitToken);
        Disposable disposable = api.resultOrderForQueue(fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new MessageListObserver<ResultOrderForQueueData>(mView) {
                    @Override
                    public void success(ResultOrderForQueueData data) {
                        boolean submitStatus = Boolean.parseBoolean(data.submitStatus);
                        if (submitStatus) {
                            mView.resultOrderForQueueSuccess();
                        }
                    }
                });
        addSubscription(disposable);
    }
}
