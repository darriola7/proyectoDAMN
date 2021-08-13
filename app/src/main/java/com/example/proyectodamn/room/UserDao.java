package com.example.proyectodamn.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * FROM user_table")
    LiveData<UserTable> getAlphabetizedUsers();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserTable user);

    @Query("DELETE FROM user_table")
    void deleteAll();

}

