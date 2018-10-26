package com.sayan.rnd.googlemapadvancedwork.locationfetchrelated;

import android.location.Location;

/**
 * Location fetch listener for getting location callbacks
 */
public interface FetchLocationSuccessListener {
    /**
     * this method will be called after successful fetch of location
     * @param location fetched location
     */
    void onLocationFetched(Location location);
}
