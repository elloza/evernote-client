package com.lozasolutions.evernoteclient.features.main;

import com.lozasolutions.evernoteclient.features.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showNoteList(List<String> notes);

    void showProgress(boolean show);

    void showError(Throwable error);
}
