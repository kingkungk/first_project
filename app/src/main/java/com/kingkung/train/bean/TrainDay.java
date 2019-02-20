package com.kingkung.train.bean;

import com.kingkung.train.ui.adapter.DateDividedAdapter;

public class TrainDay extends DateDividedAdapter.TimedItem {
    public final static int DAY_NORM = 0x00000001;
    public final static int DAY_PAST = 0x00000002;
    public final static int DAY_WEEKEND = 0x00000004;
    public final static int DAY_TODAY = 0x00000100;

    private int dayOfMonth;

    private long timestamp;

    private int datType;

    private boolean isSelect;

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public long getStableId() {
        return 0;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDatType() {
        return datType;
    }

    public void setDatType(int datType) {
        this.datType = datType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
