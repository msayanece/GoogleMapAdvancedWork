package com.sayan.rnd.googlemapadvancedwork.mapsrelated.activities

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.sayan.rnd.googlemapadvancedwork.R
import java.util.ArrayList
import kotlin.math.min

/**
 * Draws profile photos inside markers (using IconGenerator).
 * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
 */
class MarkerClusterRenderer(
        private val activity: Activity,
        googleMap: GoogleMap,
        clusterManager: ClusterManager<MarkerItem>
) : DefaultClusterRenderer<MarkerItem>(activity, googleMap, clusterManager) {

    private val mIconGenerator: IconGenerator
    private val mClusterIconGenerator: IconGenerator
    private val mImageView: ImageView
    private val mClusterImageView: ImageView
    private val mDimension: Int

    init {
        mIconGenerator = IconGenerator(activity)
        mClusterIconGenerator = IconGenerator(activity)

        val multiProfile = activity.layoutInflater.inflate(R.layout.multi_profile, null)
        mClusterIconGenerator.setContentView(multiProfile)
        mClusterImageView = multiProfile.findViewById(R.id.image)
        mImageView = ImageView(activity)
        mDimension = activity.resources.getDimension(R.dimen.custom_profile_image).toInt()
        mImageView.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
        val padding = activity.resources.getDimension(R.dimen.custom_profile_padding).toInt()
        mImageView.setPadding(padding, padding, padding, padding)
        mIconGenerator.setContentView(mImageView)
    }

    override fun onBeforeClusterItemRendered(markerItem: MarkerItem, markerOptions: MarkerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        mImageView.setImageResource(markerItem.profilePhoto)
        val icon = mIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(markerItem.title)
    }

    override fun onBeforeClusterRendered(cluster: Cluster<MarkerItem>, markerOptions: MarkerOptions) {
        // Draw multiple markers.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        val profilePhotos = ArrayList<Drawable>(min(4, cluster.size))
        val width = mDimension
        val height = mDimension

        for (markerItem in cluster.items) {
            // Draw 4 at most.
            if (profilePhotos.size == 4) break
            val drawable: Drawable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getDrawable(markerItem.profilePhoto)
            }else{
                activity.resources.getDrawable(markerItem.profilePhoto)
            }
            drawable?.let {
                it.setBounds(0, 0, width, height)
                profilePhotos.add(it)
            }
        }
        val multiDrawable = MultiDrawable(profilePhotos)
        multiDrawable.setBounds(0, 0, width, height)

        mClusterImageView.setImageDrawable(multiDrawable)
        val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MarkerItem>?): Boolean {
        // Always render clusters.
        return cluster?.size!! > 1
    }
}