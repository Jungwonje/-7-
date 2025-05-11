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
            // 디버깅 로그 추가
            System.out.println("btnMyPage 클릭 시 데이터 확인:");
            System.out.println("rentedItems: " + rentedItems);
            System.out.println("rentalDates: " + rentalDates);
            System.out.println("rentalQuantities: " + rentalQuantities);

            // 최신 데이터를 Intent로 전달
            Intent myPageIntent = new Intent(SecondActivity.this, MyPageActivity.class);
            myPageIntent.putStringArrayListExtra("itemNames", rentedItems);
            myPageIntent.putStringArrayListExtra("rentalDates", rentalDates);
            myPageIntent.putStringArrayListExtra("rentalQuantities", rentalQuantities);
            startActivity(myPageIntent);
        });

        // 대여 버튼 설정
        setupRentalButtons();
    }

    private void setupRentalButtons() {
        // 가위 대여 버튼
        Button btnRentScissors = findViewById(R.id.btnRentScissors);
        btnRentScissors.setOnClickListener(v -> {
            Intent scissorsIntent = new Intent(SecondActivity.this, RentScissorsActivity.class);
            startActivityForResult(scissorsIntent, 1);
        });

        // 노트북 대여 버튼
        Button btnRentLabtop = findViewById(R.id.btnRentLaptop);
        btnRentLabtop.setOnClickListener(v -> {
            Intent laptopIntent = new Intent(SecondActivity.this, RentLaptopActivity.class);
            startActivity(laptopIntent);
        });

        // 마우스 대여 버튼 (수량 없음 페이지)
        Button btnRentMouse = findViewById(R.id.btnRentMouse);
        btnRentMouse.setOnClickListener(v -> {
            Intent mouseIntent = new Intent(SecondActivity.this, OutOfStockActivity.class);
            startActivity(mouseIntent);
        });

        // 이어폰 대여 버튼
        Button btnRentEarphone = findViewById(R.id.btnRentEarphone);
        btnRentEarphone.setOnClickListener(v -> {
            Intent earphoneIntent = new Intent(SecondActivity.this, RentEarphoneActivity.class);
            startActivity(earphoneIntent);
        });

        // 우산 대여 버튼
        Button btnRentUmbrella = findViewById(R.id.btnRentUmbrella);
        btnRentUmbrella.setOnClickListener(v -> {
            Intent umbrellaIntent = new Intent(SecondActivity.this, RentUmbrellaActivity.class);
            startActivity(umbrellaIntent);
        });

        // 충전기 대여 버튼
        Button btnRentCharger = findViewById(R.id.btnRentCharger);
        btnRentCharger.setOnClickListener(v -> {
            Intent chargerIntent = new Intent(SecondActivity.this, RentChargerActivity.class);
            startActivity(chargerIntent);
        });

        // 태블릿 대여 버튼
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
            // 반납된 물품인지 확인
            String returnedItem = data.getStringExtra("returnedItem");
            String itemType = data.getStringExtra("itemType"); // 예: "가위"
            if (returnedItem != null && itemType != null) {
                handleReturnedItem(returnedItem, itemType); // 반납된 아이템 처리

                // 반납 후 최신 리스트 상태 확인
                System.out.println("onActivityResult 후 rentedItems: " + rentedItems);
                System.out.println("onActivityResult 후 rentalDates: " + rentalDates);
                System.out.println("onActivityResult 후 rentalQuantities: " + rentalQuantities);
                return;
            }

            // 대여된 물품 처리
            String itemName = data.getStringExtra("itemName");
            String rentalDate = data.getStringExtra("rentalDate");
            String rentalQuantityString = data.getStringExtra("rentalQuantity");

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
                handleRentedItems(itemName, rentalDate, rentalQuantity);
            }
        }
    }

    private void handleReturnedItem(String returnedItem, String itemType) {
        // rentedItems 리스트에서 반납된 항목 제거
        int index = rentedItems.indexOf(returnedItem);
        if (index != -1) {
            rentedItems.remove(index);
            rentalDates.remove(index);
            rentalQuantities.remove(index);
            System.out.println("반납 성공: " + returnedItem + "이 rentedItems에서 제거되었습니다.");
        } else {
            System.out.println("반납 실패: " + returnedItem + "이 rentedItems 리스트에 없습니다.");
        }

        // RentManager에서 반납 처리 및 UI 업데이트
        updateRemainingQuantity(itemType); // 남은 수량 업데이트

        // 디버깅 로그 추가
        System.out.println("handleReturnedItem 후 rentedItems 상태: " + rentedItems);
        System.out.println("handleReturnedItem 후 rentalDates 상태: " + rentalDates);
        System.out.println("handleReturnedItem 후 rentalQuantities 상태: " + rentalQuantities);
    }

    private void handleRentedItems(String itemName, String rentalDate, int rentalQuantity) {
        for (int i = 0; i < rentalQuantity; i++) {
            String rentedItemId = rentManager.rentItem(itemName);
            if (rentedItemId != null) {
                rentedItems.add(rentedItemId); // 고유번호 추가
                rentalDates.add(rentalDate);
                rentalQuantities.add("1");
                System.out.println("대여 성공: " + rentedItemId);
            } else {
                System.out.println("대여 실패: 재고가 부족합니다. (물품명: " + itemName + ")");
            }
        }

        // 남은 수량 업데이트
        updateRemainingQuantity(itemName);
    }

    private void updateRemainingQuantity(String itemType) {
        int remainingCount = rentManager.getItemCount(itemType);

        // UI 업데이트: 예, "가위"의 남은 수량
        if ("가위".equals(itemType)) {
            TextView scissorsRemaining = findViewById(R.id.tvScissorsRemaining);
            if (scissorsRemaining != null) {
                scissorsRemaining.setText("남은 수량: " + remainingCount);
            } else {
                System.out.println("Error: tvScissorsRemaining 텍스트뷰를 찾을 수 없습니다.");
            }
        }

        System.out.println("UI 업데이트 완료: " + itemType + "의 남은 수량은 " + remainingCount + "개입니다.");
    }
}