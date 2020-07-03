package com.example.pharmacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pharmacyapp.Utility.RunnTimePermissions;
import com.example.pharmacyapp.adapter.ViewPagerAdapter;
import com.example.pharmacyapp.appdata.MydApplication;
import com.example.pharmacyapp.fragment.AddEventDialogFragment;
import com.example.pharmacyapp.fragment.AddPrescriptionFragment;
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
}
