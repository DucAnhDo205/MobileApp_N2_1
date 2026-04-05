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

  private volatile LanguageDao _languageDao;

  private volatile LearningProgressDao _learningProgressDao;

  private volatile LessonDao _lessonDao;

  private volatile GrammarDao _grammarDao;

  private volatile ListeningDao _listeningDao;

  private volatile PronunciationDao _pronunciationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `vocabulary_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT, `meaning` TEXT, `example` TEXT, `language` TEXT, `category` TEXT, `lessonNumber` INTEGER NOT NULL, `nextReviewDate` INTEGER NOT NULL, `interval` INTEGER NOT NULL, `easeFactor` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fullName` TEXT, `username` TEXT, `email` TEXT, `password` TEXT, `phoneNumber` TEXT, `lastLogin` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `languages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `flagResourceId` INTEGER NOT NULL, `animalResourceId` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `learning_progress` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `languageId` INTEGER NOT NULL, `currentStreak` INTEGER NOT NULL, `totalPoints` INTEGER NOT NULL, `level` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `lesson_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lessonNumber` INTEGER NOT NULL, `title` TEXT, `language` TEXT, `isPassed` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `grammar_questions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category` TEXT, `theoryTitle` TEXT, `theoryContent` TEXT, `theoryStructure` TEXT, `theoryHint` TEXT, `questionType` TEXT, `questionText` TEXT, `optionA` TEXT, `optionB` TEXT, `optionC` TEXT, `optionD` TEXT, `correctAnswer` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `listening_questions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `level` TEXT, `audioFile` TEXT, `transcript` TEXT, `q1Text` TEXT, `q1A` TEXT, `q1B` TEXT, `q1C` TEXT, `q1D` TEXT, `q1Correct` TEXT, `q2Text` TEXT, `q2A` TEXT, `q2B` TEXT, `q2C` TEXT, `q2D` TEXT, `q2Correct` TEXT, `q3Text` TEXT, `q3A` TEXT, `q3B` TEXT, `q3C` TEXT, `q3D` TEXT, `q3Correct` TEXT, `q4Text` TEXT, `q4A` TEXT, `q4B` TEXT, `q4C` TEXT, `q4D` TEXT, `q4Correct` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pronunciation_questions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lesson_id` TEXT, `transcript` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '762a167ccfbd0a591e1c274a0b120d9c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `vocabulary_table`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `languages`");
        db.execSQL("DROP TABLE IF EXISTS `learning_progress`");
        db.execSQL("DROP TABLE IF EXISTS `lesson_table`");
        db.execSQL("DROP TABLE IF EXISTS `grammar_questions`");
        db.execSQL("DROP TABLE IF EXISTS `listening_questions`");
        db.execSQL("DROP TABLE IF EXISTS `pronunciation_questions`");
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
        final HashMap<String, TableInfo.Column> _columnsLanguages = new HashMap<String, TableInfo.Column>(4);
        _columnsLanguages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLanguages.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLanguages.put("flagResourceId", new TableInfo.Column("flagResourceId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLanguages.put("animalResourceId", new TableInfo.Column("animalResourceId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLanguages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLanguages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLanguages = new TableInfo("languages", _columnsLanguages, _foreignKeysLanguages, _indicesLanguages);
        final TableInfo _existingLanguages = TableInfo.read(db, "languages");
        if (!_infoLanguages.equals(_existingLanguages)) {
          return new RoomOpenHelper.ValidationResult(false, "languages(com.duolingo.app.models.Language).\n"
                  + " Expected:\n" + _infoLanguages + "\n"
                  + " Found:\n" + _existingLanguages);
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
        final HashMap<String, TableInfo.Column> _columnsGrammarQuestions = new HashMap<String, TableInfo.Column>(13);
        _columnsGrammarQuestions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("theoryTitle", new TableInfo.Column("theoryTitle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("theoryContent", new TableInfo.Column("theoryContent", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("theoryStructure", new TableInfo.Column("theoryStructure", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("theoryHint", new TableInfo.Column("theoryHint", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("questionType", new TableInfo.Column("questionType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("questionText", new TableInfo.Column("questionText", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("optionA", new TableInfo.Column("optionA", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("optionB", new TableInfo.Column("optionB", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("optionC", new TableInfo.Column("optionC", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("optionD", new TableInfo.Column("optionD", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrammarQuestions.put("correctAnswer", new TableInfo.Column("correctAnswer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGrammarQuestions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGrammarQuestions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGrammarQuestions = new TableInfo("grammar_questions", _columnsGrammarQuestions, _foreignKeysGrammarQuestions, _indicesGrammarQuestions);
        final TableInfo _existingGrammarQuestions = TableInfo.read(db, "grammar_questions");
        if (!_infoGrammarQuestions.equals(_existingGrammarQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "grammar_questions(com.duolingo.app.models.GrammarQuestion).\n"
                  + " Expected:\n" + _infoGrammarQuestions + "\n"
                  + " Found:\n" + _existingGrammarQuestions);
        }
        final HashMap<String, TableInfo.Column> _columnsListeningQuestions = new HashMap<String, TableInfo.Column>(28);
        _columnsListeningQuestions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("level", new TableInfo.Column("level", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("audioFile", new TableInfo.Column("audioFile", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("transcript", new TableInfo.Column("transcript", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q1Text", new TableInfo.Column("q1Text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q1A", new TableInfo.Column("q1A", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q1B", new TableInfo.Column("q1B", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q1C", new TableInfo.Column("q1C", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q1D", new TableInfo.Column("q1D", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q1Correct", new TableInfo.Column("q1Correct", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q2Text", new TableInfo.Column("q2Text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q2A", new TableInfo.Column("q2A", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q2B", new TableInfo.Column("q2B", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q2C", new TableInfo.Column("q2C", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q2D", new TableInfo.Column("q2D", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q2Correct", new TableInfo.Column("q2Correct", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q3Text", new TableInfo.Column("q3Text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q3A", new TableInfo.Column("q3A", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q3B", new TableInfo.Column("q3B", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q3C", new TableInfo.Column("q3C", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q3D", new TableInfo.Column("q3D", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q3Correct", new TableInfo.Column("q3Correct", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q4Text", new TableInfo.Column("q4Text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q4A", new TableInfo.Column("q4A", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q4B", new TableInfo.Column("q4B", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q4C", new TableInfo.Column("q4C", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q4D", new TableInfo.Column("q4D", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsListeningQuestions.put("q4Correct", new TableInfo.Column("q4Correct", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysListeningQuestions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesListeningQuestions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoListeningQuestions = new TableInfo("listening_questions", _columnsListeningQuestions, _foreignKeysListeningQuestions, _indicesListeningQuestions);
        final TableInfo _existingListeningQuestions = TableInfo.read(db, "listening_questions");
        if (!_infoListeningQuestions.equals(_existingListeningQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "listening_questions(com.duolingo.app.models.ListeningQuestion).\n"
                  + " Expected:\n" + _infoListeningQuestions + "\n"
                  + " Found:\n" + _existingListeningQuestions);
        }
        final HashMap<String, TableInfo.Column> _columnsPronunciationQuestions = new HashMap<String, TableInfo.Column>(3);
        _columnsPronunciationQuestions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPronunciationQuestions.put("lesson_id", new TableInfo.Column("lesson_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPronunciationQuestions.put("transcript", new TableInfo.Column("transcript", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPronunciationQuestions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPronunciationQuestions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPronunciationQuestions = new TableInfo("pronunciation_questions", _columnsPronunciationQuestions, _foreignKeysPronunciationQuestions, _indicesPronunciationQuestions);
        final TableInfo _existingPronunciationQuestions = TableInfo.read(db, "pronunciation_questions");
        if (!_infoPronunciationQuestions.equals(_existingPronunciationQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "pronunciation_questions(com.duolingo.app.models.PronunciationQuestion).\n"
                  + " Expected:\n" + _infoPronunciationQuestions + "\n"
                  + " Found:\n" + _existingPronunciationQuestions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "762a167ccfbd0a591e1c274a0b120d9c", "06251e22a972f3d0fbe892c7371ce8fc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "vocabulary_table","users","languages","learning_progress","lesson_table","grammar_questions","listening_questions","pronunciation_questions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `vocabulary_table`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `languages`");
      _db.execSQL("DELETE FROM `learning_progress`");
      _db.execSQL("DELETE FROM `lesson_table`");
      _db.execSQL("DELETE FROM `grammar_questions`");
      _db.execSQL("DELETE FROM `listening_questions`");
      _db.execSQL("DELETE FROM `pronunciation_questions`");
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
    _typeConvertersMap.put(LanguageDao.class, LanguageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LearningProgressDao.class, LearningProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LessonDao.class, LessonDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GrammarDao.class, GrammarDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ListeningDao.class, ListeningDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PronunciationDao.class, PronunciationDao_Impl.getRequiredConverters());
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
  public LanguageDao languageDao() {
    if (_languageDao != null) {
      return _languageDao;
    } else {
      synchronized(this) {
        if(_languageDao == null) {
          _languageDao = new LanguageDao_Impl(this);
        }
        return _languageDao;
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
  public GrammarDao grammarDao() {
    if (_grammarDao != null) {
      return _grammarDao;
    } else {
      synchronized(this) {
        if(_grammarDao == null) {
          _grammarDao = new GrammarDao_Impl(this);
        }
        return _grammarDao;
      }
    }
  }

  @Override
  public ListeningDao listeningDao() {
    if (_listeningDao != null) {
      return _listeningDao;
    } else {
      synchronized(this) {
        if(_listeningDao == null) {
          _listeningDao = new ListeningDao_Impl(this);
        }
        return _listeningDao;
      }
    }
  }

  @Override
  public PronunciationDao pronunciationDao() {
    if (_pronunciationDao != null) {
      return _pronunciationDao;
    } else {
      synchronized(this) {
        if(_pronunciationDao == null) {
          _pronunciationDao = new PronunciationDao_Impl(this);
        }
        return _pronunciationDao;
      }
    }
  }
}
