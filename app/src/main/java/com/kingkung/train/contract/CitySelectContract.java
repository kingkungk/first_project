package com.kingkung.train.contract;

import android.content.Context;
import android.widget.EditText;

import com.kingkung.train.bean.City;
import com.kingkung.train.contract.base.BaseContract;
import com.kingkung.train.ui.adapter.CitySelectAdapter;

import java.util.List;

public interface CitySelectContract {
    interface View extends BaseContract.View {
        void indexSucceed(String[] cityUrl);

        void cityCodeSucceed(List<City> cities);

        void hotCityCodeSucceed(List<City> cities);

        void citySelectCallBack(List<City> cities);

        void readerOftenAndLocationSucceed(List<City> cities);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void index();

        void cityCode(String cityUrl);

        void hotCityCode(String hotCityUrl);

        void citySelectListener(CitySelectAdapter citySelectAdapter, EditText etSelectCity);

        void readerOftenAndLocation(Context context);

        void writeOften(Context context, List<City> cities);
    }
}
