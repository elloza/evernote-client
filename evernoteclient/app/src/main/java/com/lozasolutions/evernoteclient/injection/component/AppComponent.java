package com.lozasolutions.evernoteclient.injection.component;

import android.app.Application;
import android.content.Context;

import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.ocr.OCRManager;
import com.lozasolutions.evernoteclient.injection.ApplicationContext;
import com.lozasolutions.evernoteclient.injection.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    EvernoteAPI evernoteAPI();

    OCRManager ocrManager();

}
