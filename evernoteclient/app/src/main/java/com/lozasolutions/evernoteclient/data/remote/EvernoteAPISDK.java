package com.lozasolutions.evernoteclient.data.remote;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;

import io.reactivex.Single;

/**
 * Created by Loza on 21/08/2017.
 */


/***
 *
 * This class is a RXJava wrapper for EvernoteClient obtained from EvernoteSession. The Author recommends use something like RxJava
 * @see <a href="https://github.com/vRallev/android-task">https://github.com/vRallev/android-task</a>
 *
 */
public class EvernoteAPISDK implements EvernoteAPI {

    private EvernoteSession session;

    public EvernoteAPISDK(EvernoteSession session) {
        this.session = session;
    }

    @Override
    public Single<Note> addNote(Note note, LinkedNotebook linkedNotebook) {

        return Single.create(e -> {

            EvernoteCallback<Note> noteEvernoteCallback = new EvernoteCallback<Note>() {
                @Override
                public void onSuccess(Note result) {
                    if (!e.isDisposed()) {
                        e.onSuccess(result);
                    }
                }

                @Override
                public void onException(Exception exception) {

                    if (!e.isDisposed()) {
                        e.onError(new Throwable(exception));
                    }
                }
            };

            session.getEvernoteClientFactory().getNoteStoreClient().createNoteAsync(note, noteEvernoteCallback);

            if (note.getNotebookGuid() == null && linkedNotebook != null) {

                session.getEvernoteClientFactory().getLinkedNotebookHelper(linkedNotebook).createNoteInLinkedNotebookAsync(note,noteEvernoteCallback);

            } else {
                session.getEvernoteClientFactory().getNoteStoreClient().createNoteAsync(note, noteEvernoteCallback);
            }

        });
    }

    @Override
    public Single<Note> getNote(String GUID) {
        return Single.create(e -> {

            EvernoteCallback<Note> noteEvernoteCallback = new EvernoteCallback<Note>() {
                @Override
                public void onSuccess(Note result) {
                    if (!e.isDisposed()) {
                        e.onSuccess(result);
                    }
                }

                @Override
                public void onException(Exception exception) {

                    if (!e.isDisposed()) {
                        e.onError(new Throwable(exception));
                    }
                }
            };

            session.getEvernoteClientFactory().getNoteStoreClient()
                    .getNoteAsync(GUID,true,true,true,true,
                    noteEvernoteCallback);

        });
    }

    @Override
    public Single<NoteList> getNotes(ISpecification query, Integer offset, Integer maxResults) {
        return Single.create(e -> {

            EvernoteCallback<NoteList> noteListEvernoteCallback = new EvernoteCallback<NoteList>() {
                @Override
                public void onSuccess(NoteList result) {
                    if (!e.isDisposed()) {
                        e.onSuccess(result);
                    }
                }

                @Override
                public void onException(Exception exception) {

                    if (!e.isDisposed()) {
                        e.onError(new Throwable(exception));
                    }
                }
            };

            session.getEvernoteClientFactory().getNoteStoreClient().findNotesAsync(((NoteFilterEvernote) query).getNoteFilter(), offset, maxResults, noteListEvernoteCallback);

        });
    }

    @Override
    public Boolean isLoggedIn() {
        return session.isLoggedIn();
    }

    @Override
    public Boolean logout() {
        return session.logOut();
    }

}
