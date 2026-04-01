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
import com.duolingo.app.models.VocabularyItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class VocabularyDao_Impl implements VocabularyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VocabularyItem> __insertionAdapterOfVocabularyItem;

  private final EntityDeletionOrUpdateAdapter<VocabularyItem> __updateAdapterOfVocabularyItem;

  public VocabularyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVocabularyItem = new EntityInsertionAdapter<VocabularyItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vocabulary_table` (`id`,`word`,`meaning`,`example`,`language`,`category`,`lessonNumber`,`nextReviewDate`,`interval`,`easeFactor`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final VocabularyItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getWord());
        }
        if (entity.getMeaning() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMeaning());
        }
        if (entity.getExample() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getExample());
        }
        if (entity.getLanguage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLanguage());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCategory());
        }
        statement.bindLong(7, entity.getLessonNumber());
        statement.bindLong(8, entity.getNextReviewDate());
        statement.bindLong(9, entity.getInterval());
        statement.bindDouble(10, entity.getEaseFactor());
      }
    };
    this.__updateAdapterOfVocabularyItem = new EntityDeletionOrUpdateAdapter<VocabularyItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vocabulary_table` SET `id` = ?,`word` = ?,`meaning` = ?,`example` = ?,`language` = ?,`category` = ?,`lessonNumber` = ?,`nextReviewDate` = ?,`interval` = ?,`easeFactor` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final VocabularyItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getWord() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getWord());
        }
        if (entity.getMeaning() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMeaning());
        }
        if (entity.getExample() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getExample());
        }
        if (entity.getLanguage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLanguage());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCategory());
        }
        statement.bindLong(7, entity.getLessonNumber());
        statement.bindLong(8, entity.getNextReviewDate());
        statement.bindLong(9, entity.getInterval());
        statement.bindDouble(10, entity.getEaseFactor());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public void insert(final VocabularyItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVocabularyItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<VocabularyItem> items) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfVocabularyItem.insert(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final VocabularyItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfVocabularyItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<VocabularyItem>> getVocabularyByLanguage(final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE language = ? ORDER BY nextReviewDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<VocabularyItem>>() {
      @Override
      @Nullable
      public List<VocabularyItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyItem _item;
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final String _tmpMeaning;
            if (_cursor.isNull(_cursorIndexOfMeaning)) {
              _tmpMeaning = null;
            } else {
              _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            }
            final String _tmpExample;
            if (_cursor.isNull(_cursorIndexOfExample)) {
              _tmpExample = null;
            } else {
              _tmpExample = _cursor.getString(_cursorIndexOfExample);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpNextReviewDate;
            _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
            _item.setNextReviewDate(_tmpNextReviewDate);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            _item.setInterval(_tmpInterval);
            final double _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
            _item.setEaseFactor(_tmpEaseFactor);
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
  public LiveData<List<String>> getCategories(final String language) {
    final String _sql = "SELECT DISTINCT category FROM vocabulary_table WHERE language = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<String>>() {
      @Override
      @Nullable
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
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
  public LiveData<List<Integer>> getLessonNumbers(final String language) {
    final String _sql = "SELECT DISTINCT lessonNumber FROM vocabulary_table WHERE language = ? ORDER BY lessonNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<Integer>>() {
      @Override
      @Nullable
      public List<Integer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Integer _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getInt(0);
            }
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
  public LiveData<List<VocabularyItem>> getVocabularyByCategory(final String category,
      final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE category = ? AND language = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 2;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<VocabularyItem>>() {
      @Override
      @Nullable
      public List<VocabularyItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyItem _item;
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final String _tmpMeaning;
            if (_cursor.isNull(_cursorIndexOfMeaning)) {
              _tmpMeaning = null;
            } else {
              _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            }
            final String _tmpExample;
            if (_cursor.isNull(_cursorIndexOfExample)) {
              _tmpExample = null;
            } else {
              _tmpExample = _cursor.getString(_cursorIndexOfExample);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpNextReviewDate;
            _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
            _item.setNextReviewDate(_tmpNextReviewDate);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            _item.setInterval(_tmpInterval);
            final double _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
            _item.setEaseFactor(_tmpEaseFactor);
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
  public List<VocabularyItem> getVocabularyByCategorySync(final String category,
      final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE category = ? AND language = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 2;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
      final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
      final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
      final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
      final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
      final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
      final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final VocabularyItem _item;
        final String _tmpWord;
        if (_cursor.isNull(_cursorIndexOfWord)) {
          _tmpWord = null;
        } else {
          _tmpWord = _cursor.getString(_cursorIndexOfWord);
        }
        final String _tmpMeaning;
        if (_cursor.isNull(_cursorIndexOfMeaning)) {
          _tmpMeaning = null;
        } else {
          _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
        }
        final String _tmpExample;
        if (_cursor.isNull(_cursorIndexOfExample)) {
          _tmpExample = null;
        } else {
          _tmpExample = _cursor.getString(_cursorIndexOfExample);
        }
        final String _tmpLanguage;
        if (_cursor.isNull(_cursorIndexOfLanguage)) {
          _tmpLanguage = null;
        } else {
          _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
        }
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        final int _tmpLessonNumber;
        _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
        _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpNextReviewDate;
        _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
        _item.setNextReviewDate(_tmpNextReviewDate);
        final int _tmpInterval;
        _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
        _item.setInterval(_tmpInterval);
        final double _tmpEaseFactor;
        _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
        _item.setEaseFactor(_tmpEaseFactor);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<VocabularyItem> getVocabularyByLessonSync(final int lessonNumber,
      final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE lessonNumber = ? AND language = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lessonNumber);
    _argIndex = 2;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
      final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
      final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
      final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
      final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
      final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
      final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final VocabularyItem _item;
        final String _tmpWord;
        if (_cursor.isNull(_cursorIndexOfWord)) {
          _tmpWord = null;
        } else {
          _tmpWord = _cursor.getString(_cursorIndexOfWord);
        }
        final String _tmpMeaning;
        if (_cursor.isNull(_cursorIndexOfMeaning)) {
          _tmpMeaning = null;
        } else {
          _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
        }
        final String _tmpExample;
        if (_cursor.isNull(_cursorIndexOfExample)) {
          _tmpExample = null;
        } else {
          _tmpExample = _cursor.getString(_cursorIndexOfExample);
        }
        final String _tmpLanguage;
        if (_cursor.isNull(_cursorIndexOfLanguage)) {
          _tmpLanguage = null;
        } else {
          _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
        }
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        final int _tmpLessonNumber;
        _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
        _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpNextReviewDate;
        _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
        _item.setNextReviewDate(_tmpNextReviewDate);
        final int _tmpInterval;
        _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
        _item.setInterval(_tmpInterval);
        final double _tmpEaseFactor;
        _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
        _item.setEaseFactor(_tmpEaseFactor);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<VocabularyItem>> getVocabularyByLesson(final int lessonNumber,
      final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE lessonNumber = ? AND language = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lessonNumber);
    _argIndex = 2;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<VocabularyItem>>() {
      @Override
      @Nullable
      public List<VocabularyItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyItem _item;
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final String _tmpMeaning;
            if (_cursor.isNull(_cursorIndexOfMeaning)) {
              _tmpMeaning = null;
            } else {
              _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            }
            final String _tmpExample;
            if (_cursor.isNull(_cursorIndexOfExample)) {
              _tmpExample = null;
            } else {
              _tmpExample = _cursor.getString(_cursorIndexOfExample);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpNextReviewDate;
            _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
            _item.setNextReviewDate(_tmpNextReviewDate);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            _item.setInterval(_tmpInterval);
            final double _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
            _item.setEaseFactor(_tmpEaseFactor);
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
  public LiveData<List<VocabularyItem>> getVocabularyByLessonAndCategory(final int lessonNumber,
      final String category, final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE lessonNumber = ? AND category = ? AND language = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lessonNumber);
    _argIndex = 2;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 3;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<VocabularyItem>>() {
      @Override
      @Nullable
      public List<VocabularyItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyItem _item;
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final String _tmpMeaning;
            if (_cursor.isNull(_cursorIndexOfMeaning)) {
              _tmpMeaning = null;
            } else {
              _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            }
            final String _tmpExample;
            if (_cursor.isNull(_cursorIndexOfExample)) {
              _tmpExample = null;
            } else {
              _tmpExample = _cursor.getString(_cursorIndexOfExample);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpNextReviewDate;
            _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
            _item.setNextReviewDate(_tmpNextReviewDate);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            _item.setInterval(_tmpInterval);
            final double _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
            _item.setEaseFactor(_tmpEaseFactor);
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
  public LiveData<List<VocabularyItem>> getRandomVocabularyForLesson(final String language,
      final int lessonNumber, final int limit) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE language = ? AND lessonNumber = ? ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, lessonNumber);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<VocabularyItem>>() {
      @Override
      @Nullable
      public List<VocabularyItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyItem _item;
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final String _tmpMeaning;
            if (_cursor.isNull(_cursorIndexOfMeaning)) {
              _tmpMeaning = null;
            } else {
              _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            }
            final String _tmpExample;
            if (_cursor.isNull(_cursorIndexOfExample)) {
              _tmpExample = null;
            } else {
              _tmpExample = _cursor.getString(_cursorIndexOfExample);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpNextReviewDate;
            _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
            _item.setNextReviewDate(_tmpNextReviewDate);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            _item.setInterval(_tmpInterval);
            final double _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
            _item.setEaseFactor(_tmpEaseFactor);
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
  public List<VocabularyItem> getRandomVocabularyForQuiz(final String language) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE language = ? ORDER BY RANDOM() LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (language == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, language);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
      final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
      final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
      final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
      final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
      final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
      final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final VocabularyItem _item;
        final String _tmpWord;
        if (_cursor.isNull(_cursorIndexOfWord)) {
          _tmpWord = null;
        } else {
          _tmpWord = _cursor.getString(_cursorIndexOfWord);
        }
        final String _tmpMeaning;
        if (_cursor.isNull(_cursorIndexOfMeaning)) {
          _tmpMeaning = null;
        } else {
          _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
        }
        final String _tmpExample;
        if (_cursor.isNull(_cursorIndexOfExample)) {
          _tmpExample = null;
        } else {
          _tmpExample = _cursor.getString(_cursorIndexOfExample);
        }
        final String _tmpLanguage;
        if (_cursor.isNull(_cursorIndexOfLanguage)) {
          _tmpLanguage = null;
        } else {
          _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
        }
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        final int _tmpLessonNumber;
        _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
        _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpNextReviewDate;
        _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
        _item.setNextReviewDate(_tmpNextReviewDate);
        final int _tmpInterval;
        _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
        _item.setInterval(_tmpInterval);
        final double _tmpEaseFactor;
        _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
        _item.setEaseFactor(_tmpEaseFactor);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<VocabularyItem>> getDueVocabulary(final long currentDate) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE nextReviewDate <= ? ORDER BY nextReviewDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentDate);
    return __db.getInvalidationTracker().createLiveData(new String[] {"vocabulary_table"}, false, new Callable<List<VocabularyItem>>() {
      @Override
      @Nullable
      public List<VocabularyItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
          final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyItem _item;
            final String _tmpWord;
            if (_cursor.isNull(_cursorIndexOfWord)) {
              _tmpWord = null;
            } else {
              _tmpWord = _cursor.getString(_cursorIndexOfWord);
            }
            final String _tmpMeaning;
            if (_cursor.isNull(_cursorIndexOfMeaning)) {
              _tmpMeaning = null;
            } else {
              _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            }
            final String _tmpExample;
            if (_cursor.isNull(_cursorIndexOfExample)) {
              _tmpExample = null;
            } else {
              _tmpExample = _cursor.getString(_cursorIndexOfExample);
            }
            final String _tmpLanguage;
            if (_cursor.isNull(_cursorIndexOfLanguage)) {
              _tmpLanguage = null;
            } else {
              _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final int _tmpLessonNumber;
            _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
            _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpNextReviewDate;
            _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
            _item.setNextReviewDate(_tmpNextReviewDate);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            _item.setInterval(_tmpInterval);
            final double _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
            _item.setEaseFactor(_tmpEaseFactor);
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
  public List<VocabularyItem> getRandomVocabulary(final int limit) {
    final String _sql = "SELECT * FROM vocabulary_table ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
      final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
      final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
      final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
      final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
      final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
      final List<VocabularyItem> _result = new ArrayList<VocabularyItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final VocabularyItem _item;
        final String _tmpWord;
        if (_cursor.isNull(_cursorIndexOfWord)) {
          _tmpWord = null;
        } else {
          _tmpWord = _cursor.getString(_cursorIndexOfWord);
        }
        final String _tmpMeaning;
        if (_cursor.isNull(_cursorIndexOfMeaning)) {
          _tmpMeaning = null;
        } else {
          _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
        }
        final String _tmpExample;
        if (_cursor.isNull(_cursorIndexOfExample)) {
          _tmpExample = null;
        } else {
          _tmpExample = _cursor.getString(_cursorIndexOfExample);
        }
        final String _tmpLanguage;
        if (_cursor.isNull(_cursorIndexOfLanguage)) {
          _tmpLanguage = null;
        } else {
          _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
        }
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        final int _tmpLessonNumber;
        _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
        _item = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpNextReviewDate;
        _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
        _item.setNextReviewDate(_tmpNextReviewDate);
        final int _tmpInterval;
        _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
        _item.setInterval(_tmpInterval);
        final double _tmpEaseFactor;
        _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
        _item.setEaseFactor(_tmpEaseFactor);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public VocabularyItem getRandomVocabFromLesson(final int lessonNum) {
    final String _sql = "SELECT * FROM vocabulary_table WHERE lessonNumber = ? ORDER BY RANDOM() LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lessonNum);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
      final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
      final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
      final int _cursorIndexOfLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "language");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfLessonNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lessonNumber");
      final int _cursorIndexOfNextReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextReviewDate");
      final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
      final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
      final VocabularyItem _result;
      if (_cursor.moveToFirst()) {
        final String _tmpWord;
        if (_cursor.isNull(_cursorIndexOfWord)) {
          _tmpWord = null;
        } else {
          _tmpWord = _cursor.getString(_cursorIndexOfWord);
        }
        final String _tmpMeaning;
        if (_cursor.isNull(_cursorIndexOfMeaning)) {
          _tmpMeaning = null;
        } else {
          _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
        }
        final String _tmpExample;
        if (_cursor.isNull(_cursorIndexOfExample)) {
          _tmpExample = null;
        } else {
          _tmpExample = _cursor.getString(_cursorIndexOfExample);
        }
        final String _tmpLanguage;
        if (_cursor.isNull(_cursorIndexOfLanguage)) {
          _tmpLanguage = null;
        } else {
          _tmpLanguage = _cursor.getString(_cursorIndexOfLanguage);
        }
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        final int _tmpLessonNumber;
        _tmpLessonNumber = _cursor.getInt(_cursorIndexOfLessonNumber);
        _result = new VocabularyItem(_tmpWord,_tmpMeaning,_tmpExample,_tmpLanguage,_tmpCategory,_tmpLessonNumber);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final long _tmpNextReviewDate;
        _tmpNextReviewDate = _cursor.getLong(_cursorIndexOfNextReviewDate);
        _result.setNextReviewDate(_tmpNextReviewDate);
        final int _tmpInterval;
        _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
        _result.setInterval(_tmpInterval);
        final double _tmpEaseFactor;
        _tmpEaseFactor = _cursor.getDouble(_cursorIndexOfEaseFactor);
        _result.setEaseFactor(_tmpEaseFactor);
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
