package com.sunyardraofa.zhihudaily.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryDetail {

    public String body;
    public String title;
    @SerializedName("image_source")
    public String imageSource;
    public String image;
    @SerializedName("share_url")
    public String shareUrl;

    public List<String> js;
    public int type;
    public String id;
    public List<String> css;

}
