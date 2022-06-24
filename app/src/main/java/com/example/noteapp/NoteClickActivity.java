package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.noteapp.Model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteClickActivity extends AppCompatActivity {
    EditText editTitle,editNote;
    ImageView saveImg;
    Note notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_click);
        editNote = findViewById(R.id.edit_note);
        editTitle = findViewById(R.id.edit_title);
        saveImg = findViewById(R.id.img_save);

try {
    notes = new Note();
    notes = (Note) getIntent().getSerializableExtra("old_note");
    editTitle.setText(notes.getTitle());
    editNote.setText(notes.getNotes());
    isOldNote = true;
}catch (Exception e){
    e.printStackTrace();
}

        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString();
                String note = editNote.getText().toString();

                if (note.isEmpty()){
                    Toast.makeText(NoteClickActivity.this, "Please add some notes", Toast.LENGTH_SHORT).show();
                    return;
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE,d MMM yyyy HH:mm a");
                Date date = new Date();

                if (!isOldNote){
                    notes = new Note();
                    notes.setTitle(title);
                    notes.setNotes(note);
                    notes.setData(formatter.format(date));


                    Intent intent = new Intent();
                    intent.putExtra("embedNote" ,notes);
                    setResult(Activity.RESULT_OK,intent);
                }

                finish();
            }
        });
    }
}