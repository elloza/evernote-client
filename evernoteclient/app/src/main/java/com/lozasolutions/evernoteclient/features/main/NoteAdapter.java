package com.lozasolutions.evernoteclient.features.main;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.util.EvernoteUtil;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;

    private OnNoteClickListener onNoteClickListener;

    /**
     * The interface that defines the onclick listener
     */
    public interface OnNoteClickListener {
        void onNoteClicked(Note note);
    }

    NoteAdapter(OnNoteClickListener listener) {
        onNoteClickListener = listener;
        noteList = Collections.emptyList();
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = this.noteList.get(position);
        holder.onBind(note);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.card_view)
        CardView cv;
        @BindView(R.id.dateCreated)
        TextView dateCreated;
        @BindView(R.id.dateUpdated)
        TextView dateUpdated;
        @BindView(R.id.thumbnail)
        ImageView imageView;

        private Note note;

        NoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onNoteClickListener.onNoteClicked(note));
        }

        void onBind(Note note) {
            this.note = note;
            title.setText(note.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
            String dateCreatedString = sdf.format(new Date(note.getCreated()));

            dateCreatedString = dateCreated.getContext().getString(R.string.created_at) + " " + dateCreatedString;

            dateCreated.setText(dateCreatedString);

            String dateUpdatedString = sdf.format(new Date(note.getUpdated()));

            dateUpdatedString = dateCreated.getContext().getString(R.string.updated_at) + " " + dateUpdatedString;

            dateUpdated.setText(dateUpdatedString);

            if (EvernoteUtil.hasImageResource(note)) {

                imageView.setVisibility(View.VISIBLE);
                /*
                Glide.with(imageView.getContext())
                        .load("https://www.smashingmagazine.com/wp-content/uploads/2015/06/10-dithering-opt.jpg")
                        .into(imageView);
                        */
            } else {
                // make sure Glide doesn't load anything into this view until told otherwise
                /*
                Glide.with(imageView.getContext()).clear(imageView);
                */
                // remove the placeholder (optional); read comments below
                imageView.setVisibility(View.INVISIBLE);
            }

        }
    }

}
