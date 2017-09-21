package com.apress.gerber.footballman.Models;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by hriso on 9/18/2017.
 */

public class GameStat extends RealmObject implements Serializable{
    private Player key;
    private int value;
    private int type;
    public GameStat(){}
    public GameStat(Player player, int value, int type) {
        this.key = player;
        this.value = value;
        this.type = type;
    }

    public void put(Player player, int value, int type) {
        this.key = player;
        this.value = value;
    }

    public Player getPlayer() {
        return key;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void updateValue() {
        value++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameStat stat = (GameStat) o;

        if (type != stat.type) return false;
        return key.equals(stat.key);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + type;
        return result;
    }
}
