package com.yongdd.oder_re;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    HomeFragment homeFragment;
    MenuFragment menuFragment;
    PaymentFragment paymentFragment;
    AccountFragment accountFragment;

    Button loginButton;
    LinearLayout login_success;
    TextView customerName;

    public static ArrayList<Menu> menus;


    public static boolean LOGIN_SUCCESS;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        menuFragment = new MenuFragment();
        paymentFragment = new PaymentFragment();
        accountFragment = new AccountFragment();

        login_success = (LinearLayout) findViewById(R.id.logIn_success);
        loginButton = (Button) findViewById(R.id.loginButton);
        customerName =(TextView) findViewById(R.id.customerName);

        loginButton.setOnClickListener(this);

        LOGIN_SUCCESS = false;

        menus=new ArrayList<>();

        //어플 시작시 홈으로 이동
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,homeFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tabSelect(item);

                if(tabSelect(item)){
                    return true;
                }else{
                    return false;
                }
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.homeTab);

        //로그인
        Intent intent = getIntent();
        if(intent!=null){
            Log.d("Result","intent not null");
            logInCheck(intent);
        }

        // firebase
        mAuth = FirebaseAuth.getInstance();

        getMenuDB();



    }

    public boolean tabSelect(MenuItem item){
        switch (item.getItemId()){
            case R.id.homeTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,homeFragment).commit();
                return true;
            case R.id.menuTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,menuFragment).commit();
                return true;
            case R.id.paymentTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,paymentFragment).commit();
                return true;
            case R.id.accountTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,accountFragment).commit();
                return true;
            default:return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(loginButton)) {
            Intent intent = new Intent(this,LogIn.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptions.makeCustomAnimation(this, R.anim.page_slide_in_right, R.anim.page_slide_out_left).toBundle();
            startActivity(intent,bundle);
        }

    }
    private void logInCheck(Intent intent){
        Log.d("Result","logIncheck");
        if(intent!=null){
            Log.d("Result","logIncheck_ intent not null");
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                Log.d("Result","logIncheck_ bundle not null");
                String logIn = bundle.getString("logIn");
                String userName = bundle.getString("userName");
                if(logIn.equals("true")){
                    Log.d("Result","logIncheck_ logInsetting go");
                    logInSetting(true,userName);
                }else{
                    Log.d("Result","logInCheck log In false");
                }
            }else{
                Log.d("Result","logIncheck_ bundle  null");
            }
        }else{
            Log.d("Result","logIncheck_ intent null");
        }
    }

    public void logInSetting(boolean login, String name){
        Log.d("logIn Result","logInsetting called");
        if(login){
            Log.d("logIn Result","login true");
            //메인페이지 세팅
            if(loginButton!=null){
                loginButton.setVisibility(View.GONE);
            }else{
                Log.d("logIn Result","loginButton is null");
            }

            customerName.setText(name);
            login_success.setVisibility(View.VISIBLE);

            //프레그먼트 세팅
            LOGIN_SUCCESS = true;

        }else{
            Log.d("logIn Result","login false");
            //메인페이지 세팅
            loginButton.setVisibility(View.VISIBLE);
            login_success.setVisibility(View.GONE);

            //프레그먼트 세팅
            LOGIN_SUCCESS = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    public void getMenuDB(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("menus");
        ref.orderByChild("menuDelimiter").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Menu menu = snapshot.getValue(Menu.class);
                menus.add(new Menu(Integer.parseInt(snapshot.getKey()),menu.getMenuDelimiter(),menu.getMenuHotIce(),menu.getMenuImgPath(),
                        menu.getMenuName(),menu.getMenuPrice()));
                Log.d("menuDB","size  "+menus.size());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}