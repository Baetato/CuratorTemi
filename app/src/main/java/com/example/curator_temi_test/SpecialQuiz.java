package com.example.curator_temi_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class SpecialQuiz extends AppCompatActivity {

    // ⇨ Temi, Firebase 초기화는 그대로 유지하되, 퀴즈 카운트는 QR 스캔 뒤 처리할 예정이므로 여기서는 일단 제거하거나 주석처리
    private Robot robot;
    private DatabaseReference quizCountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_quiz);

        // Temi & Firebase (필요하다면 유지)
        robot = Robot.getInstance();
        quizCountRef = FirebaseDatabase.getInstance().getReference("quizCount");

        // 뒤로가기 버튼이 필요하면 XML에 추가 후 바인딩
        // findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        // '다음' 버튼
        LinearLayout nextContainer = findViewById(R.id.nextContainer);
        nextContainer.setOnClickListener(v -> {
            // (원하면 TTS)
            robot.speak(TtsRequest.create("다음 문제로 이동합니다", true));
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });
    }
}
