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
    private TextView tvEmptyState; // Empty state text
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

        System.out.println("MyPageActivity로 전달된 데이터 확인:");
        System.out.println("itemNames: " + itemNames);
        System.out.println("rentalDates: " + rentalDates);
        System.out.println("rentalQuantities: " + rentalQuantities);

        if (itemNames == null || rentalDates == null || rentalQuantities == null || itemNames.isEmpty()) {
            System.out.println("데이터가 없거나 비어 있습니다.");
            return; // 데이터가 없으면 종료
        }

        for (int i = 0; i < itemNames.size(); i++) {
            System.out.println("Adding item to UI: " + itemNames.get(i));
            addRentedItem(itemContainer, itemNames.get(i), rentalDates.get(i), rentalQuantities.get(i));
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

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
            String itemId = itemName.split(":")[1]; // 고유번호 추출
            rentManager.returnItem(itemName.split("-")[0], itemId); // 반납
            itemContainer.removeView(itemView); // MyPage에서 항목 제거
            updateEmptyState(itemContainer); // 빈 상태 업데이트
        });

        itemContainer.addView(itemView);
        updateEmptyState(itemContainer); // 빈 상태 업데이트
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
            e.printStackTrace();
            return "날짜 오류";
        }
    }

    // 빈 상태 텍스트뷰 업데이트
    private void updateEmptyState(LinearLayout itemContainer) {
        if (itemContainer.getChildCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE); // 빈 상태 텍스트뷰 표시
        } else {
            tvEmptyState.setVisibility(View.GONE); // 빈 상태 텍스트뷰 숨기기
        }
    }
}
