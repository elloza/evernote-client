package com.lozasolutions.evernoteclient.util;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;

import java.util.List;

/**
 * Created by Loza on 22/08/2017.
 */

public class EvernoteUtil {

    private final static String MIME_JPEG = "image/jpeg";
    private final static String MIME_PNG = "image/png";

    public static Boolean hasImageResource(Note note) {

        List<Resource> resources = note.getResources();
        if (resources != null && resources.size() > 0) {

            for (Resource resource : resources) {

                if (resource.getMime().equals(MIME_JPEG) || resource.getMime().equals(MIME_PNG)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Resource getFirstImageResource(Note note){


        List<Resource> resources = note.getResources();
        if (resources != null && resources.size() > 0) {

            for (Resource resource : resources) {

                if (resource.getMime().equals(MIME_JPEG) || resource.getMime().equals(MIME_PNG)) {
                    return resource;
                }
            }
        }

        return null;

    }
}
