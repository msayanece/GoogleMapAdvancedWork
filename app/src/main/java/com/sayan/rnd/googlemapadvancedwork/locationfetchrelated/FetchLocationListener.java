package com.sayan.rnd.googlemapadvancedwork.locationfetchrelated;

public interface FetchLocationListener {
    void onLocationFetched(double latitude, double longitude);
    void onLocationFetchFailed(String errorMessage);
}
