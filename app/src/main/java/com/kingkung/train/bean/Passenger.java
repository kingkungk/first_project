package com.kingkung.train.bean;

import java.util.List;
import java.util.Objects;

public class Passenger {
    public String code; //1
    public String passenger_name; //程航
    public String sex_code; //M
    public String sex_name; //男
    public String born_date; //1993-11-25 00:00:00
    public String country_code; //CN
    public String passenger_id_type_code; //1
    public String passenger_id_type_name; //中国居民身份证
    public String passenger_id_no; //421126199311250031
    public String passenger_type; //1
    public String passenger_flag; //0
    public String passenger_type_name; //成人
    public String mobile_no; //
    public String phone_no; //
    public String email; //3225319184@qq.com
    public String address; //
    public String postalcode; //
    public String first_letter; //
    public String recordCount; //1
    public String isUserSelf; //Y
    public String total_times; //99
    public String delete_time; //1994/05/24
    public String gat_born_date; //
    public String gat_valid_date_start; //
    public String gat_valid_date_end; //
    public String gat_version; //public String

    public boolean isCheck;

    @Override
    public String toString() {
        return passenger_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(passenger_id_no, passenger.passenger_id_no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passenger_id_no);
    }

    public class PassengerData {
        public List<Passenger> datas;
        public boolean flag;
        public int pageTotal;
    }
}
