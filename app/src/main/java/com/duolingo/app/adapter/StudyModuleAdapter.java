package com.duolingo.app.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.models.StudyModule;
import java.util.List;

public class StudyModuleAdapter extends RecyclerView.Adapter<StudyModuleAdapter.ModuleViewHolder> {

    private List<StudyModule> moduleList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(StudyModule module);
    }

    public StudyModuleAdapter(List<StudyModule> moduleList, OnItemClickListener listener) {
        this.moduleList = moduleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        StudyModule module = moduleList.get(position);
        holder.tvTitle.setText(module.getTitle());
        holder.tvDescription.setText(module.getDescription());
        holder.imgIcon.setImageResource(module.getIconResId());

        // Lấy màu từ file XML colors.xml đã tách ra
        int mainColor = ContextCompat.getColor(holder.itemView.getContext(), module.getColorRes());
        int bgColor = ContextCompat.getColor(holder.itemView.getContext(), module.getBgColorRes());

        holder.imgIcon.setImageTintList(ColorStateList.valueOf(mainColor));
        holder.cardIconBg.setCardBackgroundColor(bgColor);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(module));
    }

    @Override
    public int getItemCount() { return moduleList.size(); }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        CardView cardIconBg;
        ImageView imgIcon;
        TextView tvTitle, tvDescription;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIconBg = itemView.findViewById(R.id.cardIconBg);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}