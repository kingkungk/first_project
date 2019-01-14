package com.kingkung.train.bean;

public class StatusResult<D> {
    private String httpstatus;
    private String status;

    private D data;

    public String getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(String httpstatus) {
        this.httpstatus = httpstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
