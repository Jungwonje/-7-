package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OutOfStockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outofstock);

        // 대여 버튼
        Button btnListItem = findViewById(R.id.btnListItem);
        btnListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SecondActivity로 이동 (대여 목록 페이지)
                Intent intent = new Intent(OutOfStockActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}