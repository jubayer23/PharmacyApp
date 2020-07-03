package com.example.pharmacyapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventResponse extends BaseResponse implements Serializable
{


    @SerializedName("events")
    @Expose
    private List<Event> events = null;
    private final static long serialVersionUID = 6022972533439703340L;


    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}