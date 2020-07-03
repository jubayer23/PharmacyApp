package com.example.pharmacyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pharmacyapp.Utility.DeviceInfoUtils;
import com.example.pharmacyapp.alertbanner.AlertDialogForAnything;
import com.example.pharmacyapp.appdata.GlobalAppAccess;
import com.example.pharmacyapp.appdata.MydApplication;
import com.example.pharmacyapp.model.User;
import com.example.pharmacyapp.model.UserLoginResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private LinearLayout ll_chemist_form, ll_employee_form;

    private Spinner sp_user_type, sp_company_name;

    List<String> list_user_type = new ArrayList<>();
    List<String> list_company_name = new ArrayList<>();

    private ArrayAdapter<String> spAdapterUserTypes;
    private static final String KEY_SELECT_USER_TYPE = "Select User Type";

    private ArrayAdapter<String> spAdapterCompanyName;
    private static final String KEY_SELECT_COMPANY_NAME = "Select Company Name";

    private static final int FORM_TYPE_EMPLOYEE = 3;
    private static final int FORM_TYPE_CHEMIST = 2;
    private static int FORM_TYPE = -1;

    private Button btn_register;


    private EditText ed_name, ed_mobile_number, ed_email, ed_password, ed_confirm_password, ed_company_code, ed_shop_name, ed_house_no, ed_street, ed_city, ed_license_num, ed_registration_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        initAdapter();
    }


    private void init() {

        ll_chemist_form = findViewById(R.id.ll_chemist_form);
        ll_employee_form = findViewById(R.id.ll_employee_form);
        ll_employee_form.setVisibility(View.GONE);
        ll_chemist_form.setVisibility(View.GONE);


        sp_user_type = (Spinner) findViewById(R.id.sp_user_type);
        sp_user_type.setOnItemSelectedListener(this);

        sp_company_name = (Spinner) findViewById(R.id.sp_company_name);
        sp_company_name.setOnItemSelectedListener(this);


        ed_name = findViewById(R.id.ed_name);

        ed_mobile_number = findViewById(R.id.ed_mobile_number);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        ed_company_code = findViewById(R.id.ed_company_code);
        ed_shop_name = findViewById(R.id.ed_shop_name);
        ed_house_no = findViewById(R.id.ed_house_no);
        ed_street = findViewById(R.id.ed_street);
        ed_city = findViewById(R.id.ed_city);
        ed_license_num = findViewById(R.id.ed_license_num);
        ed_registration_num = findViewById(R.id.ed_registration_num);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    private void initAdapter() {

        list_user_type.add(KEY_SELECT_USER_TYPE);
        list_user_type.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        spAdapterUserTypes = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_user_type);
        sp_user_type.setAdapter(spAdapterUserTypes);


        list_company_name.add(KEY_SELECT_COMPANY_NAME);
        list_company_name.addAll(Arrays.asList(getResources().getStringArray(R.array.companyNames)));
        spAdapterCompanyName = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_company_name);
        sp_company_name.setAdapter(spAdapterCompanyName);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        String selected_value = adapterView.getItemAtPosition(i).toString();
        if (id == R.id.sp_user_type) {

            if (!selected_value.equals(KEY_SELECT_USER_TYPE)) {

                if (selected_value.equalsIgnoreCase("chemist")) {
                    ll_employee_form.setVisibility(View.GONE);
                    ll_chemist_form.setVisibility(View.VISIBLE);
                    FORM_TYPE = FORM_TYPE_CHEMIST;
                } else if (selected_value.equalsIgnoreCase("employee")) {
                    ll_employee_form.setVisibility(View.VISIBLE);
                    ll_chemist_form.setVisibility(View.GONE);
                    FORM_TYPE = FORM_TYPE_EMPLOYEE;
                }

            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private boolean validateForm(){

        boolean validForm = true;

        if(ed_name.getText().toString().isEmpty()){

            ed_name.setError("Required");
            return false;
        }

        if(ed_mobile_number.getText().toString().isEmpty()){

            ed_mobile_number.setError("Required");
            return false;
        }
        if(ed_email.getText().toString().isEmpty()){

            ed_email.setError("Required");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString()).matches()){

            ed_email.setError("Invalid Email!");
            return false;
        }
        if(ed_password.getText().toString().isEmpty()){

            ed_password.setError("Required");
            return false;
        }
        if(ed_confirm_password.getText().toString().isEmpty()){

            ed_confirm_password.setError("Required");
            return false;
        }
        if(!ed_password.getText().toString().equals(ed_confirm_password.getText().toString())){
            ed_confirm_password.setError("Password does not match!");
            return false;

        }

        if(sp_user_type.getSelectedItem().toString().equals(KEY_SELECT_USER_TYPE)){
            Toast.makeText(this,"Select a user type!", Toast.LENGTH_LONG).show();
        }

        if(FORM_TYPE == FORM_TYPE_EMPLOYEE){

            if(sp_company_name.getSelectedItem().toString().equals(KEY_SELECT_COMPANY_NAME)){

                Toast.makeText(this,"Select a company name!", Toast.LENGTH_LONG).show();
            }
            if(ed_company_code.getText().toString().isEmpty()){

                ed_company_code.setError("Required");
                return false;
            }
        }else if(FORM_TYPE == FORM_TYPE_CHEMIST){

            if(ed_shop_name.getText().toString().isEmpty()){

                ed_shop_name.setError("Required");
                return false;
            }
            if(ed_house_no.getText().toString().isEmpty()){

                ed_house_no.setError("Required");
                return false;
            }
            if(ed_street.getText().toString().isEmpty()){

                ed_street.setError("Required");
                return false;
            }
            if(ed_city.getText().toString().isEmpty()){

                ed_city.setError("Required");
                return false;
            } if(ed_license_num.getText().toString().isEmpty()){

                ed_license_num.setError("Required");
                return false;
            }if(ed_registration_num.getText().toString().isEmpty()){

                ed_registration_num.setError("Required");
                return false;
            }
        }

        return validForm;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_register){

            if(!DeviceInfoUtils.isConnectingToInternet(this)){
                AlertDialogForAnything.showAlertDialogWhenComplte(this, "ERROR", "Please connect to a working internet connection!", false);
                return;

            }

            if(validateForm()){


                sendRequestToRegister();
            }
        }
    }


    private void sendRequestToRegister(){


        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_REGISTRATION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG",response);

                        dismissProgressDialog();

                        UserLoginResponse login = MydApplication.gson.fromJson(response, UserLoginResponse.class);

                        if(login.getStatus()){

                            User user = login.getUser();


                            MydApplication.getInstance().getPrefManger().setUser(user);
                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                            finish();

                        }else{
                            AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this,"Error",login.getMessage(),false);
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Network problem. please try again!", false);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", ed_name.getText().toString());
                params.put("mobile", ed_mobile_number.getText().toString());
                params.put("email", ed_email.getText().toString());
                params.put("password", ed_password.getText().toString());
                params.put("password2", ed_password.getText().toString());
                params.put("type", String.valueOf(FORM_TYPE));
                if(FORM_TYPE == FORM_TYPE_CHEMIST){
                    params.put("shopName", ed_shop_name.getText().toString());
                    params.put("addressHouse", ed_house_no.getText().toString());
                    params.put("addressStreet", ed_street.getText().toString());
                    params.put("addressCity", ed_city.getText().toString());
                    params.put("licenseNo", ed_license_num.getText().toString());
                    params.put("pharmacistRegNo", ed_registration_num.getText().toString());
                    params.put("companyId", "1");
                    params.put("companySecretCode", "213");

                }else if( FORM_TYPE == FORM_TYPE_EMPLOYEE){

                    params.put("companyId", String.valueOf(list_company_name.indexOf(sp_company_name.getSelectedItem().toString())));
                    params.put("companySecretCode", ed_company_code.getText().toString());
                }
                Log.d("DEBUG", MydApplication.gson.toJson(params));
                return params;
            }
        }
                ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }
}
