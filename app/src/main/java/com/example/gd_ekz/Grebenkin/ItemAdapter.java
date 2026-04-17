package com.example.gd_ekz.Grebenkin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gd_ekz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private OnItemClickListener listener;
    private OnFavoriteClickListener favoriteListener;

    // Интерфейс для обработки нажатий на элемент
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    // Интерфейс для обработки нажатия на кнопку избранного
    public interface OnFavoriteClickListener {
        void onFavoriteClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.textName.setText(item.getName());
        holder.textDescription.setText(item.getDescription());
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f ₽", item.getPrice()));

        // Устанавливаем иконку избранного
        holder.buttonFavorite.setImageResource(
                item.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off
        );

        // Обработчик нажатия на весь элемент
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        // Обработчик нажатия на кнопку избранного
        holder.buttonFavorite.setOnClickListener(v -> {
            if (favoriteListener != null) {
                favoriteListener.onFavoriteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDescription, textPrice;
        ImageButton buttonFavorite;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textItemName);
            textDescription = itemView.findViewById(R.id.textItemDescription);
            textPrice = itemView.findViewById(R.id.textItemPrice);
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
        }
    }
}