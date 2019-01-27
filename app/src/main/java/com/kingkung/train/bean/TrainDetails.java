package com.kingkung.train.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingkung.train.TrainActivity.SeatType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TrainDetails implements Comparable<TrainDetails>, Parcelable {
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

    public boolean isCheck;

    public TrainDetails() {

    }

    public TrainDetails(Parcel in) {
        trainNo = in.readString();
        startStationCode = in.readString();
        endStationCode = in.readString();
        fromStationCode = in.readString();
        toStationCode = in.readString();
        leaveTime = in.readString();
        arriveTime = in.readString();
        totalConsume = in.readString();
        businessSeat = in.readString();
        firstClassSeat = in.readString();
        secondClassSeat = in.readString();
        advancedSoftSleep = in.readString();
        softSleep = in.readString();
        moveSleep = in.readString();
        hardSleep = in.readString();
        softSeat = in.readString();
        hardSeat = in.readString();
        noSeat = in.readString();
        other = in.readString();
        mark = in.readString();
        startStation = in.readString();
        endStation = in.readString();
        fromStation = in.readString();
        toStation = in.readString();
        secretStr = in.readString();
        startDate = in.readString();
    }

    public static final Creator<TrainDetails> CREATOR = new Creator<TrainDetails>() {
        @Override
        public TrainDetails createFromParcel(Parcel in) {
            return new TrainDetails(in);
        }

        @Override
        public TrainDetails[] newArray(int size) {
            return new TrainDetails[size];
        }
    };

    public void mapSeatType() throws NoSuchFieldException, IllegalAccessException {
        Class c = getClass();
        SeatType[] seatTypes = SeatType.values();
        for (SeatType type : seatTypes) {
            Field field = c.getField(type.field);
            seatTypeMap.put(type, String.valueOf(field.get(this)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainDetails details = (TrainDetails) o;
        return Objects.equals(trainNo, details.trainNo) &&
                Objects.equals(leaveTime, details.leaveTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainNo, leaveTime);
    }

    @Override
    public String toString() {
        return trainNo;
    }

    @Override
    public int compareTo(TrainDetails o) {
        return leaveTime.compareTo(o.leaveTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trainNo);
        dest.writeString(startStationCode);
        dest.writeString(endStationCode);
        dest.writeString(fromStationCode);
        dest.writeString(toStationCode);
        dest.writeString(leaveTime);
        dest.writeString(arriveTime);
        dest.writeString(totalConsume);
        dest.writeString(businessSeat);
        dest.writeString(firstClassSeat);
        dest.writeString(secondClassSeat);
        dest.writeString(advancedSoftSleep);
        dest.writeString(softSleep);
        dest.writeString(moveSleep);
        dest.writeString(hardSleep);
        dest.writeString(softSeat);
        dest.writeString(hardSeat);
        dest.writeString(noSeat);
        dest.writeString(other);
        dest.writeString(mark);
        dest.writeString(startStation);
        dest.writeString(endStation);
        dest.writeString(fromStation);
        dest.writeString(toStation);
        dest.writeString(secretStr);
        dest.writeString(startDate);
    }
}
