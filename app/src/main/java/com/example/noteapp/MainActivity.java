package com.example.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.noteapp.Adapter.Adapter;
import com.example.noteapp.DataBase.RoomDB;
import com.example.noteapp.Model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView noteRecycler;
    List<Note> notes = new ArrayList<>();
    RoomDB DB;
    FloatingActionButton fabAdd;
    Adapter adapter;
    SearchView searchView;
    Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_bar);

        noteRecycler = findViewById(R.id.recycler_note);
        fabAdd = findViewById(R.id.fab_add);
        DB = RoomDB.getInstance(this);

        notes = DB.mainDAO().getAllNotes();

        updateRecycler(notes);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,NoteClickActivity.class),101);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        searchView.setDrawingCacheBackgroundColor(R.drawable.search_bg);


    }

    private void filter(String newText) {
        List<Note> filterList = new ArrayList<>();
        for (Note singleNote:notes){
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(singleNote);
                adapter.filterList(filterList);
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101)
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                Note newNote = (Note) data.getSerializableExtra("embedNote");
                DB.mainDAO().insert(newNote);
                notes.clear();
                notes.addAll(DB.mainDAO().getAllNotes());
                adapter.notifyDataSetChanged();
            }
    }

    private void updateRecycler(List<Note> notes) {
        noteRecycler.setHasFixedSize(true);
        noteRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        adapter = new Adapter(MainActivity.this,notes,listener);
        noteRecycler.setAdapter(adapter);
    }



    private final NoteClickListener listener = new NoteClickListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this,NoteClickActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent,102);

        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            selectedNote = new Note();
            selectedNote = note;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.pin:
                if (selectedNote.isPinned()) {
                    DB.mainDAO().pin(selectedNote.getID(), false);
                    Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
                } else {
                    DB.mainDAO().pin(selectedNote.getID(), true);
                    Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(DB.mainDAO().getAllNotes());
                adapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                DB.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;

        }
    }
}