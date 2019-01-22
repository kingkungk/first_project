package com.kingkung.train.bean;

public class StatusResult<D, M> {
    private int httpstatus;
    private boolean status;

    private D data;

    private M messages;

    public int getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public M getMessages() {
        return messages;
    }

    public void setMessages(M messages) {
        this.messages = messages;
    }
}
