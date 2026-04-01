package com.duolingo.app.persistence;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duolingo.app.models.GameHistory;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ProgressDao_Impl implements ProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GameHistory> __insertionAdapterOfGameHistory;

  public ProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGameHistory = new EntityInsertionAdapter<GameHistory>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `GameHistory` (`HistoryID`,`UserID`,`GameType`,`Score`,`PlayedAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final GameHistory entity) {
        statement.bindLong(1, entity.HistoryID);
        statement.bindLong(2, entity.UserID);
        if (entity.GameType == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.GameType);
        }
        statement.bindLong(4, entity.Score);
        statement.bindLong(5, entity.PlayedAt);
      }
    };
  }

  @Override
  public void insertGameHistory(final GameHistory history) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGameHistory.insert(history);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int getTotalStars(final int userId) {
    final String _sql = "SELECT SUM(Score) FROM GameHistory WHERE UserID = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<GameHistory> getAllHistory(final int userId) {
    final String _sql = "SELECT * FROM GameHistory WHERE UserID = ? ORDER BY PlayedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfHistoryID = CursorUtil.getColumnIndexOrThrow(_cursor, "HistoryID");
      final int _cursorIndexOfUserID = CursorUtil.getColumnIndexOrThrow(_cursor, "UserID");
      final int _cursorIndexOfGameType = CursorUtil.getColumnIndexOrThrow(_cursor, "GameType");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "Score");
      final int _cursorIndexOfPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "PlayedAt");
      final List<GameHistory> _result = new ArrayList<GameHistory>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final GameHistory _item;
        _item = new GameHistory();
        _item.HistoryID = _cursor.getInt(_cursorIndexOfHistoryID);
        _item.UserID = _cursor.getInt(_cursorIndexOfUserID);
        if (_cursor.isNull(_cursorIndexOfGameType)) {
          _item.GameType = null;
        } else {
          _item.GameType = _cursor.getString(_cursorIndexOfGameType);
        }
        _item.Score = _cursor.getInt(_cursorIndexOfScore);
        _item.PlayedAt = _cursor.getLong(_cursorIndexOfPlayedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
