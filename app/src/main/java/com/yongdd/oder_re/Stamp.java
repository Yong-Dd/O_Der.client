package com.yongdd.oder_re;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Stamp extends AppCompatActivity {

    AccountFragment accountFragment = new AccountFragment();
    ImageView s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stamp);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        s5 = findViewById(R.id.s5);
        s6 = findViewById(R.id.s6);
        s7 = findViewById(R.id.s7);
        s8 = findViewById(R.id.s8);
        s9 = findViewById(R.id.s9);
        s10 = findViewById(R.id.s10);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int stampCount = AccountFragment.lastStampCount;
        stampSetting(stampCount);
    }
    private void stampSetting(int stampCount){
       for(int i=1; i<=stampCount; i++){
           switch (i){
               case 1:
                   s1.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 2:
                   s2.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 3:
                   s3.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 4:
                   s4.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 5:
                   s5.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 6:
                   s6.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 7:
                   s7.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 8:
                   s8.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 9:
                   s9.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;
               case 10:
                   s10.setColorFilter(getResources().getColor(R.color.mainColor), android.graphics.PorterDuff.Mode.MULTIPLY);
                   break;

           }
       }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);
    }
}
