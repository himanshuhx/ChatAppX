package com.first75494.chatappx;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;  //Declaring the variables
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.mytoolbar); //Finding toolbar id
        setSupportActionBar(toolbar); //Setting the ToolBar As The AppBar For The Activity
        getSupportActionBar().setTitle("ChatAppX"); //Setting Up The Title Of Our Action Bar

        viewPager = (ViewPager) findViewById(R.id.myviewpager); //Finding viewpager id
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager()); //calling the android Fragment Manager
        viewPager.setAdapter(viewPagerAdapter); //Binding the viewpagerAdapter

        tabLayout = (TabLayout) findViewById(R.id.tablayout); //Finding tabLayout id
        tabLayout.setupWithViewPager(viewPager); //Setting up with our view
    }



}
