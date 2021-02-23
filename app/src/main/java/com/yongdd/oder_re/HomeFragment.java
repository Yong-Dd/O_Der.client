package com.yongdd.oder_re;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class HomeFragment extends Fragment {
    static RecyclerView bannerRecyclerView;
    static BannerAdapter bannerAdapter;
    static View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);
        bannerRecyclerView = (RecyclerView) view.findViewById(R.id.bannerRecyclerview);
        bannerRecyclerView.setHasFixedSize(true);
        bannerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setBanner(MainActivity.banners,MainActivity.uris);

        return view;
    }

    public void setBanner(ArrayList<Banner> banners, ArrayList<Uri> uris){
        RecyclerView bannerRecyclerView = view.findViewById(R.id.bannerRecyclerview);
        bannerRecyclerView.setHasFixedSize(true);
        bannerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        BannerAdapter bannerAdapter = new BannerAdapter();
        if(banners.size()==uris.size()) {
            for (int i = 0; i < banners.size(); i++) {
                Banner banner = banners.get(i);
                Uri uri = uris.get(i);
                bannerAdapter.addItem(new BannerUri(banner,uri));
                bannerRecyclerView.setAdapter(bannerAdapter);
                bannerAdapter.notifyDataSetChanged();

            }
        }

    }
}
