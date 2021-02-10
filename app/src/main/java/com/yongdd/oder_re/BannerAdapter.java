package com.yongdd.oder_re;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    ArrayList<Banner> banners = new ArrayList<>();

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_banner,parent,false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = banners.get(position);
        holder.setItem(banner);
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }


    public void addItem(Banner banner){
        banners.add(banner);
    }

    public Banner getItem(int position){
        return banners.get(position);
    }


    public class BannerViewHolder extends RecyclerView.ViewHolder{
        TextView menuTitle;
        TextView menuName;
        TextView menuDesc;
        Button menuGo;
        ImageView menuImg;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            menuTitle = itemView.findViewById(R.id.banner_Title);
            menuName = itemView.findViewById(R.id.banner_MenuName);
            menuDesc = itemView.findViewById(R.id.banner_MenuDesc);
            menuGo = itemView.findViewById(R.id.banner_menuGo);
            menuImg = itemView.findViewById(R.id.banner_img);

        }
        public void setItem(Banner banner){
            String bannerTitleName = banner.getTitleName();
            String bannerMenuName = banner.getMenuName();
            String bannerMenuDesc = banner.getMenuDesc();
            int bannerMenuId = banner.getMenuId();
            String bannerMenuImgPath = banner.getMenuImgPath();

            menuTitle.setText(bannerTitleName);
            menuName.setText(bannerMenuName);
            menuDesc.setText(bannerMenuDesc);
            menuGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //프래그먼트 이동
                }
            });
            //차후 이미지 glide 등으로 대체
            menuImg.setImageResource(R.drawable.strawberry_cake);



        }
    }
}
