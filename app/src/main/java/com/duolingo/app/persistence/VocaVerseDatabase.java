package com.duolingo.app.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.duolingo.app.models.GameHistory;
import com.duolingo.app.models.LearningProgress;
import com.duolingo.app.models.Lesson;
import com.duolingo.app.models.User;
import com.duolingo.app.models.VocabularyItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {VocabularyItem.class, User.class, Lesson.class, GameHistory.class, LearningProgress.class}, version = 1, exportSchema = false)
public abstract class VocaVerseDatabase extends RoomDatabase {

    public abstract VocabularyDao vocabularyDao();
    public abstract UserDao userDao();
    public abstract LessonDao lessonDao();
    public abstract ProgressDao progressDao();
    public abstract LearningProgressDao learningProgressDao();

    private static volatile VocaVerseDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static Context appContext;

    public static VocaVerseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VocaVerseDatabase.class) {
                if (INSTANCE == null) {
                    appContext = context.getApplicationContext();
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VocaVerseDatabase.class, "voca_verse_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                VocabularyDao dao = INSTANCE.vocabularyDao();

                // Use the stored appContext
                List<VocabularyItem> vocabularyItems = CSVHelper.readVocabularyFromCSV(
                        appContext,
                        "english_lessons.csv",
                        "English" // Default language
                );

                if (!vocabularyItems.isEmpty()) {
                    dao.insertAll(vocabularyItems);
                }
            });
        }
    };
}
