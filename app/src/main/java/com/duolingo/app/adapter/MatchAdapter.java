package com.duolingo.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.models.MatchItem;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
    private final List<MatchItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MatchAdapter(List<MatchItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchItem item = items.get(position);
        holder.textContent.setText(item.getContent());

        if (item.isMatched()) {
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.setClickable(false);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setScaleX(1.0f);
            holder.itemView.setScaleY(1.0f);
            holder.itemView.setClickable(true);

            if (item.isSelected()) {
                holder.cardView.setStrokeColor(Color.parseColor("#2196F3"));
                holder.cardView.setStrokeWidth(6);
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
            } else {
                // Trạng thái bình thường: Viền mờ nhẹ
                holder.cardView.setStrokeColor(Color.parseColor("#E0E0E0"));
                holder.cardView.setStrokeWidth(2);
                holder.cardView.setCardBackgroundColor(Color.WHITE);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView textContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_item);
            textContent = itemView.findViewById(R.id.text_content);
        }
    }
}