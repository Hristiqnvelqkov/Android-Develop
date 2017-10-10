package com.apress.gerber.footballman.Models;

import java.io.Serializable;

/**
 * Created by hriso on 9/18/2017.
 */

public class GameStat implements Serializable {
    private Player key;
    private int value;
    private int type;

    public GameStat() {
    }

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

    public void setPlayer(Player player) {
        this.key = player;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
