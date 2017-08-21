package com.lozasolutions.evernoteclient.features.main;

import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;

import java.util.List;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final EvernoteAPI evernoteAPI;
    private List<String> pokemonList;

    @Inject
    public MainPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getPokemon(int limit, boolean reload) {

        if(reload) {
            checkViewAttached();
            getView().showProgress(true);
         loadPokemons(limit);
        }else {
            if(pokemonList == null){
                getView().showProgress(true);
                loadPokemons(limit);

            }else{
                checkViewAttached();
                getView().showPokemon(pokemonList);
            }

        }
    }

    private void loadPokemons(int limit){
        //TODO load
    }
}
