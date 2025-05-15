package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RentItemActivity extends AppCompatActivity {
    private RentManager rentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_item);

        rentManager = RentManager.getInstance(); // 싱글톤 RentManager 가져오기

        // Retrieve rental data from intent
        String itemName = getIntent().getStringExtra("itemName");

        // Validate itemName format
        if (itemName == null || !itemName.matches("^[가위|노트북|우산|충전기|마우스|이어폰|태블릿]-[A-Z]$")) {
            Toast.makeText(this, "올바르지 않은 물품 이름입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvItemName = findViewById(R.id.tvItemName);
        EditText etRentalDate = findViewById(R.id.etRentalDate);
        TextView tvTotalTime = findViewById(R.id.tvTotalTime);
        TextView tvRemainingTime = findViewById(R.id.tvRemainingTime);
        Button btnComplete = findViewById(R.id.btnComplete);

        tvItemName.setText(itemName);

        btnComplete.setOnClickListener(v -> {
            String rentalDate = etRentalDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);

            try {
                sdf.parse(rentalDate); // 날짜 검증
            } catch (ParseException e) {
                Toast.makeText(this, "올바른 날짜 형식을 입력하세요. (예: 2025-05-14)", Toast.LENGTH_SHORT).show();
                return;
            }

            // RentManager에서 대여 처리
            String rentedItemId = rentManager.rentItem(itemName, rentalDate);
            if (rentedItemId == null) {
                Toast.makeText(this, "대여 실패: 재고 부족", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass data back to parent activity
            Intent intent = new Intent();
            intent.putExtra("itemName", rentedItemId); // 정확한 아이템 ID 전달
            intent.putExtra("rentalDate", rentalDate);
            intent.putExtra("totalTime", "7일");
            intent.putExtra("remainingTime", "7일");
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}