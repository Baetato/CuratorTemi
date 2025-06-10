package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

public class ActivityArtifactInfo2 extends AppCompatActivity implements OnGoToLocationStatusChangedListener {

    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_info2);

        String artifactName = getIntent().getStringExtra("artifact_name");
        String descText = getDescriptionByArtifact(artifactName);

        robot = Robot.getInstance();
        robot.addOnGoToLocationStatusChangedListener(this);

        // 자동 설명 TTS
        robot.speak(TtsRequest.create(descText, false));

        // 일정 시간 후 Here로 복귀
        new android.os.Handler().postDelayed(() -> {
            robot.goTo("Here");
        }, descText.length() * 100); // 문자당 100ms
    }

    @Override
    public void onGoToLocationStatusChanged(@NonNull String location, @NonNull String status, int descriptionId, @NonNull String description) {
        if (status.equals("complete") && location.equals("Here")) {
            robot.removeOnGoToLocationStatusChangedListener(this);
            Intent intent = new Intent(this, StayInHereActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // 설명 액티비티 종료
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robot.removeOnGoToLocationStatusChangedListener(this);
    }

    private String getDescriptionByArtifact(String artifactName) {
        switch (artifactName) {
            case "사도세자_뒤주":
                return "지금 당신 앞에 놓인 건 조선의 왕세자 사도세자가 죽음을 맞이한 뒤주입니다...";
            case "선덕여왕":
                return "선덕여왕은 하늘의 별을 읽으며 백성을 다스렸습니다...";
            default:
                return "설명 없음";
        }
    }
}

