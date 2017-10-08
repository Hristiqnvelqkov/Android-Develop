package com.apress.gerber.footballman.Models;

import java.io.Serializable;

/**
 * Created by hriso on 9/12/2017.
 */

public class Event implements Serializable{
    private  int time ;
    private boolean host;
    Player player;
    private int type;
    public Event(int time, int type,Player player){
        this.time=time;
        this.player = player;
        this.type=type;
    }
    public Event(){}
    public void setHost(boolean host){
        this.host = host;
    }
    public boolean getHost(){
        return host;
    }
    public int getType(){
        return type;
    }
    public int getTime(){
       return time/60;
    }
    public Player getPlayer(){
        return this.player;
    }
}
