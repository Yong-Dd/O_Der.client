package com.yongdd.oder_re;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    ArrayList<Payment> paymentLists = new ArrayList<>();

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.payment_menu_list,parent,false);
        return new PaymentViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentLists.get(position);
        holder.setItem(payment);
    }

    @Override
    public int getItemCount() {
        return paymentLists.size();
    }


    public void addItem(Payment payment){
        paymentLists.add(payment);
    }

    public Payment getItem(int position){
        return paymentLists.get(position);
    }


    public class PaymentViewHolder extends RecyclerView.ViewHolder{
        TextView menuName;
        TextView menuCount;
        TextView menuPrice;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.P_menuName);
            menuCount = itemView.findViewById(R.id.P_menuCount);
            menuPrice = itemView.findViewById(R.id.P_menuPrice);

        }
        public void setItem(Payment payment){
            menuName.setText(payment.getMenuName());
            menuCount.setText(payment.getMenuCount()+"개");
            menuPrice.setText(payment.getMenuPrice()+"원");

        }
    }
}
