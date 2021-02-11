package com.yongdd.oder_re;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        Button orderListButton = findViewById(R.id.A_orderListButton);
        orderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OrderList.class);
                startActivity(intent);
            }
        });

        Button stampButton = findViewById(R.id.A_stampButton);
        stampButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Stamp.class);
                startActivity(intent);
            }
        });

    }
}