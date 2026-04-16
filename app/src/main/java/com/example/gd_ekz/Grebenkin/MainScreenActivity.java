package com.example.gd_ekz.Grebenkin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gd_ekz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton buttonAdd;
    private Button buttonLogout;
    private ItemAdapter adapter;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Инициализация БД
        databaseAdapter = new DatabaseAdapter(this).open();

        // Инициализация View
        recyclerView = findViewById(R.id.recyclerViewItems);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        // Обработчики нажатий
        adapter.setOnItemClickListener(item -> {
            Toast.makeText(this, "Выбран: " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        adapter.setOnFavoriteClickListener(item -> {
            // Переключаем избранное
            item.setFavorite(!item.isFavorite());
            databaseAdapter.updateItem(item);
            loadItems();
            Toast.makeText(this, item.isFavorite() ? "Добавлено в избранное" : "Удалено из избранного", Toast.LENGTH_SHORT).show();
        });

        buttonAdd.setOnClickListener(v -> {
            // Пока для теста добавим тестовый предмет
            Item testItem = new Item("Тестовый товар", "Описание тестового товара", 999.99);
            databaseAdapter.insertItem(testItem);
            loadItems();
            Toast.makeText(this, "Добавлен тестовый предмет", Toast.LENGTH_SHORT).show();
        });

        // Кнопка выхода (ОДИН РАЗ!)
        buttonLogout.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(MainScreenActivity.this);
            sessionManager.logout();
            Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadItems() {
        List<Item> items = databaseAdapter.getAllItems();
        adapter.setItems(items);

        // Показываем или скрываем сообщение о пустом списке
        if (items.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseAdapter != null) {
            databaseAdapter.close();
        }
    }
}