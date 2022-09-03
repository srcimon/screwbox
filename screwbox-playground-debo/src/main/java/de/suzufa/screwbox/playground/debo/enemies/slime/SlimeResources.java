package de.suzufa.screwbox.playground.debo.enemies.slime;

import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.SpriteDictionary;

public final class SlimeResources {

    private SlimeResources() {
    } // hide constructor

    private static final SpriteDictionary TILESET = SpriteDictionary.fromJsonTileset("tilesets/enemies/slime.json");

    public static final Sprite DEAD_SPRITE = TILESET.findByName("dead");
    public static final Sprite MOVING_SPRITE = TILESET.findByName("moving");

    public static final Sound KILL_SOUND = Sound.fromFile("sounds/kill.wav");
}
