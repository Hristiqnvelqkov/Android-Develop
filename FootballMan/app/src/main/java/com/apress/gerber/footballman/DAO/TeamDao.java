package com.apress.gerber.footballman.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.apress.gerber.footballman.Models.Team;

/**
 * Created by hriso on 9/2/2017.
 */
@Dao
public interface TeamDao {
    @Insert
    void insertTeam(Team team);

    @Delete
    void deleteTeam(Team team);

    @Update
    void updateTeam(Team team);
}
