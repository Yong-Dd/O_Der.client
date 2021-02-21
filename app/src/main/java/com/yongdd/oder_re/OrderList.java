package com.yongdd.oder_re;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;

public class OrderList extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        RecyclerView orderListRecyclerview = findViewById(R.id.O_orderListRecyclerview);
        OrderListAdapter adapter = new OrderListAdapter();

        adapter.addItem(new Order(LocalDateTime.of(2021,2,11,8,30),LocalDateTime.of(2021,2,11,8,35)
        ,LocalDateTime.of(2021,2,11,8,40),7000));

        orderListRecyclerview.setHasFixedSize(true);
        orderListRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orderListRecyclerview.setAdapter(adapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AccountFragment accountFragment = new AccountFragment();

        finish();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);
//        accountFragment.reloadView();
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,accountFragment)
//                .setCustomAnimations(R.anim.page_slide_in_left,R.anim.page_slide_out_right).commitAllowingStateLoss() ;

    }
}
