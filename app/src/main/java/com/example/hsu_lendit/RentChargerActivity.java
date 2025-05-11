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
import java.util.Calendar;
import java.util.Locale;

public class RentChargerActivity extends AppCompatActivity {
    private RentManager rentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_charger);

        rentManager = new RentManager();

        EditText etRentalDate = findViewById(R.id.etRentalDate);
        EditText etRentalQuantity = findViewById(R.id.etRentalQuantity);
        TextView tvReturnDate = findViewById(R.id.tvReturnDate); // 반납날짜 표시용 TextView
        Button btnConfirmRental = findViewById(R.id.btnConfirmRental);
        Button btnBack = findViewById(R.id.btnBack); // 뒤로가기 버튼 추가

        // 뒤로가기 버튼 클릭 이벤트
        btnBack.setOnClickListener(v -> {
            // 현재 액티비티 종료 (이전 화면으로 돌아감)
            finish();
        });

        // 대여일자 입력 시 반납날짜 자동 계산
        etRentalDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String rentalDate = etRentalDate.getText().toString();
                if (!rentalDate.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(sdf.parse(rentalDate));
                        calendar.add(Calendar.DAY_OF_YEAR, 7); // 대여 기간 7일 추가
                        String returnDate = sdf.format(calendar.getTime());
                        tvReturnDate.setText("반납 날짜: " + returnDate); // 반납날짜 표시
                    } catch (ParseException e) {
                        Toast.makeText(this, "날짜 형식이 잘못되었습니다. 예: 2025-05-11", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 대여 버튼 클릭 이벤트
        btnConfirmRental.setOnClickListener(v -> {
            String rentalDate = etRentalDate.getText().toString();
            String rentalQuantityStr = etRentalQuantity.getText().toString();

            if (rentalDate.isEmpty() || rentalQuantityStr.isEmpty()) {
                Toast.makeText(this, "날짜와 수량을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            int rentalQuantity = Integer.parseInt(rentalQuantityStr);
            int availableQuantity = rentManager.getItemCount("충전기");

            if (rentalQuantity > availableQuantity) {
                Toast.makeText(this, "재고가 부족합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < rentalQuantity; i++) {
                rentManager.rentItem("충전기");
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("itemName", "충전기");
            resultIntent.putExtra("rentalDate", rentalDate);
            resultIntent.putExtra("rentalQuantity", rentalQuantityStr);
            resultIntent.putExtra("returnDate", tvReturnDate.getText().toString().replace("반납 날짜: ", ""));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}