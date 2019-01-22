package com.kastor.wwwsl.kastorua;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class SerieWeelsActivity extends AppCompatActivity {
    private  ImageView toolbarButton;
    private int treeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_weels);
        treeId = getIntent().getExtras().getInt("treeId");
        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarButton = (ImageView) findViewById(R.id.toolbar_btn);
        toolbarButton.setImageResource(R.drawable.back_img);
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CharactFragment charactFragment = new CharactFragment();
        WeelFragment weelFragment = new WeelFragment();
        Bundle args = new Bundle();
        args.putInt("treeId",treeId);
        charactFragment.setArguments(args);
        weelFragment.setArguments(args);
        adapter.addFragment(weelFragment, "Колеса");
        adapter.addFragment(charactFragment, "Характеристика");
        adapter.addFragment(new PageFragment(), "Кронштейны");
        viewPager.setAdapter(adapter);
    }
}
