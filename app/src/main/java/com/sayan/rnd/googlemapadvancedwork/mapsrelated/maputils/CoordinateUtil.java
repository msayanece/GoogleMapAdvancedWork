package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

public class CoordinateUtil {

    /**
     * Static method used for calculating distance between two co-ordinates
     *
     * @param lat1 latitude of point 1
     * @param lon1 longitude of point 1
     * @param lat2 latitude of point 2
     * @param lon2 longitude of point 1
     *
     * @return the calculated distance between two points
      */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
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

    /**
     * Static method used for converting degree to radian
     *
     * @param deg degree
     *
     * @return the calculated radian
     */
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Static method used for converting radian to degree
     *
     * @param rad radian number
     *
     * @return the calculated degree
     */
    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /**
     * Static method used for finding bearing difference in degree
     *
     * @param oldBearingInDegree old bearing
     * @param newBearingInDegree new bearing
     *
     * @return the calculated difference of bearings
     */
    public static float findBearingDifference(float oldBearingInDegree, float newBearingInDegree) {
        return Math.abs(oldBearingInDegree - newBearingInDegree);
    }
}
