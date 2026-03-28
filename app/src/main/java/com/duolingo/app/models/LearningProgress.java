package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "learning_progress")
public class LearningProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int userId;
    private int languageId;
    private int currentStreak;
    private int totalPoints;
    private int level;

    public LearningProgress(int userId, int languageId, int currentStreak, int totalPoints, int level) {
        this.userId = userId;
        this.languageId = languageId;
        this.currentStreak = currentStreak;
        this.totalPoints = totalPoints;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
