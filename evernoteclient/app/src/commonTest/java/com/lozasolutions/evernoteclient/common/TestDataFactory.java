package com.lozasolutions.evernoteclient.common;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.data.model.response.NamedResource;
import com.lozasolutions.evernoteclient.data.model.response.Sprites;
import com.lozasolutions.evernoteclient.data.model.response.Statistic;

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

    public static List<String> makePokemonNameList(List<NamedResource> pokemonList) {
        List<String> names = new ArrayList<>();
        for (NamedResource pokemon : pokemonList) {
            names.add(pokemon.name);
        }
        return names;
    }

    public static Statistic makeStatistic() {
        Statistic statistic = new Statistic();
        statistic.baseStat = random.nextInt();
        statistic.stat = makeNamedResource(randomUuid());
        return statistic;
    }

    public static List<Statistic> makeStatisticList(int count) {
        List<Statistic> statisticList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            statisticList.add(makeStatistic());
        }
        return statisticList;
    }

    public static Sprites makeSprites() {
        Sprites sprites = new Sprites();
        sprites.frontDefault = randomUuid();
        return sprites;
    }

    public static NamedResource makeNamedResource(String unique) {
        NamedResource namedResource = new NamedResource();
        namedResource.name = randomUuid() + unique;
        namedResource.url = randomUuid();
        return namedResource;
    }

    public static List<NamedResource> makeNamedResourceList(int count) {
        List<NamedResource> namedResourceList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            namedResourceList.add(makeNamedResource(String.valueOf(i)));
        }
        return namedResourceList;
    }
}
