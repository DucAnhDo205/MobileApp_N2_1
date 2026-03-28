package com.duolingo.app.persistence;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duolingo.app.models.Lesson;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LessonDao_Impl implements LessonDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Lesson> __insertionAdapterOfLesson;

  private final EntityDeletionOrUpdateAdapter<Lesson> __updateAdapterOfLesson;

  public LessonDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLesson = new EntityInsertionAdapter<Lesson>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `lesson_table` (`id`,`lessonNumber`,`title`,`language`,`isPassed`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Lesson entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getLessonNumber());
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getLanguage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLanguage());
        }
        final int _tmp = entity.isPassed() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__updateAdapterOfLesson = new EntityDeletionOrUpdateAdapter<Lesson>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `lesson_table` SET `id` = ?,`lessonNumber` = ?,`title` = ?,`language` = ?,`isPassed` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Lesson entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getLessonNumber());
        if (entity.getTitle() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTitle());
        }
        if (entity.getLanguage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLanguage());
        }
        final int _tmp = entity.isPassed() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public void insert(final Lesson lesson) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLesson.insert(lesson);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Lesson lesson) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfLesson.handle(lesson);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Lesson>> getLessonsByLanguage(final String language) {
    final String _sql = "SELECT * FROM lesson_table WHERE language = ? ORDER BY lessonNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"lesson_table"}, false, new Callable<List<Lesson>>() {
      @Override
      @Nullable
      public List<Lesson> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfIsPassed = CursorUtil.getColumnIndexOrThrow(_cursor, "isPassed");
          final List<Lesson> _result = new ArrayList<Lesson>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Lesson _item;
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            _item = new Lesson(_tmpLessonNumber,_tmpTitle,_tmpLanguage);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final boolean _tmpIsPassed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPassed);
            _tmpIsPassed = _tmp != 0;
            _item.setPassed(_tmpIsPassed);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int countLessons() {
    final String _sql = "SELECT COUNT(*) FROM lesson_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
