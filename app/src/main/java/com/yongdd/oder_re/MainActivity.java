package com.yongdd.oder_re;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    HomeFragment homeFragment;
    MenuFragment menuFragment;
    static PaymentFragment paymentFragment;
    AccountFragment accountFragment;

    static Button loginButton;
    static LinearLayout login_success;
    static TextView customerName;

    public static ArrayList<MenuUri> menus;


    public static boolean LOGIN_SUCCESS;

    static boolean accountClicked;

    private AlertDialog exitAlertDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    static String USER_ID;
    static int CURRENT_STAMP;

    static ArrayList<Banner> banners;
    static ArrayList<Uri> uris;

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

        USER_ID = "-1";


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

        //homeFragment banner setting
        Intent intent = getIntent();
        if(intent!=null){
            banners = intent.getParcelableArrayListExtra("banner");
            uris = intent.getParcelableArrayListExtra("uri");
            Log.d("Banner","main banner size: "+banners.size());
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,homeFragment).commit();
        }


        // 메뉴 가져오기
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
            case R.id.accountTab:
                accountClicked = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,accountFragment).commit();
                return true;
            default:return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(loginButton)) {
            Intent intent = new Intent(this,LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptions.makeCustomAnimation(this, R.anim.page_slide_in_right, R.anim.page_slide_out_left).toBundle();
            startActivity(intent,bundle);
        }

    }

    public void logInSetting(boolean login, String name){
        if(login){
            //메인페이지 세팅
            if(loginButton!=null){
                loginButton.setVisibility(View.GONE);
            }

            customerName.setText(name);
            login_success.setVisibility(View.VISIBLE);

            //프레그먼트 세팅
            LOGIN_SUCCESS = true;

            //User Id setting
            getUserId();
            getStampCount();


        }else{
            Log.d("logIn Result","login false");
            //메인페이지 세팅
            loginButton.setVisibility(View.VISIBLE);
            login_success.setVisibility(View.GONE);

            //프레그먼트 세팅
            LOGIN_SUCCESS = false;
        }
    }


    public void getMenuDB(){
        DatabaseReference ref = database.getReference("menus");
        ref.orderByChild("menuDelimiter").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Menu menu = snapshot.getValue(Menu.class);
                getMenuImgUri(menu,Integer.parseInt(snapshot.getKey()));

                Log.d("menuDB","size  "+menus.size());
                Log.d("menuDB","id  "+snapshot.getKey());


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Menu menu = snapshot.getValue(Menu.class);
                getMenuImgUriChange(menu,Integer.parseInt(snapshot.getKey()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                deleteMenu(Integer.parseInt(snapshot.getKey()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMenuImgUri(Menu menu,int menuId){

        String imgPath = menu.getMenuImgPath();
        if(imgPath.equals("") || imgPath == ""){
            menus.add(new MenuUri(new Menu(menuId,menu.getMenuDelimiter(),menu.getMenuHotIce(),menu.getMenuImgPath(),
                    menu.getMenuName(),menu.getMenuPrice()),null));
        }else{
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://oder-e6555.appspot.com");
            StorageReference storageRef = storage.getReference();
            storageRef.child(imgPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    menus.add(new MenuUri(new Menu(menuId,menu.getMenuDelimiter(),menu.getMenuHotIce(),menu.getMenuImgPath(),
                            menu.getMenuName(),menu.getMenuPrice()),uri));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    menus.add(new MenuUri(new Menu(menuId,menu.getMenuDelimiter(),menu.getMenuHotIce(),menu.getMenuImgPath(),
                            menu.getMenuName(),menu.getMenuPrice()),null));
                }
            });
        }
    }

    public void getUserId(){
        Log.d("userId","userid start: "+USER_ID);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String email = user.getEmail();
            Log.d("userId","userid user not null: "+USER_ID);
            if(email!=null||email.equals("")){
                Log.d("userId","userid user email not null: "+USER_ID);
                Log.d("userId","userid user email not null email: "+email);
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("users");
                ref.orderByChild("userEmail").equalTo(email).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        USER_ID = snapshot.getKey();
                        Log.d("userId","userid end: "+USER_ID);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        USER_ID = snapshot.getKey();
                        Log.d("userId","userid end changed: "+USER_ID);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("userId","userid error: "+error);
                    }
                });
            }else{
                Log.d("Main","getUserId__email null");
            }
        }else{
            Log.d("Main","getUserId__Login user null");
        }

    }

    public void getMenuImgUriChange(Menu menu, int menuId){

        String imgPath = menu.getMenuImgPath();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://oder-e6555.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child(imgPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                for(int i=0; i<menus.size(); i++) {
                    int menu_id = menus.get(i).getMenu().getMenuId();
                    if(menu_id==menuId) {
                        menus.set(i,new MenuUri(menu,uri));
                        menuFragment.updateItem(i,new MenuUri(menu,uri));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"수정된 메뉴를 새로 불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show();
                for(int i=0; i<menus.size(); i++) {
                    int menu_id = menus.get(i).getMenu().getMenuId();
                    if(menu_id==menu_id) {
                        menus.set(i,new MenuUri(menu,null));
                    }
                }
            }
        });
    }

    public void deleteMenu(int menuId){
        for(int i=0; i<menus.size(); i++) {
            int menu_id = menus.get(i).getMenu().getMenuId();
            if(menu_id==menuId) {
                menus.remove(i);
                menuFragment.deleteMenu(i);
            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true); // 태스크를 백그라운드로 이동
                        finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
                        android.os.Process.killProcess(android.os.Process.myPid()); // 앱 프로세스 종료
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(175,18,18));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();

    }

    public void getStampCount(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        Log.d("stamp","email:"+email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.orderByChild("userEmail").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    int curretStamp = child.child("userStamp").getValue(Integer.class);
                    CURRENT_STAMP = curretStamp;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exitAlertDialog != null) {
            exitAlertDialog.dismiss();
        }


    }
}