package com.example.pharmacyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("addressHouse")
    @Expose
    private String addressHouse;
    @SerializedName("addressStreet")
    @Expose
    private String addressStreet;
    @SerializedName("addressArea")
    @Expose
    private String addressArea;
    @SerializedName("addressCity")
    @Expose
    private String addressCity;
    @SerializedName("licenseNo")
    @Expose
    private String licenseNo;
    @SerializedName("pharmacistRegNo")
    @Expose
    private String pharmacistRegNo;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("companySecretCode")
    @Expose
    private String companySecretCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddressHouse() {
        return addressHouse;
    }

    public void setAddressHouse(String addressHouse) {
        this.addressHouse = addressHouse;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getPharmacistRegNo() {
        return pharmacistRegNo;
    }

    public void setPharmacistRegNo(String pharmacistRegNo) {
        this.pharmacistRegNo = pharmacistRegNo;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanySecretCode() {
        return companySecretCode;
    }

    public void setCompanySecretCode(String companySecretCode) {
        this.companySecretCode = companySecretCode;
    }

}