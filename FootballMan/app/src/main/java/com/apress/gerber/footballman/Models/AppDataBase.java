package com.apress.gerber.footballman.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.apress.gerber.footballman.DAO.GameDao;
import com.apress.gerber.footballman.DAO.LeagueDao;
import com.apress.gerber.footballman.DAO.PlayerDao;
import com.apress.gerber.footballman.DAO.TeamDao;

/**
 * Created by hriso on 9/2/2017.
 */
//@Database(version=1,entities = {Game.class,League.class,Team.class,Player.class})
//public abstract class AppDataBase  extends RoomDatabase{
//    private static AppDataBase instance;
//    public static AppDataBase getInstace(Context context){
//        if(instance==null){
//            instance = Room.databaseBuilder(context,AppDataBase.class,"football_db").build();
//        }
//        return  instance;
//    }
//    public abstract GameDao gameDao();
//    public abstract LeagueDao leagueDao();
//    public abstract PlayerDao playerDao();
//    public abstract TeamDao teamDao();
//}
