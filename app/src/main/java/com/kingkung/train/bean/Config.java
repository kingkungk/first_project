package com.kingkung.train.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

public class Config implements Parcelable {
    private City fromCity;
    private City toCity;

    private List<PassengerInfo> passengers;

    private List<TrainDetails> trainDetails;

    private List<String> trainDates = Arrays.asList("2019-02-12");

    private int refreshInterval = 3000;

    private List<String> emails;

    public Config() {

    }

    protected Config(Parcel in) {
        fromCity = in.readParcelable(City.class.getClassLoader());
        toCity = in.readParcelable(City.class.getClassLoader());
        passengers = in.createTypedArrayList(PassengerInfo.CREATOR);
        trainDetails = in.createTypedArrayList(TrainDetails.CREATOR);
        trainDates = in.createStringArrayList();
        refreshInterval = in.readInt();
        emails = in.createStringArrayList();
    }

    public static final Creator<Config> CREATOR = new Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel in) {
            return new Config(in);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };

    public City getFromCity() {
        return fromCity;
    }

    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public List<PassengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerInfo> passengers) {
        this.passengers = passengers;
    }

    public List<TrainDetails> getTrainDetails() {
        return trainDetails;
    }

    public void setTrainDetails(List<TrainDetails> trainDetails) {
        this.trainDetails = trainDetails;
    }

    public List<String> getTrainDates() {
        return trainDates;
    }

    public void setTrainDates(List<String> trainDates) {
        this.trainDates = trainDates;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(fromCity, flags);
        dest.writeParcelable(toCity, flags);
        dest.writeTypedList(passengers);
        dest.writeTypedList(trainDetails);
        dest.writeStringList(trainDates);
        dest.writeInt(refreshInterval);
        dest.writeStringList(emails);
    }
}
