package com.yongdd.oder_re;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Join extends Activity implements View.OnClickListener, View.OnFocusChangeListener {
    Button xButton,registerButton;
    EditText emailText,passwordText,passwordConfirmText,nameText,phoneNumText;
    TextView passwordConfirmAnnounce,emailConfirmText,passwordNotice,passwordValid;
    ConstraintLayout loadingLayout;

    boolean passwordCheck;
    static boolean emailCheckCompleted;
    static boolean passwordCheckCompleted;

    private FirebaseAuth mAuth;
    final String TAG = "JOIN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        //뷰 초기화
        xButton = findViewById(R.id.J_xButton);
        registerButton = findViewById(R.id.registerButton);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        passwordConfirmText = findViewById(R.id.passwordConfirmText);
        nameText = findViewById(R.id.nameText);
        phoneNumText = findViewById(R.id.phoneNumText);
        passwordConfirmAnnounce = findViewById(R.id.passwordConfirmAnnounce);
        emailConfirmText = findViewById(R.id.emailConfirmText);
        passwordNotice = findViewById(R.id.passwordNotice);
        passwordValid = findViewById(R.id.passwordValid);
        loadingLayout = findViewById(R.id.loadingLayout);

        //클릭 리스너 관련
        xButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //text confirm 관련
        passwordConfirmText.addTextChangedListener(passwordCheckListen);
        passwordText.setOnFocusChangeListener(this);
        emailText.addTextChangedListener(emailInput);
        passwordCheck = false;
        passwordText.addTextChangedListener(passwordInput);
        phoneNumText.addTextChangedListener(phoneNumberChange);
        passwordCheckCompleted = false;
        emailCheckCompleted = false;


        //파이어베이스 회원가입 관련
        mAuth = FirebaseAuth.getInstance();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if(v==xButton){
            onBackPressed();
        }else if(v==registerButton){
            loadingLayout.setVisibility(View.VISIBLE);
            ProgressBar proBar = (ProgressBar) findViewById(R.id.progressBar);
            if (proBar != null) {
                proBar.setIndeterminate(true);
                proBar.setIndeterminateTintList(ColorStateList.valueOf(Color.rgb(175,18,18)));
            }
            createAccount();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);
    }

    private void createAccount(){

        if(wholeCheck()) {
            //가입 내용 가져오기
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            String name = nameText.getText().toString().trim();
            String phoneNumber = phoneNumText.getText().toString().trim();

            //등록 위한 세팅
            User user = new User(email, name, phoneNumber,0, "");

            //db 등록 위한 maxCount가져오기
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users");
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long maxCount = snapshot.getChildrenCount();
                    getMaxId(maxCount,user,password);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            Log.d(TAG,"확인 안된 부분이 있음");
        }
    }

    private void getMaxId(long maxCount,User user,String password){
        TreeSet<Integer> Ids = new TreeSet<>();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users");
        database.orderByChild("userName").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ids.add(Integer.parseInt(snapshot.getKey()));

                Log.d(TAG,"id "+Integer.parseInt(snapshot.getKey()));

                Log.d(TAG,"maxCount "+maxCount);

                if(Ids.size()==maxCount){
                    Log.d(TAG,"Ids.size == maxCount");
                    maxId(Ids,user,password);
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



    private void maxId(TreeSet<Integer> ids,User user,String password){
        int maxId = ids.last();
        Log.d(TAG,"max second id :"+maxId);
        if(maxId>0){
            addDB(maxId,user,password);
        }
    }

    private void addDB(int maxId,User user,String password){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users");
        database.child(String.valueOf(maxId + 1)).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "addDB complete");
                        addAuthUser(user, password);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "addDB failed" + e.toString());
                    Toast.makeText(Join.this, "회원등록에 실패하였습니다.", Toast.LENGTH_SHORT);
                }
            });

    }
    private void addAuthUser(User user, String password){
         mAuth.createUserWithEmailAndPassword(user.getUserEmail(),password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");

                                addUserName(user.getUserName());

                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Join.this, "회원가입 실패 : 이미 존재하는 이메일입니다.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

    }

    public void addUserName(String name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT);
                            loadingLayout.setVisibility(View.GONE);
                            onBackPressed();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"error :" +e);
                Toast.makeText(Join.this,"회원등록에 실패하였습니다.",Toast.LENGTH_SHORT);
            }
        });
    }

    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true; }
        return err;
    }

    TextWatcher emailInput = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String email = s.toString();
            if(isValidEmail(email)){
                emailConfirmText.setVisibility(View.GONE);
                emailCheckCompleted = true;
            }else{
                emailConfirmText.setVisibility(View.VISIBLE);
                emailCheckCompleted  = false;
            }

        }
    };
    TextWatcher passwordInput = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count<before){
                if(s.length()==0){
                    passwordValid.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputPassword = s.toString();
            String checkedPassword = passwordConfirmText.getText().toString().trim();

            if(inputPassword.length()>0){
                if(passwordValidCheck(inputPassword)){
                    Log.d("JOIN","password ok");
                    passwordValid.setVisibility(View.GONE);
                }else{
                    Log.d("JOIN","password not ok");
                    passwordValid.setVisibility(View.VISIBLE);
                }

            }

            if(passwordCheck){
                if(inputPassword.equals(checkedPassword)||inputPassword==checkedPassword){
                    passwordConfirmAnnounce.setVisibility(View.GONE);
                    passwordText.setTextColor(getResources().getColor(R.color.mainColor));
                    passwordConfirmText.setTextColor(getResources().getColor(R.color.mainColor));
                }else{
                    passwordConfirmAnnounce.setVisibility(View.VISIBLE);
                    passwordConfirmAnnounce.setText("비밀번호가 일치하지 않습니다.");
                    passwordText.setTextColor(getResources().getColor(R.color.black));
                    passwordConfirmText.setTextColor(getResources().getColor(R.color.black));
                }
            }



        }
    };

    TextWatcher passwordCheckListen= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputPassword = s.toString();
            String password = passwordText.getText().toString().trim();
            if(inputPassword == null || inputPassword.equals("")){
                passwordConfirmAnnounce.setVisibility(View.GONE);
            }else{
                if(password.equals(inputPassword) || password == inputPassword){
                    passwordConfirmAnnounce.setVisibility(View.GONE);
                    passwordText.setTextColor(getResources().getColor(R.color.mainColor));
                    passwordConfirmText.setTextColor(getResources().getColor(R.color.mainColor));
                    passwordCheck = true;


                }else{
                    passwordConfirmAnnounce.setVisibility(View.VISIBLE);
                    passwordConfirmAnnounce.setText("비밀번호가 일치하지 않습니다.");
                    passwordText.setTextColor(getResources().getColor(R.color.black));
                    passwordConfirmText.setTextColor(getResources().getColor(R.color.black));
                    passwordCheck = false;
                }
            }
        }
    };

    TextWatcher phoneNumberChange = new TextWatcher() {
        boolean deleteCheck = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(count>before){
                deleteCheck = true;
            }else if(count<before){
                deleteCheck = false;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(deleteCheck){
                if (s.length() == 3 || s.length() == 8) {
                    s.append("-");
                }
            }

        }
    };


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v==passwordText) {
            if(hasFocus){
                passwordNotice.setVisibility(View.VISIBLE);
            }else {
                passwordNotice.setVisibility(View.GONE);
            }
        }
    }

    private Boolean passwordValidCheck(String inputPassword){
        Log.d("JOIN","passwordValidCheck called "+inputPassword);
        Pattern p = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
        Matcher m = p.matcher(inputPassword);

        if(inputPassword.length()>7){
            Log.d("JOIN","passwordValidCheck -length>7");
            if(m.find()){
                Log.d("JOIN","passwordValidCheck -특수문자, 숫자 ok");
               if(!spaceCheck(inputPassword)){
                   Log.d("JOIN","passwordValidCheck -공백 없음");
                   return true;
               }
            }
        }
        return false;
    }

    private boolean spaceCheck(String spaceCheck){
        Log.d("JOIN","spaceCheck called");
        for(int i = 0 ; i < spaceCheck.length() ; i++) {
            if(spaceCheck.charAt(i) == ' '){
                Log.d("JOIN","spaceCheck -space 있음");
                return true;
            }

        }
        return false;
    }

   private boolean wholeCheck(){
        if(emailText.getText().toString().length()>0 && emailCheckCompleted == true){
            if(passwordCheck){
                if(nameText.getText().toString().length()>0){
                    if(phoneNumText.getText().toString().length()==13){
                        return true;
                    }else{
                        Toast.makeText(Join.this,"전화번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Join.this,"이름을 확인해주세요",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Join.this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Join.this,"이메일을 확인해주세요",Toast.LENGTH_SHORT).show();
        }
        return false;
   }
/*    public void buildActionCodeSettings() {

        FirebaseUser user = mAuth.getCurrentUser();
        String url = "https://yongdd.page.link/join";
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl(url)
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.yongdd.order_re")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, *//* installIfNotAvailable *//*
                                "12"    *//* minimumVersion *//*)
                        .build();


    }*/


    /*private void sendEmailVerification(String email) {
        Log.d(TAG, "sendEmailVerification called.");
        final FirebaseUser user = mAuth.getCurrentUser();

        String url = "https://oder-e6555.firebaseapp.com";
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl(url)
                        .setDynamicLinkDomain("https://yongdd.page.link/main")
                        // This must be true
                        .setAndroidPackageName(
                                "com.yongdd.oder_re",
                                true,
                                "16"   )
                        .build();


//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "Email sent.");
//                        }else{
//                            Log.d(TAG, "Email doesn't sent."+task.getException());
//                        }
//                    }
//                });
        user.sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Join.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                                EmailCheck emailCheck = new EmailCheck();
                                emailCheck.onDynamicLinkClick();


                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(Join.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();


                        }

                    }
                });

    }*/



}
