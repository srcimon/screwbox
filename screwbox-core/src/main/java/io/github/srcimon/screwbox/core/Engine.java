package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.achievements.Achievements;
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
import io.github.srcimon.screwbox.core.particles.Particles;
import io.github.srcimon.screwbox.core.physics.Physics;
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
     * Add achievements to challenge players with custom goals.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/achivements">Documentation</a>
     */
    Achievements achievements();

    /**
     * Loads your game {@link Asset}s. Used to avoid stutter due to in game loading.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/assets">Documentation</a>
     */
    Assets assets();

    /**
     * Returns the games {@link Entity} management system ({@link Environment}). This is where all the
     * game logic and objects are kept.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/environment">Documentation</a>
     */
    Environment environment();

    /**
     * Provides access to current performance metrics and to controls the target
     * frames per second.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/loop">Documentation</a>
     */
    Loop loop();

    /**
     * Provides access to the {@link Graphics}, which has methods for drawing on
     * screen, changing the {@link GraphicsConfiguration}, to update and read the
     * {@link Graphics#camera()}.
     */
    Graphics graphics();

    /**
     * Retrieve information about the current {@link Keyboard} state.
     *
     * @see Keyboard
     * @see <a href="http://screwbox.dev/docs/core-modules/loop">Documentation</a>
     */
    Keyboard keyboard();

    /**
     * Use {@link Scenes} to structure different game situations.
     *
     * @see Scenes
     * @see <a href="http://screwbox.dev/docs/core-modules/scenes">Documentation</a>
     */
    Scenes scenes();

    /**
     * Controls the audio playback of the {@link Engine}.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/audio">Documentation</a>
     */
    Audio audio();

    /**
     * Advanced searching for entities, pathfinding, raycasting and adjusting entities to a {@link Grid}.
     */
    Physics physics();

    /**
     * Add particle effects to create some nice visuals.
     */
    Particles particles();

    /**
     * Subsystem for getting Information on the {@link Mouse}.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/mouse">Documentation</a>
     */
    Mouse mouse();

    /**
     * Create simple in game menus.
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
     * Used to control and retrieve information about the game window.
     *
     * @see <a href="http://screwbox.dev/docs/core-modules/ui">Documentation</a>
     */
    Window window();

    /**
     * Starts the {@link Engine}. This opens the game {@link Window} and starts the
     * {@link Loop}. The {@link Engine} can be stopped by calling {@link #stop()}
     * from within an {@link EntitySystem} or a {@link UiMenu}.
     */
    void start();

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
     * Returns the version of the used {@link ScrewBox} runtime. Will return '0.0.0 (dev-mode)' when working directly
     * with {@link ScrewBox} source.
     */
    String version();

    /**
     * Returns {@code true} if the engine has reached a certain speed for once. Once it is {@code true} it will
     * never switch back to false. Will also return true after a certain amount of time so that slow machines wont have any issue.
     * This mainly covers JVM warmup and can be used to wait before  starting the real business of your game.
     */
    boolean isWarmedUp();
}
