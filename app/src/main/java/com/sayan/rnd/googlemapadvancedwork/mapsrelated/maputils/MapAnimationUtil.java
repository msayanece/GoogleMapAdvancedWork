package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public final class MapAnimationUtil {
    private static Location convertLatLngToLocation(LatLng latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }

    public static float bearingBetweenLatLngs(LatLng begin,LatLng end) {
        if (getDistance(begin.latitude,begin.longitude,end.latitude,end.longitude)<=0.018){
            return 0.0f;
        }
        Location beginL= convertLatLngToLocation(begin);
        Location endL= convertLatLngToLocation(end);
//        return beginL.bearingTo(endL);
        return  0.0f;
    }

    // for calculating distance between two co-ordinates
    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /**
     * Add the marker to the polyline.
     */
    public static void updatePolyLine(LatLng latLng, Polyline polyLine) {
        List<LatLng> points = polyLine.getPoints();
        points.add(latLng);
        polyLine.setPoints(points);
    }


}
