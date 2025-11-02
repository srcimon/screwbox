package dev.screwbox.core;

import dev.screwbox.core.achievements.Achievements;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.Assets;
import dev.screwbox.core.async.Async;
import dev.screwbox.core.audio.Audio;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.keyboard.Keyboard;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.log.LoggingAdapter;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.mouse.Mouse;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.navigation.Navigation;
import dev.screwbox.core.scenes.Scenes;
import dev.screwbox.core.ui.Ui;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.window.Window;

/**
 * This is the central point of controlling the ScrewBox-Engine. Grants access
 * to all public subsystems.
 *
 * @see ScrewBox#createEngine()
 */
public interface Engine {
//TODO cleanup draw order in docs
//TODO custom chapter on draw ordering
    //TODO fix all @Order appearances within documentation
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
    Navigation navigation();

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

}
