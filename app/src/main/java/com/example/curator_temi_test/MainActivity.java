package com.example.curator_temi_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

public class MainActivity extends AppCompatActivity implements OnGoToLocationStatusChangedListener {

    private static final String TAG = "TemiFirebaseApp";

    Button buttonFollow;
    Button buttonBack;
    Robot robot;
    private DatabaseReference sensorRef;
    private String exhibitLocation = "EXIBITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ⇨ 1. 따라가기 버튼 & 뒤로가기 버튼 초기화
        buttonFollow = findViewById(R.id.buttonFollow);
        buttonBack   = findViewById(R.id.buttonBack);
        robot        = Robot.getInstance();

        // 앱 시작 시 따라가기 비활성화
        if (robot != null) robot.stopMovement();
        buttonFollow.setText("따라가기");

        buttonFollow.setOnClickListener(v -> {
            if (robot == null) {
                Toast.makeText(this, "Temi 연결 안 됨", Toast.LENGTH_SHORT).show();
                return;
            }
            if (buttonFollow.getText().equals("따라가기")) {
                robot.beWithMe(null);
                buttonFollow.setText("중지");
                Toast.makeText(this, "따라가기 시작", Toast.LENGTH_SHORT).show();
            } else {
                robot.stopMovement();
                buttonFollow.setText("따라가기");
                Toast.makeText(this, "따라가기 중지", Toast.LENGTH_SHORT).show();
            }
        });
        buttonBack.setOnClickListener(v -> finish());

        // ⇨ 2. Firebase 센서 리스너 → Temi 이동
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        sensorRef = database.getReference("sensor").child("sensor3");
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot ds) {
                Integer sensorValue = ds.getValue(Integer.class);
                Log.d(TAG, "sensor3=" + sensorValue);
                if (sensorValue != null && sensorValue == 1 && robot != null) {
                    robot.goTo(exhibitLocation);
                    Toast.makeText(MainActivity.this, "전시품 앞으로 이동", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError e) {
                Log.w(TAG, "sensor3 read failed", e.toException());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ⇨ 3. goTo 상태 리스너 등록
        if (robot != null) {
            robot.addOnGoToLocationStatusChangedListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (robot != null) {
            robot.removeOnGoToLocationStatusChangedListener(this);
        }
    }

    // ⇨ 3. goTo 상태 콜백
    @Override
    public void onGoToLocationStatusChanged(@NonNull String location,
                                            @NonNull String status,
                                            int descriptionId,
                                            @NonNull String description) {
        Log.d(TAG, "status=" + status + " desc=" + description);
        switch (status) {
            case "start":
                Toast.makeText(this, location + "으로 이동 시작", Toast.LENGTH_SHORT).show();
                break;
            case "calculating":
                Toast.makeText(this, "경로 계산 중...", Toast.LENGTH_SHORT).show();
                break;
            case "going":
                // 로봇이 목표 지점으로 이동 중
                break;
            case "complete":
                Toast.makeText(this, location + " 도착 완료!", Toast.LENGTH_SHORT).show();
                // 도착 후 추가 동작 (예: 전시품 설명 TtsRequest)
                if (location.equals(exhibitLocation)) {
                    robot.speak(TtsRequest.create("안녕하세요! 이 전시품에 대해 설명해 드릴까요?", false));
                }
                break;
            case "abort":
                Toast.makeText(this, location + " 이동 중단됨: " + description, Toast.LENGTH_SHORT).show();
                break;
            case "obstacle_bloking_path": // 오타가 있을 수 있으니 SDK 문서 확인
                Toast.makeText(this, "경로에 장애물이 있습니다. 이동 중단됨.", Toast.LENGTH_SHORT).show();
                // 장애물 감지 시 다른 조치 (예: 다시 시도, 우회, 안내 등)
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (robot != null) robot.stopMovement();
    }
}

//package com.example.curator_temi_test;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MainActivity extends AppCompatActivity {
//
//    private boolean hasShownModeSelection = false; // 한 번만 실행되도록
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        buttonFollow = findViewById(R.id.buttonFollow);
//        buttonBack   = findViewById(R.id.buttonBack);
//        robot        = Robot.getInstance();
//
//        // 앱 시작 시 따라가기 비활성
//        robot.stopMovement();
//        buttonFollow.setText("따라가기");
//
//        // ModeSelectionActivity 먼저 띄우기
//        if (!hasShownModeSelection) {
//            hasShownModeSelection = true;
//            Intent intent = new Intent(this, ModeSelectionActivity.class);
//            startActivity(intent);
//            // MainActivity 유지
//        }
//
//        // 이후 메인 화면 구성
//        setContentView(R.layout.activity_main);
//
//        Button btnStart = findViewById(R.id.btn_start); // 유물 이동 버튼
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ActivityMoving.class);
//                intent.putExtra("artifact_name", "사도세자_뒤주"); // 유물 이름
//                startActivity(intent);
//            }
//        });
//    }
//}
