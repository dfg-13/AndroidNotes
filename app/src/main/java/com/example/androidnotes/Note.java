package com.example.androidnotes;

import androidx.annotation.NonNull;

public class Note {
    private String title;
    private String noteText;
    private String lastSave;

    public Note(String title, String noteText, String lastSave){
        this.title = title;
        this.noteText = noteText;
        this.lastSave = lastSave;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getLastSave() {
        return lastSave;
    }

    public void setLastSave(String lastSave) {
        this.lastSave = lastSave;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                ", lastSave='" + lastSave + '\'' +
                '}';
    }
}
