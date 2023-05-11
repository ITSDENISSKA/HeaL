package com.example.heal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        MainActivity main_fragment = new MainActivity();
        FoodActivity food_fragment = new FoodActivity();
        AccountActivity account_fragment = new AccountActivity();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_food:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            food_fragment).commit();
                    return true;
                case R.id.nav_home:
                     getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            main_fragment).commit();
                    return true;
                case R.id.nav_account:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            account_fragment).commit();
                    return true;
                default:
                    return false;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, food_fragment).commit();
    }
}

