package com.sunyardraofa.zhihudaily;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sunyardraofa.zhihudaily.view.HomeFragment;

public class MainActivity extends AppCompatActivity {
    
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private String storyType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drwaer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar mactionBar = getSupportActionBar();
        if(mactionBar != null) {
            mactionBar.setDisplayHomeAsUpEnabled(true);
            mactionBar.setHomeAsUpIndicator(R.drawable.icon_home);
        }
        navigationView.setCheckedItem(R.id.dailyHome);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                case R.id.dailyHome:
                    toolbar.setTitle("首页");
                    changeFragment(new HomeFragment());
                    break;
                case R.id.dailyPsychology:
                    storyType = "13";
                    toolbar.setTitle("日常心理学");
                    break;
                case R.id.userRecommendDaily:
                    storyType = "12";
                    toolbar.setTitle("用户推荐日报");
                    break;
                case R.id.movieDaily:
                    storyType = "3";
                    toolbar.setTitle("电影日报");
                    break;
                case R.id.dontBored:
                    storyType = "11";
                    toolbar.setTitle("不许无聊");
                    break;
                case R.id.designDaily:
                    storyType = "4";
                    toolbar.setTitle("设计日报");
                    break;
                case R.id.companyDaily:
                    storyType = "5";
                    toolbar.setTitle("大公司日报");
                    break;
                case R.id.financeDaily:
                    storyType = "6";
                    toolbar.setTitle("财经日报");
                    break;
                case R.id.internetSecurity:
                    storyType = "10";
                    toolbar.setTitle("互联网安全");
                    break;
                case R.id.startGame:
                    storyType = "2";
                    toolbar.setTitle("开始游戏");  
                    break;
                case R.id.musicDaily:
                    storyType = "7";
                    toolbar.setTitle("音乐日报");
                    break;
                case R.id.animeDaily:
                    storyType = "9";
                    toolbar.setTitle("动物日报");
                    break;
                case R.id.sportsDaily:
                    storyType = "8";
                    toolbar.setTitle("体育日报");
                    break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        initfragment();
    }

    private void initfragment() {
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        changeFragment(homeFragment);
    }
    
    private void changeFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content,fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case android.R.id.home:
            drawerLayout.openDrawer(GravityCompat.START);
            break;
            default:
                break;
        }
        return true;
    }
    
    public String getStoryType(){
        return storyType;
    }
}
