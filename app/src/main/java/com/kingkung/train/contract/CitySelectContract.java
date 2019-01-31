package com.kingkung.train.contract;

import com.kingkung.train.bean.City;
import com.kingkung.train.contract.base.BaseContract;

import java.util.List;

public interface CitySelectContract {
    interface View extends BaseContract.View {
        void indexSucceed(String[] cityUrl);

        void cityCodeSucceed(List<City> cities);

        void hotCityCodeSucceed(List<City> cities);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void index();

        void cityCode(String cityUrl);

        void hotCityCode(String hotCityUrl);
    }
}
