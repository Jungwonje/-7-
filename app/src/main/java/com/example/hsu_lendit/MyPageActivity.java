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
import java.util.List;
import java.util.Locale;

public class MyPageActivity extends AppCompatActivity {
    private TextView tvEmptyState; // 빈 상태 텍스트뷰
    private RentManager rentManager; // RentManager 객체 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        rentManager = RentManager.getInstance(); // RentManager 싱글톤 인스턴스 가져오기

        LinearLayout itemContainer = findViewById(R.id.itemContainer);
        tvEmptyState = findViewById(R.id.tvEmptyState); // 빈 상태 텍스트뷰

        // 초기 데이터 로드
        loadRentalData(itemContainer);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // MyPageActivity 재개 시 데이터 다시 로드
        LinearLayout itemContainer = findViewById(R.id.itemContainer);
        itemContainer.removeAllViews(); // 기존 UI 초기화
        loadRentalData(itemContainer);  // 최신 데이터로 로드
    }

    private void loadRentalData(LinearLayout itemContainer) {
        List<String> itemNames = new ArrayList<>();
        List<String> rentalDates = new ArrayList<>();

        // RentManager에서 대여 상태 가져오기
        List<String> logs = new ArrayList<>();
        for (String itemType : rentManager.getAllItemCounts().keySet()) {
            logs.addAll(rentManager.getRentalLogs(itemType)); // 모든 항목의 대여 로그 추가
        }

        for (String log : logs) {
            if (log.startsWith("대여:")) { // 대여 기록만 필터링
                String[] parts = log.split(" ");
                String itemName = parts[1]; // 예: "가위-A"
                String rentalDate = log.substring(log.indexOf("(") + 1, log.indexOf(")")); // 괄호 안의 날짜만 추출

                if (!itemNames.contains(itemName)) { // 중복 방지
                    itemNames.add(itemName);
                    rentalDates.add(rentalDate); // 정확한 날짜 추가
                }
            }
        }

        System.out.println("MyPageActivity로 전달된 데이터 확인:");
        System.out.println("itemNames: " + itemNames);
        System.out.println("rentalDates: " + rentalDates);

        itemContainer.removeAllViews(); // 기존 UI 초기화

        if (itemNames.isEmpty()) {
            updateEmptyState(itemContainer);
            return;
        }

        for (int i = 0; i < itemNames.size(); i++) {
            String itemName = itemNames.get(i);
            String rentalDate = rentalDates.get(i);
            addRentedItem(itemContainer, itemName, rentalDate, "1");
        }

        updateEmptyState(itemContainer);
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
            String[] itemParts = itemName.split("-");
            if (itemParts.length < 2) {
                System.out.println("Error: Invalid itemName format: " + itemName);
                return; // 잘못된 형식이면 처리 중단
            }

            String itemType = itemParts[0];
            String itemId = itemParts[1];

            // RentManager를 통해 반납 처리
            if (rentManager.returnItem(itemType, itemId)) {
                System.out.println("반납 성공: " + itemName);

                // MyPage UI에서 해당 항목 제거
                itemContainer.removeView(itemView);

                // 빈 상태 업데이트
                updateEmptyState(itemContainer);

                // SecondActivity로 반납 정보를 전달
                Intent resultIntent = new Intent();
                resultIntent.putExtra("returnedItem", itemName);
                resultIntent.putExtra("itemType", itemType);
                setResult(RESULT_OK, resultIntent);
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
            // 괄호 제거 및 날짜 파싱
            String sanitizedDate = rentalDate.replace("(", "").replace(")", ""); // 괄호 제거
            Date rental = sdf.parse(sanitizedDate); // 대여일자 파싱
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