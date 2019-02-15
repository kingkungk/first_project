package com.kingkung.train.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kingkung.train.api.TrainApi;
import com.kingkung.train.api.Urls;
import com.kingkung.train.bean.City;
import com.kingkung.train.bean.Config;
import com.kingkung.train.bean.response.DataObserver;
import com.kingkung.train.contract.CitySelectContract;
import com.kingkung.train.presenter.base.BasePresenter;
import com.kingkung.train.ui.adapter.CharacterDividedAdapter;
import com.kingkung.train.ui.adapter.CitySelectAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
                .map(new Function<String, String[]>() {
                    @Override
                    public String[] apply(String s) throws Exception {
                        int index = s.indexOf("script/core/common/station_name");
                        String path = s.substring(index, s.indexOf("\"", index));
                        int hotIndex = s.indexOf("script/dist/index/main");
                        String hotPath = s.substring(hotIndex, s.indexOf("\"", hotIndex)) + ".js";
                        return new String[]{Urls.INDEX + path, Urls.INDEX + hotPath};
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<String[]>(mView) {
                    @Override
                    public void success(String[] s) {
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
                        String content = s.substring(s.indexOf("\'") + 1, s.lastIndexOf("\'"));
                        Map<Character, List<City>> cityMap = new TreeMap();
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

    @Override
    public void hotCityCode(String hotCityUrl) {
        Disposable disposable = api.cityCode(hotCityUrl)
                .map(new Function<String, List<City>>() {
                    @Override
                    public List<City> apply(String s) throws Exception {
                        int index = s.indexOf("favorite_names");
                        int startIndex = s.indexOf("\"", index) + 1;
                        String content = s.substring(startIndex, s.indexOf("\"", startIndex));
                        List<City> cities = new ArrayList<>();
                        String[] items = content.split("@");  //bjb|北京北|VAP|beijingbei|bjb|0
                        for (String item : items) {
                            if (TextUtils.isEmpty(item)) {
                                continue;
                            }
                            City city = new City();
                            String[] filed = item.split("\\|");
                            if (filed.length == 6) {
                                city.abbreviationSpell = filed[0];
                                city.name = filed[1];
                                city.code = filed[2];
                                city.spell = filed[3];
                                city.firstSpell = filed[4];
                                city.num = filed[5];
                            } else if (filed.length == 4) {
                                city.abbreviationSpell = filed[0];
                                city.name = filed[1];
                                city.code = filed[2];
                                city.num = filed[3];
                            } else {
                                continue;
                            }

                            cities.add(city);
                        }
                        return cities;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<List<City>>(mView) {
                    @Override
                    public void success(List<City> cities) {
                        mView.hotCityCodeSucceed(cities);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void citySelectListener(CitySelectAdapter citySelectAdapter, EditText etSelectCity) {
        Disposable disposable = RxTextView.textChanges(etSelectCity)
                .skipInitialValue()
                .debounce(50, TimeUnit.MILLISECONDS)
                .map(new Function<CharSequence, Optional<List<City>>>() {
                    @Override
                    public Optional<List<City>> apply(CharSequence charSequence) {
                        String fitter = charSequence.toString();
                        if (TextUtils.isEmpty(fitter)) {
                            return Optional.empty();
                        }
                        List<City> cities = new ArrayList<>();
                        List<CharacterDividedAdapter.CharacterItem> items = citySelectAdapter.getCharacterItem();
                        for (CharacterDividedAdapter.CharacterItem item : items) {
                            City city = (City) item;
                            if (city.firstSpell.startsWith(fitter) || city.spell.startsWith(fitter)
                                    || city.name.startsWith(fitter)) {
                                cities.add(city);
                            }
                        }
                        return Optional.of(cities);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<Optional<List<City>>>(mView) {
                    @Override
                    public void success(Optional<List<City>> optional) {
                        if (optional.isPresent()) {
                            mView.citySelectCallBack(optional.get());
                        } else {
                            mView.citySelectCallBack(null);
                        }
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void readerOftenAndLocation(Context context) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<List<City>>() {
            @Override
            public void subscribe(ObservableEmitter<List<City>> emitter) {
                try {
                    FileReader fileReader = new FileReader(new File(context.getFilesDir(), "often"));
                    City[] cities = new Gson().fromJson(fileReader, City[].class);
                    fileReader.close();
                    emitter.onNext(new ArrayList<>(Arrays.asList(cities)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<List<City>>(mView) {
                    @Override
                    public void success(List<City> cities) {
                        mView.readerOftenAndLocationSucceed(cities);
                    }
                });
        addSubscription(disposable);
    }

    @Override
    public void writeOften(Context context, List<City> cities) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                try {
                    FileWriter fileWriter = new FileWriter(new File(context.getFilesDir(), "often"));
                    new Gson().toJson(cities, fileWriter);
                    fileWriter.close();
                    emitter.onNext(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                emitter.onNext(false);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DataObserver<Boolean>(mView) {
                    @Override
                    public void success(Boolean b) {
                    }
                });
        addSubscription(disposable);
    }
}
