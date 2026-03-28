package com.duolingo.app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.VocabularyRepository;
import com.duolingo.app.services.DictionaryService;
import com.duolingo.app.services.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVocabularyActivity extends AppCompatActivity {

    private TextInputEditText editWord, editMeaning, editExample;
    private VocabularyRepository repository;
    private DictionaryService dictionaryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocabulary);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Word");
        }

        repository = new VocabularyRepository(getApplication());
        dictionaryService = RetrofitClient.getDictionaryClient().create(DictionaryService.class);

        editWord = findViewById(R.id.edit_word);
        editMeaning = findViewById(R.id.edit_meaning);
        editExample = findViewById(R.id.edit_example);
        MaterialButton buttonSave = findViewById(R.id.button_save);

        // Lắng nghe khi người dùng nhập xong từ để tự động tìm nghĩa từ API
        editWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String word = s.toString().trim();
                if (word.length() > 2) { // Chỉ tìm kiếm khi từ có trên 2 ký tự
                    fetchDefinition(word);
                }
            }
        });

        buttonSave.setOnClickListener(v -> saveWord());
    }

    private void fetchDefinition(String word) {
        dictionaryService.getWordDefinition(word).enqueue(new Callback<List<DictionaryService.DictionaryResponse>>() {
            @Override
            public void onResponse(Call<List<DictionaryService.DictionaryResponse>> call, Response<List<DictionaryService.DictionaryResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    DictionaryService.DictionaryResponse data = response.body().get(0);
                    
                    // Lấy nghĩa đầu tiên của từ
                    if (!data.meanings.isEmpty() && !data.meanings.get(0).definitions.isEmpty()) {
                        String def = data.meanings.get(0).definitions.get(0).definition;
                        editMeaning.setText(def);
                        
                        // Nếu có ví dụ, tự động điền vào
                        String example = data.meanings.get(0).definitions.get(0).example;
                        if (example != null) {
                            editExample.setText(example);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DictionaryService.DictionaryResponse>> call, Throwable t) {
                Log.e("DictionaryAPI", "Error: " + t.getMessage());
            }
        });
    }

    private void saveWord() {
        String word = editWord.getText().toString().trim();
        String meaning = editMeaning.getText().toString().trim();
        String example = editExample.getText().toString().trim();

        if (TextUtils.isEmpty(word) || TextUtils.isEmpty(meaning)) {
            Toast.makeText(this, "Please fill in Word and Meaning", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiện tại vẫn đang lưu vào Room Database (SQLite)
        // Mặc định cho vào bài học 1 khi thêm thủ công
        VocabularyItem newItem = new VocabularyItem(word, meaning, example, "English", "General", 1);
        repository.insert(newItem);

        Toast.makeText(this, "Word saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
