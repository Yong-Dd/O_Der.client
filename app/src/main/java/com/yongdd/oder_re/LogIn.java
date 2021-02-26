package com.yongdd.oder_re;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends Activity implements View.OnClickListener {
    Button joinButton, logInButton;
    ImageButton xButton;
    EditText idText, passwordText;
    ConstraintLayout loadingLayout;

    String TAG = "LOGIN";
    MainActivity mainActivity;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        joinButton = findViewById(R.id.joinButton);
        xButton = findViewById(R.id.L_xButton);
        logInButton = findViewById(R.id.loginButton);
        idText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.pwText);
        loadingLayout = findViewById(R.id.loadingLayout);

        mainActivity = new MainActivity();

        //버튼 클릭리스너 등록
        joinButton.setOnClickListener(this);
        xButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if(v==joinButton){
            Intent intent = new Intent(getApplicationContext(),Join.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.page_slide_in_right, R.anim.page_slide_out_left).toBundle();
            startActivity(intent,bundle);
        }else if(v==xButton){
            onBackPressed();
        }else if(v==logInButton){

            loadingLayout.setVisibility(View.VISIBLE);

            ProgressBar proBar = (ProgressBar) findViewById(R.id.progressBar);
            if (proBar != null) {
                proBar.setIndeterminate(true);
                proBar.setIndeterminateTintList(ColorStateList.valueOf(Color.rgb(175,18,18)));
            }

            String emailId = idText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            logIn(emailId, password);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.page_slide_in_left, R.anim.page_slide_out_right);
    }

    private void logIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"logIn completed");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {

                                String name = user.getDisplayName();
                                loadingLayout.setVisibility(View.GONE);
                                finish();
                                mainActivity.logInSetting(true,name);
                                overridePendingTransition(R.anim.page_slide_in_left,R.anim.page_slide_out_right);

                            }


                        }else{
                            Log.d(TAG,"logIn failed");
                            loadingLayout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
        );
    }
}
