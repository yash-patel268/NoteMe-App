package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    //Creating variables for the button, listview, and search view to be assigned too
    private Button newNote;
    private ListView noteListView;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Once the app is created the home_screen.xml will load
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        //Initialize the list which will hold notes
        initWidgets();

        //Create object called note adapter which will allow use to adapt/translate the data from the DB
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), Note.noteArrayList);
        noteListView.setAdapter(noteAdapter);

        //Call custom function to get data
        loadFromDBToMemory();

        //Assign the virtual searchView to variable
        search = findViewById(R.id.searchView);

        //Create an text listener which will take user input when text is changed
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //When input is submitted filter for the input
            @Override
            public boolean onQueryTextSubmit(String s) {
                noteAdapter.getFilter().filter(s.toString());
                noteListView.setAdapter(noteAdapter);
                return false;
            }
            //When input is changed filter for the input
            @Override
            public boolean onQueryTextChange(String s) {;
                noteAdapter.getFilter().filter(s);
                noteListView.setAdapter(noteAdapter);
                return false;
            }
        });

        //Assign button to variable
        newNote = findViewById(R.id.newNoteButton);
        //Create onclick listener which will move to new note page
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });
    }

    //Custom function to assign listview to variable
    private void initWidgets(){
        noteListView = findViewById(R.id.listView);
    }

    //Custom function to initial SQLite manager which will pull data and add it to list
    private void loadFromDBToMemory()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateNoteListArray();
    }

    //Custom function to set adapter to take data from DB
    private void setNoteAdapter(){
        NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), Note.noteArrayList);
        noteListView.setAdapter(noteAdapter);
    }

    //Custom function which creates intent to switch pages
    private void newNote(){
        Intent intent = new Intent(this, NewNote.class);
        startActivity(intent);
    }

    //Overwriting onResume returning main page to call adapter for notes
    @Override
    protected void onResume()
    {
        super.onResume();
        setNoteAdapter();
    }
}