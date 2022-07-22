package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.smartshopping.fragment.HomeFragment;
import com.example.smartshopping.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShopKeeperActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_keeper);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_navigation);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.home_navigation:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                return true;
            case R.id.profile_navigation:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                return true;
        }

        return false;
    }
}