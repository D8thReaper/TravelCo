package com.d8threaper.travelco.CustomListView.model;

import java.util.Date;

/**
 * Created by root on 13/9/15.
 */
public class Companion {

    private String name,duration,thumbnailUrl;
    private Date srcTime,destTime;

    public Companion() {
    }

    public Companion(String name, String thumbnailUrl, Date srcTime, Date destTime) {
        this.name = name;
        this.srcTime = srcTime;
        this.destTime = destTime;
        this.thumbnailUrl = thumbnailUrl;

        duration = srcTime + " - " + destTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
