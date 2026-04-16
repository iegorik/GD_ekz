package com.example.gd_ekz.Grebenkin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gd_ekz.R;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    // Переменные для UI элементов
    private TextView textViewTitle;
    private EditText editTextEmail, editTextPassword;
    private Button buttonAction, buttonToggleMode;

    // Переменные для работы с БД и сессией
    private DatabaseAdapter databaseAdapter;
    private SessionManager sessionManager;

    // Флаг, показывающий, находимся ли мы в режиме "Входа" (true) или "Регистрации" (false)
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Инициализация БД и Сессии
        databaseAdapter = new DatabaseAdapter(this).open();
        sessionManager = new SessionManager(this);

        // 2. Сразу проверяем: если пользователь уже залогинен, переходим на главный экран (пока пропустим этот шаг, сделаем позже в пункте 1.2)
        if (sessionManager.isLoggedIn()) {

            Toast.makeText(this, "Вы уже вошли как: " + sessionManager.getUserEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
            return; // Важно! Чтобы не выполнять дальше onCreate

        }

        // 3. Находим элементы на экране
        initViews();

        // 4. Обработчик нажатия на главную кнопку (ВОЙТИ / ЗАРЕГИСТРИРОВАТЬСЯ)
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Валидация (проверка на пустоту и корректность)
                if (!validateInputs(email, password)) {
                    return; // Если данные не валидны, дальше не идем
                }

                if (isLoginMode) {
                    loginUser(email, password);
                } else {
                    registerUser(email, password);
                }
            }
        });

        // 5. Обработчик нажатия на кнопку переключения режима (Регистрация/Вход)
        buttonToggleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });
    }

    // Метод инициализации View
    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonAction = findViewById(R.id.buttonAction);
        buttonToggleMode = findViewById(R.id.buttonToggleMode);
    }

    // Валидация полей
    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email обязателен");
            return false;
        }
        // Простейшая проверка email (содержит @ и точку)
        if (!email.contains("@") || !email.contains(".")) {
            editTextEmail.setError("Некорректный Email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Пароль обязателен");
            return false;
        }
        if (!isLoginMode && password.length() < 6) {
            editTextPassword.setError("Пароль должен быть минимум 6 символов");
            return false;
        }
        return true;
    }

    // Логика входа
    private void loginUser(String email, String password) {
        User user = databaseAdapter.getUser(email, password);
        if (user != null) {
            // Успешный вход!
            sessionManager.createLoginSession(user);
            Toast.makeText(this, "Добро пожаловать, " + user.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Неверный Email или пароль", Toast.LENGTH_SHORT).show();
        }
    }

    // Логика регистрации
    private void registerUser(String email, String password) {
        // 1. Проверим, нет ли уже такого email
        if (databaseAdapter.isEmailExists(email)) {
            editTextEmail.setError("Этот Email уже используется");
            return;
        }

        // 2. Создаем пользователя и сохраняем в БД
        User newUser = new User(email, password);
        long userId = databaseAdapter.insertUser(newUser);

        if (userId != -1) {
            Toast.makeText(this, "Регистрация успешна! Теперь войдите.", Toast.LENGTH_SHORT).show();
            // Переключаемся обратно в режим входа
            toggleMode();
            // Очищаем поля
            editTextEmail.setText("");
            editTextPassword.setText("");
        } else {
            Toast.makeText(this, "Ошибка регистрации. Попробуйте позже.", Toast.LENGTH_SHORT).show();
        }
    }

    // Переключение UI между "Входом" и "Регистрацией"
    private void toggleMode() {
        if (isLoginMode) {
            // Переключаем в режим Регистрации
            isLoginMode = false;
            textViewTitle.setText("Регистрация");
            buttonAction.setText("Зарегистрироваться");
            buttonToggleMode.setText("Уже есть аккаунт? Войти");
            // При регистрации подсказка о 6 символах видна будет только при валидации
        } else {
            // Переключаем в режим Входа
            isLoginMode = true;
            textViewTitle.setText("Вход");
            buttonAction.setText("Войти");
            buttonToggleMode.setText("Нет аккаунта? Зарегистрироваться");
        }
        // Очищаем ошибки, если были
        editTextEmail.setError(null);
        editTextPassword.setError(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Не забываем закрыть соединение с БД
        if (databaseAdapter != null) {
            databaseAdapter.close();
        }
    }
}
