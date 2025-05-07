package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // 뒤로가기 버튼
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SecondActivity로 이동 (대여 목록 페이지)
                Intent intent = new Intent(MyPageActivity.this, SecondActivity.class);
                startActivity(intent);
                finish(); // 현재 마이페이지 종료
            }
        });

        // 반납하기 버튼
        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SecondActivity로 이동 (대여 목록 페이지)
                Intent intent = new Intent(MyPageActivity.this, SecondActivity.class);
                startActivity(intent);
                finish(); // 현재 마이페이지 종료
            }
        });

        // 목록 버튼
        Button btnList = findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SecondActivity로 이동 (대여 목록 페이지)
                Intent intent = new Intent(MyPageActivity.this, SecondActivity.class);
                startActivity(intent);
                finish(); // 현재 마이페이지 종료
            }
        });

    }
}
