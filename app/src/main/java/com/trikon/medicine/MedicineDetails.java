package com.trikon.medicine;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.model.Medicine;

public class MedicineDetails extends BaseActivity {

    private Medicine medicine;

    private TextView tv_brand_name, tv_company_name, tv_generic_name, tv_start_date, tv_end_date, tv_dosage;

    private SimpleDraweeView draweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        initToolbar("Medicine Details",true);



        medicine = (Medicine) getIntent().getExtras().getSerializable("medicine");

        init();

        display();
    }

    private void init(){
        tv_brand_name = findViewById(R.id.tv_brand_name);
        tv_company_name = findViewById(R.id.tv_company_name);
        tv_generic_name = findViewById(R.id.tv_generic_name);
        tv_dosage = findViewById(R.id.tv_dosage);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);
        draweeView = findViewById(R.id.my_image_view);

       ImageView my_ad_view = (ImageView) findViewById(R.id.my_ad_view);
        //Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/2.jpg");
        Glide.with(this)
                .load(GlobalAppAccess.BASE_URL_2 + "/resources/advertise/2.jpg")

                .error(R.drawable.ic_placeholder)
                .into(my_ad_view);

    }

    private void display(){

        tv_brand_name.setText(medicine.getBrandName());

        tv_start_date.setText("Start: " + medicine.getStartDate());
        tv_end_date.setText("End: " + medicine.getEndDate());

        tv_company_name.setText(medicine.getCompanyName());
        tv_generic_name.setText(medicine.getGenericName());
        tv_dosage.setText(medicine.getDosage());


        if(medicine.getImageUrl() != null){
            Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + medicine.getImageUrl());
            draweeView.setImageURI(uri);
        }
    }
}
