package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.audio.SoundPool;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TiledSupport;

public class TracerResources {

    private static final SpriteDictionary TILESET = TiledSupport.loadTileset("tilesets/enemies/tracer.json");
    public static final Sprite ACTIVE_SPRITE = TILESET.findByName("active");
    public static final Sprite INACTIVE_SPRITE = TILESET.findByName("inactive");

    public static final SoundPool POEBELEI_SOUND = SoundPool.fromFile("sounds/scream.wav");
}
