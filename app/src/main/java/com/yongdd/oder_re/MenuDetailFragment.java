package com.yongdd.oder_re;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuDetailFragment extends Fragment implements View.OnClickListener {
    static Button xButton, minusButton, plusButton, menuAddButton ,hotButton, iceButton;
    static LinearLayout hotAndIce, hot, ice;
    static TextView menuCountText, menuPriceText, menuNameText;

    static int lastTotalPrice;
    static int price;
    static int totalCount;
    static String lastMenuName;
    static int lastMenuId;
    static String lastHotIce;
    static int menuDelimiter;

    final static DecimalFormat priceFormat = new DecimalFormat("###,###");

    static View view;

    MenuFragment menuFragment = new MenuFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_detail_fragment, container, false);


        //hot ice setting
        hotAndIce = view.findViewById(R.id.hotAndIce);
        hot = view.findViewById(R.id.hot);
        ice = view.findViewById(R.id.ice);
        hotButton = view.findViewById(R.id.MD_hotButton);
        iceButton = view.findViewById(R.id.MD_IceButton);
        hotButton.setOnClickListener(this);
        iceButton.setOnClickListener(this);



        //count , price setting
        minusButton = view.findViewById(R.id.MD_minusButton);
        plusButton = view.findViewById(R.id.MD_plusButton);
        minusButton.setOnClickListener(this);
        plusButton.setOnClickListener(this);
        menuCountText = view.findViewById(R.id.MD_menuCountText);
        menuPriceText = view.findViewById(R.id.MD_menuPriceText);

        //기타
        xButton = view.findViewById(R.id.xButton);
        xButton.setOnClickListener(this);
        menuAddButton = view.findViewById(R.id.MD_menuAddButton);
        menuAddButton.setOnClickListener(this);
        menuNameText = view.findViewById(R.id.MD_menuNameText);



        return view;
    }
    public static void setItem(Menu menu){

        //해당 menu content
        int menuId = menu.getMenuId();
        menuDelimiter = menu.getMenuDelimiter();
        String menuName = menu.getMenuName();
        String menuImgPath = menu.getMenuImgPath();
        int hotIce = menu.getMenuHotIce();
        int menuPrice = menu.getMenuPrice();

        //이미지 설정
        if(menuImgPath!=null){

        }

        //count 설정
        totalCount = 1;
        menuCountText.setText(totalCount+"개");

        //hotIce 설정
        if(hotIce>0){
            lastHotIce="";
            hotIceUISetting(hotIce);


            hotButton.setBackgroundResource(R.drawable.main_button);
            iceButton.setBackgroundResource(R.drawable.rect);
        }

        //아이디 설정
        if(menuId>=0){
            lastMenuId=menuId;
        }else{
            lastMenuId=-1;
        }

        //이름 설정
        if(menuName!=null){
            menuNameText.setText(menuName);
            lastMenuName = menuName;
        }else{
            menuNameText.setText("");
            lastMenuName = "";
        }


        //가격 설정
        if(menuPrice>0){
            price = menuPrice;
            lastTotalPrice = menuPrice;

            String totalPriceFormat = priceFormat.format(menuPrice);
            menuPriceText.setText(totalPriceFormat+"원");
        }else{
            lastTotalPrice=0;
        }

    }
    public static void hotIceUISetting(int hotIce){
        switch (hotIce){
            case 1:
                lastHotIce="hot";
                hotAndIce.setVisibility(View.VISIBLE);
                ice.setVisibility(View.GONE);
                hot.setVisibility(View.GONE);
                break;

            case 2:
                ice.setVisibility(View.VISIBLE);
                hot.setVisibility(View.GONE);
                hotAndIce.setVisibility(View.GONE);
                break;

            case 3:
                hot.setVisibility(View.VISIBLE);
                ice.setVisibility(View.GONE);
                hotAndIce.setVisibility(View.GONE);
                break;

            case 4:
                hot.setVisibility(View.GONE);
                ice.setVisibility(View.GONE);
                hotAndIce.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if(v==xButton){
            MenuFragment.menuDetailShow(false);
        }else if(v==plusButton){
            countSetting(true);
        }else if(v==minusButton){
            countSetting(false);
        }else if(v==hotButton){
            lastHotIce = hotIce(true);
            hotButton.setBackgroundResource(R.drawable.main_button);
            iceButton.setBackgroundResource(R.drawable.rect);
        }else if(v==iceButton){
            lastHotIce = hotIce(false);
            iceButton.setBackgroundResource(R.drawable.main_button);
            hotButton.setBackgroundResource(R.drawable.rect);
        }else if(v==menuAddButton){
            orderListAdd(new Payment(lastMenuId,menuDelimiter,lastMenuName,lastTotalPrice,totalCount,lastHotIce));
            menuFragment.setOrderButton(true,totalCount);
            menuFragment.menuDetailShow(false);
        }
    }


    public void countSetting(boolean plusCount){


        ArrayList<Integer> countAndPrice = new ArrayList<>();

        if(plusCount){
            totalCount+=1;
            menuCountText.setText(totalCount+"개");
            lastTotalPrice = priceSetting(totalCount);

        }else{
            totalCount-=1;
            if(totalCount>=1){
                menuCountText.setText(totalCount+"개");
                lastTotalPrice = priceSetting(totalCount);
            }else{
                totalCount=1;
                menuCountText.setText(totalCount+"개");
                lastTotalPrice = priceSetting(totalCount);
            }
        }

    }

    public int priceSetting(int count){

        int totalPrice = price*count;

        String totalPriceFormat = priceFormat.format(totalPrice);
        String totalPriceText = totalPriceFormat+"원";
        menuPriceText.setText(totalPriceText);

        return totalPrice;
    }

    public String hotIce(boolean hot){
        String hotAndIce = "";

        if(hot){
            hotAndIce = "hot";
        }else{
            hotAndIce = "ice";
        }
        return hotAndIce;
    }

    public void orderListAdd(Payment orderItem){

        String name = orderItem.getMenuName();
        String hotIce = orderItem.getMenuHotIce();
        int count = orderItem.getMenuTotalCount();
        int price = orderItem.getMenuTotalPrice();
        int id = orderItem.getMenuId();
        int delimiter = orderItem.getMenuDelimiter();

        Payment payment = new Payment(id,delimiter,name,price,count,hotIce);

        menuFragment.OrderPlus(payment);
    }

}
