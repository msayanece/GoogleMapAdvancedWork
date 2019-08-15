package com.sayan.rnd.googlemapadvancedwork;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Updated by Sayan on 25-10-2018.
 */

public class GlideConnector {

    private static GlideConnector instance = null;

    private GlideConnector() {

    }

    public static GlideConnector getInstance() {
        if (instance == null) {
            instance = new GlideConnector();
        }
        return instance;
    }

    public void loadImageDirectly(Context context, String imageURL, ImageView imageView) {
//        if (imageURL.contains(".gif")) {
//            Glide.with(context)
//                    .load(imageURL)
//                    .thumbnail(0.1f)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                .placeholder(R.drawable.ic_launcher_background)
////                .crossFade()
//                    .into(new GlideDrawableImageViewTarget(imageView));
//        } else {
//            Glide.with(context)
//                    .load(imageURL)
//                    .thumbnail(0.1f)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                .placeholder(R.drawable.ic_launcher_background)
////                .crossFade()
//                    .into(imageView);
//        }
        loadImageDirectlyWithoutThumbnail(context, imageURL, imageView);
    }

    public void loadImageDirectlyWithoutThumbnail(Context context, String imageURL, ImageView imageView) {
        if (imageURL.contains(".gif")) {
            Glide.with(context)
                    .load(imageURL)
                    .apply(
                            new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(new DrawableImageViewTarget(imageView));
        } else {
            Glide.with(context)
                    .load(imageURL)
                    .apply(
                            new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(imageView);

        }
    }

    public void loadImageDirectlyWithSize(Context context, String imageURL, ImageView imageView, int height, int width) {
        Glide.with(context)
                .load(imageURL)
                .apply(
                        new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(width, height)
                                .centerCrop()
                )
                .into(imageView);
    }

    public void loadImageBitmapWithSize(Context context, String imageURL, ImageView imageView, int height, int width) {
        Glide.with(context)
                .load(imageURL)
                .apply(
                        new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(width, height)
                                .centerCrop()
                )
                .into(imageView);
    }

    public void loadImageBitmapWithSize(Context context, String imageURL, final ImageView imageView, final int height, final int width, final GlideCallback callback) {
        DrawableImageViewTarget viewTarget = Glide.with(context.getApplicationContext())
                .load(imageURL)
                .apply(
                        new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(width, height)
                                .centerCrop()
                )
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        //on load failed
//                        callback.onFailure();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        //on load success
//                        callback.onSuccess();
//                        return false;
//                    }
//                })
                .into(new DrawableImageViewTarget(imageView, true) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        callback.onSuccess();
                    }
                });
    }

    public static interface GlideCallback {
        void onSuccess();

        void onFailure();
    }
}
