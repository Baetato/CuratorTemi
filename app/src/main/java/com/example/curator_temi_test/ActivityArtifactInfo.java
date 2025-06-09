package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

public class ActivityArtifactInfo extends AppCompatActivity {

    private Robot robot;
    private DatabaseReference curatorCountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_info);

        // 1) Intent 로부터 유물 이름 가져오기
        String artifactName = getIntent().getStringExtra("artifact_name");
        if (artifactName == null) artifactName = "사도세자_귀주";
        // getArtifactDescription 함수 사용안함 주석처리
        // String desc = getArtifactDescription(name);

        // 2) 화면 요소 바인딩 (새 UI에 맞춘 ID)
        TextView    tvTitle       = findViewById(R.id.tv_secret_title);
        ImageView   ivArtifact    = findViewById(R.id.iv_artifact);
        TextView    tvDescription = findViewById(R.id.tv_description);
        ImageButton btnNext       = findViewById(R.id.btn_next);
        Button      btnExplain    = findViewById(R.id.buttonExplain);
        Button      btnBack       = findViewById(R.id.buttonBack);

        // 3) Temi & Firebase 초기화
        robot            = Robot.getInstance();
        curatorCountRef  = FirebaseDatabase.getInstance().getReference("curatorCount");

        // 4) 유물별 리소스 매핑
        int    imgRes;
        String titleText, descText;
        switch (artifactName) {
            case "사도세자_뒤주":
                imgRes    = R.drawable.box;
                titleText = "[비밀 1] 조선의 비극: 사도세자의 뒤주";
                descText  = "지금 당신 앞에 놓인 건 조선의 왕세자 사도세자가 죽음을 맞이한 뒤주입니다.\n"
                        + "어떤 이는 그를 광인이라 하고, 어떤 이는 정치적 희생자라 했지요.\n"
                        + "진실은 봉인됐지만… 그 단서를 풀 수 있다면, 다음 비밀에 도달할 수 있답니다.";
                break;
            case "선덕여왕":
                imgRes    = R.drawable.queen;
                titleText = "[비밀 2] 신라의 여왕: 선덕과 하늘의 대화";
                descText  = "선덕여왕은 하늘의 별을 읽으며 백성을 다스렸습니다.\n"
                        + "첨성대는 그 시대 과학의 결정체였죠.\n"
                        + "그녀가 정말 미래를 꿰뚫어 봤을까요?\n"
                        + "진실은 봉인됐지만… 그 단서를 풀 수 있다면, 다음 비밀에 도달할 수 있답니다.";
                break;
            case "무령왕릉":
                imgRes    = R.drawable.dead;
                titleText = "[비밀 3] 봉인된 왕릉: 무령왕의 재발견";
                descText  = "500년 동안 잠들어 있던 무령왕릉은 뜻밖의 우연으로 발견됐습니다.\n"
                        + "왕의 무덤에 적힌 생몰일자, 그것은 한국 고고학의 놀라운 전환점이었죠.\n"
                        + "진실은 봉인됐지만… 그 단서를 풀 수 있다면, 다음 비밀에 도달할 수 있답니다.";
                break;
            case "팔만대장경":
                imgRes    = R.drawable.dead;
                titleText = "[비밀 4] 새겨진 저항: 고려의 기도";
                descText  = "불타는 절망 속에서도, 고려는 기록을 포기하지 않았습니다.\n"
                        + "팔만 장의 경판, 그것은 단지 종교의 언어가 아니었죠.\n"
                        + "몽골의 침입에 맞선, 나무에 새긴 조용한 투쟁.\n"
                        + "진실은 봉인됐지만… 그 단서를 풀 수 있다면, 다음 비밀에 도달할 수 있답니다.";
                break;
            default:
                imgRes    = R.drawable.box;
                titleText = "[비밀 1] 조선의 비극: 사도세자의 뒤주";
                descText  = "설명 없음";
        }

        // 5) UI에 세팅
        tvTitle      .setText(titleText);
        ivArtifact   .setImageResource(imgRes);
        tvDescription.setText(descText);

        // 6) 자동 TTS 재생 (도착 즉시)
        robot.speak(TtsRequest.create(descText, false)); // 자동재생

        // 7) “설명 듣기” 버튼 → curatorCount 0→1 전송 & TTS
        btnExplain.setOnClickListener(v -> {
            // 설명 시작 플래그
            curatorCountRef.setValue(0);
            // TTS 재생
            robot.speak(TtsRequest.create("작품 설명을 시작합니다" + descText, true));
            // 설명 완료 플래그
            curatorCountRef.setValue(1);
        });

        // 8) “다음 퀴즈” 버튼 → QuizActivity로 이동
        btnNext.setOnClickListener(v -> {
            Intent it = new Intent(this, QuizActivity.class);
            it.putExtra("artifact_name", artifactName);
            startActivity(it);
            finish();
        });

        // 9) 뒤로가기
        btnBack.setOnClickListener(v -> finish());
    }
}

