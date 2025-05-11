package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyPageActivity extends AppCompatActivity {
    private TextView tvEmptyState; // 빈 상태 텍스트뷰
    private RentManager rentManager; // RentManager 객체 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        rentManager = new RentManager(); // RentManager 초기화

        LinearLayout itemContainer = findViewById(R.id.itemContainer);
        tvEmptyState = findViewById(R.id.tvEmptyState); // 빈 상태 텍스트뷰

        Intent intent = getIntent();
        ArrayList<String> itemNames = intent.getStringArrayListExtra("itemNames");
        ArrayList<String> rentalDates = intent.getStringArrayListExtra("rentalDates");
        ArrayList<String> rentalQuantities = intent.getStringArrayListExtra("rentalQuantities");

        // 데이터 확인 로그
        System.out.println("MyPageActivity로 전달된 데이터 확인:");
        System.out.println("itemNames: " + itemNames);
        System.out.println("rentalDates: " + rentalDates);
        System.out.println("rentalQuantities: " + rentalQuantities);

        if (itemNames == null || rentalDates == null || rentalQuantities == null || itemNames.isEmpty()) {
            System.out.println("데이터가 없거나 비어 있습니다.");
            updateEmptyState(itemContainer); // 빈 상태 업데이트
            return; // 데이터가 없으면 종료
        }

        for (int i = 0; i < itemNames.size(); i++) {
            System.out.println("Adding item to UI: " + itemNames.get(i));
            addRentedItem(itemContainer, itemNames.get(i), rentalDates.get(i), rentalQuantities.get(i));
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        updateEmptyState(itemContainer); // 빈 상태 업데이트
    }

    // 대여 항목 추가 메서드
    private void addRentedItem(LinearLayout itemContainer, String itemName, String rentalDate, String rentalQuantity) {
        System.out.println("addRentedItem 호출: " + itemName);

        View itemView = getLayoutInflater().inflate(R.layout.item_rented, null);

        ((TextView) itemView.findViewById(R.id.tvItemName)).setText("물품명: " + itemName);
        ((TextView) itemView.findViewById(R.id.tvRentalDate)).setText("대여일자: " + rentalDate);
        ((TextView) itemView.findViewById(R.id.tvRentalQuantity)).setText("대여 수량: " + rentalQuantity);

        TextView tvRemainingTime = itemView.findViewById(R.id.tvRemainingTime);
        String remainingTime = calculateRemainingTime(rentalDate);
        tvRemainingTime.setText("남은 대여시간: " + remainingTime);

        Button btnReturn = itemView.findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(v -> {
            // itemName을 안전하게 분리
            String[] itemParts = itemName.split("-");
            if (itemParts.length < 2) {
                System.out.println("Error: Invalid itemName format: " + itemName);
                return; // 잘못된 형식이면 처리 중단
            }

            String itemType = itemParts[0]; // 예: "가위"
            String itemId = itemParts[1];  // 예: "A"

            // RentManager를 통해 반납 처리
            if (rentManager.returnItem(itemType, itemId)) { // 반납 성공 시
                System.out.println("반납 성공: " + itemName);

                // MyPage UI에서 해당 항목 제거
                itemContainer.removeView(itemView);

                // 빈 상태 업데이트
                updateEmptyState(itemContainer);

                // SecondActivity로 반납 정보를 전달
                Intent resultIntent = new Intent();
                resultIntent.putExtra("returnedItem", itemName);
                resultIntent.putExtra("itemType", itemType); // 예: "가위"
                setResult(RESULT_OK, resultIntent);

                // Activity 종료
                finish();
            } else {
                System.out.println("반납 실패: " + itemName);
            }
        });

        itemContainer.addView(itemView);
    }

    // 남은 대여 시간을 계산하는 메서드
    private String calculateRemainingTime(String rentalDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date rental = sdf.parse(rentalDate);
            Date today = new Date();
            long diff = rental.getTime() + (7 * 24 * 60 * 60 * 1000) - today.getTime(); // 7일 대여 기간
            if (diff > 0) {
                long days = diff / (24 * 60 * 60 * 1000);
                long hours = (diff / (60 * 60 * 1000)) % 24;
                return days + "일 " + hours + "시간";
            } else {
                return "기간 종료";
            }
        } catch (ParseException e) {
            System.out.println("날짜 파싱 오류: " + e.getMessage());
            e.printStackTrace();
            return "날짜 오류";
        }
    }

    // 빈 상태 텍스트뷰 업데이트
    private void updateEmptyState(LinearLayout itemContainer) {
        boolean isEmpty = itemContainer.getChildCount() == 0;
        tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        if (isEmpty) {
            System.out.println("빈 상태 텍스트뷰 표시: 대여한 항목이 없습니다.");
        } else {
            System.out.println("빈 상태 텍스트뷰 숨김: 대여한 항목이 존재합니다.");
        }
    }
}