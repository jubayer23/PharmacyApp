package com.example.pharmacyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CompanyResponse extends BaseResponse implements Serializable
{


    @SerializedName("companies")
    @Expose
    private List<Company> companies = null;
    private final static long serialVersionUID = -8290817218664973461L;



    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

}