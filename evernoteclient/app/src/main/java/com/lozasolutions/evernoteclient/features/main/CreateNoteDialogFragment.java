package com.lozasolutions.evernoteclient.features.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.evernote.client.android.helper.Cat;
import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.data.local.NoteCreator;

import java.io.IOException;

/**
 * @author rwondratschek
 */
public class CreateNoteDialogFragment extends DialogFragment {

    public static final int REQ_SELECT_IMAGE = 100;

    public static final String TAG = "CreateNoteDialogFragment";

    private static final Cat CAT = new Cat(TAG);

    private static final String KEY_IMAGE_DATA = "KEY_IMAGE_DATA";

    private NoteCreator.ImageData mImageData;

    private String title = "";
    private String content = "";
    TextInputLayout titleView;
    TextInputLayout contentView;

    public interface CreateNormalNoteListener {
        void onNoteCreated(Note note);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mImageData = savedInstanceState.getParcelable(KEY_IMAGE_DATA);
        }
        setRetainInstance(true);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_note, null);
        titleView = view.findViewById(R.id.textInputLayout_title);
        contentView = view.findViewById(R.id.textInputLayout_content);

        titleView.getEditText().setText(title);
        contentView.getEditText().setText(content);

        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (isAdded()) {
                        try {
                            Note note = NoteCreator.createNote(titleView.getEditText().getText().toString(), contentView.getEditText().getText().toString(),
                                    mImageData, null, null);
                            ((CreateNormalNoteListener) getActivity()).onNoteCreated(note);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                    break;
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.create_new_note)
                .setView(view)
                .setPositiveButton(R.string.create, onClickListener)
                .setNegativeButton(android.R.string.cancel, onClickListener)
                .create();
    }


    @Override
    public void onDestroyView() {
        saveData();
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    private void saveData() {
        title = titleView.getEditText().getText().toString();
        content = contentView.getEditText().getText().toString();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


}
