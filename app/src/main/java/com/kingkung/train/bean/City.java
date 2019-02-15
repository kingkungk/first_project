package com.kingkung.train.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingkung.train.ui.adapter.CharacterDividedAdapter;

import java.util.Objects;

public class City extends CharacterDividedAdapter.CharacterItem implements Parcelable, Comparable<City> {
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

    @Override
    public String getCharacter() {
        return String.valueOf(spell.charAt(0)).toUpperCase();
    }

    @Override
    public char getSortCharacter() {
        return spell.charAt(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(code, city.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
