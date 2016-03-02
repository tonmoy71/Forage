package io.github.plastix.forage;

import android.app.Application;
import android.content.Context;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.squareup.leakcanary.LeakCanary;

public class ForageApplication extends Application {

    private ApplicationComponent component;

    public static ApplicationComponent getComponent(Context context) {
        return ((ForageApplication) context.getApplicationContext()).getComponent();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //Use debug tools only in debug builds
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            AndroidDevMetrics.initWith(this);
        }
    }

}
