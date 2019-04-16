package com.example.movielibraryAu538062;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

// https://developer.android.com/training/data-storage/room/index.html
@Dao
public interface MovieModelDAO {

    @Query("SELECT * FROM MovieModel")
    MovieModel[] getAll();

    @Query("SELECT * FROM MovieModel where Title LIKE  :Title")
    MovieModel findByName(String Title);


    @Update
    void update(MovieModel Movie);

    @Insert
    void insertAll(MovieModel... Movies);

    @Insert
    void InsertOne(MovieModel Movie);

    @Delete
    void delete(MovieModel Movie);
}