package com.apress.gerber.footballman.Models;


import android.os.Parcel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class League extends RealmObject  {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private RealmList<Team> mTeamList = new RealmList<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }


}
