package com.sayan.rnd.googlemapadvancedwork.locationfetchrelated;

public interface LocationPermissionListener {
    void onPermissionGranted();

    void onPermissionDenied(String errorMessage);
}
