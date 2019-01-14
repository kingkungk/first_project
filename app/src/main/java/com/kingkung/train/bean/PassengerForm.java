package com.kingkung.train.bean;

public class PassengerForm {
    private String leftTicketStr;
    private String purpose_codes;
    private String train_location;
    private String key_check_isChange;

    private String tour_flag = "dc";

    private OrderRequest orderRequestDTO;
    private LeftTicketRequest queryLeftTicketRequestDTO;

    public String getLeftTicketStr() {
        return leftTicketStr;
    }

    public void setLeftTicketStr(String leftTicketStr) {
        this.leftTicketStr = leftTicketStr;
    }

    public String getPurpose_codes() {
        return purpose_codes;
    }

    public void setPurpose_codes(String purpose_codes) {
        this.purpose_codes = purpose_codes;
    }

    public String getTrain_location() {
        return train_location;
    }

    public void setTrain_location(String train_location) {
        this.train_location = train_location;
    }

    public String getKey_check_isChange() {
        return key_check_isChange;
    }

    public void setKey_check_isChange(String key_check_isChange) {
        this.key_check_isChange = key_check_isChange;
    }

    public String getTour_flag() {
        return tour_flag == null ? "dc" : tour_flag;
    }

    public void setTour_flag(String tour_flag) {
        this.tour_flag = tour_flag;
    }

    public OrderRequest getOrderRequestDTO() {
        return orderRequestDTO;
    }

    public void setOrderRequestDTO(OrderRequest orderRequestDTO) {
        this.orderRequestDTO = orderRequestDTO;
    }

    public LeftTicketRequest getQueryLeftTicketRequestDTO() {
        return queryLeftTicketRequestDTO;
    }

    public void setQueryLeftTicketRequestDTO(LeftTicketRequest queryLeftTicketRequestDTO) {
        this.queryLeftTicketRequestDTO = queryLeftTicketRequestDTO;
    }

    public class LeftTicketRequest {
        private String train_date;
        private String train_no;

        public String getTrain_date() {
            return train_date;
        }

        public void setTrain_date(String train_date) {
            this.train_date = train_date;
        }

        public String getTrain_no() {
            return train_no;
        }

        public void setTrain_no(String train_no) {
            this.train_no = train_no;
        }
    }

    public class OrderRequest {
        private String cancel_flag = "2";
        private String bed_level_order_num = "000000000000000000000000000000";

        public String getCancel_flag() {
            return cancel_flag == null ? "2" : cancel_flag;
        }

        public void setCancel_flag(String cancel_flag) {
            this.cancel_flag = cancel_flag;
        }

        public String getBed_level_order_num() {
            return bed_level_order_num == null ? "000000000000000000000000000000" : bed_level_order_num;
        }

        public void setBed_level_order_num(String bed_level_order_num) {
            this.bed_level_order_num = bed_level_order_num;
        }
    }
}
