package com.yongdd.oder_re;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    ArrayList<Payment> paymentLists = new ArrayList<>();
    Context context;

    final DecimalFormat priceFormat = new DecimalFormat("###,###");

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.payment_menu_list,parent,false);
        context = view.getContext();
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
        TextView menuName,menuCount, menuPrice;
        Button itemDeleteButton;


        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.P_menuName);
            menuCount = itemView.findViewById(R.id.P_menuCount);
            menuPrice = itemView.findViewById(R.id.P_menuPrice);
            itemDeleteButton = itemView.findViewById(R.id.P_ItemDeleteButton);
            itemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkDeleteItem(getAdapterPosition());

                }
            });


        }
        public void setItem(Payment payment){
            String hotIce = payment.getMenuHotIce();
            if(hotIce=="" || hotIce.equals("")){
                menuName.setText(payment.getMenuName());
            }else{
                menuName.setText(payment.getMenuName()+"("+hotIce+")");
            }

            menuCount.setText(payment.getMenuTotalCount()+"개");

            String itemPriceFormat = priceFormat.format(payment.getMenuTotalPrice());
            menuPrice.setText(itemPriceFormat+"원");
        }
        public void checkDeleteItem(int position){
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.dialogChange);
            builder.setMessage("메뉴를 삭제하시겠습니까?")
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem(position);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();


        }
        public void deleteItem(int position){
            PaymentFragment paymentFragment = new PaymentFragment();
            MenuFragment menuFragment = new MenuFragment();

            int count = paymentLists.get(position).getMenuTotalCount();

            //삭제
            paymentLists.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,getItemCount());
            notifyItemChanged(position);

            //페이먼트 프래그먼트 데이터 삭제
            paymentFragment.orderItemDeleted(position);

            //오더버튼 개수 수정
            menuFragment.orderCountMinus(count);

            if(paymentLists.size()==0){
                paymentFragment.emptyCheck(true);
            }
        }

    }
}
