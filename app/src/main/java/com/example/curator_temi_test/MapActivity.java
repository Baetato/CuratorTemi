package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MapActivity extends AppCompatActivity {

    // Firebase 참조 제거
    // private DatabaseReference locationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map); // map.xml 사용

        if(FirebaseApp.getApps(this).isEmpty()){
            FirebaseApp.initializeApp(this);
        }

        Button buttonTemi = findViewById(R.id.secret1);
        Button buttonMe = findViewById(R.id.secret2);

        // 비밀1 버튼 클릭 시 SecretsExplorerActivity 이동
        buttonTemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ModeSelectionActivity.class);
                startActivity(intent);
            }
        });

        // 비밀2 버튼 클릭 시 SecretsExplorerActivity 이동
        buttonMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, SecretsExplorerActivity.class);
                startActivity(intent);
            }
        });
    }
}