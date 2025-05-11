package com.example.hsu_lendit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReturnItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_item);

        TextView tvReturnMessage = findViewById(R.id.tvReturnMessage);
        tvReturnMessage.setText("반납이 완료되었습니다!");

        Button btnConfirmReturn = findViewById(R.id.btnConfirmReturn);
        btnConfirmReturn.setOnClickListener(v -> finish());
    }
}