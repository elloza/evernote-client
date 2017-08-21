package com.lozasolutions.evernoteclient.features.login;

import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private final EvernoteAPI evernoteAPI;

    @Inject
    public LoginPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getPokemon(String name) {
        checkViewAttached();
        //TODO
    }
}
