package com.kingkung.train.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

public class PassengerInfo implements Parcelable {
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
    public String mobile_no;
    public String phone_no;
    public String email; //3225319184@qq.com
    public String address;
    public String postalcode;
    public String first_letter;
    public String recordCount; //1
    public String total_times; //99
    public String index_id;
    public String gat_born_date;
    public String gat_valid_date_start;
    public String gat_valid_date_end;
    public String gat_version;

    public boolean isCheck;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerInfo info = (PassengerInfo) o;
        return Objects.equals(passenger_id_no, info.passenger_id_no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passenger_id_no);
    }

    @Override
    public String toString() {
        return passenger_name;
    }

    protected PassengerInfo(Parcel in) {
        code = in.readString();
        passenger_name = in.readString();
        sex_code = in.readString();
        sex_name = in.readString();
        born_date = in.readString();
        country_code = in.readString();
        passenger_id_type_code = in.readString();
        passenger_id_type_name = in.readString();
        passenger_id_no = in.readString();
        passenger_type = in.readString();
        passenger_flag = in.readString();
        passenger_type_name = in.readString();
        mobile_no = in.readString();
        phone_no = in.readString();
        email = in.readString();
        address = in.readString();
        postalcode = in.readString();
        first_letter = in.readString();
        recordCount = in.readString();
        total_times = in.readString();
        index_id = in.readString();
        gat_born_date = in.readString();
        gat_valid_date_start = in.readString();
        gat_valid_date_end = in.readString();
        gat_version = in.readString();
    }

    public static final Creator<PassengerInfo> CREATOR = new Creator<PassengerInfo>() {
        @Override
        public PassengerInfo createFromParcel(Parcel in) {
            return new PassengerInfo(in);
        }

        @Override
        public PassengerInfo[] newArray(int size) {
            return new PassengerInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(passenger_name);
        dest.writeString(sex_code);
        dest.writeString(sex_name);
        dest.writeString(born_date);
        dest.writeString(country_code);
        dest.writeString(passenger_id_type_code);
        dest.writeString(passenger_id_type_name);
        dest.writeString(passenger_id_no);
        dest.writeString(passenger_type);
        dest.writeString(passenger_flag);
        dest.writeString(passenger_type_name);
        dest.writeString(mobile_no);
        dest.writeString(phone_no);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(postalcode);
        dest.writeString(first_letter);
        dest.writeString(recordCount);
        dest.writeString(total_times);
        dest.writeString(index_id);
        dest.writeString(gat_born_date);
        dest.writeString(gat_valid_date_start);
        dest.writeString(gat_valid_date_end);
        dest.writeString(gat_version);
    }

    public class PassengerNormal {
        public List<PassengerInfo> normal_passengers;
    }

    public class PassengerData {
        public List<PassengerInfo> datas;
        public boolean flag;
        public int pageTotal;
    }
}
