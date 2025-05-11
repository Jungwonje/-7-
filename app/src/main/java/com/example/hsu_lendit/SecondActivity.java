package com.example.hsu_lendit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private RentManager rentManager;
    private ArrayList<String> rentedItems = new ArrayList<>();
    private ArrayList<String> rentalDates = new ArrayList<>();
    private ArrayList<String> rentalQuantities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rentManager = new RentManager();

        // SharedPreferences에서 사용자 데이터 가져오기
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String studentId = preferences.getString("studentId", "알 수 없음");
        String name = preferences.getString("name", "알 수 없음");

        // 환영 메시지 설정
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText(studentId + "학번, " + name + "님 환영합니다!");

        // 마이페이지 버튼
        Button btnMyPage = findViewById(R.id.btnMyPage);
        btnMyPage.setOnClickListener(v -> {
            Intent myPageIntent = new Intent(SecondActivity.this, MyPageActivity.class);
            myPageIntent.putStringArrayListExtra("itemNames", rentedItems);
            myPageIntent.putStringArrayListExtra("rentalDates", rentalDates);
            myPageIntent.putStringArrayListExtra("rentalQuantities", rentalQuantities);
            startActivity(myPageIntent);
        });

        // 가위 대여 버튼
        Button btnRentScissors = findViewById(R.id.btnRentScissors);
        btnRentScissors.setOnClickListener(v -> {
            Intent scissorsIntent = new Intent(SecondActivity.this, RentScissorsActivity.class);
            startActivityForResult(scissorsIntent, 1);
        });

        // 노트북 대여 버튼 연결
        Button btnRentLabtop = findViewById(R.id.btnRentLaptop);
        btnRentLabtop.setOnClickListener(v -> {
            Intent laptopIntent = new Intent(SecondActivity.this, RentLaptopActivity.class);
            startActivity(laptopIntent);
        });

        // 마우스 대여 버튼 연결 ( 수량 없음 페이지로 이동)
        Button btnRentMouse = findViewById(R.id.btnRentMouse);
        btnRentMouse.setOnClickListener(v -> {
            Intent mouseIntent = new Intent(SecondActivity.this, OutOfStockActivity.class);
            startActivity(mouseIntent);
        });

        // 이어폰 대여 버튼 연결
        Button btnRentEarphone = findViewById(R.id.btnRentEarphone);
        btnRentEarphone.setOnClickListener(v -> {
            Intent earphoneIntent = new Intent(SecondActivity.this, RentEarphoneActivity.class);
            startActivity(earphoneIntent);
        });

        // 우산 대여 버튼 연결
        Button btnRentUmbrella = findViewById(R.id.btnRentUmbrella);
        btnRentUmbrella.setOnClickListener(v -> {
            Intent umbrellaIntent = new Intent(SecondActivity.this, RentUmbrellaActivity.class);
            startActivity(umbrellaIntent);
        });

        // 충전기 대여 버튼 연결
        Button btnRentCharger = findViewById(R.id.btnRentCharger);
        btnRentCharger.setOnClickListener(v -> {
            Intent chargerIntent = new Intent(SecondActivity.this, RentChargerActivity.class);
            startActivity(chargerIntent);
        });

        // 태블릿 대여 버튼 연결
        Button btnRentTablet = findViewById(R.id.btnRentTablet);
        btnRentTablet.setOnClickListener(v -> {
            Intent tabletIntent = new Intent(SecondActivity.this, RentTabletActivity.class);
            startActivity(tabletIntent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String itemName = data.getStringExtra("itemName");
            String rentalDate = data.getStringExtra("rentalDate");
            String rentalQuantity = data.getStringExtra("rentalQuantity");

            if (itemName != null && rentalDate != null && rentalQuantity != null) {
                rentedItems.add(itemName);
                rentalDates.add(rentalDate);
                rentalQuantities.add(rentalQuantity);
            }
        }
    }
}