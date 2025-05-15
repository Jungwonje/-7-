package com.example.hsu_lendit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OutOfStockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outofstock);

        // Intent로 전달된 물품 이름 가져오기
        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");

        // ImageView 및 TextView 참조
        ImageView ivItemImage = findViewById(R.id.ivItemImage);
        TextView tvOutOfStockMessage = findViewById(R.id.tvOutOfStockMessage);

        // 물품 이름에 따라 이미지를 동적으로 설정
        if ("가위".equals(itemName)) {
            ivItemImage.setImageResource(R.drawable.scissors); // 가위 이미지로 변경
            tvOutOfStockMessage.setText("가위의 재고가 없습니다.");
        } else if ("마우스".equals(itemName)) {
            ivItemImage.setImageResource(R.drawable.mouse); // 마우스 이미지
            tvOutOfStockMessage.setText("마우스의 재고가 없습니다.");
        } else if ("노트북".equals(itemName)) {
            ivItemImage.setImageResource(R.drawable.laptop); // 노트북 이미지
            tvOutOfStockMessage.setText("노트북의 재고가 없습니다.");
        } else {
            // 기본 동작: 이미지를 설정하지 않고 메시지만 표시
            ivItemImage.setVisibility(View.GONE); // ImageView 숨기기
            tvOutOfStockMessage.setText(itemName + "의 재고가 없습니다.");
        }

        // 물품 목록 버튼
        Button btnListItem = findViewById(R.id.btnListItem);
        btnListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OutOfStockActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}