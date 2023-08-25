package com.example.ceyda.mobilbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {

    private CheckBox checkBox;
    private TextView devamEtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        checkBox = findViewById(R.id.checkBox);
        devamEtBtn = findViewById(R.id.devamEtBtn);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    devamEtBtn.setBackgroundResource(R.drawable.regular_button_background);
                    devamEtBtn.setClickable(true);
                    devamEtBtn.setEnabled(true);
                    devamEtBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // MainActivity2'yi aç
                            Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    devamEtBtn.setBackgroundResource(R.drawable.inactive_button_background);
                    devamEtBtn.setClickable(false);
                    devamEtBtn.setEnabled(false);
                }
            }
        });

    }
}