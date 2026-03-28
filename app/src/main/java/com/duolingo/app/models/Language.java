package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "languages")
public class Language {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private int flagResourceId;
    private int animalResourceId;

    public Language(String name, int flagResourceId, int animalResourceId) {
        this.name = name;
        this.flagResourceId = flagResourceId;
        this.animalResourceId = animalResourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlagResourceId() {
        return flagResourceId;
    }

    public void setFlagResourceId(int flagResourceId) {
        this.flagResourceId = flagResourceId;
    }

    public int getAnimalResourceId() {
        return animalResourceId;
    }

    public void setAnimalResourceId(int animalResourceId) {
        this.animalResourceId = animalResourceId;
    }
}
