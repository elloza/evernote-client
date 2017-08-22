package com.lozasolutions.evernoteclient.features.detail;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;
import com.lozasolutions.evernoteclient.util.rx.scheduler.SchedulerUtils;

import javax.inject.Inject;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final EvernoteAPI evernoteAPI;

    private Note note;

    @Inject
    public DetailPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getNoteComplete(String GUID) {
        checkViewAttached();
        getView().showProgress(true);
        if(note == null){
            evernoteAPI.getNote(GUID)
                    .compose(SchedulerUtils.ioToMain()).subscribe(
                    note -> {
                        this.note = note;
                        getView().showProgress(false);
                        getView().showNoteInformation(note);
                    },
                    throwable -> {
                        getView().showProgress(false);
                        getView().showError(throwable);
                    });
        }else{
            getView().showProgress(false);
            getView().showNoteInformation(note);
        }

    }

}
