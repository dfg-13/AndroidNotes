package com.example.androidnotes;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerView recyclerView;
    private MyAdapter mAdapter; //data to recyclerView adapter
    private final List<Note> noteList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recycler);
        mAdapter = new MyAdapter(noteList, this);
        recyclerView.setAdapter(mAdapter);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

    }

    public void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }
        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK){
            Note note = (Note) data.getSerializableExtra("Note");

        }
        else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }

    public List<Note> load(){
        List<Note> noteLst = new ArrayList<>();
        try{
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        return noteLst;
    }

    public void save(){
        Log.d(TAG, "save: Saving JSON File");
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(noteList);

            printWriter.close();
            fos.close();
        } catch (Exception e){
            e.getStackTrace();
        }
    }

    public void delete(int pos){ //enter the index/position of the note to be deleted
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note?");

        builder.setPositiveButton("Yes", (dialog, id) -> {
            noteList.remove(pos); //delete it from the list at the marked index 'pos'
            mAdapter.notifyItemRemoved(pos); //update the adapter
        });
        builder.setNegativeButton("No", (dialog, id) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAbout:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemAdd:
                Intent intent1 = new Intent(this, EditActivity.class);
                startActivity(intent1);
                //Toast.makeText(this, "Add Note", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {//click on a note, enter that note
        int pos = recyclerView.getChildLayoutPosition(view);
        Note n = noteList.get(pos); //fetch position of the note at 'pos' in the list
        //TODO: access note to edit

    }

    @Override
    public boolean onLongClick(View view) { //when holding a note, use delete function
        int pos = recyclerView.getChildLayoutPosition(view);
        delete(pos);
        return false;
    }
}