package com.sunyardraofa.zhihudaily.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.sunyardraofa.zhihudaily.R;
import com.sunyardraofa.zhihudaily.adapter.CommentAdapter;
import com.sunyardraofa.zhihudaily.gson.Comment;
import com.sunyardraofa.zhihudaily.gson.CommentObject;
import com.sunyardraofa.zhihudaily.network.NetWork;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";

    private ActionBar actionBar;
    private CommentAdapter mCommentAdapter;
    private String storyId;
    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar commentToolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        Intent intent = getIntent();
        storyId = intent.getStringExtra("story_id");

        RecyclerView commentRecyclerView = findViewById(R.id.comment_recyclerview);
        commentRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        mCommentAdapter = new CommentAdapter(this,comments);
        commentRecyclerView.setAdapter(mCommentAdapter);

        initComment();

    }

    @SuppressLint("CheckResult")
    private void initComment(){

        NetWork.getZhihuDailyApi().getCommentObject(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommentObject>() {
                    @Override
                    public void accept(CommentObject commentObject) throws Exception {
                        actionBar.setTitle(commentObject.count+"条评论");
                        Comment comment = new Comment();
                        commentObject.comments.add(comment);
                        mCommentAdapter.setData(commentObject.comments);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Log.d(TAG,"comment Call fail");
                    }
                });
 

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
