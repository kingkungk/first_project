package com.kingkung.train.bean;

import java.util.List;

public class Config {
    private City fromCity;
    private City toCity;

    private List<Passenger> passengers;

    private List<TrainDetails> trainDetails;

    private List<String> trainDate;

    private int refreshInterval;

    private List<String> emails;

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

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public List<TrainDetails> getTrainDetails() {
        return trainDetails;
    }

    public void setTrainDetails(List<TrainDetails> trainDetails) {
        this.trainDetails = trainDetails;
    }

    public List<String> getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(List<String> trainDate) {
        this.trainDate = trainDate;
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
}
