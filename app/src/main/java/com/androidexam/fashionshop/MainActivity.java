package com.androidexam.fashionshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidexam.fashionshop.Fragment.Cart.CartFragment;
import com.androidexam.fashionshop.Fragment.HistoryBuy.HistoryFragment;
import com.androidexam.fashionshop.Fragment.Home.HomeFragment;
import com.androidexam.fashionshop.Fragment.Individual.IndividualFragment;
import com.androidexam.fashionshop.Fragment.LogIn.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements CartFragment.OnCartBackPressedListener {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        Fragment defaultFragment = new HomeFragment();
        replaceFragment(defaultFragment);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Fragment homeFragment = new HomeFragment();
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.navigation_cart:
                        Fragment cartFragment = new CartFragment();
                        replaceFragment(cartFragment);
                        hideBottomNavigation();
                        return true;
                    case R.id.navigation_individual:
                        Fragment individual = new IndividualFragment();
                        replaceFragment(individual);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_filter:
                //mm
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hideBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void showBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void updateBottomNavigationState() {
        if (isInHomePage()) {
            Log.e("Hoang", "");
            bottomNavigationView.getMenu().getItem(1).setChecked(false);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }


    @Override
    public void onCartFragmentBackPressed() {

        updateBottomNavigationState();
    }

    private boolean isInHomePage() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return currentFragment instanceof HomeFragment;
    }
}
