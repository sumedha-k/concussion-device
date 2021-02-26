package com.example.bleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button b1,b2;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        b1 = (Button) findViewById(R.id.button1);
//        b2 = (Button) findViewById(R.id.button2);
//        txt = (TextView) findViewById(R.id.text);
    }

    public void control(View view) {
        Intent intent = new Intent(this, DeviceControlActivity.class);
//        startActivity(intent);
    }

    public void scan(View view) {
        Intent intent = new Intent(this, DeviceScanActivity.class);
//        startActivity(intent);
    }
}