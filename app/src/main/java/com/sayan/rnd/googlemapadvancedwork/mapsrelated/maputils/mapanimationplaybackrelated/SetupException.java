package com.sayan.rnd.googlemapadvancedwork.mapsrelated.maputils.mapanimationplaybackrelated;

public class SetupException extends RuntimeException {

    public SetupException() {
        super("Some unknown set up problem occurs");
    }

    public SetupException(String message) {
        super(message);
    }
}
