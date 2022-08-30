package de.suzufa.screwbox.playground.debo.specials.player;

import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TiledSupport;

public class PlayerResources {

    private PlayerResources() {
    } // hide constructor

    private static final SpriteDictionary TILESET = TiledSupport.loadTileset("tilesets/specials/player.json");

    public static final Sprite STANDING_SPRITE = TILESET.findByName("standing");
    public static final Sprite DEAD_SPRITE = TILESET.findByName("dead");
    public static final Sprite RUNNING_SPRITE = TILESET.findByName("running");
    public static final Sprite JUMPING_SPRITE = TILESET.findByName("jumping");
    public static final Sprite IDLE_SPRITE = TILESET.findByName("idle");
    public static final Sprite DIGGING_SPRITE = TILESET.findByName("digging");

    public static final Sound OUCH_SOUND = Sound.fromFile("sounds/ouch.wav");
    public static final Sound BLUPP_SOUND = Sound.fromFile("sounds/blupp.wav");
    public static final Sound ZISCH_SOUND = Sound.fromFile("sounds/zisch.wav");
    public static final Sound JUMP_SOUND = Sound.fromFile("sounds/jump.wav");

}
