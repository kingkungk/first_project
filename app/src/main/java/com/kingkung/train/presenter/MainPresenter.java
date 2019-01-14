package com.kingkung.train.presenter;

import com.kingkung.train.contract.MainContract;
import com.kingkung.train.presenter.base.BasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private Subject<Integer> backSubject = PublishSubject.create();

    @Inject
    public MainPresenter() {
        listenBackEvent(backSubject);
    }

    @Override
    public void clickBack() {
        backSubject.onNext(1);
    }

    public void listenBackEvent(Subject<Integer> back) {
        Disposable disposable = Observable.merge(back, back.debounce(2000, TimeUnit.MILLISECONDS)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer i) throws Exception {
                        return 0;
                    }
                }))
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        if (integer2 == 0) {
                            return 0;
                        }
                        return integer + 1;
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 0;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            mView.showMsg("再按一次退出程序");
                        } else if (integer == 2) {
                            mView.realBack();
                        }
                    }
                });
        addSubscription(disposable);
    }
}
