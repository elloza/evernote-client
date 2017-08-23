package com.lozasolutions.evernoteclient.features.handwrittennote;

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
import android.widget.Button;

import com.evernote.client.android.helper.Cat;
import com.lozasolutions.evernoteclient.R;

/**
 * @author rwondratschek
 */
public class TitleDialogFragment extends DialogFragment {

    public static final int REQ_SELECT_IMAGE = 100;

    public static final String TAG = "CreateNoteTitleDialogFragment";

    private static final Cat CAT = new Cat(TAG);

    private String title = "";
    TextInputLayout titleView;

    public interface DialogTitleListener {
        void onTitleNoteEntered(String titleNote);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_title, null);
        titleView = view.findViewById(R.id.textInputLayout_title);

        titleView.getEditText().setText(title);

        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {};


        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_title_dialog)
                .setView(view)
                .setPositiveButton(R.string.create, onClickListener)
                .setNegativeButton(android.R.string.cancel, onClickListener)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (isAdded()) {

                            if (!titleView.getEditText().getText().toString().isEmpty()) {
                                ((DialogTitleListener) getActivity()).onTitleNoteEntered(titleView.getEditText().getText().toString());
                                dismiss();
                            } else {
                                titleView.setError("Enter a valid title");
                            }

                        } else {
                            throw new IllegalStateException();
                        }

                    }
                });
            }
        });

        return alertDialog;
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
