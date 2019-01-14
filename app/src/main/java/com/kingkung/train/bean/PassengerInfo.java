package com.kingkung.train.bean;

import java.util.List;

public class PassengerInfo {
    public String code;
    public String passenger_name;
    public String sex_code;
    public String sex_name;
    public String born_date;
    public String country_code;
    public String passenger_id_type_code;
    public String passenger_id_type_name;
    public String passenger_id_no;
    public String passenger_type;
    public String passenger_flag;
    public String passenger_type_name;
    public String mobile_no;
    public String phone_no;
    public String email;
    public String address;
    public String postalcode;
    public String first_letter;
    public String recordCount;
    public String total_times;
    public String index_id;
    public String gat_born_date;
    public String gat_valid_date_start;
    public String gat_valid_date_end;
    public String gat_version;

    public class PassengerData {
        private List<PassengerInfo> normal_passengers;

        public List<PassengerInfo> getNormal_passengers() {
            return normal_passengers;
        }

        public void setNormal_passengers(List<PassengerInfo> normal_passengers) {
            this.normal_passengers = normal_passengers;
        }
    }
}
