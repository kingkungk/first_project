package com.kingkung.train.component;

import android.content.Context;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.module.AppModule;

import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent {
    Context provideContext();

    TrainApi provideTrainApi();
}
