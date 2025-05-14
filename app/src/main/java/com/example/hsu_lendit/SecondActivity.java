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
            System.out.println("btnMyPage 클릭 시 데이터 확인:");
            System.out.println("rentedItems: " + rentedItems);
            System.out.println("rentalDates: " + rentalDates);
            System.out.println("rentalQuantities: " + rentalQuantities);

            Intent myPageIntent = new Intent(SecondActivity.this, MyPageActivity.class);
            myPageIntent.putStringArrayListExtra("itemNames", rentedItems);
            myPageIntent.putStringArrayListExtra("rentalDates", rentalDates);
            myPageIntent.putStringArrayListExtra("rentalQuantities", rentalQuantities);
            startActivity(myPageIntent);
        });

        setupRentalButtons();
    }

    private void setupRentalButtons() {
        Button btnRentScissors = findViewById(R.id.btnRentScissors);
        btnRentScissors.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RentScissorsActivity.class);
            startActivityForResult(intent, 1);
        });

        Button btnRentLabtop = findViewById(R.id.btnRentLaptop);
        btnRentLabtop.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RentLaptopActivity.class);
            startActivity(intent);
        });

        Button btnRentMouse = findViewById(R.id.btnRentMouse);
        btnRentMouse.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, OutOfStockActivity.class);
            startActivity(intent);
        });

        Button btnRentEarphone = findViewById(R.id.btnRentEarphone);
        btnRentEarphone.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RentEarphoneActivity.class);
            startActivity(intent);
        });

        Button btnRentUmbrella = findViewById(R.id.btnRentUmbrella);
        btnRentUmbrella.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RentUmbrellaActivity.class);
            startActivity(intent);
        });

        Button btnRentCharger = findViewById(R.id.btnRentCharger);
        btnRentCharger.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RentChargerActivity.class);
            startActivity(intent);
        });

        Button btnRentTablet = findViewById(R.id.btnRentTablet);
        btnRentTablet.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, RentTabletActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String returnedItem = data.getStringExtra("returnedItem");
            String itemType = data.getStringExtra("itemType");

            if (returnedItem != null && itemType != null) {
                handleReturnedItem(returnedItem, itemType);
                System.out.println("onActivityResult 후 rentedItems: " + rentedItems);
                System.out.println("onActivityResult 후 rentalDates: " + rentalDates);
                System.out.println("onActivityResult 후 rentalQuantities: " + rentalQuantities);
                return;
            }

            String itemName = data.getStringExtra("itemName");
            String rentalDate = data.getStringExtra("rentalDate");
            String rentalQuantityString = data.getStringExtra("rentalQuantity");

            System.out.println("onActivityResult 데이터 확인:");
            System.out.println("itemName: " + itemName);
            System.out.println("rentalDate: " + rentalDate);
            System.out.println("rentalQuantity: " + rentalQuantityString);

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
        int index = rentedItems.indexOf(returnedItem);
        if (index != -1) {
            rentedItems.remove(index);
            rentalDates.remove(index);
            rentalQuantities.remove(index);
            System.out.println("반납 성공: " + returnedItem + " 제거됨");
        } else {
            System.out.println("반납 실패: " + returnedItem + "이 목록에 없음");
        }

        updateRemainingQuantity(itemType);
    }

    private void handleRentedItems(String itemName, String rentalDate, int rentalQuantity) {
        for (int i = 0; i < rentalQuantity; i++) {
            String rentedItemId = rentManager.rentItem(itemName);
            if (rentedItemId != null) {
                rentedItems.add(rentedItemId);
                rentalDates.add(rentalDate);
                rentalQuantities.add("1");
                System.out.println("대여 성공: " + rentedItemId);
            } else {
                System.out.println("대여 실패: 재고 부족 (" + itemName + ")");
            }
        }

        updateRemainingQuantity(itemName);
    }

    private void updateRemainingQuantity(String itemType) {
        int remainingCount = rentManager.getItemCount(itemType);

        if ("가위".equals(itemType)) {
            TextView scissorsRemaining = findViewById(R.id.tvScissorsRemaining);
            if (scissorsRemaining != null) {
                scissorsRemaining.setText("남은 수량: " + remainingCount);
            } else {
                System.out.println("Error: tvScissorsRemaining 찾을 수 없음");
            }
        }

        System.out.println("UI 업데이트 완료: " + itemType + " 남은 수량 = " + remainingCount);
    }
}
