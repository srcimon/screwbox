package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.PlaybackReference;

import java.util.UUID;

public class PlaybackReferenceImpl implements PlaybackReference {

    private final UUID id;

    public PlaybackReferenceImpl() {
        this.id = UUID.randomUUID();
    }
    @Override
    public UUID id() {
        return id;
    }
}
