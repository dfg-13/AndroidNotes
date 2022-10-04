package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText noteTitle;
    EditText noteText;
    Note editNote;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = findViewById(R.id.noteTitle);
        noteText = findViewById(R.id.noteText);
        noteText.setMovementMethod(new ScrollingMovementMethod());//allows for scrolling capability

        Intent intent = getIntent(); //passing data to a new activity
        if (intent.hasExtra("EDIT_NOTE")){
            editNote = (Note) intent.getSerializableExtra("EDIT_NOTE");
            if (editNote != null){ //check to make sure the note is not empty/null
                noteTitle.setText(editNote.getTitle());
                noteText.setText(editNote.getNoteText());
            }
        }
        if (intent.hasExtra("Position")){
            position = intent.getIntExtra("position", 0);
        }
    }

    public void save(){
        String title = noteTitle.getText().toString();
        String text = noteText.getText().toString();
        Note nTemp = new Note(title, text);

        if (title.trim().isEmpty()){ //no title is present, but there is text
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok", (Dialog, id) -> { //Selecting Ok should close the dialog and EditActivity without saving
                Toast.makeText(this, "Notes with no title are not saved", Toast.LENGTH_LONG).show();
                finish(); //closes activity, returning to main without saving
            });
            builder.setNegativeButton("Cancel", (Dialog, id) -> {}); //does nothing. remains in EditActivity
            builder.setMessage("Note will not be saved without a title");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (title.trim().isEmpty() && text.trim().isEmpty()){ //COMPLETELY EMPTY NOTE
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok", (Dialog, id) -> {finish();});
            builder.setNegativeButton("Cancel", (Dialog, id) -> {}); //does nothing. remains in EditActivity
            builder.setMessage("Leave the empty note?");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else { //new title and new noteText
            nTemp.setTitle(title);
            nTemp.setNoteText(text);
            nTemp.setDateTime(); //takes the current time

            String key = "NEW_NOTE";
            Intent intent = getIntent();
            if(intent.hasExtra("EDIT_NOTE")){
                key = "UPDATE_NOTE";
            }

            Intent data = new Intent();
            data.putExtra(key, nTemp);
            data.putExtra("Note", nTemp);
            data.putExtra("Position", position);
            if (intent.hasExtra("EDIT_POS")){
                int pos = intent.getIntExtra("EDIT_POS", 0);
                data.putExtra("UPDATE_POS", pos);
            }
            setResult(RESULT_OK, data);
            finish();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {// create the menu for the save button
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_2, menu);
        return true;
    }

}