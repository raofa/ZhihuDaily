package com.sunyardraofa.zhihudaily.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunyardraofa.zhihudaily.R;
import com.sunyardraofa.zhihudaily.gson.StoryDetail;
import com.sunyardraofa.zhihudaily.network.NetWork;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StoryActivity extends AppCompatActivity {

    private static final String TAG = "StoryActivity";

    private String storyId;
    private RelativeLayout storyTopLayout;
    private ImageView storyImage;
    private TextView storyTitle;
    private TextView storyImageSource;
    private WebView storyWebView;

    private String shareUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Toolbar storyToolbar = findViewById(R.id.story_toolbar);
        setSupportActionBar(storyToolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        Intent intent = getIntent();
        storyId = intent.getStringExtra("story_id");

        storyImage = findViewById(R.id.story_detail_image);
        storyTitle = findViewById(R.id.story_detail_title);
        storyTopLayout = findViewById(R.id.story_detail_top);
        storyImageSource = findViewById(R.id.story_detail_image_source);
        storyWebView =findViewById(R.id.story_detail_webview);
        storyWebView.getSettings().setJavaScriptEnabled(true);


        initStory();


    }

    @SuppressLint("CheckResult")
    private void initStory(){
        NetWork.getZhihuDailyApi().getStoryDetail(storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoryDetail>() {
                    @Override
                    public void accept(StoryDetail storyDetail) throws Exception {

                        if(storyDetail.image == null || storyDetail.image.length() == 0){
                            storyTopLayout.setVisibility(View.GONE);

                        }else{
                            storyTopLayout.setVisibility(View.VISIBLE);

                            Glide.with(StoryActivity.this).load(storyDetail.image).into(storyImage);
                            storyTitle.setText(storyDetail.title);
                            storyImageSource.setText(storyDetail.imageSource);
                        }

                        shareUrl = storyDetail.shareUrl;

//                      String html = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\""+storyDetail.css.get(0)+"\"/></head><body>"+storyDetail.body+"</body></html>";
//                      storyWebView.loadDataWithBaseURL(null, html , "text/html", "utf-8", null);
                        String html = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"ZhiHuCSS.css\"/></head><body>"+storyDetail.body+"</body></html>";
                        storyWebView.loadDataWithBaseURL("file:///android_asset/", html , "text/html", "utf-8", null);
                    }
                });
             
        


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_detail_menu,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.story_detail_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,shareUrl);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "分享给"));
                break;
            case R.id.story_detail_comment:
                Intent intentToComment = new Intent(StoryActivity.this,CommentActivity.class);
                intentToComment.putExtra("story_id",storyId);
                startActivity(intentToComment);
                break;

        }

        return true;
    }



}
