package com.sunyardraofa.zhihudaily.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunyardraofa.zhihudaily.R;
import com.sunyardraofa.zhihudaily.adapter.StoryAdapter;
import com.sunyardraofa.zhihudaily.adapter.ViewPageAdapter;
import com.sunyardraofa.zhihudaily.gson.Latest;
import com.sunyardraofa.zhihudaily.gson.Story;
import com.sunyardraofa.zhihudaily.gson.TopStory;
import com.sunyardraofa.zhihudaily.network.NetWork;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    ViewPager viewPager ;
    TabLayout tab;
    RecyclerView recv;
    Context mContext;
    List<TopStory> topStories = new ArrayList<>();
    ViewPageAdapter viewPageAdapter;
    List<Story> stories = new ArrayList<>();
    StoryAdapter storyAdapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        viewPager = view.findViewById(R.id.home_viewPager);
        tab = view.findViewById(R.id.dot_indicator);
        recv = view.findViewById(R.id.home_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recv.setLayoutManager(manager);
        
        viewPageAdapter = new ViewPageAdapter(mContext,topStories);
        viewPager.setAdapter(viewPageAdapter);

        storyAdapter = new StoryAdapter(mContext,stories);
        recv.setAdapter(storyAdapter);
        
        tab.setupWithViewPager(viewPager);
        
        initcache();

        refreshLayout = view.findViewById(R.id.home_refresh);
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        
        refreshLayout.setRefreshing(true);
        getData();
        
        return view;
    }

    private void initcache() {
        SharedPreferences preferences = mContext.getSharedPreferences("home_data",Context.MODE_PRIVATE);
        String cachedata = preferences.getString("latestjson",null);
        if(cachedata != null) {
            Gson gson = new Gson();
            Latest latest = gson.fromJson(cachedata,Latest.class);
            viewPageAdapter.setData(latest.top_stories);

            storyAdapter.setData(latest.stories);
        }
    }

    @SuppressLint("CheckResult")
    private void getData() {
        NetWork.getZhihuDailyApi().getlatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Latest>() {
                    @Override
                    public void accept(Latest latest) throws Exception {

                        Gson gson = new Gson();
                        String latestjson = gson.toJson(latest);

                        SharedPreferences preferences = mContext.getSharedPreferences("home_data",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("latestjson",latestjson);
                        editor.commit();
                        
                        viewPageAdapter.setData(latest.top_stories);
                        
                        storyAdapter.setData(latest.stories);
                        
                        refreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Toast.makeText(mContext, "网络开小差了...", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });
    }
}
