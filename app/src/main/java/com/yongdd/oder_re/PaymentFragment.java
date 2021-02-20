package com.yongdd.oder_re;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PaymentFragment extends Fragment implements View.OnClickListener {
    FrameLayout noLogin;
    static FrameLayout emptyPayment;
    RecyclerView choiceMenuRecyclerView;
    static PaymentAdapter paymentAdapter;
    public static ArrayList<Payment> paymentLists;
    boolean logIn;
    static Button paymentButton;
//    static int totalPrice;
    CheckBox stampCheckBox;

    final static DecimalFormat priceFormat = new DecimalFormat("###,###");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.payment_fragment, container, false);

        noLogin = (FrameLayout)view.findViewById(R.id.P_noLogIn);
        emptyPayment = view.findViewById(R.id.emptyPayment);
        paymentButton = view.findViewById(R.id.paymentButton);

        //스탬프 할인 체크
        stampCheckBox = view.findViewById(R.id.stampCheckBox);
        stampCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    totalPriceSetting(true);
                }else{
                    totalPriceSetting(false);
                }
            }
        });

        //결제 내역 관련
        paymentLists = new ArrayList<>();
        paymentLists = MenuFragment.orderLists;
        Log.d("PaymentFragment","실 선택 사이즈: "+MenuFragment.orderLists.size());
        Log.d("PaymentFragment","결제 내역 사이즈:"+paymentLists.size());


        //결제 방식
        spinnerSetting(view);

        //선택한 주문 목록 리싸이클러뷰
        choiceMenuRecyclerView = (RecyclerView)view.findViewById(R.id.choiceMenuList);
        paymentAdapter = new PaymentAdapter();

        choiceMenuRecyclerView.setHasFixedSize(true);
        choiceMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        paymentListSetting();


        //로그인 체크하여 세팅
        logIn = MainActivity.LOGIN_SUCCESS;

        if(logIn){
            logInSetting(true);
        }else{
            logInSetting(false);
        }

        //총가격 세팅
//        totalPrice=0;

        return view;
    }

    //결제수단
    public void spinnerSetting(View view){

        Spinner spinner = (Spinner)view.findViewById(R.id.paymentSpinner);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.paymentMethod, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void logInSetting(boolean login){
        if(login){
            noLogin.setVisibility(View.GONE);
        }else{
            noLogin.setVisibility(View.VISIBLE);
            paymentLists.clear();
        }
    }

    //장바구니 비었을때
    public void emptyCheck(boolean empty){
        if(empty){
            emptyPayment.setVisibility(View.VISIBLE);
        }else{
            emptyPayment.setVisibility(View.GONE);
        }
    }


    public void paymentListSetting(){

        if(paymentLists.size()>0){
            emptyCheck(false);

            for(int i=0; i<paymentLists.size(); i++){
                Payment payment = paymentLists.get(i);
                if(payment!=null) {
                    Log.d("PaymentFragment","payment not null "+payment.getMenuName());

                    //어뎁터 설정
                    paymentAdapter.addItem(payment);
                    choiceMenuRecyclerView.setAdapter(paymentAdapter);
                    paymentAdapter.notifyDataSetChanged();


                    Log.d("PaymentFragment","payment set adapter");
                }
            }
            totalPriceSetting(false);


        }else{
            emptyCheck(true);
        }

    }

    public void orderItemDeleted(int position){
        int beforeSize = paymentLists.size();
        paymentLists.remove(position);
        int changedSize = paymentLists.size();

        if(beforeSize>changedSize) {
            totalPriceSetting(false);
        }
    }

    @Override
    public void onClick(View v) {
        if(v==paymentButton){

        }
    }

    public void totalPriceSetting(boolean stampChecked){
        int totalPrice = getTotalPrice(stampChecked);
        String totalPriceFormat = priceFormat.format(totalPrice);
        paymentButton.setText(totalPriceFormat+"원 결제하기");
    }

    public int getTotalPrice(boolean stampChecked){
        int totalPrice=0;

        for(int i=0; i<paymentLists.size();i++){
            totalPrice += paymentLists.get(i).getMenuTotalPrice();
        }

        if(stampChecked){
            totalPrice-=3000;
        }

        return totalPrice;
    }
}
