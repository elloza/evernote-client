package com.lozasolutions.evernoteclient.features.main;

import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.lozasolutions.evernoteclient.data.remote.EvernoteAPI;
import com.lozasolutions.evernoteclient.data.remote.NoteFilterEvernote;
import com.lozasolutions.evernoteclient.features.base.BasePresenter;
import com.lozasolutions.evernoteclient.injection.ConfigPersistent;
import com.lozasolutions.evernoteclient.util.rx.scheduler.SchedulerUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final EvernoteAPI evernoteAPI;
    private List<Note> noteList;

    private int currentOrder = 0;

    private final static int DATE_CREATION_ASC = 1;
    private final static int DATE_CREATION_DSC = 2;
    private final static int ALPHABETICALLY_ASC = 3;
    private final static int ALPHABETICALLY_DSC = 4;
    private final static int DATE_UPDATED_ASC = 5;
    private final static int DATE_UPDATED_DSC = 6;


    @Inject
    public MainPresenter(EvernoteAPI evernoteAPI) {
        this.evernoteAPI = evernoteAPI;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getNotes(int limit, int offset, boolean reload) {

        if (reload) {
            checkViewAttached();
            getView().showProgress(true);
            NoteFilter noteFilter = new NoteFilter();
            noteFilter.setAscending(true);
            loadNotes(limit, offset, getNotefilterByOption(currentOrder));
        } else {
            if (noteList == null) {
                this.noteList = new ArrayList<>();
                getView().showProgress(true);
                loadNotes(limit, offset, getNotefilterByOption(currentOrder));

            } else {
                checkViewAttached();
                getView().showNoteList(noteList,offset);
            }

        }
    }

    public void getMoreNotes(int limit, int offset, boolean reload){

        loadNotes(limit, offset, getNotefilterByOption(currentOrder));

    }


    public NoteFilter getNotefilterByOption(int currentOrder) {

        NoteFilter noteFilter = new NoteFilter();

        switch (currentOrder) {
            case DATE_CREATION_ASC:
                noteFilter.setAscending(true);
                noteFilter.setOrder(NoteSortOrder.CREATED.getValue());
                return noteFilter;

            case DATE_CREATION_DSC:
                noteFilter.setAscending(false);
                noteFilter.setOrder(NoteSortOrder.CREATED.getValue());

                return noteFilter;

            case ALPHABETICALLY_ASC:
                noteFilter.setAscending(true);
                noteFilter.setOrder(NoteSortOrder.TITLE.getValue());

                return noteFilter;

            case ALPHABETICALLY_DSC:

                // TODO This option seems not work as expected....
                noteFilter.setAscending(false);
                noteFilter.setAscendingIsSet(false);
                noteFilter.setOrder(NoteSortOrder.TITLE.getValue());

            case DATE_UPDATED_ASC:
                noteFilter.setAscending(true);
                noteFilter.setOrder(NoteSortOrder.UPDATED.getValue());

                return noteFilter;

            case DATE_UPDATED_DSC:
                noteFilter.setAscending(false);
                noteFilter.setOrder(NoteSortOrder.UPDATED.getValue());

                return noteFilter;

        }

        return noteFilter;

    }


    public void getNotesSortedByDateCreation(int limit, int offset) {

        int order = currentOrder == DATE_CREATION_ASC ? DATE_CREATION_DSC : DATE_CREATION_ASC;
        loadNotes(limit, offset, getNotefilterByOption(order));
        currentOrder = order;
    }

    public void getNotesSortedByDateUpdated(int limit, int offset) {

        int order = currentOrder == DATE_UPDATED_ASC ? DATE_UPDATED_DSC : DATE_UPDATED_ASC;
        loadNotes(limit, offset, getNotefilterByOption(order));
        currentOrder = order;
    }

    public void getNotesSortedByTitle(int limit, int offset) {

        int order = currentOrder == ALPHABETICALLY_ASC ? ALPHABETICALLY_DSC : ALPHABETICALLY_ASC;
        loadNotes(limit, offset, getNotefilterByOption(order));
        currentOrder = order;
    }

    private void loadNotes(int limit, int offset, NoteFilter noteFilter) {

        evernoteAPI.getNotes(new NoteFilterEvernote(noteFilter), offset, limit).compose(SchedulerUtils.ioToMain()).subscribe(
                noteListObtained -> {
                    getView().showProgress(false);

                    if(offset != 0){
                        noteList.addAll(noteListObtained.getNotes());
                    }else{
                        noteList = noteListObtained.getNotes();
                    }

                    getView().showNoteList(noteList,offset);
                },
                throwable -> {
                    getView().showProgress(false);
                    getView().showError(throwable);
                });
    }

    public void createNote(Note note) {

        checkViewAttached();
        getView().showProgress(true);

        evernoteAPI.addNote(note, null).compose(SchedulerUtils.ioToMain()).subscribe(
                createdNote -> {
                    getView().showProgress(false);
                    noteList.add(createdNote);
                    getView().showNoteList(noteList,0);
                },
                throwable -> {
                    getView().showProgress(false);
                    getView().showNoteList(noteList,0);
                });

    }

    public boolean logout() {

        return evernoteAPI.logout();
    }
}
