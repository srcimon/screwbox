package de.suzufa.screwbox.core.savegame;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.scenes.Scene;

/**
 * Provides methods to save and load the game state.
 */
public interface Savegame {

    /**
     * Creates a savegame file with the given name. The savegame contains all
     * {@link Entities} attached to the {@link Engine#entities()}.
     * 
     * @see #create(String, Class)
     */
    Savegame create(String name);

    /**
     * Creates a savegame file with the given name. The savegame contains all
     * {@link Entities} attached to the given {@link Scene}.
     *
     * @see #create(String)
     */
    Savegame create(String name, Class<? extends Scene> scene);

    /**
     * Loads a previously created savegame file from disc and restores the saved
     * {@link Entity}s in {@link Engine#entities()}.
     */
    Savegame load(String name);

    /**
     * Loads a previously created savegame file from disc and restores the saved
     * {@link Entity}s in the given {@link Scene}.
     */
    Savegame load(String name, Class<? extends Scene> scene);

    /**
     * Deletes the savegame with the given name. Triggers an {@link Exception} if
     * there is no such savegame.
     */
    Savegame delete(String name);

    /**
     * Returns true if there is a savegame with the given name.
     */
    boolean exists(String name);

}