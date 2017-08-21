package com.lozasolutions.evernoteclient.features.splashscreen;

import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class SplashPresenter extends BasePresenter<SplashMvpView> {

    private final EvernoteAPI evernoteAPI;

    @Inject
    public SplashPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(SplashMvpView mvpView) {
        super.attachView(mvpView);
    }

    public boolean isAuthenticated() {
        checkViewAttached();
        return evernoteAPI.isLoggedIn();
    }
}
