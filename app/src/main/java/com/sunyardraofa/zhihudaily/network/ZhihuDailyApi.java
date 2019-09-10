package com.sunyardraofa.zhihudaily.network;

import com.sunyardraofa.zhihudaily.gson.CommentObject;
import com.sunyardraofa.zhihudaily.gson.Latest;
import com.sunyardraofa.zhihudaily.gson.StoryDetail;
import com.sunyardraofa.zhihudaily.gson.StoryExtra;
import com.sunyardraofa.zhihudaily.gson.TypeStory;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZhihuDailyApi {
    @GET("news/latest")
    Observable<Latest> getlatest ();

    @GET("story-extra/{id}")
    Observable<StoryExtra> getStoryExtra(@Path("id") String id);
    
    @GET("news/{id}")
    Observable<StoryDetail> getStoryDetail(@Path("id") String id);
    
    @GET("news/{id}/comments")
    Observable<CommentObject> getCommentObject(@Path("id") String id);
    
    @GET("theme/{typeid}")
    Observable<TypeStory> getTypeStotry(@Path("typeid") String typeid);
}
