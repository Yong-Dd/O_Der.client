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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment implements View.OnClickListener {
    Button orderListButton, stampButton, logOutButton,nameEditButton,editNameConfirmButton;
    FrameLayout noLogin;
    static TextView stampCountText, nameText;
    LinearLayout editNameLayout;
    EditText editName;

    boolean logIn;
    static int lastStampCount;

    MainActivity mainActivity = new MainActivity();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment,container,false);

        orderListButton = view.findViewById(R.id.A_orderListButton);
        stampButton = view.findViewById(R.id.A_stampButton);
        logOutButton = view.findViewById(R.id.A_logOutButton);
        noLogin = (FrameLayout) view.findViewById(R.id.A_noLogIn);
        stampCountText = view.findViewById(R.id.A_stampCountText);
        nameEditButton = view.findViewById(R.id.A_NameEditButton);
        editNameLayout = view.findViewById(R.id.A_editNameLayout);
        editName = view.findViewById(R.id.A_editName);
        editNameConfirmButton = view.findViewById(R.id.A_EditNameConfirmButton);
        nameText = view.findViewById(R.id.A_Name);

        orderListButton.setOnClickListener(this);
        stampButton.setOnClickListener(this);
        logOutButton.setOnClickListener(this);
        nameEditButton.setOnClickListener(this);
        editNameConfirmButton.setOnClickListener(this);

        //스탬프 세팅
        stampSetting(MainActivity.CURRENT_STAMP);

        //이름 세팅
        nameSetting();

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

            PaymentFragment paymentFragment = new PaymentFragment();

            mainActivity.logInSetting(false,"");
            OrderList.orderLists.clear();

            reloadView();
        }else if(v==nameEditButton){
            editNameLayout.setVisibility(View.VISIBLE);
        }else if(v==editNameConfirmButton){
            String name = editName.getText().toString();
            editNameLayout.setVisibility(View.GONE);
            nameText.setText(name);
            editName.setText("");
            mainActivity.logInSetting(true,name);
            editName(name);

        }
    }

    public void logInSetting(boolean login){
        if(login){
            noLogin.setVisibility(View.GONE);
//            reloadView();
        }else{
            noLogin.setVisibility(View.VISIBLE);
        }
    }

    public void reloadView(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(AccountFragment.this).attach(AccountFragment.this).commitAllowingStateLoss();

    }


    public void stampSetting(int stampCount){
        stampCountText.setText(String.valueOf(stampCount));
        lastStampCount = stampCount;
    }

    public void editName(String name){
        String email = user.getEmail();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    child.getRef().child("userName").setValue(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("account","프로필 업데이트 성공");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("account","프로필 업데이트 실패 :"+e);
            }
        });
    }

    private void nameSetting(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        Log.d("account",userName);
        if(userName!=null){
            nameText.setText(userName);
        }

    }

}