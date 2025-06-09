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

public class QuizActivity extends AppCompatActivity {

    private final int correctAnswerIndex = 3; // 두 번째 버튼이 정답
    private ImageButton[] buttons;
    private RelativeLayout rootLayout;
    private ImageView resultImage;
    private ImageButton actionButton;

    // ⇨ 추가된 필드: Temi Robot, Firebase 참조
    private Robot robot;
    private DatabaseReference quizCountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_1);

        // 1) Temi & Firebase 초기화
        robot = Robot.getInstance();
        quizCountRef = FirebaseDatabase.getInstance().getReference("quizCount");

        // 2) 레이아웃 뷰들 바인딩
        rootLayout = findViewById(R.id.rootLayout);
        LinearLayout quizOptions = findViewById(R.id.quizOptions);

        // 뒤로가기 버튼 (XML에 buttonBack 이 있어야 함)
        //findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        // 선택지 ImageButton 배열
        ImageButton img1 = findViewById(R.id.img1);
        ImageButton img2 = findViewById(R.id.img2);
        ImageButton img3 = findViewById(R.id.img3);
        ImageButton img4 = findViewById(R.id.img4);
        buttons = new ImageButton[]{img1, img2, img3, img4};

        // 3) 결과 오버레이 뷰 준비
        resultImage = new ImageView(this);
        RelativeLayout.LayoutParams resultParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        resultParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        resultImage.setLayoutParams(resultParams);
        resultImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        resultImage.setVisibility(View.GONE);
        rootLayout.addView(resultImage);

        // 4) 클릭 리스너 등록 (기존 listener 대체)
        for (int i = 0; i < buttons.length; i++) {
            final int selectedIndex = i + 1;
            buttons[i].setOnClickListener(v -> handleAnswer(selectedIndex));
        }
    }

    /**
     * 사용자가 선택지를 누를 때 호출됩니다.
     * Temi TTS와 Firebase 업데이트, 결과 오버레이를 처리합니다.
     */
    private void handleAnswer(int selectedIndex) {
        boolean isCorrect = (selectedIndex == correctAnswerIndex);

        // 5) Firebase에 정답/오답 기록
        quizCountRef.setValue(isCorrect ? 1 : 0);

        // 6) Temi TTS 출력
        String speech = isCorrect ? "정답입니다" : "틀렸습니다";
        robot.speak(TtsRequest.create(speech, true));

        // 7) UI 결과 표시
        showResultOverlay(isCorrect);
    }

    /**
     * 오버레이로 정답/오답 이미지를 띄우고,
     * 정답일 경우 다음 퀴즈로 이동합니다.
     */
    private void showResultOverlay(boolean isCorrect) {
        // 1) 선택지 비활성화 및 회색처리
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            if ((i + 1) != correctAnswerIndex) {
                buttons[i].setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                buttons[i].setScaleX(0.8f);
                buttons[i].setScaleY(0.8f);
            }
        }

        // 2) 오버레이 이미지 설정
        resultImage.setImageResource(isCorrect
                ? R.drawable.ooo
                : R.drawable.xxx);
        resultImage.setVisibility(View.VISIBLE);
        resultImage.bringToFront();

        // 3) 오답인 경우 재시도 버튼, 정답인 경우 자동 이동
        if (isCorrect) {
            // 잠시 대기 후 Quiz2Activity 로 이동 (원하면 Handler.postDelayed 사용)
            startActivity(new Intent(this, Quiz2Activity.class));
            finish();
        } else {
            // 재시도 버튼
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

    /** 오답 시 원래 상태로 돌아갑니다. */
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

//package com.example.curator_temi_test;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.os.Bundle;
//import android.util.TypedValue;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class QuizActivity extends AppCompatActivity {
//
//    private int correctAnswerIndex = 2; // 두 번째 버튼이 정답
//    private ImageButton[] buttons;
//    private LinearLayout quizOptions;
//    private RelativeLayout rootLayout;
//    private ImageView resultImage;
//    private ImageButton actionButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.quiz_1);
//
//        rootLayout = findViewById(R.id.rootLayout);
//        quizOptions = findViewById(R.id.quizOptions);
//
//        ImageButton img1 = findViewById(R.id.img1);
//        ImageButton img2 = findViewById(R.id.img2);
//        ImageButton img3 = findViewById(R.id.img3);
//        ImageButton img4 = findViewById(R.id.img4);
//        buttons = new ImageButton[]{img1, img2, img3, img4};
//
//        resultImage = new ImageView(this);
//        RelativeLayout.LayoutParams resultParams = new RelativeLayout.LayoutParams(1920, 1200);
//        resultParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        resultImage.setLayoutParams(resultParams);
//        resultImage.setScaleType(ImageView.ScaleType.FIT_XY);
//        resultImage.setVisibility(View.GONE);
//        rootLayout.addView(resultImage);
//
//        View.OnClickListener listener = v -> {
//            int selectedIndex = -1;
//            if (v.getId() == R.id.img1) selectedIndex = 1;
//            else if (v.getId() == R.id.img2) selectedIndex = 2;
//            else if (v.getId() == R.id.img3) selectedIndex = 3;
//            else if (v.getId() == R.id.img4) selectedIndex = 4;
//
//            boolean isCorrect = selectedIndex == correctAnswerIndex;
//            showResult(isCorrect, selectedIndex);
//        };
//
//        for (ImageButton button : buttons) {
//            button.setOnClickListener(listener);
//        }
//    }
//
//    private void showResult(boolean isCorrect, int selectedIndex) {
//        // 버튼 비활성화 및 선택되지 않은 보기 회색 처리
//        for (int i = 0; i < buttons.length; i++) {
//            buttons[i].setEnabled(false);
//            if ((i + 1) != selectedIndex) {
//                buttons[i].setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
//                buttons[i].setScaleX(0.8f);
//                buttons[i].setScaleY(0.8f);
//            }
//        }
//
//        // 결과 이미지 설정 (1920x1200px 전체 화면에 오버레이)
//        resultImage.setImageResource(isCorrect ? R.drawable.ooo : R.drawable.xxx);
//        resultImage.setVisibility(View.VISIBLE);
//        resultImage.bringToFront(); // 가장 위로
//
//        if (actionButton != null) {
//            rootLayout.removeView(actionButton);
//        }
//
//        // 다음 또는 다시 버튼 생성 (394x110px, 위치 수동 조정)
//        actionButton = new ImageButton(this);
//
//// 버튼 크기 정의 (예: 축소된 크기)
//        int widthPx = 394; // 원래 이미지 크기
//        int heightPx = 110;
//
//// 레이아웃 파라미터 설정
//        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(widthPx, heightPx);
//        btnParams.leftMargin = 700;
//        btnParams.topMargin = 880;
//
//        actionButton.setLayoutParams(btnParams);
//        actionButton.setScaleType(ImageView.ScaleType.FIT_CENTER); // 내부 이미지 축소
//        actionButton.setImageResource(isCorrect ? R.drawable.next_quiz : R.drawable.re_quiz);
//
//// 배경 제거 및 padding 제거
//        actionButton.setBackgroundColor(Color.TRANSPARENT);  // background 제거
//        actionButton.setPadding(0, 0, 0, 0); // 패딩 제거
//
//// 추가 및 동작 연결
//        rootLayout.addView(actionButton);
//        actionButton.bringToFront();;
//
//
//        // 버튼 클릭 이벤트
//        actionButton.setOnClickListener(v -> {
//            if (isCorrect) {
//                startActivity(new Intent(this, Quiz2Activity.class)); // 다음 퀴즈로 이동
//            } else {
//                // 다시 시도: 오버레이 제거 + 버튼 원상 복구
//                resultImage.setVisibility(View.GONE);
//                rootLayout.removeView(actionButton);
//                for (ImageButton btn : buttons) {
//                    btn.clearColorFilter();
//                    btn.setScaleX(1f);
//                    btn.setScaleY(1f);
//                    btn.setEnabled(true);
//                }
//            }
//        });
//    }
//}
