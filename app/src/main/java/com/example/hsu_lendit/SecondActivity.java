package com.example.hsu_lendit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private RentManager rentManager; // 싱글톤 RentManager
    private ArrayList<String> rentedItems = new ArrayList<>();
    private ArrayList<String> rentalDates = new ArrayList<>();
    private ArrayList<String> rentalQuantities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rentManager = RentManager.getInstance(); // 싱글톤 인스턴스 가져오기

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
            startActivityForResult(myPageIntent, 2); // requestCode 2로 변경
        });

        updateRentalButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRentalButtons(); // 버튼 상태 업데이트
        updateAllRemainingQuantities(); // 남은 수량 업데이트
    }

    private void updateRentalButtons() {
        Button btnRentScissors = findViewById(R.id.btnRentScissors);

        // 가위 대여 버튼 동작 설정
        btnRentScissors.setOnClickListener(v -> {
            int scissorsStock = rentManager.getItemCount("가위"); // 가위의 최신 남은 수량 확인

            if (scissorsStock == 0) {
                // 남은 수량이 0일 경우: OutOfStockActivity로 이동
                Intent intent = new Intent(SecondActivity.this, OutOfStockActivity.class);
                startActivity(intent);
            } else {
                // 남은 수량이 있을 경우: RentScissorsActivity로 이동
                Intent intent = new Intent(SecondActivity.this, RentScissorsActivity.class);
                startActivityForResult(intent, 1); // requestCode 1
            }
        });

        // 다른 대여 버튼 설정
        setButtonAction(R.id.btnRentLaptop, RentLaptopActivity.class);
        setButtonAction(R.id.btnRentMouse, OutOfStockActivity.class);
        setButtonAction(R.id.btnRentEarphone, RentEarphoneActivity.class);
        setButtonAction(R.id.btnRentUmbrella, RentUmbrellaActivity.class);
        setButtonAction(R.id.btnRentCharger, RentChargerActivity.class);
        setButtonAction(R.id.btnRentTablet, RentTabletActivity.class);
    }

    private void setButtonAction(int buttonId, Class<?> activityClass) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, activityClass);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) { // 대여 처리
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
            } else if (requestCode == 2) { // 반납 처리
                String returnedItem = data.getStringExtra("returnedItem");
                String itemType = data.getStringExtra("itemType");

                if (returnedItem != null && itemType != null) {
                    handleReturnedItem(returnedItem, itemType);

                    // SecondActivity 데이터를 최신화
                    updateAllRemainingQuantities(); // 남은 수량 업데이트
                }
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
        System.out.println("대여 처리 시작: itemName=" + itemName + ", rentalDate=" + rentalDate + ", rentalQuantity=" + rentalQuantity);

        int successfullyRented = 0;

        for (int i = 0; i < rentalQuantity; i++) {
            String rentedItemId = rentManager.rentItem(itemName, rentalDate); // 대여 날짜 전달
            if (rentedItemId != null) {
                rentedItems.add(rentedItemId); // 정확한 이름 추가 (예: "가위-B")
                rentalDates.add(rentalDate);
                rentalQuantities.add("1");
                successfullyRented++;
                System.out.println("대여 성공: " + rentedItemId + ", 대여 날짜: " + rentalDate);
            } else {
                System.out.println("대여 실패: 재고 부족 (" + itemName + ")");
                break;
            }
        }

        System.out.println("총 대여 성공 수량: " + successfullyRented);
        updateRemainingQuantity(itemName); // UI 업데이트
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

    private void updateAllRemainingQuantities() {
        updateRemainingQuantity("가위");
        updateRemainingQuantity("노트북");
        updateRemainingQuantity("마우스");
        updateRemainingQuantity("이어폰");
        updateRemainingQuantity("우산");
        updateRemainingQuantity("충전기");
        updateRemainingQuantity("태블릿");
    }
}