package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // 마이페이지 버튼 연결
        Button btnMyPage = findViewById(R.id.btnMyPage);
        btnMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MyPageActivity로 이동
                Intent intent = new Intent(SecondActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
