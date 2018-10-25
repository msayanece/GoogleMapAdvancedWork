package com.sayan.rnd.googlemapadvancedwork.mapsrelated;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sayan.rnd.googlemapadvancedwork.GlideConnector;
import com.sayan.rnd.googlemapadvancedwork.R;

public class MarkerStyleUtil {

    /**
     * Static method used to set a default marker to a GoogleMap using the given latitude and longitude.
     * This will also set camera to the location accordingly (if any or both boolean param is true).
     * Set both the boolean param for not setting the camera.
     *
     * @param googleMap           the map where the marker will be set on
     * @param markerTitle         the title to be set. it is visible when a marker is being tapped
     * @param latitude            the latitude value of the location of the marker
     * @param longitude           the longitude value of the location of the marker
     * @param shouldAnimateCamera if true, animate camera to the location
     * @param shouldMoveCamera    if true, move camera to the location. This param will be ignored if shouldAnimateCamera is true
     * @return the marker after all the setup. The marker will be set to invisible
     */
    public static Marker setMarkerInMap(GoogleMap googleMap, String markerTitle, double latitude,
                                        double longitude, boolean shouldAnimateCamera, boolean shouldMoveCamera) {
        LatLng latLng = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
        marker.setVisible(false);
        if (shouldAnimateCamera) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        } else if (shouldMoveCamera) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        return marker;
    }

    /**
     * Static method used to animate a marker to a position from the current position.
     * Current position will be calculated from the marker itself. default duration of animation will be 500 ms.
     *
     * @param googleMap  the map where the marker will be set on
     * @param marker     the marker to animate
     * @param toPosition the LatLng of the location where the marker needs to be moved
     * @param hideMarker marker will be invisible if it set to true. Set it to false if you are unsure about what argument to pass
     */
    public static void animateMarker(final GoogleMap googleMap, final Marker marker, final LatLng toPosition,
                                     final boolean hideMarker) {
        if (toPosition == null || marker == null) {
            return;
        }
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        if (googleMap == null) {
            return;
        }
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setPosition(toPosition);
                            marker.setVisible(true);
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Static method used to animate a marker to a position from the current position.
     * Current position will be calculated from the marker itself. default duration of animation will be set as 500 ms if argument duration is passed as -1
     *
     * @param googleMap  the map where the marker will be set on
     * @param marker     the marker to animate
     * @param toPosition the LatLng of the location where the marker needs to be moved
     * @param hideMarker marker will be invisible if it set to true. Set it to false if you are unsure about what argument to pass
     * @param duration   the duration of animation from one position to another position in milliseconds.
     *                   Default duration of animation will be set as 500 ms if argument duration is passed as -1
     */
    public static void animateMarker(final GoogleMap googleMap, final Marker marker, final LatLng toPosition,
                                     final boolean hideMarker, long duration) {
        if (toPosition == null || marker == null) {
            return;
        }
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        if (googleMap == null) {
            return;
        }
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        if (duration == -1) {
            duration = 500;
        }
        final Interpolator interpolator = new LinearInterpolator();

        final long finalDuration = duration;
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / finalDuration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setPosition(toPosition);
                            marker.setVisible(true);
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setCustomMarkerCircular(Context context, final Marker marker, String imageURL) {
        final View markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_circular_layout, null);
        final ImageView markerImage = (ImageView) markerView.findViewById(R.id.markerImageCircular);
        GlideConnector.getInstance().loadImageBitmapWithSize(context, imageURL, markerImage, 95, 95, new GlideConnector.GlideCallback() {
            @Override
            public void onSuccess() {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(markerView)));
                marker.setVisible(true);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public static void setCustomMarkerRectangular(Context context, final Marker marker, String imageURL) {
        final View markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_layout, null);
        final ImageView markerImage = (ImageView) markerView.findViewById(R.id.markerImageRectangular);
        GlideConnector.getInstance().loadImageBitmapWithSize(context, imageURL, markerImage, 95, 95, new GlideConnector.GlideCallback() {
            @Override
            public void onSuccess() {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(markerView)));
                marker.setVisible(true);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    //for creating drawable from view for picasso
    private static Bitmap createDrawableFromView(View view) {
        int heightPixels = 50, widthPixels = 50;
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        view.measure(widthPixels, heightPixels);
        view.layout(0, 0, widthPixels, heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
