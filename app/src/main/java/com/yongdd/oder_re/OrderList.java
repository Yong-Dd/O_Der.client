package com.yongdd.oder_re;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OrderList extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    static RecyclerView orderListRecyclerview;
    static OrderListAdapter orderListAdapter;
    static ArrayList<Order> orderLists = new ArrayList<>();
    int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);

        count = 0;

        getMaxOrderCount();

        orderListRecyclerview = findViewById(R.id.O_orderListRecyclerview);
        orderListAdapter = new OrderListAdapter();

        orderListRecyclerview.setHasFixedSize(true);
        orderListRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        orderListRecyclerview.setAdapter(adapter);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
    public void getMaxOrderCount(){
        String userId = MainActivity.USER_ID;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("orderList");
        database.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long maxCount = snapshot.getChildrenCount();
                getOrderList(maxCount,userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getOrderList(long totalCount, String userId){
        DatabaseReference ref = database.getReference("orderList");
        ref.orderByChild("userId").equalTo(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Order order = snapshot.getValue(Order.class);
                orderLists.add(order);

                count+=1;

               if(count==totalCount){
                   setReverseOrderList();
               }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setReverseOrderList();
    }

    private void setReverseOrderList(){
        Collections.reverse(orderLists);

        for(Order order:orderLists){
            orderListAdapter.addItem(order);
            orderListRecyclerview.setAdapter(orderListAdapter);
            orderListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);

    }
}
