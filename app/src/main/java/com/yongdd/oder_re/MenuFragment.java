package com.yongdd.oder_re;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuFragment extends Fragment {
    static ArrayList<Menu> menus; //메뉴 목록
    static RecyclerView menuRecyclerView;
    static MenuAdapter menuAdapter;
    static FrameLayout menuDetailFragContainer;
    private static Context context;

    static Button orderButton;

    static int orderCount;
    static boolean orderButtonClicked;

    //주문 목록
    static ArrayList<Payment> orderLists = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_fragment,container,false);

        context = view.getContext();
        menus = new ArrayList<>();
        menuDetailFragContainer = view.findViewById(R.id.menuDetailFragContainer);
        orderButton = view.findViewById(R.id.orderButton);
        orderButtonClicked = false;

        //메인화면서 db메뉴 가져옴
        menus = MainActivity.menus;


        //menu List RecyclerView
        RecyclerView menuListRecyclerView = (RecyclerView) view.findViewById(R.id.menuListRecycleview);
        MenuListAdapter menuListAdapter = new MenuListAdapter();
        menuListAdapter.addItem("COFFEE");
        menuListAdapter.addItem("NONCOFFEE");
        menuListAdapter.addItem("TEA");
        menuListAdapter.addItem("JUICE");
        menuListAdapter.addItem("DESSERT");

        menuListRecyclerView.setHasFixedSize(true);
        menuListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        menuListRecyclerView.setAdapter(menuListAdapter);


        //menu RecyclerView
        menuRecyclerView = (RecyclerView)view.findViewById(R.id.menuRecyclerview);
        menuAdapter = new MenuAdapter();
        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allMenuSetting();

        //orderButton clicked
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderButtonClicked = true;
                if(MainActivity.LOGIN_SUCCESS){
                    PaymentFragment paymentFragment = new PaymentFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer,paymentFragment).commit();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("로그인을 먼저 해주세요!")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                }

            }
        });

        //orderButton 안 뜨는 것을 방지
        if(orderCount>0){
            orderButton.setVisibility(View.VISIBLE);
        }


        return view;
    }

    public void menuChoice(int position){
        menuAdapter.clearItem();
       for(int i=0; i<menus.size(); i++){
           Menu menu = menus.get(i);
           int delimiter = menus.get(i).getMenuDelimiter();
           if((position+1) == delimiter){
               if(menu!=null){
                   menuAdapter.addItem(menu);
                   menuRecyclerView.setAdapter(menuAdapter);
                   menuAdapter.notifyDataSetChanged();
               }else{
                   Log.d("menuDB","menuChoice = menu null");
               }

           }else{
               Log.d("menuDB","해당 선택 메뉴 없음");
           }
       }
    }

    public void allMenuSetting(){
        for(int i=0; i<menus.size(); i++) {
            Menu menu = menus.get(i);
            if (menu != null) {
                menuAdapter.addItem(menu);
                menuRecyclerView.setAdapter(menuAdapter);
                menuAdapter.notifyDataSetChanged();
            } else {
                Log.d("menuDB", "menuChoice = menu null");
            }
        }
    }

    public static void menuDetailShow(boolean show){
        int duration = 200;

        Animation pageUpAnim = AnimationUtils.loadAnimation(context, R.anim.page_slide_up);
        pageUpAnim.setDuration(duration);
        Animation pageDownAnim = AnimationUtils.loadAnimation(context, R.anim.page_slide_down);
        pageDownAnim.setDuration(duration);

        if(show) {
            menuDetailFragContainer.setVisibility(View.VISIBLE);
            menuDetailFragContainer.setAnimation(pageUpAnim);
        }else{
            menuDetailFragContainer.setVisibility(View.GONE);
            menuDetailFragContainer.setAnimation(pageDownAnim);
        }

    }

    public void setOrderButton(boolean order, int count){
        orderCount +=count;

        if(order){
            orderButton.setVisibility(View.VISIBLE);
            orderButton.setText(orderCount+"개");
        }else{
            orderButton.setVisibility(View.GONE);
        }
    }

    public void OrderPlus(Payment orderItem){

        orderLists.add(orderItem);
    }

    public void orderCountMinus(int count){
        orderCount-=count;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(orderCount>0){
            setOrderButton(true,0);
        }

    }
}
