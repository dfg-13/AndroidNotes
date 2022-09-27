package com.example.androidnotes;

import android.util.JsonWriter;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    private String title;
    private String noteText;
    private String dateTime;

    public Note(String title, String noteText){
        setTitle(title);
        setNoteText(noteText);
        setDateTime();
    }

    public Note(String title, String noteText, String dateTime){
        setTitle(title);
        setNoteText(noteText);
        setDateTime(dateTime);
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.dateTime = dateFormat.format(date);
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    @NonNull
    public String toJSON(){
        try{
            StringWriter sw = new StringWriter();
            JsonWriter jw = new JsonWriter(sw);
            jw.setIndent("  ");
            jw.beginObject();

            jw.name("title").value(getTitle());
            jw.name("noteText").value(getNoteText());
            jw.name("dateTime").value(getDateTime());

            jw.endObject();
            jw.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                ", lastSave='" + dateTime + '\'' +
                '}';
    }
}
