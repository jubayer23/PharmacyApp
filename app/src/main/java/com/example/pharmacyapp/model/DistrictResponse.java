package com.example.pharmacyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DistrictResponse extends BaseResponse implements Serializable
{


    @SerializedName("districts")
    @Expose
    private List<Thana> thanas = null;

    private final static long serialVersionUID = 6778502279446515406L;



    public List<Thana> getThanas() {
        return thanas;
    }

    public void setThanas(List<Thana> thanas) {
        this.thanas = thanas;
    }



}