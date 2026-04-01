package com.duolingo.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.models.Language;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<Language> languageList;
    private int selectedPosition = 0;
    private OnLanguageClickListener listener;

    public interface OnLanguageClickListener {
        void onLanguageClick(int position);
    }

    public LanguageAdapter(List<Language> languageList, OnLanguageClickListener listener) {
        this.languageList = languageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = languageList.get(position);
        holder.languageName.setText(language.getName());
        holder.flagImage.setImageResource(language.getFlagResourceId());
        holder.animalImage.setImageResource(language.getAnimalResourceId());

        // Đổi sang màu xanh Blue (Material Blue 500: #2196F3)
        if (position == selectedPosition) {
            holder.cardView.setStrokeWidth(8);
            holder.cardView.setStrokeColor(Color.parseColor("#2196F3")); 
            holder.cardView.setCardElevation(24f);
            holder.itemView.setAlpha(1.0f);
        } else {
            holder.cardView.setStrokeWidth(0);
            holder.cardView.setCardElevation(4f);
            holder.itemView.setAlpha(0.5f);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onLanguageClick(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView flagImage;
        ImageView animalImage;
        TextView languageName;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_language);
            flagImage = itemView.findViewById(R.id.image_flag);
            animalImage = itemView.findViewById(R.id.image_animal);
            languageName = itemView.findViewById(R.id.text_language_name);
        }
    }
}