package com.kingkung.train.app;

import android.app.Application;

import com.kingkung.train.component.AppComponent;
import com.kingkung.train.component.DaggerAppComponent;
import com.kingkung.train.module.AppModule;

public class TrainApplication extends Application {
    private static TrainApplication application;

    public AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return mAppComponent;
    }

    public static TrainApplication getApplication() {
        return application;
    }
}