//6월 9일 테스트 코드 (UI 미완 - Temi 테스트 완)
//package com.example.curator_temi_test;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import com.robotemi.sdk.TtsRequest;
//import com.robotemi.sdk.Robot;
//import android.widget.Button;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class ActivityArtifactInfo extends Activity {
//
//    private Robot robot;
//    private String artifactName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_artifact_info);
//
//        // 하드코딩 부분
//        String name = getIntent().getStringExtra("artifact_name");
//        String desc = getArtifactDescription(name);
//        robot = Robot.getInstance();
//        robot.speak(TtsRequest.create(desc, false));  // 자동 재생
//
//        Button btnExplain = findViewById(R.id.buttonExplain);
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference("curatorCount");
//        btnExplain.setOnClickListener(v -> {
//            ref.setValue(0);
//            robot.speak(TtsRequest.create("작품 설명을 시작합니다...지금 당신 앞에 놓인 건 조선의 왕세자가 죽음을 맞이한 뒤주입니다.\n" +
//                    "어떤 그를 이는 광인이라 하고, 어떤 이는 정치의 희생자라 했지요.\n" +
//                    "진실은 봉인됐지만... 그 단서를 모험가 당신이 풀 수 있다면,\n" +
//                    "다음 비밀에 도달할 수 있답니다.", true));
//            ref.setValue(1);
        //
//
////        robot = Robot.getInstance();
////        artifactName = getIntent().getStringExtra("artifact_name");
////
////        // 자동 TTS
////        String desc = getArtifactDescription(artifactName);
////        robot.speak(TtsRequest.create(desc, false));
//
//        // UI 세팅
//        TextView tvTitle = findViewById(R.id.tv_title);
//        TextView tvBody = findViewById(R.id.tv_body);
//        tvTitle.setText(artifactName);
//        tvBody.setText(desc);
//        });
//        // 설명 듣기 버튼
////        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("curatorCount");
////        Button btnExplain = findViewById(R.id.buttonExplain);
////        btnExplain.setOnClickListener(v -> {
////            ref.setValue(0);
////            robot.speak(TtsRequest.create("설명을 시작합니다.", true));
////            ref.setValue(1);
////        });
//
//        // 뒤로가기
//        Button btnBack = findViewById(R.id.buttonBack);
//        btnBack.setOnClickListener(v -> finish());
//
////        String description = getArtifactDescription(artifactName);
////        TtsRequest request = TtsRequest.create(description, false);
////
////        Log.d("TTS_DEBUG", "설명 시작: " + description);
////
////        // TTS 자동재생
////        robot.speak(request);
////
////        // UI 업데이트
////        TextView tvTitle = findViewById(R.id.tv_title);
////        TextView tvBody = findViewById(R.id.tv_body);
////
////        if ("사도세자_뒤주".equals(artifactName)) {
////            tvTitle.setText("조선의 비극 : 사도세자의 뒤주");
////        } else {
////            tvTitle.setText("유물 정보");
////        }
////
////        tvBody.setText(description);
//    }
//
//    private String getArtifactDescription(String name) {
//        // 간단 예시
//        if ("사도세자_뒤주".equals(name)) {
//            return "사도세자의 비극적 이야기...";
//        }
//        return "설명 없음";
//    }
//}