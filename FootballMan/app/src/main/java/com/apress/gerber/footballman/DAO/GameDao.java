package com.apress.gerber.footballman.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.apress.gerber.footballman.Models.Game;

import java.util.List;

/**
 * Created by hriso on 9/2/2017.
 */
@Dao
public interface GameDao {
    @Query("Select * from Game")
    List<Game> getAll();


    @Update
    void update(Game game);

    @Delete
    void delete(Game game);
}
