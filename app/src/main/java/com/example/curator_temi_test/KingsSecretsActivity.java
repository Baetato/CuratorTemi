package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KingsSecretsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kings_secrets);

        // Firebase 값 변경
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quizAnswerRef = database.getReference("QuizAnswer");
        quizAnswerRef.setValue(3);

        Button startButton = findViewById(R.id.startAdventureButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(KingsSecretsActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }
}
