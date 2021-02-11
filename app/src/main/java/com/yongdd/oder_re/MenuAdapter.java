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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    ArrayList<Menu> menus = new ArrayList<>();

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu,parent,false);
        return new MenuViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);
        holder.setItem(menu);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }


    public void addItem(Menu menu){
        menus.add(menu);
    }

    public Menu getItem(int position){
        return menus.get(position);
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder{
        ImageView menuImage;
        TextView menuName;
        TextView menuPrice;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.M_menuImg);
            menuName = itemView.findViewById(R.id.M_menuName);
            menuPrice = itemView.findViewById(R.id.M_menuPrice);

        }
        public void setItem(Menu menu){
            //차후 변경
            menuImage.setImageResource(R.drawable.strawberry_cake);

            menuName.setText(menu.getMenuName());
            menuPrice.setText(Integer.toString(menu.getMenuPrice()));

        }
    }
}
