package com.lozasolutions.evernoteclient.features.detail;

import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final EvernoteAPI evernoteAPI;

    @Inject
    public DetailPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getPokemon(String name) {
        checkViewAttached();
        getView().showProgress(true);
        //TODO
    }
}
