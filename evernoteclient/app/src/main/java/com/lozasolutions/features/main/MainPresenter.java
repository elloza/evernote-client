package com.lozasolutions.features.main;

import com.lozasolutions.data.DataManager;
import com.lozasolutions.features.base.BasePresenter;
import com.lozasolutions.injection.ConfigPersistent;
import com.lozasolutions.util.rx.scheduler.SchedulerUtils;

import java.util.List;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager dataManager;
    private List<String> pokemonList;

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
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
        dataManager
                .getPokemonList(limit)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        pokemons -> {
                            pokemonList = pokemons;
                            getView().showProgress(false);
                            getView().showPokemon(pokemons);
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError(throwable);
                        });
    }
}
