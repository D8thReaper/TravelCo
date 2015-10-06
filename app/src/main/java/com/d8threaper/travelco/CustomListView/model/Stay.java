package com.d8threaper.travelco.CustomListView.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 13/9/15.
 */
public class Stay {

    private String address, thumbnailUrl, finalCost, duration;
    private int cost;
    private Date startDate,endDate;

    public Stay() {
    }

    public Stay(String address, String thumbnailUrl, int cost, Date startDate, Date endDate) {
        this.address = address;
        this.thumbnailUrl = thumbnailUrl;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(String finalCost) {
        this.finalCost = finalCost;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
