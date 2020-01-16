package com.example.androidschoolmvp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidschoolmvp.R;
import com.example.androidschoolmvp.view.fragments.LecturesFragment;

/**
 * Контейнер для лекций
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_my, LecturesFragment.newInstance())
                .commit();
    }


}