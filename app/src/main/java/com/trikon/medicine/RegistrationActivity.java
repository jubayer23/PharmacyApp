package com.trikon.medicine;

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
import com.trikon.medicine.Utility.CommonMethods;
import com.trikon.medicine.Utility.DeviceInfoUtils;
import com.trikon.medicine.alertbanner.AlertDialogForAnything;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.appdata.MydApplication;
import com.trikon.medicine.model.Company;
import com.trikon.medicine.model.CompanyResponse;
import com.trikon.medicine.model.DistrictResponse;
import com.trikon.medicine.model.DivisionResponse;
import com.trikon.medicine.model.Generic;
import com.trikon.medicine.model.GenericResponse;
import com.trikon.medicine.model.Thana;
import com.trikon.medicine.model.ThanaResponse;
import com.trikon.medicine.model.User;
import com.trikon.medicine.model.UserLoginResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private LinearLayout ll_chemist_form, ll_employee_form;

    private Spinner sp_user_type, sp_company_name;

    List<String> list_user_type = new ArrayList<>();
    //List<String> list_company_name = new ArrayList<>();
    List<Company> list_companies = new ArrayList<>();

    private ArrayAdapter<String> spAdapterUserTypes;
    private static final String KEY_SELECT_USER_TYPE = "Select User Type";

    private ArrayAdapter<Company> spAdapterCompanyName;
    private static final String KEY_SELECT_COMPANY_NAME = "Select Company Name";

    private static final int FORM_TYPE_EMPLOYEE = 3;
    private static final int FORM_TYPE_CHEMIST = 2;
    private static int FORM_TYPE = -1;

    private Button btn_register;


    private EditText ed_name, ed_mobile_number, ed_email, ed_password, ed_confirm_password, ed_company_code, ed_shop_name, ed_house_no, ed_street, ed_license_num, ed_registration_num;


    private Spinner sp_thana, sp_district, sp_division;

    List<Thana> list_thana = new ArrayList<>();
    List<Thana> list_district = new ArrayList<>();
    List<Thana> list_division = new ArrayList<>();

    private ArrayAdapter<Thana> adapterSpThana, adapterSpDistrict, adapterSpDivision;
    private static final String KEY_SELECT_THANA = "Select Thana";
    private static final String KEY_SELECT_DISTRICT = "Select District";
    private static final String KEY_SELECT_DIVISION = "Select Division";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        initAdapter();

        sendRequestToGetCompanyList("");
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
        //ed_city = findViewById(R.id.ed_city);
        ed_license_num = findViewById(R.id.ed_license_num);
        ed_registration_num = findViewById(R.id.ed_registration_num);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        sp_thana = (Spinner) findViewById(R.id.sp_thana);
        sp_thana.setOnItemSelectedListener(this);

        sp_district = (Spinner) findViewById(R.id.sp_districts);
        sp_district.setOnItemSelectedListener(this);

        sp_division = (Spinner) findViewById(R.id.sp_division);
        sp_division.setOnItemSelectedListener(this);
    }

    private void initAdapter() {

        list_user_type.add(KEY_SELECT_USER_TYPE);
        list_user_type.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        spAdapterUserTypes = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_user_type);
        sp_user_type.setAdapter(spAdapterUserTypes);


       /* list_company_name.add(KEY_SELECT_COMPANY_NAME);
        list_company_name.addAll(Arrays.asList(getResources().getStringArray(R.array.companyNames)));
        spAdapterCompanyName = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_company_name);
        sp_company_name.setAdapter(spAdapterCompanyName);*/

        Company t1 = new Company(KEY_SELECT_COMPANY_NAME);
        list_companies.add(t1);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        spAdapterCompanyName = new ArrayAdapter<Company>
                (this, R.layout.spinner_item, list_companies);
        sp_company_name.setAdapter(spAdapterCompanyName);


        //list_thana.add(KEY_SELECT_THANA);
        Thana t11 = new Thana(KEY_SELECT_THANA);
        list_thana.add(t11);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        adapterSpThana = new ArrayAdapter<Thana>
                (this, R.layout.spinner_item_black_text, list_thana);
        sp_thana.setAdapter(adapterSpThana);

        //list_district.add(KEY_SELECT_DISTRICT);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        Thana t2 = new Thana(KEY_SELECT_DISTRICT);
        list_district.add(t2);
        adapterSpDistrict = new ArrayAdapter<Thana>
                (this, R.layout.spinner_item_black_text, list_district);
        sp_district.setAdapter(adapterSpDistrict);

        // list_division.add(KEY_SELECT_DIVISION);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        Thana t3 = new Thana(KEY_SELECT_DIVISION);
        list_division.add(t3);
        adapterSpDivision = new ArrayAdapter<Thana>
                (this, R.layout.spinner_item_black_text, list_division);
        sp_division.setAdapter(adapterSpDivision);


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

        } if(adapterView.getId() == R.id.sp_division && i != 0){
            Log.d("DEBUG", "its called");
            int id2 = list_division.get(i).getId();
            sendRequestToGetDistrict(id2);
             CommonMethods.hideKeybaord(this);


        }else if(adapterView.getId() == R.id.sp_districts && i != 0){
            int id2 = list_district.get(i).getId();
            sendRequestToGetThana(id2);
            CommonMethods.hideKeybaord(this);


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

        if(!ed_email.getText().toString().isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString()).matches()){

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
            return false;
        }

        if(FORM_TYPE == FORM_TYPE_EMPLOYEE){

            if(list_companies.get(sp_company_name.getSelectedItemPosition()).getName().equals(KEY_SELECT_COMPANY_NAME)){
                Toast.makeText(this,"Select a Company!", Toast.LENGTH_LONG).show();
                return false;
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
                if(!ed_email.getText().toString().isEmpty())params.put("email", ed_email.getText().toString());
                params.put("password", ed_password.getText().toString());
                params.put("password2", ed_password.getText().toString());
                params.put("type", String.valueOf(FORM_TYPE));
                if(FORM_TYPE == FORM_TYPE_CHEMIST){
                    params.put("shopName", ed_shop_name.getText().toString());
                    params.put("houseNo", ed_house_no.getText().toString());
                    params.put("street", ed_street.getText().toString());
                    params.put("thana", sp_thana.getSelectedItem().toString());
                    params.put("district", sp_district.getSelectedItem().toString());
                    params.put("division", sp_division.getSelectedItem().toString());
                   // params.put("addressCity", ed_city.getText().toString());
                    if(!ed_license_num.getText().toString().isEmpty())params.put("licenseNo", ed_license_num.getText().toString());
                    if(!ed_registration_num.getText().toString().isEmpty())params.put("pharmacistRegNo", ed_registration_num.getText().toString());
                    params.put("companyId", "1");
                    params.put("companySecretCode", "213");

                }else if( FORM_TYPE == FORM_TYPE_EMPLOYEE){

                    params.put("companyId", String.valueOf(list_companies.get(sp_company_name.getSelectedItemPosition()).getId()));
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


    private void sendRequestToGetCompanyList(String prefix){

        showProgressDialog("Loading..", true, false);


        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_GET_COMPANIES,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        dismissProgressDialog();

                        //parsing logic, please change it as per your requirement
                       // List<Generic> generics = new ArrayList<>();
                        //List<Company> companies = new ArrayList<>();

                        dismissProgressDialog();


                            CompanyResponse login = MydApplication.gson.fromJson(response, CompanyResponse.class);
                            if(login.getStatus())
                                list_companies.addAll(login.getCompanies());

                             else   Toast.makeText(RegistrationActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();



                        //IMPORTANT: set data here and notify

                            //spAdapterCompanyName.setData(companies);
                            spAdapterCompanyName.notifyDataSetChanged();


                        sendRequestToGetDivision();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                dismissProgressDialog();

                //AlertDialogForAnything.showAlertDialogWithoutTitle(getActivity(),"Network problem!", false);
                Toast.makeText(RegistrationActivity.this, "Server Down!", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("prefix", prefix);

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    private void sendRequestToGetThana(int id){


        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_GET_THANA,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        dismissProgressDialog();

                        ThanaResponse login = MydApplication.gson.fromJson(response, ThanaResponse.class);

                        if(login.getStatus()){

                            list_thana.clear();

                            Thana t1 = new Thana(KEY_SELECT_THANA);
                            list_thana.add(t1);


                            list_thana.addAll(login.getThanas());
                            adapterSpThana.notifyDataSetChanged();

                            //sendRequestToGetDistrict();


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
                params.put("districtId", String.valueOf(id));

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    private void sendRequestToGetDistrict(int id){


        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_GET_DISTRIC,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        dismissProgressDialog();

                        DistrictResponse login = MydApplication.gson.fromJson(response, DistrictResponse.class);

                        if(login.getStatus()){

                            list_district.clear();

                            Thana t2 = new Thana(KEY_SELECT_DISTRICT);
                            list_district.add(t2);

                            list_district.addAll(login.getThanas());
                            adapterSpDistrict.notifyDataSetChanged();

                            sendRequestToGetDivision();


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
                params.put("divisionId", String.valueOf(id));

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    private void sendRequestToGetDivision(){


        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_GET_DIVISION,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        dismissProgressDialog();

                        DivisionResponse login = MydApplication.gson.fromJson(response, DivisionResponse.class);

                        if(login.getStatus()){

                            list_division.addAll(login.getThanas());
                            adapterSpDivision.notifyDataSetChanged();

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

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }
}
