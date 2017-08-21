package com.lozasolutions.evernoteclient.features.main;

import java.util.List;

import com.lozasolutions.evernoteclient.features.base.MvpView;

public interface MainMvpView extends MvpView {

    void showPokemon(List<String> pokemon);

    void showProgress(boolean show);

    void showError(Throwable error);
}
