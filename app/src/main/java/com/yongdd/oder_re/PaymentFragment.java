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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaymentFragment extends Fragment implements View.OnClickListener {
    FrameLayout noLogin;
    FrameLayout emptyPayment;
    RecyclerView choiceMenuRecyclerView;
    LinearLayout deleteLayout;
    static PaymentAdapter paymentAdapter;
    public static ArrayList<Payment> paymentLists;
    boolean logIn;
    Button paymentButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.payment_fragment, container, false);

        noLogin = (FrameLayout)view.findViewById(R.id.P_noLogIn);
        emptyPayment = view.findViewById(R.id.emptyPayment);
        paymentButton = view.findViewById(R.id.paymentButton);
        //결제 내역 관련
        paymentLists = new ArrayList<>();
        paymentLists = MenuFragment.orderLists;


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
                    paymentAdapter.addItem(payment);
                    choiceMenuRecyclerView.setAdapter(paymentAdapter);
                    paymentAdapter.notifyDataSetChanged();
                    Log.d("PaymentFragment","payment set adapter");
                }
            }


        }else{
            emptyCheck(true);
        }

    }

    @Override
    public void onClick(View v) {
        if(v==paymentButton){

        }


    }
}
