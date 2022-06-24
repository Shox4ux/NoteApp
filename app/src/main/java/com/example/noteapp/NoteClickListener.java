package com.example.noteapp;

import androidx.cardview.widget.CardView;

import com.example.noteapp.Model.Note;

public interface NoteClickListener {
    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);
}
