package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView dateTime;
    TextView summary;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.noteRowTitle);
        summary = itemView.findViewById(R.id.peekText);
        dateTime = itemView.findViewById(R.id.dateTime);
    }
}
