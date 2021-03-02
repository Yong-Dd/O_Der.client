package com.yongdd.oder_re;

import android.os.Bundle;
import android.util.Log;
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
    static ArrayList<OrderId> orderLists = new ArrayList<>();
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
                Log.d("orderList","order count called");
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
                Log.d("orderList","child add called");
                String orderId = snapshot.getKey();
                Order order = snapshot.getValue(Order.class);
                orderLists.add(0,new OrderId(orderId,order));
                orderListAdapter.addReverseItem(order);
                orderListRecyclerview.setAdapter(orderListAdapter);
                orderListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("orderList","child Changed called");
                String orderId = snapshot.getKey();
                Order order = snapshot.getValue(Order.class);
                Log.d("orderList","child Changed orderId "+orderId);
                setOrderConditionChanged(orderId,order);
                Log.d("orderList","child Changed end");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void setOrderConditionChanged(String orderId, Order order){
        Log.d("orderList","setOrderConditionChanged called");
        for(int i=0; i<orderLists.size(); i++){
            String orderId1 = orderLists.get(i).getOrderId();
            Log.d("orderList","setOrderConditionChanged i "+i);
            Log.d("orderList","setOrderConditionChanged orderId1 "+orderId1);
            if(orderId1.equals(orderId)){
                Log.d("orderList","equals id , changed");
                orderListAdapter.updateItem(i,order);
                orderListAdapter.notifyItemChanged(i);
            //    orderListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        orderLists.clear();
        orderListAdapter.clearItem();
        finish();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);

    }
}
