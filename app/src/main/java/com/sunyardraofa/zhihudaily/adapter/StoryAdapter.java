package com.sunyardraofa.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunyardraofa.zhihudaily.R;
import com.sunyardraofa.zhihudaily.gson.Story;
import com.sunyardraofa.zhihudaily.gson.StoryExtra;
import com.sunyardraofa.zhihudaily.network.NetWork;
import com.sunyardraofa.zhihudaily.view.StoryActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    
    List<Story> storyList;
    Context mContext;
    
    public StoryAdapter(Context context, List<Story> datalist){
        this.mContext = context;
        this.storyList = datalist;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder{
        
        CardView cardView;
        ImageView img;
        TextView title;
        TextView justtitle;
        TextView comments;
        TextView popularity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            img = itemView.findViewById(R.id.recv_image);
            title = itemView.findViewById(R.id.title);
            justtitle = itemView.findViewById(R.id.justtitle);
            comments = itemView.findViewById(R.id.comments);
            popularity = itemView.findViewById(R.id.popularity);
        }
    }
    

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.stories_item,viewGroup,false));
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =viewHolder.getAdapterPosition();
                String id = storyList.get(position).id+"";
                Intent intent = new Intent(mContext, StoryActivity.class);
                intent.putExtra("story_id",id);
                mContext.startActivity(intent);
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Story story = storyList.get(i);
        int id = story.id;
        NetWork.getZhihuDailyApi().getStoryExtra(id+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoryExtra>() {
                    @Override
                    public void accept(StoryExtra storyExtra) throws Exception {
                        viewHolder.comments.setText(storyExtra.comments+"");
                        viewHolder.popularity.setText(storyExtra.popularity+"");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Toast.makeText(mContext, "网络开小差了...", Toast.LENGTH_SHORT).show();
                    }
                });
        
        if(story.images != null){
            viewHolder.justtitle.setVisibility(View.GONE);
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.title.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(story.images.get(0))
                    .into(viewHolder.img);
            viewHolder.title.setText(story.title);
        }else {
            viewHolder.justtitle.setVisibility(View.VISIBLE);
            viewHolder.img.setVisibility(View.GONE);
            viewHolder.title.setVisibility(View.GONE);
            viewHolder.justtitle.setText(story.title);
        }
    }


    @Override
    public int getItemCount() {
        return storyList.size();
    }
    
    public void setData(List<Story> data){
        this.storyList.clear();
        this.storyList.addAll(data);
        notifyDataSetChanged();
    }
}
