package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "GameHistory")
public class GameHistory {
    @PrimaryKey(autoGenerate = true)
    public int HistoryID;
    public int UserID;
    public String GameType;
    public int Score;
    public long PlayedAt;

    public GameHistory() {
    }

    @Ignore
    public GameHistory(int UserID, String GameType, int Score, long PlayedAt) {
        this.UserID = UserID;
        this.GameType = GameType;
        this.Score = Score;
        this.PlayedAt = PlayedAt;
    }
}