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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerView recyclerView;
    private MyAdapter mAdapter; //data to recyclerView adapter
    private List<Note> noteList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        mAdapter = new MyAdapter(noteList, this);
        recyclerView.setAdapter(mAdapter);

        noteList.clear();//empty out the list before loading
        noteList.addAll(load()); //load up the notes

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

    }

    private void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }
        if (result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            if (data == null){
                return;
            }
            if (data.hasExtra("NEW_NOTE")){
                Note note = (Note) data.getSerializableExtra("Note");
                noteList.add(0,note);
                save();
                mAdapter.notifyItemInserted(0);
            }
            /*
            if (data.getBooleanExtra("New?", false) &&
            data.getBooleanExtra("Edited?", true)){
                int val = data.getIntExtra("pos", 0);
                noteList.remove(val);
                noteList.add(0,note);
                save();
                mAdapter.notifyItemMoved(val, 0);
                mAdapter.notifyItemChanged(0);
            }

             */
        }
    }

    public List<Note> load(){
        List<Note> noteLst = new ArrayList<>();
        try{
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArr = new JSONArray(sb.toString());
            //because it has to load N amount of notes it would have to load the JSONs for each note
            for (int i=0; i<jsonArr.length(); i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i); //fetch the JSON object within the JSON array at 'i' index
                String noteTitle = jsonObject.getString("title");
                String noteText = jsonObject.getString("text");
                String time = jsonObject.getString("time");
                Note n = new Note(noteTitle, noteText, time);
                noteLst.add(n);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "No JSON file present", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();
        }
        return noteLst;
    }

    public void save(){
        setTitle("Android Notes " + "(" + noteList.size() + ")");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAbout:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemAdd:
                Intent intent1 = new Intent(this, EditActivity.class);
                Note n = new Note("", "");
                intent1.putExtra("Note", n);
                intent1.putExtra("Position", -1);
                activityResultLauncher.launch(intent1);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause(){//saves content when not in activity
        save();//if HOME button is pressed and returns to activity, then info is saved upon return
        super.onPause();
    }

    @Override
    public void onClick(View view) {//click on a note, enter that note
        int pos = recyclerView.getChildLayoutPosition(view);
        Note n = noteList.get(pos); //fetch position of the note at 'pos' in the list
        //given a position(index) and the index of said note enter the note
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Note", n);
        intent.putExtra("Position", pos);
        intent.putExtra("New?", false); //clicking on an already existing note, therefore not new
        activityResultLauncher.launch(intent);//using given data access the note
    }

    @Override
    public boolean onLongClick(View view) { //when holding a note, use delete function
        int pos = recyclerView.getChildLayoutPosition(view);
        //using position/index delete the note at that 'pos'
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteList.remove(pos); //remove note from the list at given position
                save(); //save is performed whenever a note is deleted.
                mAdapter.notifyItemRemoved(pos); //update the adapter
            }
        });
        builder.setNegativeButton("NO", (dialog, id) -> {});
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
}