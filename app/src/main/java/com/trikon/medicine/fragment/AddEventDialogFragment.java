package com.trikon.medicine.fragment;

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


import com.trikon.medicine.BuildConfig;
import com.trikon.medicine.CustomView.DialogFragmentCallBack;
import com.trikon.medicine.R;
import com.trikon.medicine.Utility.AccessDirectory;
import com.trikon.medicine.Utility.CompressImage;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.appdata.MydApplication;
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
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class AddEventDialogFragment extends BaseDialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private DialogFragmentCallBack dialogFragmentCallBack;

    private EditText ed_event_name, ed_description;
    private TextView tv_start_date, tv_end_date, tv_retake;
    private ImageView img_start_date, img_end_date, img_preview, img_take_pic;
    private LinearLayout ll_img_preview;

    private static final int TAG_START_DATE = 1;
    private static final int TAG_END_DATE = 2;
    private static int DATE_PICKER = TAG_START_DATE;
    private String startDate = "";
    private String endDate = "";


    private Button btn_add, btn_cancel;

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_REQUEST = 2;

    private ArrayAdapter<String> spAdapterVisibleTo;
    private static final String KEY_SELECT_VISIBILITY = "Select Visibility To";
    private Spinner sp_visible_to;
    List<String> list_visible_to = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            dialogFragmentCallBack = (DialogFragmentCallBack) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }

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
        return inflater.inflate(R.layout.fragment_dialog_add_event, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_start_date = view.findViewById(R.id.tv_start_date);
        tv_end_date = view.findViewById(R.id.tv_end_date);
        ed_description = view.findViewById(R.id.ed_description);
        ed_event_name = view.findViewById(R.id.ed_event_name);

        ll_img_preview = view.findViewById(R.id.ll_img_preview);
        ll_img_preview.setVisibility(View.GONE);
        img_preview = view.findViewById(R.id.img_preview);
        tv_retake = view.findViewById(R.id.tv_retake);
        tv_retake.setOnClickListener(this);

        img_end_date = view.findViewById(R.id.img_end_date);
        img_end_date.setOnClickListener(this);
        img_start_date = view.findViewById(R.id.img_start_date);
        img_start_date.setOnClickListener(this);
        img_take_pic = view.findViewById(R.id.img_take_pic);
        img_take_pic.setOnClickListener(this);


        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        sp_visible_to = (Spinner) view.findViewById(R.id.sp_visible_to);

        initAdapter();


    }

    private void initAdapter(){
        list_visible_to.add(KEY_SELECT_VISIBILITY);
        list_visible_to.addAll(Arrays.asList(getResources().getStringArray(R.array.userTypeVisibility)));
        spAdapterVisibleTo = new ArrayAdapter<String>
                (getActivity(), R.layout.spinner_item_black_text, list_visible_to);
        sp_visible_to.setAdapter(spAdapterVisibleTo);
    }

    private Uri photoURI;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.img_take_pic || id == R.id.tv_retake) {

            showAlertDialogForOptionToChoosePic();

        }

        if (id == R.id.img_start_date) {

            DATE_PICKER = TAG_START_DATE;

            showDatePicker();


        }
        if (id == R.id.img_end_date) {

            DATE_PICKER = TAG_END_DATE;

            showDatePicker();

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


    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (DATE_PICKER == TAG_START_DATE) {

            startDate = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
            tv_start_date.setText(startDate);

        }
        if (DATE_PICKER == TAG_END_DATE) {

            endDate = dayOfMonth + "/" + (monthOfYear+1)  + "/" + year;
            tv_end_date.setText(endDate);
        }
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
       List<String> listViwerType =  Arrays.asList(getResources().getStringArray(R.array.userTypeVisibility));

        try {
            MultipartUploadRequest req = new MultipartUploadRequest(getActivity(),
                    GlobalAppAccess.URL_ADD_EVENT)

                    .addFileToUpload(absolutePath, "image") //Adding file
                    .addParameter("name",ed_event_name.getText().toString())
                    .addParameter("description",ed_description.getText().toString())
                    .addParameter("viewerType", String.valueOf(listViwerType.indexOf(sp_visible_to.getSelectedItem().toString())))
                    .addParameter("startDate",tv_start_date.getText().toString())
                    .addParameter("endDate",tv_end_date.getText().toString())
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
                    //Log.d("DEBUG", serverResponse.getBodyAsString());
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), "Event Added Successfully!", Toast.LENGTH_LONG).show();
                    dismiss();


                    dialogFragmentCallBack.onRefresh();
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
        if (ed_event_name.getText().toString().isEmpty()) {
            ed_event_name.setError("Required");
            return false;
        } else if (ed_description.getText().toString().isEmpty()) {
            ed_description.setError("Required");
            return false;
        } else if (startDate.isEmpty()) {
            Toast.makeText(getActivity(), "Start date cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (endDate.isEmpty()) {
            Toast.makeText(getActivity(), "End date cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (photoURI == null) {
            Toast.makeText(getActivity(), "Add an image!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(sp_visible_to.getSelectedItem().toString().equals(KEY_SELECT_VISIBILITY)){
            Toast.makeText(getActivity(), "Select Event Visibility!", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }





}
