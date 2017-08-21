package com.lozasolutions.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.lozasolutions.data.DataManager;
import com.lozasolutions.injection.ApplicationContext;
import com.lozasolutions.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager apiManager();
}
