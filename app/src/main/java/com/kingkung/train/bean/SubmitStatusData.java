package com.kingkung.train.bean;

public class SubmitStatusData {
    private boolean submitStatus;
    private String errMsg;

    public boolean isSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(boolean submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
