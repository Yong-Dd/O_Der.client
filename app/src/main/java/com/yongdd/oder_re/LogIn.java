package com.yongdd.oder_re;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends Activity implements View.OnClickListener {
    Button joinButton, logInButton;
    ImageButton xButton;
    EditText idText, passwordText;

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

        mainActivity = new MainActivity();

        //버튼 클릭리스너 등록
        joinButton.setOnClickListener(this);
        xButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

    }

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

                                Intent intent = new Intent(LogIn.this,MainActivity.class);
                                intent.putExtra("logIn","true");
                                intent.putExtra("userName",name);
                                startActivityForResult(intent,100);
                                startActivity(intent);
                                overridePendingTransition(R.anim.page_slide_in_left,R.anim.page_slide_out_right);

                            }else{
                                Log.d(TAG,"doen't have user name");
                            }


                        }else{
                            Log.d(TAG,"logIn failed");
                            Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
        );
    }
}
