package com.example.noteme;

import java.util.ArrayList;
import java.util.Date;
public class Note {
    //Note class for note object
    public static ArrayList<Note> noteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA =  "noteEdit";

    //Variables which will be used by the object
    private int id;
    private String title;
    private String description;
    private Date deleted;
    private String noteColor;
    private byte[] image;

    //Assign all values to a note with date if deleted
    public Note(int id, String title, String description, Date deleted, String noteColor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deleted = deleted;
        this.noteColor = noteColor;
        this.image = image;
    }

    //Assign all values to a note with date if deleted
    public Note(int id, String title, String description, Date deleted, String noteColor, byte[] image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deleted = deleted;
        this.noteColor = noteColor;
        this.image = image;
    }

    //Assign all values to a note with date if not deleted
    public Note(int id, String title, String description, String noteColor) {
        this.id = id;
        this.title = title;
        this.description = description;
        deleted = null;
        this.noteColor = noteColor;
        this.image = image;
    }

    //Assign all values to a note with date if not deleted
    public Note(int id, String title, String description, String noteColor, byte[] image) {
        this.id = id;
        this.title = title;
        this.description = description;
        deleted = null;
        this.noteColor = noteColor;
        this.image = image;
    }

    public static Note getNoteForID(int passedNoteID)
    {
        for (Note note : noteArrayList)
        {
            if(note.getId() == passedNoteID)
                return note;
        }

        return null;
    }

    public static ArrayList<Note> nonDeletedNotes()
    {
        ArrayList<Note> nonDeleted = new ArrayList<>();
        for(Note note : noteArrayList)
        {
            if(note.getDeleted() == null)
                nonDeleted.add(note);
        }

        return nonDeleted;
    }

    //Creating getters and setter for each component of a note
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
