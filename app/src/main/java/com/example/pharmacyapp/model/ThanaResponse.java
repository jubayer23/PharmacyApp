package com.example.pharmacyapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThanaResponse extends BaseResponse implements Serializable
{


    @SerializedName("thanas")
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