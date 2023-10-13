package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class NewNote extends AppCompatActivity {
    //Creating variables for the buttons, textinputedittext to be assigned too
    private Button cancelButton;
    private Button saveButton;
    private Button yellowButton;
    private Button orangeButton;
    private Button blueButton;
    private TextInputEditText title;
    private TextInputEditText description;

    //Variable which will hold user inputs/selections
    private String titleInput;
    private String descriptionInput;
    private String noteColorChoice = "#ffff00";

    //Variable to display message if user doesnt fill out the required boxes
    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Once the app is created the new_note.xml will load
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);

        //Assign the graphical components to variables
        title = findViewById(R.id.noteTitleInput);
        description = findViewById(R.id.noteDescriptionInput);
        error = findViewById(R.id.errorTextView);

        //Assign button and give it a onclick to change the background colour of note to yellow
        yellowButton = findViewById(R.id.btnYellowBackground);
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setBackgroundColor(Color.parseColor("#ffff00"));
                description.setBackgroundColor(Color.parseColor("#ffff00"));
                noteColorChoice = "#ffff00";
            }
        });

        //Assign button and give it a onclick to change the background colour of note to orange
        orangeButton = findViewById(R.id.btnOrangeBackground);
        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setBackgroundColor(Color.parseColor("#ff9900"));
                description.setBackgroundColor(Color.parseColor("#ff9900"));
                noteColorChoice = "#ff9900";
            }
        });

        //Assign button and give it a onclick to change the background colour of note to blue
        blueButton = findViewById(R.id.btnBlueBackground);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setBackgroundColor(Color.parseColor("#0099ff"));
                description.setBackgroundColor(Color.parseColor("#0099ff"));
                noteColorChoice = "#0099ff";
            }
        });

        //Assign button and give it a onclick to return to home page without creating note
        cancelButton = findViewById(R.id.cancelNewNoteButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backFn();
            }
        });

        //Assign button and give it a onclick to save note and return to home page
        saveButton = findViewById(R.id.saveNewNoteButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Display message if not all input is filled
                if(title.getText().toString().isEmpty() || description.getText().toString().isEmpty()){
                    error.setText("Please enter Title and/or description");
                //If all are inputted save note
                } else {
                    saveNote();
                }
            }
        });

    }

    //Custom function which uses intent to return to main page
    public void backFn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Custom function which get all values entered and sends to DB
    public void saveNote(){
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        titleInput = String.valueOf(title.getText());
        descriptionInput = String.valueOf(description.getText());

        int id = Note.noteArrayList.size();
        Note newNote = new Note(id, titleInput, descriptionInput, noteColorChoice);
        Note.noteArrayList.add(newNote);
        sqLiteManager.addNoteToDatabase(newNote);


        backFn();
        finish();
    }
}