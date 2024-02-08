package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;

record Volume(Percent value, boolean isMuted) {

    Volume() {
        this(Percent.max(), false);
    }

    Volume updatedValue(final Percent value) {
        return new Volume(value, isMuted);
    }

    Percent playbackVolume() {
        return isMuted ? Percent.zero() : value;
    }

    Volume muted(boolean isMuted) {
        return new Volume(value, isMuted);
    }
}
