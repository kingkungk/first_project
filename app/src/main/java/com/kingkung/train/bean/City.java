package com.kingkung.train.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable, Comparable<City> {
    public String abbreviationSpell;
    public String name;
    public String code;
    public String spell;
    public String firstSpell;
    public String num;

    public City() {

    }

    protected City(Parcel in) {
        abbreviationSpell = in.readString();
        name = in.readString();
        code = in.readString();
        spell = in.readString();
        firstSpell = in.readString();
        num = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(abbreviationSpell);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(spell);
        dest.writeString(firstSpell);
        dest.writeString(num);
    }


    @Override
    public int compareTo(City o) {
        return spell.compareTo(o.spell);
    }
}
