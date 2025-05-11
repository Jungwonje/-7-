package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MyPageActivity extends AppCompatActivity {
    private RentManager rentManager;
    private TextView tvEmptyState; // Empty state text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        rentManager = new RentManager(); // Initialize RentManager

        LinearLayout itemContainer = findViewById(R.id.itemContainer);
        tvEmptyState = findViewById(R.id.tvEmptyState); // Find Empty State TextView

        // Add rented items dynamically
        Intent intent = getIntent();
        String[] itemNames = intent.getStringArrayExtra("itemNames");
        String[] rentalDates = intent.getStringArrayExtra("rentalDates");
        String[] rentalQuantities = intent.getStringArrayExtra("rentalQuantities");
        String[] remainingTimes = intent.getStringArrayExtra("remainingTimes");

        if (itemNames != null && rentalDates != null && rentalQuantities != null && remainingTimes != null) {
            for (int i = 0; i < itemNames.length; i++) {
                addRentedItem(itemContainer, itemNames[i], rentalDates[i], rentalQuantities[i], remainingTimes[i]);
            }
        }

        // 뒤로가기 버튼
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Update empty state visibility
        updateEmptyState(itemContainer);
    }

    // Add rented item dynamically
    private void addRentedItem(LinearLayout itemContainer, String itemName, String rentalDate, String rentalQuantity, String remainingTime) {
        View itemView = getLayoutInflater().inflate(R.layout.item_rented, null);

        ((TextView) itemView.findViewById(R.id.tvItemName)).setText("물품명: " + itemName);
        ((TextView) itemView.findViewById(R.id.tvRentalDate)).setText("대여일자: " + rentalDate);
        ((TextView) itemView.findViewById(R.id.tvRentalQuantity)).setText("대여 수량: " + rentalQuantity);
        ((TextView) itemView.findViewById(R.id.tvRemainingTime)).setText("반납까지 남은 시간: " + remainingTime);

        Button btnReturn = itemView.findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(v -> {
            rentManager.returnItem(itemName, itemName + "-A"); // Return the item
            itemContainer.removeView(itemView); // Remove item from MyPage
            updateEmptyState(itemContainer); // Update empty state visibility
        });

        itemContainer.addView(itemView);
        updateEmptyState(itemContainer); // Update empty state visibility
    }

    // Update the visibility of the empty state text
    private void updateEmptyState(LinearLayout itemContainer) {
        if (itemContainer.getChildCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE); // Show empty state text
        } else {
            tvEmptyState.setVisibility(View.GONE); // Hide empty state text
        }
    }
}