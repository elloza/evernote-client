package com.lozasolutions.evernoteclient.features.detail;

import com.lozasolutions.evernoteclient.data.model.response.Pokemon;
import com.lozasolutions.evernoteclient.data.model.response.Statistic;
import com.lozasolutions.evernoteclient.features.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showPokemon(Pokemon pokemon);

    void showStat(Statistic statistic);

    void showProgress(boolean show);

    void showError(Throwable error);
}
