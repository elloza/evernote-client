package com.lozasolutions.evernoteclient;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.lozasolutions.evernoteclient.injection.ApplicationContext;
import com.lozasolutions.evernoteclient.injection.component.AppComponent;
import com.lozasolutions.evernoteclient.injection.component.DaggerAppComponent;
import com.lozasolutions.evernoteclient.injection.module.AppModule;
import com.lozasolutions.evernoteclient.injection.module.NetworkModule;
import com.singhajit.sherlock.core.Sherlock;
import com.squareup.leakcanary.LeakCanary;
import com.tspoon.traceur.Traceur;

import timber.log.Timber;

public class EvernoteClientApplication extends MultiDexApplication {

    private AppComponent appComponent;

    public static EvernoteClientApplication get( @ApplicationContext  Context context) {
        return (EvernoteClientApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initializeWithDefaults(this);
            LeakCanary.install(this);
            Sherlock.init(this);
            Traceur.enableLogging();
        }

        registerActivityLifecycleCallbacks(new LoginChecker(getComponent().evernoteAPI()));
    }

    public AppComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .networkModule(new NetworkModule(this, ""))
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}
