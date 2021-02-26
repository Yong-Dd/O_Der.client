package com.yongdd.oder_re;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {
    static ArrayList<Banner> banners = new ArrayList<>();
    static ArrayList<Uri> uris = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        Log.d("Banner","create");

        ProgressBar proBar = (ProgressBar) findViewById(R.id.progressBar);
        if (proBar != null) {
            proBar.setIndeterminate(true);
            proBar.setIndeterminateTintList(ColorStateList.valueOf(Color.rgb(175,18,18)));
        }

        getBannerDB();

    }

    public void getBannerDB(){
        Log.d("Banner","getBannerDB called");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("Banner","getBannerDB database");
        DatabaseReference ref = database.getReference("banners");
        Log.d("Banner","getBannerDB reference");
        ref.orderByChild("titleName").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Banner banner = snapshot.getValue(Banner.class);
                getImageUri(banner);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Log.d("Banner","getBannerDB error "+error);}
        });

    }

    public void getImageUri(Banner banner){
        Log.d("Banner","imageUri called");

        String imgPath = banner.getMenuImgPath();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://oder-e6555.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child(imgPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                banners.add(banner);
                uris.add(uri);
                if((banners.size()>=3)&&(uris.size()>=3)){
                    Log.d("Banner","banner 넘김"+banners.size());
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("banner",banners);
                    intent.putExtra("uri",uris);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Banner","Uri 오류 "+exception);
            }
        });

    }
}
