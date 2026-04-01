package com.duolingo.app.persistence;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class VocaVerseDatabase_Impl extends VocaVerseDatabase {
  private volatile VocabularyDao _vocabularyDao;

  private volatile UserDao _userDao;

  private volatile LessonDao _lessonDao;

  private volatile ProgressDao _progressDao;

  private volatile LearningProgressDao _learningProgressDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `vocabulary_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT, `meaning` TEXT, `example` TEXT, `language` TEXT, `category` TEXT, `lessonNumber` INTEGER NOT NULL, `nextReviewDate` INTEGER NOT NULL, `interval` INTEGER NOT NULL, `easeFactor` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fullName` TEXT, `username` TEXT, `email` TEXT, `password` TEXT, `phoneNumber` TEXT, `lastLogin` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `lesson_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lessonNumber` INTEGER NOT NULL, `title` TEXT, `language` TEXT, `isPassed` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `GameHistory` (`HistoryID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `UserID` INTEGER NOT NULL, `GameType` TEXT, `Score` INTEGER NOT NULL, `PlayedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `learning_progress` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `languageId` INTEGER NOT NULL, `currentStreak` INTEGER NOT NULL, `totalPoints` INTEGER NOT NULL, `level` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a4f0f91c75265c642939454764a02da3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `vocabulary_table`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `lesson_table`");
        db.execSQL("DROP TABLE IF EXISTS `GameHistory`");
        db.execSQL("DROP TABLE IF EXISTS `learning_progress`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsVocabularyTable = new HashMap<String, TableInfo.Column>(10);
        _columnsVocabularyTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("word", new TableInfo.Column("word", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("meaning", new TableInfo.Column("meaning", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("example", new TableInfo.Column("example", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("language", new TableInfo.Column("language", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("lessonNumber", new TableInfo.Column("lessonNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("nextReviewDate", new TableInfo.Column("nextReviewDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("interval", new TableInfo.Column("interval", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVocabularyTable.put("easeFactor", new TableInfo.Column("easeFactor", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVocabularyTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVocabularyTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVocabularyTable = new TableInfo("vocabulary_table", _columnsVocabularyTable, _foreignKeysVocabularyTable, _indicesVocabularyTable);
        final TableInfo _existingVocabularyTable = TableInfo.read(db, "vocabulary_table");
        if (!_infoVocabularyTable.equals(_existingVocabularyTable)) {
          return new RoomOpenHelper.ValidationResult(false, "vocabulary_table(com.duolingo.app.models.VocabularyItem).\n"
                  + " Expected:\n" + _infoVocabularyTable + "\n"
                  + " Found:\n" + _existingVocabularyTable);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(7);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fullName", new TableInfo.Column("fullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("lastLogin", new TableInfo.Column("lastLogin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.duolingo.app.models.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsLessonTable = new HashMap<String, TableInfo.Column>(5);
        _columnsLessonTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLessonTable.put("lessonNumber", new TableInfo.Column("lessonNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLessonTable.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLessonTable.put("language", new TableInfo.Column("language", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLessonTable.put("isPassed", new TableInfo.Column("isPassed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLessonTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLessonTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLessonTable = new TableInfo("lesson_table", _columnsLessonTable, _foreignKeysLessonTable, _indicesLessonTable);
        final TableInfo _existingLessonTable = TableInfo.read(db, "lesson_table");
        if (!_infoLessonTable.equals(_existingLessonTable)) {
          return new RoomOpenHelper.ValidationResult(false, "lesson_table(com.duolingo.app.models.Lesson).\n"
                  + " Expected:\n" + _infoLessonTable + "\n"
                  + " Found:\n" + _existingLessonTable);
        }
        final HashMap<String, TableInfo.Column> _columnsGameHistory = new HashMap<String, TableInfo.Column>(5);
        _columnsGameHistory.put("HistoryID", new TableInfo.Column("HistoryID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameHistory.put("UserID", new TableInfo.Column("UserID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameHistory.put("GameType", new TableInfo.Column("GameType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameHistory.put("Score", new TableInfo.Column("Score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameHistory.put("PlayedAt", new TableInfo.Column("PlayedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGameHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGameHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGameHistory = new TableInfo("GameHistory", _columnsGameHistory, _foreignKeysGameHistory, _indicesGameHistory);
        final TableInfo _existingGameHistory = TableInfo.read(db, "GameHistory");
        if (!_infoGameHistory.equals(_existingGameHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "GameHistory(com.duolingo.app.models.GameHistory).\n"
                  + " Expected:\n" + _infoGameHistory + "\n"
                  + " Found:\n" + _existingGameHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsLearningProgress = new HashMap<String, TableInfo.Column>(6);
        _columnsLearningProgress.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLearningProgress.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLearningProgress.put("languageId", new TableInfo.Column("languageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLearningProgress.put("currentStreak", new TableInfo.Column("currentStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLearningProgress.put("totalPoints", new TableInfo.Column("totalPoints", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLearningProgress.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLearningProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLearningProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLearningProgress = new TableInfo("learning_progress", _columnsLearningProgress, _foreignKeysLearningProgress, _indicesLearningProgress);
        final TableInfo _existingLearningProgress = TableInfo.read(db, "learning_progress");
        if (!_infoLearningProgress.equals(_existingLearningProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "learning_progress(com.duolingo.app.models.LearningProgress).\n"
                  + " Expected:\n" + _infoLearningProgress + "\n"
                  + " Found:\n" + _existingLearningProgress);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a4f0f91c75265c642939454764a02da3", "ad27afd82f58d1acff110a86a4447702");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "vocabulary_table","users","lesson_table","GameHistory","learning_progress");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `vocabulary_table`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `lesson_table`");
      _db.execSQL("DELETE FROM `GameHistory`");
      _db.execSQL("DELETE FROM `learning_progress`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(VocabularyDao.class, VocabularyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LessonDao.class, LessonDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgressDao.class, ProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LearningProgressDao.class, LearningProgressDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public VocabularyDao vocabularyDao() {
    if (_vocabularyDao != null) {
      return _vocabularyDao;
    } else {
      synchronized(this) {
        if(_vocabularyDao == null) {
          _vocabularyDao = new VocabularyDao_Impl(this);
        }
        return _vocabularyDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public LessonDao lessonDao() {
    if (_lessonDao != null) {
      return _lessonDao;
    } else {
      synchronized(this) {
        if(_lessonDao == null) {
          _lessonDao = new LessonDao_Impl(this);
        }
        return _lessonDao;
      }
    }
  }

  @Override
  public ProgressDao progressDao() {
    if (_progressDao != null) {
      return _progressDao;
    } else {
      synchronized(this) {
        if(_progressDao == null) {
          _progressDao = new ProgressDao_Impl(this);
        }
        return _progressDao;
      }
    }
  }

  @Override
  public LearningProgressDao learningProgressDao() {
    if (_learningProgressDao != null) {
      return _learningProgressDao;
    } else {
      synchronized(this) {
        if(_learningProgressDao == null) {
          _learningProgressDao = new LearningProgressDao_Impl(this);
        }
        return _learningProgressDao;
      }
    }
  }
}
