package com.example.noteme;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class NewNote extends AppCompatActivity {
    //Creating variables for the buttons, textinputedittext to be assigned too
    private Button cancelButton;
    private Button saveButton;
    private Button yellowButton;
    private Button orangeButton;
    private Button blueButton;
    private Button galleryButton;
    private Button captureButton;
    private TextInputEditText title;
    private TextInputEditText description;
    private Button deleteButton;
    private Note selectedNote;

    //Variables required for the image selection
    private ImageView image;
    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    Uri imageUri;
    private Bitmap bitmap;

    //Variable which will hold user inputs/selections
    private String titleInput;
    private String descriptionInput;
    private String noteColorChoice = "#ffff00";
    private byte[] imageByteArray;

    //Variable to display message if user doesnt fill out the required boxes
    private TextView error;

    //Custom function to set image after taking a capture
    private final ActivityResultLauncher<Intent> captureImageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
            image.setImageBitmap(bitmap);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Once the app is created the new_note.xml will load
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);

        //Assign the graphical components to variables
        title = findViewById(R.id.noteTitleInput);
        description = findViewById(R.id.noteDescriptionInput);
        error = findViewById(R.id.errorTextView);

        galleryButton = findViewById(R.id.galleryButton);

        //Add onclick to button to add image from gallery
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        captureButton = findViewById(R.id.captureButton);

        //Add onclick to button to add image after capture
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureImageActivityResultLauncher.launch(takePictureIntent);
            }
        });

        captureButton = findViewById(R.id.captureButton);
        image = findViewById(R.id.imageView);

        //Assign onclick listener to allow user to click on object to add image
        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery = new Intent();
                gallery.setType("image/");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                Intent chooser = Intent.createChooser(gallery, "Pick between gallery and camera");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
                startActivityForResult(chooser, PICK_IMAGE);
            }
        });*/

        //Assign button and give it a onclick to delete note if required
        deleteButton = findViewById(R.id.deleteNoteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote(view);
            }
        });

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
                    saveNote(view);
                }
            }
        });

        checkForEditNote();
    }

    //Custom function which uses intent to return to main page
    public void backFn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Custom function which allows notes to be edited
    private void checkForEditNote()
    {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null)
        {
            title.setText(selectedNote.getTitle());
            title.setBackgroundColor(Color.parseColor(selectedNote.getNoteColor()));
            description.setText(selectedNote.getDescription());
            description.setBackgroundColor(Color.parseColor(selectedNote.getNoteColor()));
            if(selectedNote.getImage() != null){
                image.setImageBitmap(BitmapFactory.decodeByteArray(selectedNote.getImage(), 0, selectedNote.getImage().length));
            }
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    //Custom function which get all values entered and sends to DB
    public void saveNote(View view){
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        titleInput = String.valueOf(title.getText());
        descriptionInput = String.valueOf(description.getText());
        imageByteArray = imageViewToByteArray(image);

        if (selectedNote == null){
            int id = Note.noteArrayList.size();
            Note newNote = new Note(id, titleInput, descriptionInput, noteColorChoice, imageByteArray);
            Note.noteArrayList.add(newNote);
            sqLiteManager.addNoteToDatabase(newNote);
        } else{
            selectedNote.setTitle(titleInput);
            selectedNote.setDescription(descriptionInput);
            selectedNote.setNoteColor(noteColorChoice);
            selectedNote.setImage(imageByteArray);
            sqLiteManager.updateNoteInDB(selectedNote);
        }
        backFn();
    }

    //Custom function which will allow notes to be deleted from sqlite table
    public void deleteNote(View view)
    {
        selectedNote.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateNoteInDB(selectedNote);
        finish();
    }

    //Override function to allow image selection from gallery to occur
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Convert the imageview object to byte array to be stored in sqlite table
    public byte[] imageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}