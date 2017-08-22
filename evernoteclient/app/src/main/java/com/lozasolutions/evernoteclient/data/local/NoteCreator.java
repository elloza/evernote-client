package com.lozasolutions.evernoteclient.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.conn.mobile.FileData;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Loza on 22/08/2017.
 */

public class NoteCreator {


    public static Note createNote(String title, String mcontent, ImageData imageData, Notebook notebook, LinkedNotebook linkedNotebook) throws IOException {

        Note note = new Note();
        note.setTitle(title);

        if (notebook != null) {
            note.setNotebookGuid(notebook.getGuid());
        }

        if (imageData == null) {
            note.setContent(EvernoteUtil.NOTE_PREFIX + mcontent + EvernoteUtil.NOTE_SUFFIX);
            return note;
        }

        InputStream in = null;
        try {
            // Hash the data in the image file. The hash is used to reference the file in the ENML note content.
            in = new BufferedInputStream(new FileInputStream(imageData.getPath()));
            FileData data = new FileData(EvernoteUtil.hash(in), new File(imageData.getPath()));

            ResourceAttributes attributes = new ResourceAttributes();
            attributes.setFileName(imageData.getFileName());

            // Create a new Resource
            Resource resource = new Resource();
            resource.setData(data);
            resource.setMime(imageData.getMimeType());
            resource.setAttributes(attributes);

            note.addToResources(resource);

            // Set the note's ENML content
            String content = EvernoteUtil.NOTE_PREFIX
                    + mcontent
                    + EvernoteUtil.createEnMediaTag(resource)
                    + EvernoteUtil.NOTE_SUFFIX;

            note.setContent(content);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return note;
    }

    public static class ImageData implements Parcelable {

        private final String mPath;
        private final String mFileName;
        private final String mMimeType;

        public ImageData(String path, String fileName, String mimeType) {
            mPath = path;
            mFileName = fileName;
            mMimeType = mimeType;
        }

        public String getPath() {
            return mPath;
        }

        public String getFileName() {
            return mFileName;
        }

        public String getMimeType() {
            return mMimeType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mPath);
            dest.writeString(mFileName);
            dest.writeString(mMimeType);
        }

        public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
            @Override
            public ImageData createFromParcel(final Parcel source) {
                return new ImageData(source.readString(), source.readString(), source.readString());
            }

            @Override
            public ImageData[] newArray(final int size) {
                return new ImageData[size];
            }
        };
    }
}
