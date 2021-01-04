package com.trikon.medicine.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.trikon.medicine.CustomView.RecyclerItemClickListener;
import com.trikon.medicine.EventDetailsActivity;
import com.trikon.medicine.MedicineDetails;
import com.trikon.medicine.R;
import com.trikon.medicine.adapter.EventListAdapter;
import com.trikon.medicine.alertbanner.AlertDialogForAnything;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.appdata.MydApplication;
import com.trikon.medicine.model.Event;
import com.trikon.medicine.model.EventResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvItemList;
    private EventListAdapter adapter;
    List<Event> items = new ArrayList<>();

    private SwipeRefreshLayout swipeContainer;

   // private OnFragmentInteractionListener mListener;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(String param1, String param2) {
        EventListFragment fragment = new EventListFragment();
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
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvItemList = view.findViewById(R.id.rv_event_list);

         swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshScreen();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        initRecyleView();

        //sendRequestForGetEventList();
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                sendRequestForGetEventList();
                swipeContainer.setRefreshing(true);
            }
        });
    }

    private void initRecyleView(){

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvItemList.setLayoutManager(mLayoutManager);


        //Set Adapter
        adapter = new EventListAdapter(items);
        rvItemList.setAdapter(adapter);

        rvItemList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rvItemList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                        intent.putExtra("event", items.get(position));
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



    }


    public void sendRequestForGetEventList() {

        // url = url + "?" + "email=" + email + "&password=" + password;
        // TODO Auto-generated method stub
        //showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_EVENT_LIST,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        dismissProgressDialog();
                        swipeContainer.setRefreshing(false);

                        EventResponse login = MydApplication.gson.fromJson(response, EventResponse.class);


                        if(login.getStatus()){

                            items.clear();
                            items.addAll(login.getEvents());
                            adapter.notifyDataSetChanged();

                        }else{
                            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),"Error",login.getMessage(),false);
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                swipeContainer.setRefreshing(false);

               AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Error", "Network problem. please try again!", false);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", String.valueOf(MydApplication.getInstance().getPrefManger().getUser().getId()));

                return params;
            }
        }
                ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    public void refreshScreen(){
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                sendRequestForGetEventList();
                swipeContainer.setRefreshing(true);
            }
        });

    }
}
