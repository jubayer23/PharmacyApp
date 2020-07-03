package com.example.pharmacyapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pharmacyapp.BuildConfig;
import com.example.pharmacyapp.R;
import com.example.pharmacyapp.RegistrationActivity;
import com.example.pharmacyapp.Utility.AccessDirectory;
import com.example.pharmacyapp.Utility.CompressImage;
import com.example.pharmacyapp.alertbanner.AlertDialogForAnything;
import com.example.pharmacyapp.appdata.GlobalAppAccess;
import com.example.pharmacyapp.appdata.MydApplication;
import com.example.pharmacyapp.model.DistrictResponse;
import com.example.pharmacyapp.model.DivisionResponse;
import com.example.pharmacyapp.model.Thana;
import com.example.pharmacyapp.model.ThanaResponse;
import com.example.pharmacyapp.model.User;
import com.example.pharmacyapp.model.UserLoginResponse;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class AddPrescriptionFragment extends BaseDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private EditText ed_dr_name, ed_house_no, ed_street;

    private LinearLayout ll_img_preview;
    private TextView  tv_retake;
    private ImageView img_start_date, img_end_date, img_preview, img_take_pic;


    private Button btn_add, btn_cancel;

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_REQUEST = 2;

    private Spinner sp_thana, sp_district, sp_division;

    List<Thana> list_thana = new ArrayList<>();
    List<Thana> list_district = new ArrayList<>();
    List<Thana> list_division = new ArrayList<>();

    private ArrayAdapter<Thana> adapterSpThana, adapterSpDistrict, adapterSpDivision;
    private static final String KEY_SELECT_THANA = "Select Thana";
    private static final String KEY_SELECT_DISTRICT = "Select District";
    private static final String KEY_SELECT_DIVISION = "Select Division";

   // private ArrayAdapter<String> spAdapterThana, spAdapterDistrict, spAdapterDivision;


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);

        if (getArguments() != null) {

            // stockData = getArguments().getParcelable("stockData");

        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return Inflater.inflate(inflater, R.layout.fragment_dialog_add_event, container, false);
        return inflater.inflate(R.layout.fragment_dialog_add_prescription, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ed_dr_name = view.findViewById(R.id.ed_doctor_name);
        ed_house_no = view.findViewById(R.id.ed_house_no);
        ed_street = view.findViewById(R.id.ed_street);

        ll_img_preview = view.findViewById(R.id.ll_img_preview);
        ll_img_preview.setVisibility(View.GONE);
        img_preview = view.findViewById(R.id.img_preview);
        tv_retake = view.findViewById(R.id.tv_retake);
        tv_retake.setOnClickListener(this);


        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        sp_thana = (Spinner) view.findViewById(R.id.sp_thana);
        sp_thana.setOnItemSelectedListener(this);

        sp_district = (Spinner) view.findViewById(R.id.sp_districts);
        sp_district.setOnItemSelectedListener(this);

        sp_division = (Spinner) view.findViewById(R.id.sp_division);
        sp_division.setOnItemSelectedListener(this);

        img_take_pic = view.findViewById(R.id.img_take_pic);
        img_take_pic.setOnClickListener(this);

        initAdapter();

        sendRequestToGetThana();

    }

    private void initAdapter() {

        //list_thana.add(KEY_SELECT_THANA);
        Thana t1 = new Thana(KEY_SELECT_THANA);
        list_thana.add(t1);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        adapterSpThana = new ArrayAdapter<Thana>
                (getActivity(), R.layout.spinner_item_black_text, list_thana);
        sp_thana.setAdapter(adapterSpThana);

        //list_district.add(KEY_SELECT_DISTRICT);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        Thana t2 = new Thana(KEY_SELECT_DISTRICT);
        list_district.add(t2);
        adapterSpDistrict = new ArrayAdapter<Thana>
                (getActivity(), R.layout.spinner_item_black_text, list_district);
        sp_district.setAdapter(adapterSpDistrict);

       // list_division.add(KEY_SELECT_DIVISION);
        //list_thana.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypes)));
        Thana t3 = new Thana(KEY_SELECT_DIVISION);
        list_division.add(t3);
        adapterSpDivision = new ArrayAdapter<Thana>
                (getActivity(), R.layout.spinner_item_black_text, list_division);
        sp_division.setAdapter(adapterSpDivision);



    }

    private Uri photoURI;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.img_take_pic || id == R.id.tv_retake) {

            showAlertDialogForOptionToChoosePic();

        }


        if (id == R.id.btn_add) {

            if (validateForm()) {
                startUploadingToServer(photoURI);
            }
        }

        if (id == R.id.btn_cancel) {

            dismiss();
        }

    }

    private void showAlertDialogForOptionToChoosePic() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Select An Option");
        alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = AccessDirectory.createImageFile(getActivity());
                    } catch (IOException ex) {

                    }

                    MydApplication.getInstance().getPrefManger().setLog(photoFile.getAbsolutePath());
                    // Continue only if the File was successfully created
                    if (photoFile != null) {


                        photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                                BuildConfig.APPLICATION_ID + ".provider", photoFile);


                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, TAKE_REQUEST);
                    }

                }
            }
        });
        alertDialog.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        alertDialog.show();
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ll_img_preview.setVisibility(View.VISIBLE);
            img_take_pic.setVisibility(View.GONE);

            if (requestCode == SELECT_PICTURE) {

                photoURI = data.getData();


                Picasso.get().load(photoURI).resize(200,200).into(img_preview);


            }

            if (requestCode == TAKE_REQUEST) {

                Bitmap bitmap=null;
                try {
                    File f= new File(MydApplication.getInstance().getPrefManger().getLog());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                    img_preview.setImageBitmap(bitmap);
                    photoURI = Uri.fromFile(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startUploadingToServer(Uri pathURI) {
        String absolutePath = CompressImage.compressImageFromURI(getActivity(), pathURI);

        try {
            MultipartUploadRequest req = new MultipartUploadRequest(getActivity(),
                    GlobalAppAccess.URL_ADD_PRESCRIPTION)

                    .addFileToUpload(absolutePath, "image") //Adding file
                    .addParameter("doctorName",ed_dr_name.getText().toString())
                    .addParameter("houseNo",ed_house_no.getText().toString())
                    .addParameter("street",ed_street.getText().toString())
                    .addParameter("thana",sp_thana.getSelectedItem().toString())
                    .addParameter("city",sp_district.getSelectedItem().toString())
                    .addParameter("district",sp_district.getSelectedItem().toString())
                    .addParameter("division",sp_division.getSelectedItem().toString())
                    .addParameter("postCode","3103")
                    .addParameter("doctorDegree","3103")
                    .addParameter("userId", String.valueOf(MydApplication.getInstance().getPrefManger().getUser().getId()))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setAutoDeleteFilesAfterSuccessfulUpload(false)
                    .setMaxRetries(2); //Starting the upload
            String uploadId = req.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    showProgressDialog("Loading...", true, false);
                }
                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    dismissProgressDialog();
                    Log.d("DEBUG", serverResponse.getBodyAsString());
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), "Prescription Added Successfully!", Toast.LENGTH_LONG).show();
                    dismiss();
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), "cancenlled", Toast.LENGTH_LONG).show();
                }
            }).startUpload();

        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private boolean validateForm() {
        if (ed_dr_name.getText().toString().isEmpty()) {
            ed_dr_name.setError("Required");
            return false;
        } else if (ed_street.getText().toString().isEmpty()) {
            ed_street.setError("Required");
            return false;
        } else if (photoURI == null) {
            Toast.makeText(getActivity(), "Add an image!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        String selected_value = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendRequestToGetThana(){


        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_GET_THANA,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                       // dismissProgressDialog();

                        ThanaResponse login = MydApplication.gson.fromJson(response, ThanaResponse.class);

                        if(login.getStatus()){

                            list_thana.addAll(login.getThanas());
                            adapterSpThana.notifyDataSetChanged();

                            sendRequestToGetDistrict();


                        }else{
                            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),"Error",login.getMessage(),false);
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Error", "Network problem. please try again!", false);

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

    private void sendRequestToGetDistrict(){


        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.POST, GlobalAppAccess.URL_GET_DISTRIC,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("DEBUG",response);

                        // dismissProgressDialog();

                        DistrictResponse login = MydApplication.gson.fromJson(response, DistrictResponse.class);

                        if(login.getStatus()){

                            list_district.addAll(login.getThanas());
                            adapterSpDistrict.notifyDataSetChanged();

                            sendRequestToGetDivision();


                        }else{
                            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),"Error",login.getMessage(),false);
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Error", "Network problem. please try again!", false);

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
                            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),"Error",login.getMessage(),false);
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Error", "Network problem. please try again!", false);

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
