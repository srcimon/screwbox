package io.github.simonbas.screwbox.core;

import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.assets.Assets;
import io.github.simonbas.screwbox.core.async.Async;
import io.github.simonbas.screwbox.core.audio.Audio;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.graphics.Graphics;
import io.github.simonbas.screwbox.core.graphics.GraphicsConfiguration;
import io.github.simonbas.screwbox.core.keyboard.Keyboard;
import io.github.simonbas.screwbox.core.log.Log;
import io.github.simonbas.screwbox.core.log.LoggingAdapter;
import io.github.simonbas.screwbox.core.loop.Loop;
import io.github.simonbas.screwbox.core.mouse.Mouse;
import io.github.simonbas.screwbox.core.physics.Physics;
import io.github.simonbas.screwbox.core.savegame.Savegame;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.scenes.Scenes;
import io.github.simonbas.screwbox.core.ui.Ui;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.core.window.Window;

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

    /**
     * Create ingame menues and show loading animation.
     */
    Ui ui();

    /**
     * Execute long running tasks within the {@link Engine}.
     */
    Async async();

    /**
     * Provides some super basic logging features and the ability to pick up engine
     * log events via {@link Log#setAdapter(LoggingAdapter)} (log.LoggingAdapter)}.
     *
     * @see Log
     */
    Log log();

    /**
     * Provides methods to save and load the game state.
     */
    Savegame savegame();

    //TODO comment
    Window window();

    /**
     * Starts the {@link Engine}. This opens the game {@link Window} and starts the
     * {@link Loop}. The {@link Engine} can be stopped by calling {@link #stop()}
     * from within an {@link EntitySystem} or a {@link UiMenu}.
     *
     * @see #start(Class)
     */
    void start();

    /**
     * Starts the {@link Engine} with the given {@link Scene}. This opens the game
     * {@link Window} and starts the {@link Loop}. The {@link Engine} can be stopped
     * by calling {@link #stop()} from within an {@link EntitySystem} or a
     * {@link UiMenu}.
     *
     * @see #start()
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
