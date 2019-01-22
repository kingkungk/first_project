package com.kingkung.train.bean;

import com.kingkung.train.TrainActivity.SeatType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrainDetails {
    //  车次：3
    public final static int INDEX_TRAIN_NO = 3;
    //  start_station_code:起始站：4
    public final static int INDEX_TRAIN_START_STATION_CODE = 4;
    //  end_station_code终点站：5
    public final static int INDEX_TRAIN_END_STATION_CODE = 5;
    //  from_station_code:出发站：6
    public final static int INDEX_TRAIN_FROM_STATION_CODE = 6;
    //  to_station_code:到达站：7
    public final static int INDEX_TRAIN_TO_STATION_CODE = 7;
    //  start_time:出发时间：8
    public final static int INDEX_TRAIN_LEAVE_TIME = 8;
    //  arrive_time:达到时间：9
    public final static int INDEX_TRAIN_ARRIVE_TIME = 9;
    //  历时：10
    public final static int INDEX_TRAIN_TOTAL_CONSUME = 10;
    //  商务特等座：32
    public final static int INDEX_TRAIN_BUSINESS_SEAT = 32;
    //  一等座：31
    public final static int INDEX_TRAIN_FIRST_CLASS_SEAT = 31;
    //  二等座：30
    public final static int INDEX_TRAIN_SECOND_CLASS_SEAT = 30;
    //  高级软卧：21
    public final static int INDEX_TRAIN_ADVANCED_SOFT_SLEEP = 21;
    //  软卧：23
    public final static int INDEX_TRAIN_SOFT_SLEEP = 23;
    //  动卧：33
    public final static int INDEX_TRAIN_MOVE_SLEEP = 33;
    //  硬卧：28
    public final static int INDEX_TRAIN_HARD_SLEEP = 28;
    //  软座：24
    public final static int INDEX_TRAIN_SOFT_SEAT = 24;
    //  硬座：29
    public final static int INDEX_TRAIN_HARD_SEAT = 29;
    //  无座：26
    public final static int INDEX_TRAIN_NO_SEAT = 28;
    //  其他：22
    public final static int INDEX_TRAIN_OTHER = 22;
    //  备注：1
    public final static int INDEX_TRAIN_MARK = 1;

    public final static int INDEX_SECRET_STR = 0;
    // 车票出发日期
    public final static int INDEX_START_DATE = 13;

    public String trainNo;
    public String startStationCode;
    public String endStationCode;
    public String fromStationCode;
    public String toStationCode;
    public String leaveTime;
    public String arriveTime;
    public String totalConsume;
    public String businessSeat;
    public String firstClassSeat;
    public String secondClassSeat;
    public String advancedSoftSleep;
    public String softSleep;
    public String moveSleep;
    public String hardSleep;
    public String softSeat;
    public String hardSeat;
    public String noSeat;
    public String other;
    public String mark;
    public String startStation;
    public String endStation;
    public String fromStation;
    public String toStation;
    public String secretStr;
    public String startDate;

    public String submitToken;
    public PassengerForm passengerForm;

    public List<PassengerInfo> passengerInfos;

    public String orderId;

    public Map<SeatType, String> seatTypeMap = new LinkedHashMap<>();
    public List<SeatType> seatTypes = new ArrayList<>();
    public String count;

    public void mapSeatType() throws NoSuchFieldException, IllegalAccessException {
        Class c = getClass();
        SeatType[] seatTypes = SeatType.values();
        for (SeatType type : seatTypes) {
            seatTypeMap.put(type, (String) c.getField(type.field).get(this));
        }
    }
}
