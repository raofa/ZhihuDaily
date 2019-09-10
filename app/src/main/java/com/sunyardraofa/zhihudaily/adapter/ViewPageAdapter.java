package com.sunyardraofa.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunyardraofa.zhihudaily.R;
import com.sunyardraofa.zhihudaily.gson.TopStory;
import com.sunyardraofa.zhihudaily.view.StoryActivity;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    
    private Context mContext;
    private List<TopStory> datalist;
    
    public ViewPageAdapter(Context context,List<TopStory> list){
        this.mContext = context;
        this.datalist = list;
    }
    
    
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpage_item,container,false);
        ImageView topImage = view.findViewById(R.id.top_image);
        TextView toptitle = view.findViewById(R.id.top_title);
        Glide.with(mContext)
                .load(datalist.get(position).image)
                .into(topImage);
        toptitle.setText(datalist.get(position).title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoryActivity.class);
                intent.putExtra("story_id",datalist.get(position).id+"");
                mContext.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    
    public void setData(List<TopStory> data){
        datalist.clear();
        datalist.addAll(data);
        notifyDataSetChanged();
    }
}
