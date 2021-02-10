package com.yongdd.oder_re;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bannerRecyclerview);
        BannerAdapter adapter = new BannerAdapter();

        //차후 수정
        adapter.addItem(new Banner("새로운 메뉴","딸기 요거트","신선한 딸기와 수제요거트의 만남",
                0,"R.drawable.strawberry_cake"));
        adapter.addItem(new Banner("추천 메뉴","딸기 듬뿍 케이크","봄 한정으로 돌아온 인기 메뉴! \n 달달한 딸기가 한가득",
                0,"R.drawable.strawberry_cake"));
        adapter.addItem(new Banner("이달의 메뉴","딸기 에이드","봄 기운 만끽~ \n 수제 딸기청으로 만든 상큼 에이드",
                0,"R.drawable.strawberry_cake"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
