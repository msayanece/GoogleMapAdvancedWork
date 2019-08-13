package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils;

import android.view.View;
import android.widget.ImageView;

public class MapPlaybackViewHolder {
    private ImageView buttonPlay;
    private ImageView buttonPause;

    public ImageView getButtonPlay() {
        return buttonPlay;
    }

    public ImageView getButtonPause() {
        return buttonPause;
    }

    public MapPlaybackViewHolder() {}

    public MapPlaybackViewHolder(ImageView buttonPlay, ImageView buttonPause) {
        this.buttonPlay = buttonPlay;
        this.buttonPause = buttonPause;
    }

    public void changePlayPauseButtonToPause() {
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
    }

    public void changePlayPauseButtonToPlay() {
        buttonPlay.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.GONE);
    }
}
