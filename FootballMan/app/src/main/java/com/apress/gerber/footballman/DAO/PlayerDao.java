package com.apress.gerber.footballman.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.apress.gerber.footballman.Models.Player;

/**
 * Created by hriso on 9/2/2017.
 */
@Dao
public interface PlayerDao {

    @Insert
    void insertPlayer(Player player);

    @Delete
    void deletePlayer(Player player);

    @Update
    void updatePlayer(Player player);
}
