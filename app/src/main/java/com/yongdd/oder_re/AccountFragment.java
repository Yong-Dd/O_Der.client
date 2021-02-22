package com.yongdd.oder_re;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment implements View.OnClickListener {
    Button orderListButton, stampButton, logOutButton;
    FrameLayout noLogin;
    boolean logIn;
    TextView stampCountText;
    static int lastStampCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment,container,false);

        Log.d("start111","accountFragment start");
        orderListButton = view.findViewById(R.id.A_orderListButton);
        stampButton = view.findViewById(R.id.A_stampButton);
        logOutButton = view.findViewById(R.id.A_logOutButton);
        noLogin = (FrameLayout) view.findViewById(R.id.A_noLogIn);
        stampCountText = view.findViewById(R.id.A_stampCountText);

        orderListButton.setOnClickListener(this);
        stampButton.setOnClickListener(this);
        logOutButton.setOnClickListener(this);

        //스탬프 세팅
        getStampCount();
//        getOrderList();

        //로그인 전달하여 세팅
        logIn = MainActivity.LOGIN_SUCCESS;

        if(logIn){
            logInSetting(true);
        }else{
            logInSetting(false);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(orderListButton)){

            Intent intent = new Intent(getActivity(),OrderList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.page_slide_in_right, R.anim.page_slide_out_left).toBundle();
            getActivity().startActivity(intent,bundle);

        }else if(v==stampButton){
            Intent intent = new Intent(getActivity(),Stamp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.page_slide_in_right, R.anim.page_slide_out_left).toBundle();
            startActivity(intent,bundle);
        }else if(v==logOutButton){
            //추후 작성
        }
    }

    public void logInSetting(boolean login){
        if(login){

            noLogin.setVisibility(View.GONE);
        }else{
            noLogin.setVisibility(View.VISIBLE);
        }
    }

    public void reloadView(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(AccountFragment.this).attach(AccountFragment.this).commitAllowingStateLoss() ;
    }

    public void getStampCount(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.orderByChild("userEmail").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    int curretStamp = child.child("userStamp").getValue(Integer.class);
                    stampSetting(curretStamp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void stampSetting(int stampCount){
        stampCountText.setText(String.valueOf(stampCount));
        lastStampCount = stampCount;
    }

}