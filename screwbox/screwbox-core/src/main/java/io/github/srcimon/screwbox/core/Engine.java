package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.Assets;
import io.github.srcimon.screwbox.core.async.Async;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.log.LoggingAdapter;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.mouse.Mouse;
import io.github.srcimon.screwbox.core.physics.Physics;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.Scenes;
import io.github.srcimon.screwbox.core.ui.Ui;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.window.Window;

/**
 * This is the central point of controlling the ScrewBox-Engine. Grants access
 * to all public subsystems.
 *
 * @see ScrewBox#createEngine()
 */
public interface Engine {

    /**
     * Loads your game {@link Asset}s. Used to avoid stutter due to in game loading.
     */
    Assets assets();

    /**
     * Returns the games {@link Entity} management system ({@link Environment}). This is where all the
     * game logic and objects are kept.
     */
    Environment environment();

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

    /**
     * Retrieve information about the current {@link Keyboard} state.
     *
     * @see Keyboard
     */
    Keyboard keyboard();

    /**
     * Use {@link Scenes} to structure different game situations.
     *
     * @see Scenes
     */
    Scenes scenes();

    /**
     * Controls the audio playback of the {@link Engine}.
     */
    Audio audio();

    /**
     * Advanced searching for entities, pathfinding, raycasting and adjusting Entites to a {@link Grid}.
     *
     * @see Physics
     */
    Physics physics();

    /**
     * Subsystem for getting Information on the Mouse.
     *
     * @see Mouse
     */
    Mouse mouse();

    /**
     * Create simple ingame menus.
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
     * Used to control the game window and retrieve information about the game window.
     */
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

    /**
     * Returns true if the engine has reached a certain speed for once. Once it is true it will
     * never switch back to false. Will also return true after a certain amount of time so that slow machines wont have any issue.
     * This mainly covers JVM warmup and can be used to wait before  starting the real business of your game.
     */
    boolean isWarmedUp();
}
