package com.example.gd_ekz.Grebenkin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gd_ekz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton buttonAdd;
    private ItemAdapter adapter;
    private DatabaseAdapter databaseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        // Инициализация БД
        databaseAdapter = new DatabaseAdapter(requireContext()).open();

        // Инициализация View
        recyclerView = view.findViewById(R.id.recyclerViewItems);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        buttonAdd = view.findViewById(R.id.buttonAdd);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        // Обработчик нажатия на элемент
        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(requireContext(), ItemDetailActivity.class);
            intent.putExtra("item_id", item.getId());
            intent.putExtra("is_new", false);
            startActivity(intent);
        });

        // Обработчик нажатия на избранное
        adapter.setOnFavoriteClickListener(item -> {
            item.setFavorite(!item.isFavorite());
            databaseAdapter.updateItem(item);
            loadItems();
            Toast.makeText(requireContext(),
                    item.isFavorite() ? "Добавлено в избранное" : "Удалено из избранного",
                    Toast.LENGTH_SHORT).show();
        });

        // Кнопка добавления
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ItemDetailActivity.class);
            intent.putExtra("is_new", true);
            startActivity(intent);
        });

        return view;
    }

    private void loadItems() {
        List<Item> items = databaseAdapter.getAllItems();
        adapter.setItems(items);

        if (items.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseAdapter != null) {
            databaseAdapter.close();
        }
    }
}