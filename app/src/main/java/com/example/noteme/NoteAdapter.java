package com.example.noteme;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> implements Filterable {
    //Adapter class which will allow notes to be assigned one cell by cell basis

    //Initialize variables which will be used to filter notes
    Context context;
    List<Note> noteModelList;
    List<Note> mOriginalValues;

    //Sets adapter to contain data
    public NoteAdapter(Context context, List<Note> noteModelList)
    {
        super(context, 0, noteModelList);
        this.context = context;
        this.noteModelList = noteModelList;
    }

    @Override
    public int getCount() {
        return noteModelList.size();
    }

    //Custom function which creates notes on a note by note basis
    //Here all the components are assigned to a note
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Note note = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_cell, parent, false);

        TextView title = convertView.findViewById(R.id.cellTitle);
        TextView desc = convertView.findViewById(R.id.cellDesc);
        LinearLayout layout = convertView.findViewById(R.id.noteCellLayout);

        title.setText(note.getTitle());
        desc.setText(note.getDescription());
        layout.setBackgroundColor(Color.parseColor(note.getNoteColor()));

        return convertView;
    }

    //Overwriting getFilter to work with note titles
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                noteModelList = (List<Note>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            //The list is checked from notes and only the titles which contain the char remain and returned
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Note> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(noteModelList);
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                }
                else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        Note data = mOriginalValues.get(i);
                        if (data.getTitle().toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}