package com.kingkung.train.bean;

public class CheckUser extends StatusResult {
    private Flag data;

    public Flag getData() {
        return data;
    }

    public void setData(Flag data) {
        this.data = data;
    }

    public class Flag {
        private String flag;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
