package com.duolingo.app.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.duolingo.app.models.GrammarQuestion;
import com.duolingo.app.models.Language;
import com.duolingo.app.models.LearningProgress;
import com.duolingo.app.models.Lesson;
import com.duolingo.app.models.User;
import com.duolingo.app.models.VocabularyItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {VocabularyItem.class, User.class, Language.class, LearningProgress.class, Lesson.class, GrammarQuestion.class}, version = 7, exportSchema = false)
public abstract class VocaVerseDatabase extends RoomDatabase {

    public abstract VocabularyDao vocabularyDao();
    public abstract UserDao userDao();
    public abstract LanguageDao languageDao();
    public abstract LearningProgressDao learningProgressDao();
    public abstract LessonDao lessonDao();
    public abstract GrammarDao grammarDao();

    private static volatile VocaVerseDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static VocaVerseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VocaVerseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VocaVerseDatabase.class, "vocaverse_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
