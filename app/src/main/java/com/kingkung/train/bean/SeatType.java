package com.kingkung.train.bean;

import android.os.Parcel;
import android.os.Parcelable;

public enum SeatType implements Parcelable {
    HARD_SLEEP("硬卧", "3", "hardSleep"),
    HARD_SEAT("硬座", "1", "hardSeat"),
    NO_SEAT("无座", "1", "noSeat"),
    UPHOLSTERED_SEAT("软座", "2", "softSeat"),
    SOFT_SLEEP("软卧", "4", "softSleep"),
    HIGH_SOFT_SLEEP("高级软卧", "6", "advancedSoftSleep"),
    SECOND_CLASS("二等座", "O", "secondClassSeat"),
    FIRST_CLASS("一等座", "M", "firstClassSeat"),
    //        PREMIER_CLASS("特等座", "P", ""),
    BUSINESS_CLASS("商务座", "9", "businessSeat"),
    SPEED_SLEEP("动卧", "F", "moveSleep");
//        HIGH_SPEED_SLEEP("高级动卧", "A", "");

    public String name;

    public String seatType;

    public String field;

    SeatType(String name, String seatType, String field) {
        this.name = name;
        this.seatType = seatType;
        this.field = field;
    }

    public static final Creator<SeatType> CREATOR = new Creator<SeatType>() {
        @Override
        public SeatType createFromParcel(Parcel in) {
            return SeatType.values()[in.readInt()];
        }

        @Override
        public SeatType[] newArray(int size) {
            return new SeatType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(seatType);
        dest.writeString(field);
    }

    @Override
    public String toString() {
        return name;
    }
}
