package de.suzufa.screwbox.core;

import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.assets.Assets;
import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.log.Log;
import de.suzufa.screwbox.core.loop.Loop;
import de.suzufa.screwbox.core.mouse.Mouse;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.savegame.Savegame;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.core.scenes.Scenes;
import de.suzufa.screwbox.core.ui.Ui;
import de.suzufa.screwbox.core.ui.UiMenu;

/**
 * This is the central point of controlling the ScrewBox-Engine. Grants access
 * to all public subsystems.
 * 
 * @see ScrewBox#createEngine()
 */
public interface Engine {

    /**
     * Loads your game {@link Asset}s.
     */
    Assets assets();

    /**
     * Returns the games {@link Entity} management system. This is where all the
     * game logic and objects are kept.
     */
    Entities entities();

    /**
     * Provides access to current performance metrics and to controls the target
     * frames per second.
     */
    Loop loop();

    /**
     * Provides access to the {@link Graphics}, which has methods for drawing on
     * screen, changing the {@link GraphicsConfiguration}, to update and read the
     * {@link Graphics#cameraPosition()} and to adjust the {@link Window}.
     */
    Graphics graphics();

    Keyboard keyboard();

    Scenes scenes();

    /**
     * Controls the audio playback of the {@link Engine}.
     */
    Audio audio();

    Physics physics();

    Mouse mouse();

    Ui ui();

    /**
     * Execute long running tasks within the {@link Engine}.
     */
    Async async();

    /**
     * Provides some super basic logging features and the ability to pick up engine
     * log events via {@link Log#setAdapter(log.LoggingAdapter)}.
     * 
     * @see Log
     */
    Log log();

    /**
     * Provides methods to save and load the game state.
     */
    Savegame savegame();

    /**
     * Starts the {@link Engine}. This opens the game {@link Window} and starts the
     * {@link Loop}. The {@link Engine} can be stopped by calling {@link #stop()}
     * from within an {@link EntitySystem} or a {@link UiMenu}.
     * 
     * @see {@link #start(Class)}
     */
    void start();

    /**
     * Starts the {@link Engine} with the given {@link Scene}. This opens the game
     * {@link Window} and starts the {@link Loop}. The {@link Engine} can be stopped
     * by calling {@link #stop()} from within an {@link EntitySystem} or a
     * {@link UiMenu}.
     * 
     * @see {@link #start()}
     */
    void start(Class<? extends Scene> sceneClass);

    /**
     * Stops a running {@link Loop} and closes the game {@link Window}. Can be
     * called from within an {@link EntitySystem} or a {@link UiMenu}.
     */
    void stop();

    /**
     * Returns the name of the {@link Engine}.
     */
    String name();

}
