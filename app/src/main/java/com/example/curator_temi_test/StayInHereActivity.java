package com.example.curator_temi_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

public class StayInHereActivity extends AppCompatActivity implements OnGoToLocationStatusChangedListener {

    private Robot robot;
    private DatabaseReference sensorRef;
    private String currentTarget = null;
    private Button buttonYes, buttonNo;
    private TextView questionText;
    private boolean isAwaitingResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_here);

        robot = Robot.getInstance();
        robot.addOnGoToLocationStatusChangedListener(this);

        sensorRef = FirebaseDatabase.getInstance().getReference("sensor");

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        questionText = findViewById(R.id.questionText);

        // 추가: buttonBack 연결 및 클릭 처리
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModeSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // 버튼 처음엔 비활성화
        setQuestionUIVisibility(false);

        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (currentTarget != null || isAwaitingResponse) return;

                for (int i = 1; i <= 2; i++) {
                    String key = "sensor" + i;
                    Integer value = snapshot.child(key).getValue(Integer.class);
                    sensorRef.child("sensor1").setValue(-1);
                    if (value != null && value == 1) {
                        currentTarget = mapSensorToArtifact(key);
                        if (currentTarget != null) {
                            robot.goTo(currentTarget);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("StayInHereActivity", "Firebase read failed: " + error.getMessage());
            }
        });

        buttonYes.setOnClickListener(v -> {
            if (currentTarget != null) {
                Intent intent = new Intent(this, ActivityArtifactInfo2.class);
                intent.putExtra("artifact_name", currentTarget);
                startActivity(intent);
                resetState();
            }
        });

        buttonNo.setOnClickListener(v -> {
            robot.goTo("Here");
            resetState();
        });
    }

    private void resetState() {
        currentTarget = null;
        isAwaitingResponse = false;
        setQuestionUIVisibility(false);
    }

    private void setQuestionUIVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        buttonYes.setVisibility(visibility);
        buttonNo.setVisibility(visibility);
        questionText.setVisibility(visibility);
    }

    private String mapSensorToArtifact(String sensorKey) {
        switch (sensorKey) {
            case "sensor1":
                return "사도세자_뒤주";
            case "sensor2":
                return "선덕여왕";
            default:
                return null;
        }
    }

    @Override
    public void onGoToLocationStatusChanged(@NonNull String location, @NonNull String status, int descriptionId, @NonNull String description) {
        if (status.equals("complete") && location.equals(currentTarget)) {
            runOnUiThread(() -> {
                isAwaitingResponse = true;
                setQuestionUIVisibility(true);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (robot != null) {
            robot.stopMovement();
            robot.removeOnGoToLocationStatusChangedListener(this);
        }
    }
}
