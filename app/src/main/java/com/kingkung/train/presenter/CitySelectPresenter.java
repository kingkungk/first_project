package com.kingkung.train.presenter;

import android.text.TextUtils;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.api.Urls;
import com.kingkung.train.bean.City;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.contract.CitySelectContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CitySelectPresenter extends BasePresenter<CitySelectContract.View> implements CitySelectContract.Presenter {
    private TrainApi api;

    @Inject
    public CitySelectPresenter(TrainApi api) {
        this.api = api;
    }

    @Override
    public void index() {
        Disposable disposable = api.index()
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        int index = s.indexOf("script/core/common/station_name");
                        String path = s.substring(index, s.indexOf("\"", index));
                        return Urls.INDEX + path;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<String>(mView) {
                    @Override
                    public void success(String s) {
                        mView.indexSucceed(s);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void cityCode(String cityUrl) {
        Disposable disposable = api.cityCode(cityUrl)
                .map(new Function<String, List<City>>() {
                    @Override
                    public List<City> apply(String s) throws Exception {
                        Map<Character, List<City>> cityMap = new TreeMap();
                        String content = s.substring(s.indexOf("\'"), s.lastIndexOf("\'"));
                        String[] items = content.split("@");  //bjb|北京北|VAP|beijingbei|bjb|0
                        for (String item : items) {
                            if (TextUtils.isEmpty(item)) {
                                continue;
                            }
                            City city = new City();
                            String[] filed = item.split("\\|");
                            if (filed.length != 6) {
                                continue;
                            }
                            city.abbreviationSpell = filed[0];
                            city.name = filed[1];
                            city.code = filed[2];
                            city.spell = filed[3];
                            city.firstSpell = filed[4];
                            city.num = filed[5];

                            Character c = city.spell.charAt(0);
                            List<City> cities = cityMap.get(c);
                            if (cities == null) {
                                cities = new ArrayList<>();
                                cityMap.put(c, cities);
                            }
                            cities.add(city);
                        }
                        List<City> resultCities = new ArrayList<>();
                        Collection<List<City>> values = cityMap.values();
                        Iterator<List<City>> it = values.iterator();
                        while (it.hasNext()) {
                            resultCities.addAll(it.next());
                        }
                        return resultCities;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<List<City>>(mView) {
                    @Override
                    public void success(List<City> cities) {
                        mView.cityCodeSucceed(cities);
                    }
                });
        addSubscription(disposable);
    }
}
