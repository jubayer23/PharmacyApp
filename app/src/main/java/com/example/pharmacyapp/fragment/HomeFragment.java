package com.example.pharmacyapp.fragment;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pharmacyapp.CustomView.AutoCompleteLoading;
import com.example.pharmacyapp.R;
import com.example.pharmacyapp.adapter.CompanyAcAdapter;
import com.example.pharmacyapp.adapter.EventListAdapter;
import com.example.pharmacyapp.adapter.GenericAcAdapter;
import com.example.pharmacyapp.adapter.MedicineListAdapter;
import com.example.pharmacyapp.alertbanner.AlertDialogForAnything;
import com.example.pharmacyapp.appdata.GlobalAppAccess;
import com.example.pharmacyapp.appdata.MydApplication;
import com.example.pharmacyapp.model.Company;
import com.example.pharmacyapp.model.CompanyResponse;
import com.example.pharmacyapp.model.Generic;
import com.example.pharmacyapp.model.GenericResponse;
import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.model.MedicineResponse;

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
    private String genericName, companyName;

    private RecyclerView rvMedcineList;
    private MedicineListAdapter adapter;
    List<Medicine> medicines = new ArrayList<>();

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

    private void initRecyleView(){

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvMedcineList.setLayoutManager(mLayoutManager);

        //Set Adapter
        adapter = new MedicineListAdapter(medicines);
        rvMedcineList.setAdapter(adapter);



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
                    }else if(genericName == null || genericName.isEmpty()){
                        ac_generics.setError("Required!!");
                        return true;
                    }else if(companyName == null || companyName.isEmpty()){
                        ac_companies.setError("Required!");
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
        ac_generics.setThreshold(2);
        ac_generics.setAdapter(adapterAcGeneric);
        ac_generics.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //selectedText.setText(autoSuggestAdapter.getObject(position));
                        genericName = adapterAcGeneric.getObject(position);
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
                        Log.d("DEBUG", "in generic");
                        makeApiCall(ac_generics.getText().toString(), 1);
                    }
                }
                return false;
            }
        });


        adapterAcCompany = new CompanyAcAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        ac_companies.setThreshold(2);
        ac_companies.setAdapter(adapterAcCompany);
        ac_companies.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //selectedText.setText(autoSuggestAdapter.getObject(position));
                        companyName = adapterAcCompany.getObject(position);
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
                        Log.d("DEBUG", "in company");
                        makeApiCall(ac_companies.getText().toString(), 2);
                    }
                }
                return false;
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
                        //Log.d("DEBUG",response);

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

                        }else{
                            adapterAcCompany.setData(companies);
                            adapterAcCompany.notifyDataSetChanged();
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


                        dismissProgressDialog();

                        MedicineResponse login = MydApplication.gson.fromJson(response, MedicineResponse.class);

                        if(login.getStatus()){

                            medicines.addAll(login.getMedicines());
                            adapter.notifyDataSetChanged();

                        }else{
                            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),"Error","Wrong login information!",false);
                        }



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


}
