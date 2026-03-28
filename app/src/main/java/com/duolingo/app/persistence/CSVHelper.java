package com.duolingo.app.persistence;

import android.content.Context;
import android.util.Log;

import com.duolingo.app.models.VocabularyItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    private static final String TAG = "CSVHelper";

    public static List<VocabularyItem> readVocabularyFromCSV(Context context, String fileName, String languageName) {
        return readVocabularyFromCSV(context, fileName, languageName, 1);
    }

    public static List<VocabularyItem> readVocabularyFromCSV(Context context, String fileName, String languageName, int lessonNumber) {
        List<VocabularyItem> vocabularyList = new ArrayList<>();
        // Ép đọc bằng chuẩn UTF-8 để không bị lỗi font Tiếng Việt
        try (InputStream is = context.getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            
            String line;
            reader.readLine(); // Bỏ qua tiêu đề

            while ((line = reader.readLine()) != null) {
                // Tách cột bằng dấu phẩy, xử lý cả trường hợp có dấu phẩy trong ngoặc kép
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                if (tokens.length >= 4) {
                    String word = cleanToken(tokens[0]);
                    String meaning = cleanToken(tokens[1]);
                    String example = cleanToken(tokens[2]);
                    String category = cleanToken(tokens[3]);
                    // Nếu CSV có cột thứ 5 là lessonNumber, có thể dùng nó, nếu không dùng tham số truyền vào
                    int currentLessonNumber = lessonNumber;
                    if (tokens.length >= 5) {
                        try {
                            currentLessonNumber = Integer.parseInt(cleanToken(tokens[4]));
                        } catch (NumberFormatException ignored) {}
                    }
                    
                    vocabularyList.add(new VocabularyItem(word, meaning, example, languageName, category, currentLessonNumber));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Lỗi đọc file: " + e.getMessage());
        }
        return vocabularyList;
    }

    private static String cleanToken(String token) {
        if (token == null) return "";
        String clean = token.trim();
        if (clean.startsWith("\"") && clean.endsWith("\"")) {
            clean = clean.substring(1, clean.length() - 1);
        }
        return clean.replace("\"\"", "\"");
    }
}
