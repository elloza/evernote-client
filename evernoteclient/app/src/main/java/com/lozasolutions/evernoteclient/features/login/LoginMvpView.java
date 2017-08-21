package com.lozasolutions.evernoteclient.features.login;

import com.lozasolutions.evernoteclient.features.base.MvpView;

public interface LoginMvpView extends MvpView {

    void showError(Throwable error);
}
