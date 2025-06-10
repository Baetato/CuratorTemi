package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class SecretsExplorerActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secrets_explorer2);

        // [비밀 1] 버튼 클릭 시 → QuizActivity로 이동
//        findViewById(R.id.secret1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SecretsExplorerActivity.this, QuizActivity.class);
//                startActivity(intent);
//            }
//        });
//        하드코딩
        findViewById(R.id.secret1).setOnClickListener(v -> {
            Intent intent = new Intent(this, StayInHereActivity.class);
            intent.putExtra("artifact_name", "사도세자_뒤주");  // 하드코딩 위치 이름
            startActivity(intent);
        });
    }
}
