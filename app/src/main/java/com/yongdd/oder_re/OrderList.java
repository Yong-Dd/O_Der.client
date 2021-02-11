package com.yongdd.oder_re;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
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


    }
}
