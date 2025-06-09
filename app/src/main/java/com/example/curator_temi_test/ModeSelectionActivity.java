package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ModeSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);

        // Temi 관람 모드 → SecretsExplorerActivity로 이동
        findViewById(R.id.temi_first).setOnClickListener(v -> {
            Intent intent = new Intent(this, SecretsExplorerActivity.class);
            startActivity(intent);
        });

        // 자기주도 모드 → KingsSecretsActivity로 이동
        findViewById(R.id.me_first).setOnClickListener(v -> {
            Intent intent = new Intent(this, KingsSecretsActivity.class);
            startActivity(intent);
        });
    }
}
