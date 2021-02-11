package com.yongdd.oder_re;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {
    ArrayList<Order> orderLists = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_list_item,parent,false);

        context = parent.getContext();

        return new OrderListViewHolder(view);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, int position) {
        Order orderList = orderLists.get(position);
        holder.setItem(orderList);
    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }


    public void addItem(Order orderList){
        orderLists.add(orderList);
    }

    public Order getItem(int position){
        return orderLists.get(position);
    }


    public class OrderListViewHolder extends RecyclerView.ViewHolder{
        TextView orderReceived;
        TextView orderAccepted;
        TextView orderCompleted;
        TextView totalCount;
        RecyclerView orderListRecycler;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            orderReceived = itemView.findViewById(R.id.orderRecivedDate);
            orderAccepted = itemView.findViewById(R.id.orderAcceptedDate);
            orderCompleted = itemView.findViewById(R.id.orderCompletedDate);
            totalCount = itemView.findViewById(R.id.inO_totalPrice);
            orderListRecycler = itemView.findViewById(R.id.O_orderListRecyclerview);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setItem(Order orderList){
            //주문 날짜 등 지정
            ZoneId defaultZoneId = ZoneId.systemDefault();

            Date receivedDate = Date.from(orderList.getOrderReceivedDate().atZone(defaultZoneId).toInstant());
            orderReceived.setText(date.format(receivedDate));

            Date acceptedDate = Date.from(orderList.getOrderAcceptedDate().atZone(defaultZoneId).toInstant());
            orderReceived.setText(date.format(acceptedDate));

            Date completedDate = Date.from(orderList.getOrderCompletedDate().atZone(defaultZoneId).toInstant());
            orderReceived.setText(date.format(completedDate));

            //결제금액
            totalCount.setText(orderList.getTotalPrice()+"원");

            //주문 메뉴 리싸이클러뷰
            PaymentAdapter adapter = new PaymentAdapter();
            adapter.addItem(new Payment("아메리카노",1,3000));
            adapter.addItem(new Payment("카페 모카",1,4000));
            orderListRecycler.setHasFixedSize(true);
            orderListRecycler.setLayoutManager(new LinearLayoutManager(context));
            orderListRecycler.setAdapter(adapter);

        }


    }
}
