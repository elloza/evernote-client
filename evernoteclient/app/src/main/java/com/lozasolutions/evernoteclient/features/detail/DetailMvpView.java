package com.lozasolutions.evernoteclient.features.detail;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.features.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showNoteInformation(Note note);

    void showProgress(boolean show);

    void showError(Throwable error);
}
