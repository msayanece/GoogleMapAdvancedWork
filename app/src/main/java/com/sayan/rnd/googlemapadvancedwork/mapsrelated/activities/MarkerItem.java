package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {

    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private int profilePhoto;

    public MarkerItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MarkerItem(double lat, double lng, String title, String snippet, int  profilePhoto) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.profilePhoto = profilePhoto;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
