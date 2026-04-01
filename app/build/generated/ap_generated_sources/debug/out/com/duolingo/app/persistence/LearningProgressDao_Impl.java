package com.duolingo.app.persistence;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duolingo.app.models.LearningProgress;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LearningProgressDao_Impl implements LearningProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LearningProgress> __insertionAdapterOfLearningProgress;

  private final EntityDeletionOrUpdateAdapter<LearningProgress> __updateAdapterOfLearningProgress;

  public LearningProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLearningProgress = new EntityInsertionAdapter<LearningProgress>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `learning_progress` (`id`,`userId`,`languageId`,`currentStreak`,`totalPoints`,`level`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final LearningProgress entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getLanguageId());
        statement.bindLong(4, entity.getCurrentStreak());
        statement.bindLong(5, entity.getTotalPoints());
        statement.bindLong(6, entity.getLevel());
      }
    };
    this.__updateAdapterOfLearningProgress = new EntityDeletionOrUpdateAdapter<LearningProgress>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `learning_progress` SET `id` = ?,`userId` = ?,`languageId` = ?,`currentStreak` = ?,`totalPoints` = ?,`level` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final LearningProgress entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getLanguageId());
        statement.bindLong(4, entity.getCurrentStreak());
        statement.bindLong(5, entity.getTotalPoints());
        statement.bindLong(6, entity.getLevel());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public void insert(final LearningProgress progress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLearningProgress.insert(progress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final LearningProgress progress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfLearningProgress.handle(progress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LearningProgress getProgressByUserId(final int userId) {
    final String _sql = "SELECT * FROM learning_progress WHERE userId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfLanguageId = CursorUtil.getColumnIndexOrThrow(_cursor, "languageId");
      final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreak");
      final int _cursorIndexOfTotalPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPoints");
      final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
      final LearningProgress _result;
      if (_cursor.moveToFirst()) {
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpLanguageId;
        _tmpLanguageId = _cursor.getInt(_cursorIndexOfLanguageId);
        final int _tmpCurrentStreak;
        _tmpCurrentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
        final int _tmpTotalPoints;
        _tmpTotalPoints = _cursor.getInt(_cursorIndexOfTotalPoints);
        final int _tmpLevel;
        _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
        _result = new LearningProgress(_tmpUserId,_tmpLanguageId,_tmpCurrentStreak,_tmpTotalPoints,_tmpLevel);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
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
