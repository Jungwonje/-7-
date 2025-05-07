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

        // 가위 대여 버튼 연결
        Button btnRentScissors = findViewById(R.id.btnRentScissors);
        btnRentScissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentScissorsActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentScissorsActivity.class);
                startActivity(intent);
            }
        });

        // 노트북 대여 버튼 연결
        Button btnRentLabtop = findViewById(R.id.btnRentLaptop);
        btnRentLabtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentLaptopActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentLaptopActivity.class);
                startActivity(intent);
            }
        });

        // 마우스 대여 버튼 연결 ( 수량 없음 페이지로 이동)
        Button btnRentMouse = findViewById(R.id.btnRentMouse);
        btnRentMouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentMouseActivity로 이동
                Intent intent = new Intent(SecondActivity.this, OutOfStockActivity.class);
                startActivity(intent);
            }
        });

        // 이어폰 대여 버튼 연결
        Button btnRentEarphone = findViewById(R.id.btnRentEarphone);
        btnRentEarphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentEarphoneActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentEarphoneActivity.class);
                startActivity(intent);
            }
        });

        // 우산 대여 버튼 연결
        Button btnRentUmbrella = findViewById(R.id.btnRentUmbrella);
        btnRentUmbrella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentUmbrellaActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentUmbrellaActivity.class);
                startActivity(intent);
            }
        });

        // 충전기 대여 버튼 연결
        Button btnRentCharger = findViewById(R.id.btnRentCharger);
        btnRentCharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentChargerActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentChargerActivity.class);
                startActivity(intent);
            }
        });

        // 태블릿 대여 버튼 연결
        Button btnRentTablet = findViewById(R.id.btnRentTablet);
        btnRentTablet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RentTabletActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentTabletActivity.class);
                startActivity(intent);
            }
        });
    }
}
