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
            System.out.println("Intent로 전달할 데이터 확인:");
            System.out.println("rentedItems: " + rentedItems);
            System.out.println("rentalDates: " + rentalDates);
            System.out.println("rentalQuantities: " + rentalQuantities);

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
            String rentalQuantityString = data.getStringExtra("rentalQuantity"); // String으로 가져옴

            System.out.println("onActivityResult 데이터 확인:");
            System.out.println("itemName: " + itemName);
            System.out.println("rentalDate: " + rentalDate);
            System.out.println("rentalQuantity: " + rentalQuantityString);

            // rentalQuantity를 String에서 int로 변환
            int rentalQuantity = 0;
            try {
                rentalQuantity = Integer.parseInt(rentalQuantityString);
            } catch (NumberFormatException e) {
                System.out.println("rentalQuantity 변환 오류: " + e.getMessage());
            }

            if (itemName != null && rentalDate != null && rentalQuantity > 0) {
                for (int i = 0; i < rentalQuantity; i++) {
                    String rentedItemId = rentManager.rentItem(itemName);
                    if (rentedItemId != null) {
                        rentedItems.add(rentedItemId); // 고유번호 추가
                        rentalDates.add(rentalDate);
                        rentalQuantities.add("1");
                    } else {
                        System.out.println("대여 실패: 재고가 부족합니다.");
                    }
                }
                updateRemainingQuantity(itemName); // 남은 수량 업데이트
            }
        }
    }

    // 물품의 남은 수량을 업데이트하는 메서드
    private void updateRemainingQuantity(String itemName) {
        int remainingCount = rentManager.getItemCount(itemName);

        // 예: 가위의 남은 수량 텍스트뷰 업데이트
        if ("가위".equals(itemName)) {
            TextView scissorsRemaining = findViewById(R.id.tvScissorsRemaining);
            scissorsRemaining.setText("남은 수량: " + remainingCount);
        }
    }
}