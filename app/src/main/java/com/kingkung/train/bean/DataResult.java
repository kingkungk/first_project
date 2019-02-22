package com.kingkung.train.bean;

public class DataResult<D> extends Result {
    private D result_data;

    public D getResult_data() {
        return result_data;
    }

    public void setResult_data(D result_data) {
        this.result_data = result_data;
    }
}
