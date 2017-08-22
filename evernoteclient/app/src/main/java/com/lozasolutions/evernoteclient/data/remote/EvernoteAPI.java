package com.lozasolutions.evernoteclient.data.remote;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;

import io.reactivex.Single;

/**
 * Created by Loza on 21/08/2017.
 */

public interface EvernoteAPI {


    Single<Note> addNote(Note note, LinkedNotebook linkedNotebook);

    Single<Note> getNote(String GUID);

    Single<NoteList> getNotes(ISpecification query, Integer offset, Integer maxResults);

    Boolean isLoggedIn();

    Boolean logout();

}
