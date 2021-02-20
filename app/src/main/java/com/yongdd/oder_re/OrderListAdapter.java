package com.yongdd.oder_re;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
        TextView orderDate;
        TextView orderReceived;
        TextView orderAccepted;
        TextView orderCompleted;
        TextView totalCount;
        RecyclerView orderListRecycler;
        ProgressBar orderProgressBar;
        TextView acceptedProgress;
        TextView completedProgress;
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat date2 = new SimpleDateFormat("HH:ss");

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.O_date);
            orderReceived = itemView.findViewById(R.id.orderRecivedDate);
            orderAccepted = itemView.findViewById(R.id.orderAcceptedDate);
            orderCompleted = itemView.findViewById(R.id.orderCompletedDate);
            totalCount = itemView.findViewById(R.id.inO_totalPrice);
            orderListRecycler = itemView.findViewById(R.id.O_orderListRecyclerview);
            orderProgressBar = itemView.findViewById(R.id.orderProgressBar);
            acceptedProgress = itemView.findViewById(R.id.acceptedProgress);
            completedProgress = itemView.findViewById(R.id.completedProgress);
            orderProgressBar.setMax(10);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setItem(Order orderList){
            //주문 날짜 등 지정
            ZoneId defaultZoneId = ZoneId.systemDefault();

            Date receivedDate = Date.from(orderList.getOrderReceivedDate().atZone(defaultZoneId).toInstant());
            orderDate.setText(date1.format(receivedDate));
            orderReceived.setText(date2.format(receivedDate));

            Date acceptedDate = Date.from(orderList.getOrderAcceptedDate().atZone(defaultZoneId).toInstant());
            orderAccepted.setText(date2.format(acceptedDate));

            Date completedDate = Date.from(orderList.getOrderCompletedDate().atZone(defaultZoneId).toInstant());
            orderCompleted.setText(date2.format(completedDate));

            //프로그래스바 - 수정 필
            orderProgressBar.setProgress(5);
            acceptedProgress.setBackgroundResource(R.drawable.progress_circle_main);

            //결제금액
            totalCount.setText(orderList.getTotalPrice()+"원");

            //주문 메뉴 리싸이클러뷰
            PaymentAdapter adapter = new PaymentAdapter();

            orderListRecycler.setHasFixedSize(true);
            orderListRecycler.setLayoutManager(new LinearLayoutManager(context));
            orderListRecycler.setAdapter(adapter);

        }


    }
}
