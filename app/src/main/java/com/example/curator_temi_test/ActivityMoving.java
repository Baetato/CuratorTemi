package com.example.curator_temi_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import org.jetbrains.annotations.NotNull;

public class ActivityMoving extends Activity implements OnGoToLocationStatusChangedListener {

    private Robot robot;
    private String artifactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving);

        artifactName = getIntent().getStringExtra("artifact_name");

        // 백그라운드 이미지 + 텍스트뷰가 이미 XML에 있으므로
        // 버튼 뒤로가기만 연결
//        Button btnBack = findViewById(R.id.buttonBack);
//        btnBack.setOnClickListener(v -> finish());


        // 화면에 “사도세자_뒤주” 로 이동 중임을 보여줌
//        TextView tv = findViewById(R.id.tv_moving_text);
//        tv.setText(artifactName + " 로 이동 중입니다…");

        // Temi 이동 명령 & 이벤트 리스너 등록
        robot = Robot.getInstance();
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.goTo("artifact_name");

        if (artifactName != null) {
            robot.goTo(artifactName); // 유물 이름 = Temi 위치명
        }
    }

    @Override
//    public void onGoToLocationStatusChanged(@NotNull String location, @NotNull String status, int descriptionId, @NotNull String description) {
//        if (status.equals("complete")) {
////            안되면 해 볼 코드
////            startActivity(new Intent(this, ActivityArtifactInfo.class)
////                    .putExtra("artifact_name", artifactName));
////            finish();
//            Intent intent = new Intent(ActivityMoving.this, ActivityArtifactInfo.class);
//            intent.putExtra("artifact_name", artifactName);
//            startActivity(intent);
//            finish();
//        }
//    }
    public void onGoToLocationStatusChanged(String location, String status, int descId, String desc) {
        if ("complete".equals(status) && artifactName.equals(location)) {
            Intent i = new Intent(this, ActivityArtifactInfo.class);
            i.putExtra("artifact_name", artifactName);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robot.removeOnGoToLocationStatusChangedListener(this);
    }
}
