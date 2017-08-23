package com.lozasolutions.evernoteclient.features.handwrittennote;

import com.lozasolutions.evernoteclient.features.base.MvpView;

public interface CreateHandwrittenNoteMvpView extends MvpView {

    void showOcrResultRealTime(String result);

    void showError(Throwable error);

    void noteCreatedSuccessfully();

    void showProgress(boolean visible);
}
