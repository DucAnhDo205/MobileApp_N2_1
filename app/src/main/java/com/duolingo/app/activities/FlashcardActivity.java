package com.duolingo.app.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;

import com.duolingo.app.R;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.viewmodels.FlashcardViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FlashcardActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private FlashcardViewModel flashcardViewModel;
    private List<VocabularyItem> dueVocabularyList;
    private int currentCardIndex = 0;
    private static final int MAX_CARDS_PER_SESSION = 15; // Limit to 15 cards

    private TextView textWord, textMeaning, textExample, textNoCards;
    private LinearLayout layoutMeaning;
    private CardView cardView;
    private MaterialButton buttonNext, buttonBack;
    private ImageButton buttonAudio;
    private MaterialToolbar toolbar;

    private AnimatorSet frontAnim, backAnim;
    private boolean isFront = true;
    
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        initializeViews();
        setSupportActionBar(toolbar);
        loadAnimations();
        
        tts = new TextToSpeech(this, this);

        flashcardViewModel = new androidx.lifecycle.ViewModelProvider(this).get(FlashcardViewModel.class);
        
        int lessonNumber = getIntent().getIntExtra("lesson_number", -1);
        String category = getIntent().getStringExtra("category");
        String language = getIntent().getStringExtra("language");
        if (language == null) language = "English";

        LiveData<List<VocabularyItem>> vocabularyLiveData;

        if (lessonNumber != -1 && category != null) {
            vocabularyLiveData = flashcardViewModel.getVocabularyByLessonAndCategory(lessonNumber, category, language);
            toolbar.setTitle("Lesson " + lessonNumber + " - " + category);
        } else if (lessonNumber != -1) {
            vocabularyLiveData = flashcardViewModel.getVocabularyByLesson(lessonNumber, language);
            toolbar.setTitle("Lesson " + lessonNumber);
        } else if (category != null) {
            vocabularyLiveData = flashcardViewModel.getVocabularyByCategory(category, language);
            toolbar.setTitle("Category: " + category);
        } else {
            vocabularyLiveData = flashcardViewModel.getDueVocabulary();
            toolbar.setTitle("Review Flashcards");
        }

        vocabularyLiveData.observe(this, vocabularyItems -> {
            if (vocabularyItems != null && !vocabularyItems.isEmpty()) {
                // Shuffle and limit cards
                List<VocabularyItem> shuffledList = new ArrayList<>(vocabularyItems);
                Collections.shuffle(shuffledList);
                dueVocabularyList = shuffledList.subList(0, Math.min(shuffledList.size(), MAX_CARDS_PER_SESSION));
                
                currentCardIndex = 0;
                showCardData();
            } else {
                showNoCardsMessage();
            }
        });

        cardView.setOnClickListener(v -> flipCard());
        buttonNext.setOnClickListener(v -> showNextCard());
        buttonBack.setOnClickListener(v -> showPreviousCard());
        buttonAudio.setOnClickListener(v -> speakWord());
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        cardView = findViewById(R.id.card_view);
        textWord = findViewById(R.id.text_word);
        layoutMeaning = findViewById(R.id.layout_meaning);
        textMeaning = findViewById(R.id.text_meaning);
        textExample = findViewById(R.id.text_example);
        textNoCards = findViewById(R.id.text_no_cards);
        buttonNext = findViewById(R.id.button_next);
        buttonBack = findViewById(R.id.button_back);
        buttonAudio = findViewById(R.id.button_audio);
    }
    
    private void loadAnimations() {
        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flashcard_flip_front);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flashcard_flip_back);
    }

    private void showCardData() {
        if (dueVocabularyList == null || dueVocabularyList.isEmpty() || currentCardIndex >= dueVocabularyList.size()) {
            showNoCardsMessage();
            return;
        }

        cardView.setVisibility(View.VISIBLE);
        findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
        textNoCards.setVisibility(View.GONE);

        VocabularyItem currentItem = dueVocabularyList.get(currentCardIndex);
        textWord.setText(currentItem.getWord());
        textMeaning.setText(currentItem.getMeaning());
        textExample.setText(currentItem.getExample());

        // Reset to front view
        isFront = true;
        textWord.setVisibility(View.VISIBLE);
        layoutMeaning.setVisibility(View.GONE);
        textWord.setAlpha(1f);
        layoutMeaning.setAlpha(1f);
        cardView.setRotationY(0);
        
        // Reset card size
        updateCardSize(true);
        
        buttonBack.setEnabled(currentCardIndex > 0);
        buttonNext.setText(currentCardIndex == dueVocabularyList.size() - 1 ? "Finish" : "Next");
    }

    private void flipCard() {
        if (isFront) {
            frontAnim.setTarget(cardView);
            frontAnim.start();
            
            cardView.postDelayed(() -> {
                textWord.setVisibility(View.GONE);
                layoutMeaning.setVisibility(View.VISIBLE);
                updateCardSize(false);
            }, 500);
            
            isFront = false;
        } else {
            backAnim.setTarget(cardView);
            backAnim.start();
            
            cardView.postDelayed(() -> {
                layoutMeaning.setVisibility(View.GONE);
                textWord.setVisibility(View.VISIBLE);
                updateCardSize(true);
            }, 500);
            
            isFront = true;
        }
    }
    
    private void updateCardSize(boolean isFront) {
        ViewGroup.LayoutParams params = cardView.getLayoutParams();
        if (isFront) {
            // Normal size (using weight/ratio from XML)
            params.height = 0; // Constraints will handle it
        } else {
            // Smaller size for back
            params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.4); 
        }
        cardView.setLayoutParams(params);
    }
    
    private void speakWord() {
        if (dueVocabularyList == null || currentCardIndex >= dueVocabularyList.size()) return;
        String word = dueVocabularyList.get(currentCardIndex).getWord();
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void showNextCard() {
        if (currentCardIndex == dueVocabularyList.size() - 1) {
            finish(); // End lesson
            return;
        }
        currentCardIndex++;
        showCardData();
    }

    private void showPreviousCard() {
        if (currentCardIndex > 0) {
            currentCardIndex--;
            showCardData();
        }
    }

    private void showNoCardsMessage() {
        cardView.setVisibility(View.GONE);
        findViewById(R.id.button_layout).setVisibility(View.GONE);
        textNoCards.setVisibility(View.VISIBLE);
        textNoCards.setText("Lesson Completed!");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
        }
    }
    
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
