package com.example.heal;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// MainActivity.java

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private AccountFragment accountFragment;
    private MainFragment mainFragment;
    private FoodFragment foodFragment;
    private int selectedNavItem;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        foodFragment = new FoodFragment();
        accountFragment = new AccountFragment();

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_food:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, foodFragment)
                            .commit();
                    selectedNavItem = R.id.nav_food;
                    return true;
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mainFragment)
                            .commit();
                    selectedNavItem = R.id.nav_home;
                    return true;
                case R.id.nav_account:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, accountFragment)
                            .commit();
                    selectedNavItem = R.id.nav_account;
                    return true;
                default:
                    return false;
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment)
                .commit();
        selectedNavItem = R.id.nav_home;

        if (savedInstanceState != null) {
            selectedNavItem = savedInstanceState.getInt("selectedNavItem", R.id.nav_home);
            bottomNavigationView.setSelectedItemId(selectedNavItem);
        }
    }
}
