package com.lozasolutions.evernoteclient.common;

import com.evernote.edam.type.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Factory class that makes instances of data models with random field values. The aim of this class
 * is to help setting up test fixtures.
 */
public class TestDataFactory {

    private static final Random random = new Random();

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static Note makeNote(String guid, String title, String content) {
        Note note = new Note();
        note.setGuid(guid);
        note.setTitle(title);
        note.setContent(content);
        return note;
    }

    public static List<Note> makeNoteList(int count) {
        List<Note> noteList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            noteList.add(makeNote(String.valueOf(i),String.valueOf(i),String.valueOf(i)));
        }
        return noteList;
    }

}
