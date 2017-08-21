package com.lozasolutions.features.detail;

import com.lozasolutions.data.model.response.Pokemon;
import com.lozasolutions.data.model.response.Statistic;
import com.lozasolutions.features.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showPokemon(Pokemon pokemon);

    void showStat(Statistic statistic);

    void showProgress(boolean show);

    void showError(Throwable error);
}
