package com.yongdd.oder_re;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Join extends Activity implements View.OnClickListener {
    Button xButton;
    Button validateButton;
    Button registerButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        xButton = findViewById(R.id.J_xButton);
        validateButton = findViewById(R.id.validateButton);
        registerButton = findViewById(R.id.registerButton);

        xButton.setOnClickListener(this);
        validateButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==xButton){
            onBackPressed();
        }else if(v==validateButton){
            //아이디 중복 확인 버튼
        }else if(v==registerButton){
            //가입하기 버튼
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);
    }
}
