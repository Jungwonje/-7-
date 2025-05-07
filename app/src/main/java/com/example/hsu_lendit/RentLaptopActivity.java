package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RentLaptopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_laptop);

        // 뒤로가기 버튼
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SecondActivity로 이동 (대여 목록 페이지)
                Intent intent = new Intent(RentLaptopActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 대여 버튼
        Button btnConfirmRental = findViewById(R.id.btnConfirmRental);
        btnConfirmRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SecondActivity로 이동 (대여 목록 페이지)
                Intent intent = new Intent(RentLaptopActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
