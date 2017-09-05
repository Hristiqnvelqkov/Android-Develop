package com.apress.gerber.footballman.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.apress.gerber.footballman.Models.League;

/**
 * Created by hriso on 9/2/2017.
 */
@Dao
public interface LeagueDao {
    @Insert
    void insertLeague(League league);

    @Delete
    void deleteLeague(League league);

    @Update
    void updateLeague(League league);

}
