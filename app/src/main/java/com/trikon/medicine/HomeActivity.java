package com.trikon.medicine;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    TabLayout tabLayout;
    ViewPager2 viewPager;


    FloatingActionButton fab_add_event, fab_add_prescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        init();


        viewPager.setAdapter(createCardAdapter());
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
    }

    private void init(){
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);

        fab_add_event = (FloatingActionButton) findViewById(R.id.fab_add_event);
        fab_add_event.setOnClickListener(this);
        fab_add_prescription = (FloatingActionButton) findViewById(R.id.fab_add_prescription);
        fab_add_prescription.setOnClickListener(this);

        
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        return adapter;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

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
        SimpleDraweeView my_image_view = (SimpleDraweeView) dialog_start.findViewById(R.id.my_image_view);
        ImageView img_close = (ImageView) dialog_start.findViewById(R.id.img_close);
        //ll_container.setBackgroundResource(R.drawable.background_rounded_green);
        //tv_msg.setText(message);

        Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/1.jpg");

        my_image_view.setImageURI(uri);

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
}
