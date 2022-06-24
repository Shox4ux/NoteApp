package com.example.noteapp.DataBase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.noteapp.Model.Note;

import java.util.List;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();

    @Query("UPDATE notes SET title = :title,note =:note WHERE ID =:id")
    void update(int id,String title,String note);
    @Query("UPDATE notes SET pinned =:pin WHERE ID =:id")
    void pin(int id,boolean pin);
}
