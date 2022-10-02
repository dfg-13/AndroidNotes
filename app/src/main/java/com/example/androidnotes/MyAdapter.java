package com.example.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    private static final String TAG = "NoteAdapter";
    private final List<Note> noteList;
    private final MainActivity mainAct;

    public MyAdapter(List<Note> noteList, MainActivity ma) {
        this.noteList = noteList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note " + position);

        Note note = noteList.get(position);

        String title = note.getTitle();
        String noteText = note.getNoteText();

        if ((title.length() >= 80)){ //title is more than 80 characters
            //first 80 characters will be taken as a substring
            holder.title.setText(String.format("%s...", title.substring(0,80)));
        }
        else{ //title is less than 80 characters
            holder.title.setText(note.getTitle());
        }

        if (noteText.length() >= 80){ //text is more than 80 characters
            //first 80 characters will be taken as a substring
            holder.summary.setText(String.format("%s...", title.substring(0,80)));
        }
        else{ //text is less than 80 characters
            holder.summary.setText(note.getNoteText());
        }

        holder.dateTime.setText(note.getDateTime());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
