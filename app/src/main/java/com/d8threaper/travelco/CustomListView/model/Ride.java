package com.d8threaper.travelco.CustomListView.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 13/9/15.
 */
public class Ride {

    private String source,destination,sourceTime,destinationTime,vacancy,finalCost;

    public Ride() {
    }

    public Ride(String source, String destination,String sourceTime, String destinationTime,
                String vacancy, String finalCost) {
        this.source = source;
        this.destination = destination;
        this.sourceTime = sourceTime;
        this.destinationTime = destinationTime;
        this.vacancy = vacancy;
        this.finalCost = finalCost;
    }

    public String getSource() {

        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(String destinationTime) {
        this.destinationTime = destinationTime;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(String finalCost) {
        this.finalCost = finalCost;
    }
}
