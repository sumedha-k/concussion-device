package com.example.concussionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {

    BottomNavigationView bottomNavigationView;
    MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        materialToolbar = findViewById(R.id.topAppBar);
        materialToolbar.setOnMenuItemClickListener(this);
    }

    Home homeFragment = new Home();
    Calendar calFragment = new Calendar();
    Newsfeed newsFragment = new Newsfeed();
    Faq faqFragment = new Faq();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        materialToolbar = findViewById(R.id.topAppBar);
        switch(item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                materialToolbar.setTitle(" Home");
                materialToolbar.setLogo(R.drawable.ic_baseline_home_24);
                return true;
            case R.id.calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, calFragment).commit();
                materialToolbar.setTitle(" Calendar");
                materialToolbar.setLogo(R.drawable.ic_baseline_event_24);
                return true;
            case R.id.newsfeed:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, newsFragment).commit();
                materialToolbar.setTitle(" Newsfeed");
                materialToolbar.setLogo(R.drawable.ic_baseline_text_snippet_24);
                return true;
            case R.id.faq:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, faqFragment).commit();
                materialToolbar.setTitle(" FAQ");
                materialToolbar.setLogo(R.drawable.ic_baseline_not_listed_location_24);
                return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String[] items = {"Device 1", "Device 2", "Device 3"}; // Replace with array of the bluetooth devices

        if(item.getItemId() == R.id.bluetooth) {

            new AlertDialog.Builder(this).setTitle("Select Bluetooth Device").setItems(items, ((dialog, which) -> {
                Log.i("a", "b");
                // Replace above line with a function that takes in which (an integer designating which
                // item was clicked) as a parameter and then connect to the corresponding bluetooth
                // device.
            })).show();
            return true;
        }

        return false;
    }
}