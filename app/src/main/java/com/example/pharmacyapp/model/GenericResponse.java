package com.example.pharmacyapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenericResponse extends BaseResponse implements Serializable
{


    @SerializedName("generics")
    @Expose
    private List<Generic> generics = null;
    private final static long serialVersionUID = -8290817218664973461L;



    public List<Generic> getGenerics() {
        return generics;
    }

    public void setGenerics(List<Generic> generics) {
        this.generics = generics;
    }

}