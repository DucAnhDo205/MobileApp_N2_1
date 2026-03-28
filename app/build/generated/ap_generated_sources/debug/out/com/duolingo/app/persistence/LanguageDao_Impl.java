package com.duolingo.app.persistence;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.duolingo.app.models.Language;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LanguageDao_Impl implements LanguageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Language> __insertionAdapterOfLanguage;

  public LanguageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLanguage = new EntityInsertionAdapter<Language>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `languages` (`id`,`name`,`flagResourceId`,`animalResourceId`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Language entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getFlagResourceId());
        statement.bindLong(4, entity.getAnimalResourceId());
      }
    };
  }

  @Override
  public void insert(final Language language) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLanguage.insert(language);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Language> getAllLanguages() {
    final String _sql = "SELECT * FROM languages";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfFlagResourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "flagResourceId");
      final int _cursorIndexOfAnimalResourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "animalResourceId");
      final List<Language> _result = new ArrayList<Language>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Language _item;
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpFlagResourceId;
        _tmpFlagResourceId = _cursor.getInt(_cursorIndexOfFlagResourceId);
        final int _tmpAnimalResourceId;
        _tmpAnimalResourceId = _cursor.getInt(_cursorIndexOfAnimalResourceId);
        _item = new Language(_tmpName,_tmpFlagResourceId,_tmpAnimalResourceId);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Language getLanguageByName(final String name) {
    final String _sql = "SELECT * FROM languages WHERE name = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfFlagResourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "flagResourceId");
      final int _cursorIndexOfAnimalResourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "animalResourceId");
      final Language _result;
      if (_cursor.moveToFirst()) {
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final int _tmpFlagResourceId;
        _tmpFlagResourceId = _cursor.getInt(_cursorIndexOfFlagResourceId);
        final int _tmpAnimalResourceId;
        _tmpAnimalResourceId = _cursor.getInt(_cursorIndexOfAnimalResourceId);
        _result = new Language(_tmpName,_tmpFlagResourceId,_tmpAnimalResourceId);
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
