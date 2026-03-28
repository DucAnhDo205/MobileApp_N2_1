package com.duolingo.app.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.duolingo.app.R;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.viewmodels.FlashcardViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class FlashcardActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private FlashcardViewModel flashcardViewModel;
    private List<VocabularyItem> dueVocabularyList;
    private int currentCardIndex = 0;

    private TextView textWord, textMeaning, textExample, textNoCards;
    private LinearLayout layoutMeaning;
    private CardView cardView;
    private MaterialButton buttonCorrect, buttonIncorrect;
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
        flashcardViewModel.getAllVocabulary().observe(this, vocabularyItems -> {
            dueVocabularyList = vocabularyItems;
            currentCardIndex = 0;
            if (dueVocabularyList == null || dueVocabularyList.isEmpty()) {
                showNoCardsMessage();
            } else {
                showCardData();
            }
        });

        cardView.setOnClickListener(v -> flipCard());
        buttonCorrect.setOnClickListener(v -> processAnswer(true));
        buttonIncorrect.setOnClickListener(v -> processAnswer(false));
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
        buttonCorrect = findViewById(R.id.button_correct);
        buttonIncorrect = findViewById(R.id.button_incorrect);
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
        frontAnim.setTarget(textWord);
        backAnim.setTarget(layoutMeaning);
        frontAnim.start();
        layoutMeaning.setVisibility(View.GONE);
        textWord.setVisibility(View.VISIBLE);
    }

    private void flipCard() {
        if (isFront) {
            frontAnim.setTarget(textWord);
            backAnim.setTarget(layoutMeaning);
            frontAnim.start();
            backAnim.start();
            isFront = false;
        } else {
            frontAnim.setTarget(layoutMeaning);
            backAnim.setTarget(textWord);
            frontAnim.start();
            backAnim.start();
            isFront = true;
        }
    }
    
    private void speakWord() {
        String word = textWord.getText().toString();
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void processAnswer(boolean isCorrect) {
        if (dueVocabularyList == null || currentCardIndex >= dueVocabularyList.size()) return;

        VocabularyItem currentItem = dueVocabularyList.get(currentCardIndex);
        updateSpacedRepetition(currentItem, isCorrect);
        flashcardViewModel.updateVocabularyItem(currentItem);

        showNextCard();
    }

    private void showNextCard() {
        currentCardIndex++;
        if (currentCardIndex >= dueVocabularyList.size()) {
            showNoCardsMessage();
        } else {
            showCardData();
        }
    }

    private void showNoCardsMessage() {
        cardView.setVisibility(View.GONE);
        findViewById(R.id.button_layout).setVisibility(View.GONE);
        textNoCards.setVisibility(View.VISIBLE);
        textNoCards.setText("You have no more cards to review today!");
    }

    // Simplified SM-2 algorithm for spaced repetition
    private void updateSpacedRepetition(VocabularyItem item, boolean isCorrect) {
        if (isCorrect) {
            int newInterval;
            if (item.getInterval() == 1) {
                newInterval = 6;
            } else {
                newInterval = (int) (item.getInterval() * item.getEaseFactor());
            }
            item.setInterval(newInterval);
            item.setEaseFactor(item.getEaseFactor() + 0.1);
        } else {
            item.setInterval(1); // Reset interval
            item.setEaseFactor(Math.max(1.3, item.getEaseFactor() - 0.2)); // Decrease ease factor
        }

        long oneDayInMillis = 24 * 60 * 60 * 1000;
        long nextReviewTime = System.currentTimeMillis() + (long)item.getInterval() * oneDayInMillis;
        item.setNextReviewDate(nextReviewTime);
    }
    
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle error
            }
        } else {
            // Handle error
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
