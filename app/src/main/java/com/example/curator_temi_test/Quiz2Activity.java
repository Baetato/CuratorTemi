package com.example.curator_temi_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class Quiz2Activity extends AppCompatActivity {

    private final int correctAnswerIndex = 2; // 두 번째 버튼이 정답
    private ImageButton[] buttons;
    private RelativeLayout rootLayout;
    private ImageView resultImage;
    private ImageButton actionButton;

    private Robot robot;
    private DatabaseReference quizCountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_2);

        // 1) Temi, Firebase 초기화
        robot = Robot.getInstance();
        quizCountRef = FirebaseDatabase.getInstance().getReference("quizCount");

        // 2) 레이아웃 바인딩
        rootLayout = findViewById(R.id.rootLayout);

        // 뒤로가기 버튼 (XML에 buttonBack 있어야 함)
//        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        // 선택지 ImageButton 배열
        buttons = new ImageButton[]{
                findViewById(R.id.img1),
                findViewById(R.id.img2),
                findViewById(R.id.img3),
                findViewById(R.id.img4)
        };

        // 3) 결과 오버레이 준비
        resultImage = new ImageView(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        resultImage.setLayoutParams(p);
        resultImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        resultImage.setVisibility(View.GONE);
        rootLayout.addView(resultImage);

        // 4) 클릭 리스너 등록
        for (int i = 0; i < buttons.length; i++) {
            final int idx = i + 1;
            buttons[i].setOnClickListener(v -> handleAnswer(idx));
        }
    }

    /** 사용자가 보기 선택 시 호출 */
    private void handleAnswer(int selectedIndex) {
        boolean isCorrect = (selectedIndex == correctAnswerIndex);

        // a) Firebase에 결과 기록
        quizCountRef.setValue(isCorrect ? 1 : 0);

        // b) Temi TTS
        robot.speak(TtsRequest.create(isCorrect ? "정답입니다" : "틀렸습니다", true));

        // c) UI 오버레이
        showResultOverlay(isCorrect);
    }

    /** 오버레이로 정답/오답을 띄우고, 재시도 버튼을 제공 */
    private void showResultOverlay(boolean isCorrect) {
        // 선택지 비활성화 및 비선택지 회색 처리
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            if ((i + 1) != correctAnswerIndex) {
                buttons[i].setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                buttons[i].setScaleX(0.8f);
                buttons[i].setScaleY(0.8f);
            }
        }

        // 오버레이 이미지 설정
        resultImage.setImageResource(isCorrect ? R.drawable.ooo : R.drawable.xxx);
        resultImage.setVisibility(View.VISIBLE);
        resultImage.bringToFront();

        // 재시도 또는 다음 동작
        if (isCorrect) {
            // 정답 -> 스페셜 퀴즈 (QR찍기 이벤트)
            // 잠시 대기 후 SpecialQuiz로 이동
            startActivity(new Intent(this, SpecialQuiz.class));
            finish();
        } else {
            // 오답: 재시도 버튼 생성
            if (actionButton != null) rootLayout.removeView(actionButton);
            actionButton = new ImageButton(this);
            RelativeLayout.LayoutParams btnParams =
                    new RelativeLayout.LayoutParams(394, 110);
            btnParams.leftMargin = 700;
            btnParams.topMargin = 880;
            actionButton.setLayoutParams(btnParams);
            actionButton.setImageResource(R.drawable.re_quiz);
            actionButton.setBackgroundColor(Color.TRANSPARENT);
            actionButton.setOnClickListener(v -> resetQuiz());
            rootLayout.addView(actionButton);
            actionButton.bringToFront();
        }
    }

    /** 오답 후 원 상태로 복구 */
    private void resetQuiz() {
        resultImage.setVisibility(View.GONE);
        if (actionButton != null) {
            rootLayout.removeView(actionButton);
            actionButton = null;
        }
        for (ImageButton btn : buttons) {
            btn.clearColorFilter();
            btn.setScaleX(1f);
            btn.setScaleY(1f);
            btn.setEnabled(true);
        }
    }
}