package com.lozasolutions.evernoteclient.data.remote;

import com.evernote.edam.notestore.NoteFilter;

/**
 * Created by Loza on 21/08/2017.
 */

public class NoteFilterEvernote implements ISpecification {


    public NoteFilterEvernote(NoteFilter noteFilter) {
        this.noteFilter = noteFilter;
    }

    NoteFilter noteFilter;

    public NoteFilter getNoteFilter() {
        return noteFilter;
    }

    public void setNoteFilter(NoteFilter noteFilter) {
        this.noteFilter = noteFilter;
    }
}
