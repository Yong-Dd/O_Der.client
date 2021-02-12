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
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaymentFragment extends Fragment {
    FrameLayout noLogin;
    FrameLayout emptyPayment;
    RecyclerView choiceMenuRecyclerView;
    PaymentAdapter paymentAdapter;
    public static ArrayList<Payment> paymentLists;
    boolean logIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.payment_fragment, container, false);

        Log.d("start111","paymentFragment start");
        noLogin = (FrameLayout)view.findViewById(R.id.P_noLogIn);
        emptyPayment = view.findViewById(R.id.emptyPayment);

        paymentLists = new ArrayList<>();

        spinnerSetting(view);

        //선택한 주문 목록 리싸이클러뷰
        choiceMenuRecyclerView = (RecyclerView)view.findViewById(R.id.choiceMenuList);
        paymentAdapter = new PaymentAdapter();

        choiceMenuRecyclerView.setHasFixedSize(true);
        choiceMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        choiceMenuRecyclerView.setAdapter(paymentAdapter);

        //로그인 체크하여 세팅
        logIn = MainActivity.LOGIN_SUCCESS;

        if(logIn){
            logInSetting(true);
        }else{
            logInSetting(false);
        }

        return view;
    }

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
        }
    }

    public void emptyCheck(boolean empty){
        if(empty){
            emptyPayment.setVisibility(View.VISIBLE);
        }else{
            emptyPayment.setVisibility(View.GONE);
        }
    }

    public void paymentListAdd(Payment payment){

        if(payment!=null){
            paymentLists.add(payment);
            if (paymentLists.size() >= 1) {
                emptyCheck(false);
                paymentAdapter.addItem(payment);

                //차후 확인 필요 - 리싸이클러뷰 onCreate아니면 안됐었음.
                choiceMenuRecyclerView.setAdapter(paymentAdapter);
                paymentAdapter.notifyDataSetChanged();
            }else{
                emptyCheck(true);
            }
        }else{
            Log.d("payment","paymentListAdd works but payment is null");
        }

    }
}
