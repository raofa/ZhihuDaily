package com.sunyardraofa.zhihudaily.gson;

import com.google.gson.annotations.SerializedName;

public class Comment {
    public String author;
    public String content;
    public String avatar;
    public String time;
    @SerializedName("reply_to")
    public ReplyTo replyTo;

    public class ReplyTo{

        public String content;
        public String author;

    }
    
}
