package com.kingkung.train.module;

import android.content.Context;

import com.kingkung.train.api.TrainApi;
import com.kingkung.train.api.TrainApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    protected TrainApi provideTrainApi() {
        return TrainApiService.getTrainApi(context);
    }
}
