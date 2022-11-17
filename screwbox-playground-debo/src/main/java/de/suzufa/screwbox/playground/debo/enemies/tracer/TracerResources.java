package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Tileset;

public final class TracerResources {

    private TracerResources() {
    } // hide constructor

    private static final Tileset TILESET = Tileset.fromJson("tilesets/enemies/tracer.json");
    public static final Sprite ACTIVE_SPRITE = TILESET.findByName("active");
    public static final Sprite INACTIVE_SPRITE = TILESET.findByName("inactive");

}
