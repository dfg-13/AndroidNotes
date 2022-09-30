package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class EditActivity extends AppCompatActivity {

    private EditText noteTitle;
    private EditText noteText;
    private Note note;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = findViewById(R.id.noteTitle);
        noteText = findViewById(R.id.noteText);

        Intent intent = getIntent(); //passing data to a new activity
        if (intent.hasExtra("Note")){
            note = (Note) intent.getSerializableExtra("Note");
            noteTitle.setText(note.getTitle());
            noteText.setText(note.getNoteText());
        }

    }

    public void save(){
        String title = noteTitle.getText().toString();
        String text = noteText.getText().toString();

        if (title.trim().isEmpty()){ //no title is present
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok", (Dialog, id) -> {
                Intent data = new Intent();

                Toast.makeText(this, "Notes with no title are not saved", Toast.LENGTH_LONG).show();
                finish(); //closes activity, returning to main
            });
            builder.setNegativeButton("Cancel", (Dialog, id) -> {}); //does nothing. remains in EditActivity
            builder.setMessage("Note will not be saved without a title");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        note.setTitle(title);
        note.setNoteText(text);
        note.setDateTime(); //takes the current time

        Intent data = new Intent();
        data.putExtra("Note", note);
        data.putExtra("Position", pos);

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public void onBackPressed() { //clicking back button displays Save Confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your note is not saved!");
        builder.setMessage("Save note?");

        builder.setPositiveButton("Yes", (dialog,id) ->{ //approving dialog saves note
            save();
        });
        builder.setNegativeButton("No", (dialog, id) ->{ //does nothing but return without saving
            super.onBackPressed();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSave: //hit the save button
                save();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_2, menu);
        return true;
    }

}