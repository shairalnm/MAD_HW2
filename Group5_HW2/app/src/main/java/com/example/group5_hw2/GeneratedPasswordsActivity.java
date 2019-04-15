package com.example.group5_hw2;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class activity_GeneratedPasswords extends AppCompatActivity {

    LinearLayout threadLayout;
    LinearLayout asyncLayout;
    LayoutInflater threadLayoutInflater;
    LayoutInflater asyncLayoutInflater;

    ArrayList<String> threadPassword;
    ArrayList<String> asyncPassword;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        setTitle("Generated Passwords");

        threadLayout = findViewById(R.id.linearLayoutThread);
        asyncLayout = findViewById(R.id.linearLayoutAsyncTask);
        threadLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        asyncLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                threadPassword = getIntent().getExtras().getStringArrayList(MainActivity.THREAD_KEY);
                asyncPassword = getIntent().getExtras().getStringArrayList(MainActivity.ASYNC_KEY);

                GradientDrawable gd = new GradientDrawable();
                gd.setStroke(1, Color.BLACK);

                for (String s : threadPassword) {
                    View myView = threadLayoutInflater.inflate(R.layout.password_field, null, false);
                    TextView threadView = myView.findViewById(R.id.textViewPassword);
                    threadView.setText(s + "");
                    threadView.setBackground(gd);
                    threadLayout.addView(myView);
                }
                for (String s : asyncPassword) {
                    View myView = asyncLayoutInflater.inflate(R.layout.password_field, null, false);
                    TextView asyncView = myView.findViewById(R.id.textViewPassword);
                    asyncView.setText(s + "");
                    asyncView.setBackground(gd);
                    asyncLayout.addView(myView);
                }
                findViewById(R.id.finish_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }
}
