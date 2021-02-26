package com.yongdd.oder_re;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeSet;

public class PaymentFragment extends Fragment{
    FrameLayout noLogin;
    static FrameLayout emptyPayment;
    static Button paymentButton;
    static int dbTotalPrice;
    CheckBox stampCheckBox;
    LinearLayout stampFalseLayout, stampTrueLayout;
    TextView stampCountText;
    static ConstraintLayout loadingLayout;
    static ProgressBar progressBar;
    EditText memoText;

    public static ArrayList<Payment> paymentLists;
    boolean logIn;
    boolean stampCheck;
    static int plusStampCount;

    RecyclerView choiceMenuRecyclerView;
    static PaymentAdapter paymentAdapter;

    MenuFragment menuFragment = new MenuFragment();
    final static DecimalFormat priceFormat = new DecimalFormat("###,###");
    final String TAG = "paymentFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.payment_fragment, container, false);

        noLogin = (FrameLayout)view.findViewById(R.id.P_noLogIn);
        emptyPayment = view.findViewById(R.id.emptyPayment);
        paymentButton = view.findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                loadingLayout.setVisibility(View.VISIBLE);
                if (progressBar != null) {
                    progressBar.setIndeterminate(true);
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.rgb(175,18,18)));
                }

                if (memoText.getText().toString().length() == 0) {
                    updateDB("");
                } else {
                    String memo = memoText.getText().toString().trim();
                    updateDB(memo);
                }


            }
        });
        memoText = view.findViewById(R.id.memo);

        //loading 관련
        loadingLayout = view.findViewById(R.id.P_loadingLayout);
        progressBar = view.findViewById(R.id.progressBar);

        //스탬프 관련
        stampCheck = false;
        stampCheckBox = view.findViewById(R.id.stampCheckBox);
        stampCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    totalPriceSetting(true);
                    stampCheck = true;
                }else{
                    totalPriceSetting(false);
                    stampCheck = false;
                }
            }
        });
        stampFalseLayout = view.findViewById(R.id.stampFalseLayout);
        stampTrueLayout = view.findViewById(R.id.stampTrueLayout);
        stampCountText = view.findViewById(R.id.stampCountText);


        SetStampLayout();

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

        choiceMenuRecyclerView.setHasFixedSize(false);
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

    private void SetStampLayout(){
        int stampCount = MainActivity.CURRENT_STAMP;
        if(stampCount>=10){
            stampTrueLayout.setVisibility(View.VISIBLE);
            stampFalseLayout.setVisibility(View.GONE);
        }else{
            stampTrueLayout.setVisibility(View.GONE);
            stampFalseLayout.setVisibility(View.VISIBLE);
            stampCountText.setText("("+stampCount+"개)");
        }
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


    public void totalPriceSetting(boolean stampChecked){
        int totalPrice = getTotalPrice(stampChecked);
        String totalPriceFormat = priceFormat.format(totalPrice);
        paymentButton.setText(totalPriceFormat+"원 결제하기");

        //db에 넘길 가격 설정
        dbTotalPrice = totalPrice;

        //stamp count
        plusStampCount = plusStampCount();
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

    private void updateDB(String memo){
        Log.d(TAG,"update DB called");

        String userId = MainActivity.USER_ID;
        String orderDate="";
        String orderReceivedTime="";

        //시간
        if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of("Asia/Seoul"));

            orderDate = String.valueOf(zonedDateTime.toLocalDate());

            orderReceivedTime = zonedDateTime.format(timeFormat);
//          orderReceivedTime = String.valueOf(zonedDateTime.toLocalTime());
        }else{
            Date now = new Date();
            TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            dateFormat.setTimeZone(tz);
            orderDate = dateFormat.format(now);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
            timeFormat.setTimeZone(tz);
            orderReceivedTime = timeFormat.format(now);

        }



        Order orderList = new Order(userId,paymentLists,dbTotalPrice,orderDate,orderReceivedTime,"","",memo);

        if(orderList!=null){
            getMaxCount(orderList);
        }
    }
    private void getMaxCount(Order order){

        Log.d(TAG,"getMaxCount called");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("orderList");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long maxCount = snapshot.getChildrenCount();
                getMaxId(maxCount,order);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMaxId(long maxCount, Order order){
        Log.d(TAG,"getMaxId called");

        TreeSet<Integer> orderId = new TreeSet<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("orderList");
        database.orderByChild("orderDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                orderId.add(Integer.parseInt(snapshot.getKey()));

                Log.d(TAG,"id "+Integer.parseInt(snapshot.getKey()));

                Log.d(TAG,"maxCount "+maxCount);

                if(orderId.size()==maxCount){
                    Log.d(TAG,"Ids.size == maxCount");
                    maxId(orderId,order);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }



    private void maxId(TreeSet<Integer> orderId, Order order){

        Log.d(TAG,"getMaxId called");

        int maxId = orderId.last();
        Log.d(TAG,"max second id :"+maxId);
        if(maxId>=0){
            addDB(maxId,order);
        }
    }

    private void addDB(int maxId,Order order){

        Log.d(TAG,"addDB called");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("orderList");
        database.child(String.valueOf(maxId + 1)).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "addDB complete");
                    stampPlus();
                    clearOrder();
                    loadingLayout.setVisibility(View.GONE);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,menuFragment).commit();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "addDB failed" + e.toString());
                Toast.makeText(getContext(), "결제에 실패했습니다.", Toast.LENGTH_SHORT);
            }
        });

    }
    public void clearOrder(){
        paymentLists.clear();
        menuFragment.orderLists.clear();
        menuFragment.setOrderButton(false,0);
    }

    public void stampPlus(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    int curretStamp = child.child("userStamp").getValue(Integer.class);
                    if(stampCheck){
                        child.getRef().child("userStamp").setValue(curretStamp - 10);
                    }else{
                        child.getRef().child("userStamp").setValue(curretStamp + plusStampCount);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public int plusStampCount(){
        int totalMenuCount=0;
        int dessertMenuCount=0;
        for(int i=0; i<paymentLists.size(); i++){
            int count = paymentLists.get(i).getMenuTotalCount();
            int menuDelimiter = paymentLists.get(i).getMenuDelimiter();

            totalMenuCount += count;

            if(menuDelimiter==5){
                dessertMenuCount+=count;
            }
        }
        int plusStamp = totalMenuCount-dessertMenuCount;

        if(plusStamp<0){
            plusStamp=0;
        }
        return plusStamp;
    }

}
