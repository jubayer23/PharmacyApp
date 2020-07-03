package com.example.pharmacyapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicineResponse extends BaseResponse implements Serializable
{


    @SerializedName("medicines")
    @Expose
    private List<Medicine> medicines = null;
    private final static long serialVersionUID = -4049326240031855229L;



    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

}