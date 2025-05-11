package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RentItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_item);

        // Retrieve rental data from intent
        String itemName = getIntent().getStringExtra("itemName");

        TextView tvItemName = findViewById(R.id.tvItemName);
        EditText etRentalDate = findViewById(R.id.etRentalDate);
        TextView tvTotalTime = findViewById(R.id.tvTotalTime);
        TextView tvRemainingTime = findViewById(R.id.tvRemainingTime);
        Button btnComplete = findViewById(R.id.btnComplete);

        tvItemName.setText(itemName);

        btnComplete.setOnClickListener(v -> {
            String rentalDate = etRentalDate.getText().toString();
            String totalTime = "7일"; // 예시: 총 대여시간
            String remainingTime = "7일"; // 초기값은 총 대여시간과 동일

            // Pass data back to MyPageActivity
            Intent intent = new Intent();
            intent.putExtra("itemName", itemName);
            intent.putExtra("rentalDate", rentalDate);
            intent.putExtra("totalTime", totalTime);
            intent.putExtra("remainingTime", remainingTime);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
