package com.trikon.medicine.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medicine implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("manufacturerId")
    @Expose
    private Integer manufacturerId;
    @SerializedName("genericId")
    @Expose
    private Integer genericId;
    @SerializedName("strength")
    @Expose
    private String strength;
    @SerializedName("dosageForm")
    @Expose
    private String dosage;
    @SerializedName("useFor")
    @Expose
    private String useFor;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("hotNews")
    @Expose
    private boolean hotNews;
    private final static long serialVersionUID = 3614445464142290791L;

    @SerializedName("manufacturerName")
    @Expose
    private String companyName;
    @SerializedName("genericName")
    @Expose
    private String genericName;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getGenericId() {
        return genericId;
    }

    public void setGenericId(Integer genericId) {
        this.genericId = genericId;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getUseFor() {
        return useFor;
    }

    public void setUseFor(String useFor) {
        this.useFor = useFor;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHotNews() {
        return hotNews;
    }

    public void setHotNews(boolean hotNews) {
        this.hotNews = hotNews;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}