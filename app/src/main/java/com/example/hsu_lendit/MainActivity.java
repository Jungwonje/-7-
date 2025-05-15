package com.example.hsu_lendit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etStudentId = findViewById(R.id.etStudentId);
        EditText etName = findViewById(R.id.etName);
        EditText etPhone = findViewById(R.id.etPhone); // 전화번호 입력 필드 추가
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String studentId = etStudentId.getText().toString();
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();

            // 사용자 데이터를 SharedPreferences에 저장
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("studentId", studentId);
            editor.putString("name", name);
            editor.putString("phone", phone);
            editor.apply();

            // SecondActivity로 이동
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });
    }
}