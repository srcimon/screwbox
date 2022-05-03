package de.suzufa.screwbox.core;

import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.mouse.Mouse;
import de.suzufa.screwbox.core.physics.Physics;
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

    EntityEngine entityEngine();

    GameLoop loop();

    Graphics graphics();

    Keyboard keyboard();

    Scenes scenes();

    Audio audio();

    Physics physics();

    Mouse mouse();

    Ui ui();

    /**
     * Starts the {@link Engine}. This opens the game {@link Window} and starts the
     * {@link GameLoop}. The {@link Engine} can be stopped by calling
     * {@link #stop()} from within an {@link EntitySystem} or a {@link UiMenu}.
     * 
     * @see {@link #start(Class)}
     */
    void start();

    /**
     * Starts the {@link Engine} with the given {@link Scene}. This opens the game
     * {@link Window} and starts the {@link GameLoop}. The {@link Engine} can be
     * stopped by calling {@link #stop()} from within an {@link EntitySystem} or a
     * {@link UiMenu}.
     * 
     * @see {@link #start()}
     */
    void start(Class<? extends Scene> sceneClass);

    /**
     * Stops a running {@link GameLoop} and closes the game {@link Window}. Can be
     * called from within an {@link EntitySystem} or a {@link UiMenu}.
     */
    void stop();

}
