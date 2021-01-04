package com.trikon.medicine.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.trikon.medicine.CustomView.AutoCompleteLoading;
import com.trikon.medicine.CustomView.RecyclerItemClickListener;
import com.trikon.medicine.HomeActivity;
import com.trikon.medicine.MedicineDetails;
import com.trikon.medicine.R;
import com.trikon.medicine.Utility.CommonMethods;
import com.trikon.medicine.adapter.CompanyAcAdapter;
import com.trikon.medicine.adapter.GenericAcAdapter;
import com.trikon.medicine.adapter.MedicineListAdapter;
import com.trikon.medicine.alertbanner.AlertDialogForAnything;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.appdata.MydApplication;
import com.trikon.medicine.model.Company;
import com.trikon.medicine.model.CompanyResponse;
import com.trikon.medicine.model.Generic;
import com.trikon.medicine.model.GenericResponse;
import com.trikon.medicine.model.Medicine;
import com.trikon.medicine.model.MedicineResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handlerGeneric, handlerCompany;
    private GenericAcAdapter adapterAcGeneric;
    private AutoCompleteLoading ac_generics, ac_companies;

    private CompanyAcAdapter adapterAcCompany;
    //private AutoCompleteLoading ac_generics;

    //private OnFragmentInteractionListener mListener;
    private EditText ed_medicine_name;
    private String genericName = "", companyName = "";

    private RecyclerView rvMedcineList;
    private MedicineListAdapter adapter;
    List<Medicine> medicines = new ArrayList<>();
    private Medicine hotNewsMedicine = null;

    private ProgressBar ac_company_loading, ac_generic_loading;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ac_generics = view.findViewById(R.id.ac_generics);
        ac_generic_loading = view.findViewById(R.id.ac_generics_loading);
        ac_generics.setLoadingIndicator(ac_generic_loading);
        //initAutoCompleteAdapter();

        ac_companies = view.findViewById(R.id.ac_companies);
        ac_company_loading = view.findViewById(R.id.ac_companies_loading);
        ac_companies.setLoadingIndicator(ac_company_loading);

        ed_medicine_name = view.findViewById(R.id.ed_medicine_name);




        rvMedcineList = view.findViewById(R.id.rv_medcine_list);
        initAutoCompleteAdapter();



        initSearch();


        initRecyleView();

    }

    private void changeUIBasedOnHotNews(boolean isHot){

        if(isHot){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rvMedcineList.getLayoutParams();
            params.setMargins(0, 0, 0, MydApplication.getInstance().getPixelValue(190));
            rvMedcineList.setLayoutParams(params);







        }else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rvMedcineList.getLayoutParams();
            params.setMargins(0, 0, 0, MydApplication.getInstance().getPixelValue(0));
            rvMedcineList.setLayoutParams(params);



        }
    }

    private void initRecyleView(){

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
       // mLayoutManager.setAutoMeasureEnabled(true);
        //recyclerView.setLayoutManager(layoutManager);

        rvMedcineList.setLayoutManager(mLayoutManager);

        //rvMedcineList.setNestedScrollingEnabled(false);

        //Set Adapter
        adapter = new MedicineListAdapter(medicines);
        rvMedcineList.setAdapter(adapter);

        rvMedcineList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rvMedcineList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(getActivity(), MedicineDetails.class);
                        intent.putExtra("medicine", medicines.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



    }

    private void initSearch(){
        ed_medicine_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click

                    if(ed_medicine_name.getText().toString().isEmpty() ){
                        ed_medicine_name.setError("Required!");
                        return true;
                    }

                    makeApiCallForSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void initAutoCompleteAdapter(){

        //Setting up the adapter for AutoSuggest
        adapterAcGeneric = new GenericAcAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        ac_generics.setThreshold(0);
        ac_generics.setAdapter(adapterAcGeneric);
        ac_generics.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //selectedText.setText(autoSuggestAdapter.getObject(position));
                        genericName = adapterAcGeneric.getObject(position);
                        makeApiCallForSearch();
                        CommonMethods.hideKeybaord(getActivity());
                    }
                });
        ac_generics.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                genericName = "";
                handlerGeneric.removeMessages(TRIGGER_AUTO_COMPLETE);
                handlerGeneric.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handlerGeneric = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(ac_generics.getText())) {
                       // Log.d("DEBUG", "in generic");
                        makeApiCall(ac_generics.getText().toString(), 1);
                    }
                }
                return false;
            }
        });


        adapterAcCompany = new CompanyAcAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        ac_companies.setThreshold(0);
        ac_companies.setAdapter(adapterAcCompany);
        ac_companies.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //selectedText.setText(autoSuggestAdapter.getObject(position));
                        companyName = adapterAcCompany.getObject(position);
                        makeApiCallForSearch();
                        CommonMethods.hideKeybaord(getActivity());
                    }
                });
        ac_companies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                companyName = "";
                handlerCompany.removeMessages(TRIGGER_AUTO_COMPLETE);
                handlerCompany.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handlerCompany = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(ac_companies.getText())) {
                        //Log.d("DEBUG", "in company");
                        makeApiCall(ac_companies.getText().toString(), 2);
                    }
                }
                return false;
            }
        });


        ac_companies.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("DEBUG", "1");
                    makeApiCall("", 2);

                  //  Toast.makeText(getActivity(), "Got the focus", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(getActivity(), "Lost the focus", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ac_generics.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("DEBUG", "2");
                    makeApiCall("", 1);
                  //  Toast.makeText(getActivity(), "Got the focus", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(getActivity(), "Lost the focus", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void makeApiCall(String prefix, int tag){

        //showProgressDialog("Loading..", true, false);
        String url = "";
        if(tag == 1){
            ac_generic_loading.setVisibility(View.VISIBLE);
            url = GlobalAppAccess.URL_GET_GENERICS;
        }else{
            ac_company_loading.setVisibility(View.VISIBLE);
            url = GlobalAppAccess.URL_GET_COMPANIES;

        }

        final StringRequest req = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.d("DEBUG",response);

                        //parsing logic, please change it as per your requirement
                        List<Generic> generics = new ArrayList<>();
                        List<Company> companies = new ArrayList<>();

                        dismissProgressDialog();

                        if(tag == 1){
                            GenericResponse login = MydApplication.gson.fromJson(response, GenericResponse.class);
                            if(login.getStatus())
                                generics.addAll(login.getGenerics());
                            else{
                                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            CompanyResponse login = MydApplication.gson.fromJson(response, CompanyResponse.class);
                            if(login.getStatus())
                                companies.addAll(login.getCompanies());
                            else{
                                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }

                        }


                        //IMPORTANT: set data here and notify
                        if(tag == 1){
                            adapterAcGeneric.setData(generics);
                            adapterAcGeneric.notifyDataSetChanged();
                           //if(generics.size() > 0) ac_generics.showDropDown();

                        }else{
                            adapterAcCompany.setData(companies);
                            adapterAcCompany.notifyDataSetChanged();
                            //if(companies.size() > 0)ac_companies.showDropDown();
                        }

                        ac_company_loading.setVisibility(View.GONE);
                        ac_generic_loading.setVisibility(View.GONE);



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ac_company_loading.setVisibility(View.GONE);
                ac_generic_loading.setVisibility(View.GONE);

                dismissProgressDialog();

               //AlertDialogForAnything.showAlertDialogWithoutTitle(getActivity(),"Network problem!", false);
                Toast.makeText(getActivity(), "Server Down!", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("prefix", prefix);

                return params;
            }
        }
                ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }


    private void makeApiCallForSearch(){

        showProgressDialog("Loading..", true, false);


        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_SEARCH_MEDICINE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        //parsing logic, please change it as per your requirement




                        MedicineResponse login = MydApplication.gson.fromJson(response, MedicineResponse.class);
                        medicines.clear();

                        if(login.getStatus()){

                            medicines.addAll(login.getMedicines());
                            adapter.notifyDataSetChanged();

                            boolean doesHotNewsExist = false;
                            for(Medicine medicine: medicines){
                                if(medicine.isHotNews()){

                                    doesHotNewsExist = true;
                                    hotNewsMedicine = medicine;
                                    break;
                                }
                            }


                            ((HomeActivity)getActivity()).gotHotNews(doesHotNewsExist, hotNewsMedicine);
                            changeUIBasedOnHotNews(doesHotNewsExist);


                        }else{
                            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),"Error","Wrong login information!",false);
                        }


                        dismissProgressDialog();


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWithoutTitle(getActivity(),"Server Down!", false);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("genericId", genericName);
                params.put("companyId", companyName);
                params.put("medicineName", ed_medicine_name.getText().toString());

                //Log.d("DEBUG", MydApplication.gson.toJson(params));
                return params;
            }
        }
                ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    public void onHotNewsClicked(Medicine hotNewsMedicine)
    {

        Intent intent = new Intent(getActivity(), MedicineDetails.class);
        intent.putExtra("medicine", hotNewsMedicine);
        startActivity(intent);

    }


}
