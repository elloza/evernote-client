package com.lozasolutions.evernoteclient.features.main;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.features.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showNoteList(List<Note> noteList,int offset);

    void showProgress(boolean show);

    void showError(Throwable error);

    void showNoteCreationError(Throwable error);

    void showNoteCreationSuccessfully();
}
