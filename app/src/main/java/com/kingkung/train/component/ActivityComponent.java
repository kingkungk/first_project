package com.kingkung.train.component;

import com.kingkung.train.ConfigActivity;
import com.kingkung.train.LoginActivity;
import com.kingkung.train.TrainActivity;
import com.kingkung.train.ui.activity.CitySelectActivity;
import com.kingkung.train.ui.activity.DateSelectActivity;
import com.kingkung.train.ui.activity.FailedLogActivity;
import com.kingkung.train.ui.activity.MainActivity;
import com.kingkung.train.ui.activity.TrainNoSelectActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(TrainActivity trainActivity);

    void inject(ConfigActivity configActivity);

    void inject(FailedLogActivity failedLogActivity);

    void inject(CitySelectActivity citySelectActivity);

    void inject(DateSelectActivity dateSelectActivity);

    void inject(TrainNoSelectActivity trainNoSelectActivity);
}
