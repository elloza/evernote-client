package com.lozasolutions.evernoteclient.injection.component;

import com.lozasolutions.evernoteclient.features.detail.DetailActivity;
import com.lozasolutions.evernoteclient.features.login.LoginActivity;
import com.lozasolutions.evernoteclient.features.main.MainActivity;
import com.lozasolutions.evernoteclient.features.splashscreen.SplashActivity;
import com.lozasolutions.evernoteclient.injection.PerActivity;
import com.lozasolutions.evernoteclient.injection.module.ActivityModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

    void inject(LoginActivity loginActivity);

    void inject(DetailActivity detailActivity);
}
