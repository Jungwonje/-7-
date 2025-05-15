package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReturnItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_item);

        EditText etItemType = findViewById(R.id.etItemType); // 물품 종류 입력
        EditText etItemId = findViewById(R.id.etItemId); // 고유 ID 입력

        Button btnConfirmReturn = findViewById(R.id.btnConfirmReturn);
        btnConfirmReturn.setOnClickListener(v -> {
            String itemType = etItemType.getText().toString().trim(); // 물품 종류
            String itemId = etItemId.getText().toString().trim(); // 고유 ID

            if (itemType.isEmpty() || itemId.isEmpty()) {
                Toast.makeText(this, "물품 종류와 고유 ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("returnedItem", itemType + "-" + itemId); // 예: "가위-A"
            resultIntent.putExtra("itemType", itemType); // 예: "가위"
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}