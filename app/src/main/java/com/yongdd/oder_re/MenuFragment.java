package com.yongdd.oder_re;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuFragment extends Fragment {
    static ArrayList<Menu> menus;
    static RecyclerView menuRecyclerView;
    static MenuAdapter menuAdapter;
    static FrameLayout menuDetailFragContainer;
    private static Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment,container,false);

        context = view.getContext();
        menus = new ArrayList<>();
        menuDetailFragContainer = view.findViewById(R.id.menuDetailFragContainer);

        getMenu();

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


        return view;
    }
    public void getMenu(){
        if(MainActivity.menus.size()>0){
            for(int i=0; i<MainActivity.menus.size(); i++){
                Menu menu = MainActivity.menus.get(i);
                if(menu!=null){
                    menus.add(menu);
                    Log.d("menuDB","MenuFragment menus size  "+menus.size());
                }else{
                    Log.d("menuDB","MainActivity menus's item null");
                }
            }
        }else{
            Log.d("menuDB","MainActivity menus size null");
        }

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

}
