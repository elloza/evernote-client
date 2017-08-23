package com.lozasolutions.evernoteclient.features.handwrittennote;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;

import javax.inject.Inject;

@ConfigPersistent
public class CreateHandwrittenNotePresenter extends BasePresenter<CreateHandwrittenNoteMvpView> {

    private final EvernoteAPI evernoteAPI;

    private Note note;

    @Inject
    public CreateHandwrittenNotePresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(CreateHandwrittenNoteMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getNoteComplete(String GUID) {
        checkViewAttached();

    }

}
