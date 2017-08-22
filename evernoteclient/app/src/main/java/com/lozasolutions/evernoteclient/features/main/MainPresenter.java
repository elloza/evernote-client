package com.lozasolutions.evernoteclient.features.main;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.data.remote.NoteFilterEvernote;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;
import com.lozasolutions.evernoteclient.util.rx.scheduler.SchedulerUtils;

import java.util.List;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final EvernoteAPI evernoteAPI;
    private List<Note> noteList;

    @Inject
    public MainPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getNotes(int limit, boolean reload) {

        if(reload) {
            checkViewAttached();
            getView().showProgress(true);
         loadNotes(limit);
        }else {
            if(noteList == null){
                getView().showProgress(true);
                loadNotes(limit);

            }else{
                checkViewAttached();
                getView().showNoteList(noteList);
            }

        }
    }

    private void loadNotes(int limit){

        NoteFilter noteFilter = new NoteFilter();
        noteFilter.setAscending(true);
        evernoteAPI.getNotes(new NoteFilterEvernote(noteFilter),0,limit).compose(SchedulerUtils.ioToMain()).subscribe(
                noteListObtained -> {
                    getView().showProgress(false);

                    noteList = noteListObtained.getNotes();
                    getView().showNoteList(noteListObtained.getNotes());
                },
                throwable -> {
                    getView().showProgress(false);
                    getView().showError(throwable);
                });
    }

    public boolean logout(){

        return evernoteAPI.logout();
    }
}
