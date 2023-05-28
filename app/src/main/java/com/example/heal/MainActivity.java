package com.example.heal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private String currentUsername;
    private boolean isLoggedIn = false;
    private BottomNavigationView bottomNavigationView;
    private AccountFragment accountFragment;
    private RegisterFragment registerFragment;
    private MainFragment mainFragment;
    private FoodFragment foodFragment;

    private MyDatabaseHelper databaseHelper;
    private int selectedNavItem; // Переменная для сохранения выбранного элемента в BottomNavigationView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация фрагментов
        mainFragment = new MainFragment();
        foodFragment = new FoodFragment();
        accountFragment = new AccountFragment();
        registerFragment = new RegisterFragment();
        databaseHelper = new MyDatabaseHelper(getApplicationContext());

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_food:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, foodFragment)
                            .commit();
                    selectedNavItem = R.id.nav_food; // Сохраняем выбранный элемент
                    return true;
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mainFragment)
                            .commit();
                    selectedNavItem = R.id.nav_home; // Сохраняем выбранный элемент
                    return true;
                case R.id.nav_account:
                    if (isLoggedIn) {
                        // Получение данных о пользователе из базы данных
                        User user = databaseHelper.getUser(currentUsername); // Замените "username" на имя пользователя, которое вы хотите получить

                        // Проверка на null, чтобы избежать NullPointerException
                        if (user != null) {
                            // Создание экземпляра фрагмента аккаунта с передачей данных пользователя
                            AccountFragment accountFragment = AccountFragment.newInstance(user.getUsername(), user.getPassword(), user.getRegistrationDate());

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, accountFragment)
                                    .commit();
                            return true;
                        } else {
                            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, registerFragment)
                                .commit();
                    }


                    selectedNavItem = R.id.nav_account; // Сохраняем выбранный элемент
                    return true;
                default:
                    return false;
            }
        });

        // Загрузка начального фрагмента
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment)
                .commit();
        selectedNavItem = R.id.nav_home; // Устанавливаем начальный выбранный элемент

        // Восстановление состояния выбранного элемента при пересоздании активности
        if (savedInstanceState != null) {
            selectedNavItem = savedInstanceState.getInt("selectedNavItem", R.id.nav_home);
            bottomNavigationView.setSelectedItemId(selectedNavItem);
        }
    }


    // Метод для установки состояния входа в аккаунт
    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedNavItem", selectedNavItem); // Сохранение состояния выбранного элемента
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
