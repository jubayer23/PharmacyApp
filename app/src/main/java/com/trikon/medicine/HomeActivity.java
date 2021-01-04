package com.trikon.medicine;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trikon.medicine.CustomView.DialogFragmentCallBack;
import com.trikon.medicine.Utility.RunnTimePermissions;
import com.trikon.medicine.adapter.ViewPagerAdapter;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.appdata.MydApplication;
import com.trikon.medicine.fragment.AddEventDialogFragment;
import com.trikon.medicine.fragment.AddPrescriptionFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.trikon.medicine.fragment.EventListFragment;
import com.trikon.medicine.fragment.HomeFragment;
import com.trikon.medicine.model.Medicine;

public class HomeActivity extends BaseActivity implements View.OnClickListener, DialogFragmentCallBack {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;


    FloatingActionButton fab_add_event, fab_add_prescription;

    ImageView my_ad_view;

    private LinearLayout ll_hot_news;

    TextView tv_brand_name, tv_strength, tv_dosage, tv_use_for, tv_start_date, tv_end_date;
    SimpleDraweeView draweeView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        init();


        viewPagerAdapter = createCardAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //tab.setText("Tab " + (position + 1));
                        if(position == 0){

                            tab.setText("Home");
                        }else if(position == 1){
                            tab.setText("Events");
                        }
                    }
                }).attach();

        RunnTimePermissions.requestForAllRuntimePermissions(this);

        showNotifyDialog();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(position == 1 && isHotNews)ll_hot_news.setVisibility(View.GONE);
                if(position == 0 && isHotNews)ll_hot_news.setVisibility(View.VISIBLE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void init(){
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);

        fab_add_event =  findViewById(R.id.fab_add_event);
        fab_add_event.setOnClickListener(this);
        fab_add_prescription =  findViewById(R.id.fab_add_prescription);
        fab_add_prescription.setOnClickListener(this);

        my_ad_view = (ImageView) findViewById(R.id.my_ad_view);
        //Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/2.jpg");
        Glide.with(this)
                .load(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/2.jpg")

                .error(R.drawable.ic_placeholder)
                .into(my_ad_view);

       // my_ad_view.setImageURI(uri);


        ll_hot_news = findViewById(R.id.ll_hot_news);
        ll_hot_news.setVisibility(View.GONE);
        ll_hot_news.setOnClickListener(this);

        tv_brand_name = findViewById(R.id.tv_brand_name);
        tv_strength = findViewById(R.id.tv_strength);
        tv_dosage = findViewById(R.id.tv_dosage);
        tv_use_for = findViewById(R.id.tv_use_for);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);
        draweeView = findViewById(R.id.my_image_view);

        
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }

    Medicine hotNews;
    boolean isHotNews = false;
    public void gotHotNews(boolean isHot, Medicine hotNewsMedicine){

        isHotNews = isHot;
        if(isHot){

            hotNews = hotNewsMedicine;

            ll_hot_news.setVisibility(View.VISIBLE);

            tv_brand_name.setText(hotNewsMedicine.getBrandName());
            tv_dosage.setText(hotNewsMedicine.getCompanyName());
            tv_strength.setText(hotNewsMedicine.getGenericName());
            if(hotNewsMedicine.getDosage() != null) tv_use_for.setText(hotNewsMedicine.getDosage());
            tv_start_date.setText(hotNewsMedicine.getStartDate());
            tv_end_date.setText(hotNewsMedicine.getEndDate());


            if(hotNewsMedicine.getImageUrl() != null){
                Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + hotNewsMedicine.getImageUrl());

                draweeView.setImageURI(uri);
            }



        }else{


            ll_hot_news.setVisibility(View.GONE);

        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


        if(id == R.id.ll_hot_news){

            int index = viewPager.getCurrentItem();
            ViewPagerAdapter adapter = ((ViewPagerAdapter)viewPager.getAdapter());
            HomeFragment fragment = adapter.getFragment2();

            //EventListFragment myFragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag("ABC");
            if (fragment != null && fragment.isVisible()) {
                //Log.d("DEBUG", "its here not here");
                // add your code here
                fragment.onHotNewsClicked(hotNews);
            }


        }

        if(id == R.id.fab_add_event){



            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }
            fragmentTransaction.addToBackStack(null);
            AddEventDialogFragment dialogFragment = new AddEventDialogFragment(); //here MyDialog is my custom dialog
            dialogFragment.show(fragmentTransaction, "dialog");



        }

        if(id == R.id.fab_add_prescription){

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog2");
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }
            fragmentTransaction.addToBackStack(null);
            AddPrescriptionFragment dialogFragment = new AddPrescriptionFragment(); //here MyDialog is my custom dialog
            dialogFragment.show(fragmentTransaction, "dialog2");


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_logout:
                MydApplication.getInstance().getPrefManger().setUser(null);
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void showNotifyDialog(){
        final Dialog dialog_start = new Dialog(HomeActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(true);
        dialog_start.setContentView(R.layout.dialog_splash);
       // LinearLayout ll_container = (LinearLayout) dialog_start.findViewById(R.id.ll_container);
        ImageView my_image_view = (ImageView) dialog_start.findViewById(R.id.my_image_view);
        ImageView img_close = (ImageView) dialog_start.findViewById(R.id.img_close);
        //ll_container.setBackgroundResource(R.drawable.background_rounded_green);
        //tv_msg.setText(message);

       // Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/1.jpg");

        //my_image_view.setImageURI(uri);


        CircularProgressDrawable circularProgressDrawable =new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(this)
                .load(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/1.jpg")
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_placeholder)
                .into(my_image_view);

        dialog_start.show();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_start.dismiss();
            }
        });

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog_start.dismiss();
            }
        }, 3000);*/
    }


    @Override
    public void onRefresh() {

        //Log.d("DEBUG", "its here 12222");

        int index = viewPager.getCurrentItem();
        ViewPagerAdapter adapter = ((ViewPagerAdapter)viewPager.getAdapter());
        EventListFragment fragment = adapter.getFragment();

        //EventListFragment myFragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag("ABC");
        if (fragment != null && fragment.isVisible()) {
            //Log.d("DEBUG", "its here not here");
            // add your code here
            fragment.refreshScreen();
        }

    }
}
