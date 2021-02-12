package com.yongdd.oder_re;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    HomeFragment homeFragment;
    MenuFragment menuFragment;
    PaymentFragment paymentFragment;
    AccountFragment accountFragment;

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        menuFragment = new MenuFragment();
        paymentFragment = new PaymentFragment();
        accountFragment = new AccountFragment();

        //어플 시작시 홈으로 이동
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,homeFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tabSelect(item);

                if(tabSelect(item)){
                    return true;
                }else{
                    return false;
                }
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.homeTab);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);



    }

    public boolean tabSelect(MenuItem item){
        switch (item.getItemId()){
            case R.id.homeTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,homeFragment).commit();
                return true;
            case R.id.menuTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,menuFragment).commit();
                return true;
            case R.id.paymentTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,paymentFragment).commit();
                return true;
            case R.id.accountTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,accountFragment).commit();
                return true;
            default:return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(loginButton)) {
            Intent intent = new Intent(this,LogIn.class);
            Bundle bundle = ActivityOptions.makeCustomAnimation(this, R.anim.page_slide_in_right, R.anim.page_slide_out_left).toBundle();
            startActivity(intent,bundle);
        };

    }
}