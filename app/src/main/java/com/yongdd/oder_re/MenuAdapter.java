package com.yongdd.oder_re;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    ArrayList<MenuUri> menus = new ArrayList<>();
    Context context;
    MenuDetailFragment menuDetailFragment;
    static int lastPosition;

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu,parent,false);

        context = parent.getContext();

        menuDetailFragment = new MenuDetailFragment();


        return new MenuViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuUri menuUri = menus.get(position);
        holder.setItem(menuUri);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }


    public void addItem(MenuUri menuUri){
        menus.add(menuUri);
    }

    public MenuUri getItem(int position){
        return menus.get(position);
    }

    public void clearItem(){
        menus.clear();
    }

    public int updateItem(MenuUri menuUri){
        menus.set(lastPosition,menuUri);
        return lastPosition;
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView menuImage;
        TextView menuName,menuPrice;
        LinearLayout menuInfo;
        final DecimalFormat priceFormat = new DecimalFormat("###,###");

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.M_menuImg);
            menuName = itemView.findViewById(R.id.M_menuName);
            menuPrice = itemView.findViewById(R.id.M_menuPrice);
            menuInfo = itemView.findViewById(R.id.M_menuInfo);

            menuImage.setOnClickListener(this);
            menuInfo.setOnClickListener(this);

        }
        public void setItem(MenuUri menuUri){
            Menu menu = menuUri.getMenu();
            Uri uri = menuUri.getUri();

            //이미지 설정
            if(uri!=null){
                Glide.with(context).load(menuUri.getUri()).into((ImageView) menuImage);
            }else{
                menuImage.setImageResource(R.drawable.standard_img);
            }

            //이미지 이름 설정
            menuName.setText(menu.getMenuName());

            //가격 설정
            String itemPriceFormat = priceFormat.format(menu.getMenuPrice());
            menuPrice.setText(itemPriceFormat+"원");

        }

        @Override
        public void onClick(View v) {
            if(v==menuImage || v==menuInfo){
              int position = getAdapterPosition();
              lastPosition = position;
              MenuUri menuUri = menus.get(position);
              MenuDetailFragment.setItem(menuUri);
              MenuFragment.menuDetailShow(true);
            }
        }
    }
}
