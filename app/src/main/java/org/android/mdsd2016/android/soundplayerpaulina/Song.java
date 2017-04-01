package org.android.mdsd2016.android.soundplayerpaulina;

/**
 * Created by paulinaberger on 2017-04-01.
 */

import java.io.Serializable;

public class Song implements Serializable {

    private String id;
    private String title;
    private String duration;
    private String country;
    private String comment;
    private float lat;
    private float lng;

    public Song(String id, String title, String duration, String country, String comment, float lat, float lng) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.country = country;
        this.comment = comment;
        this.lat = lat;
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getCountry() {
        return country;
    }

    public String getComment() {
        return comment;
    }
}